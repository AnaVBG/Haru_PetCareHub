package com.dam2.app.service;

import com.dam2.app.model.RolUsuario;
import com.dam2.app.model.Usuario;
import com.dam2.app.repo.UsuarioRepository;
import com.dam2.app.security.JwtUtil;
import com.dam2.app.dto.LoginRequestDTO;
import com.dam2.app.dto.LoginResponseDTO;
import com.dam2.app.dto.UsuarioRegistroDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Gestiona el registro y login de usuarios.
 *
 * Flujo de login: 
 * 1. Android envía email + password 
 * 2. Buscamos el usuario por email 
 * 3. BCrypt compara el password con el hash almacenado 
 * 4. Si coincide, generamos JWT y lo devolvemos a Android 
 * 5. Android guarda el JWT en SharedPreferences y lo envía en el 
 * header de cada petición posterior
 */
@Service
public class AuthService {

	private final UsuarioRepository usuarioRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public AuthService(UsuarioRepository usuarioRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.usuarioRepo = usuarioRepo;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	@Transactional
	public LoginResponseDTO registrar(UsuarioRegistroDTO dto) {
		if (usuarioRepo.findByEmail(dto.email()).isPresent()) {
			throw new RuntimeException("El email ya está registrado");
		}

		Usuario usuario = new Usuario();
		usuario.setNombre(dto.nombre());
		usuario.setEmail(dto.email());
		// Hasheamos la contraseña — nunca se guarda en texto plano
		usuario.setPassword(passwordEncoder.encode(dto.password()));
		usuario.setRol(RolUsuario.valueOf(dto.rol()));
		usuario.setTelefono(dto.telefono());
		usuario.setFechaRegistro(LocalDateTime.now());

		usuarioRepo.save(usuario);

		String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol().name());
		return new LoginResponseDTO(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getRol().name(),
				token);
	}

	@Transactional(readOnly = true)
	public LoginResponseDTO login(LoginRequestDTO dto) {
		Usuario usuario = usuarioRepo.findByEmail(dto.email())
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		if (!passwordEncoder.matches(dto.password(), usuario.getPassword())) {
			throw new RuntimeException("Contraseña incorrecta");
		}

		String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol().name());
		return new LoginResponseDTO(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getRol().name(),
				token);
	}

	/**
	 * Cuando Android abre la app, envía su token FCM al servidor. Así el backend
	 * sabe a qué dispositivo enviar la notificación push.
	 */
	@Transactional
	public void actualizarTokenFcm(Long idUsuario, String tokenFcm) {
		Usuario u = usuarioRepo.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
		u.setTokenFcm(tokenFcm);
	}
}