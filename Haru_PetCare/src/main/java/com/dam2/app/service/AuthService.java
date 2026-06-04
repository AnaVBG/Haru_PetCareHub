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

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepo,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
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
        usuario.setPassword(passwordEncoder.encode(dto.password()));
        usuario.setRol(RolUsuario.valueOf(dto.rol()));
        usuario.setTelefono(dto.telefono());
        usuario.setFechaRegistro(LocalDateTime.now());

        if (RolUsuario.VETERINARIO.equals(usuario.getRol()) && dto.idClinica() != null) {
            usuarioRepo.findById(dto.idClinica()).ifPresent(usuario::setClinica);
        }

        usuarioRepo.save(usuario);

        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol().name());
        return new LoginResponseDTO(usuario.getId(), usuario.getNombre(), usuario.getEmail(),
                usuario.getRol().name(), usuario.getTelefono(), token);
    }

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepo.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.password(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol().name());
        return new LoginResponseDTO(usuario.getId(), usuario.getNombre(), usuario.getEmail(),
                usuario.getRol().name(), usuario.getTelefono(), token);
    }

    @Transactional
    public void actualizarTokenFcm(Long idUsuario, String tokenFcm) {
        Usuario u = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        u.setTokenFcm(tokenFcm.replaceAll("^\"|\"$", ""));
    }
}