package com.dam2.app.service;

import com.dam2.app.dto.ClinicaDTO;
import com.dam2.app.dto.UsuarioDTO;
import com.dam2.app.model.RolUsuario;
import com.dam2.app.model.Usuario;
import com.dam2.app.repo.MascotaRepository;
import com.dam2.app.repo.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final MascotaRepository mascotaRepo;

    public UsuarioService(UsuarioRepository usuarioRepo, MascotaRepository mascotaRepo) {
        this.usuarioRepo = usuarioRepo;
        this.mascotaRepo = mascotaRepo;
    }

    @Transactional(readOnly = true)
    public List<ClinicaDTO> obtenerClinicas() {
        return usuarioRepo.findByRol(RolUsuario.CLINICA)
                .stream()
                .map(u -> new ClinicaDTO(u.getId(), u.getNombre(), u.getTelefono()))
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorEmail(String email) {
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        int totalMascotas = mascotaRepo.findByDueno_Id(usuario.getId()).size();
        return new UsuarioDTO(
                usuario.getId(), usuario.getNombre(), usuario.getEmail(),
                usuario.getRol().name(), usuario.getTelefono(), totalMascotas);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerVeterinariosDeClinica(Long idClinica) {
        return usuarioRepo.findByClinica_Id(idClinica)
                .stream()
                .map(u -> new UsuarioDTO(
                        u.getId(), u.getNombre(), u.getEmail(),
                        u.getRol().name(), u.getTelefono(), null))
                .toList();
    }

    @Transactional
    public void actualizarUbicacion(Long id, Double lat, Double lng) {
        Usuario usuario = usuarioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setUltimaLat(lat);
        usuario.setUltimaLng(lng);
        usuarioRepo.save(usuario);
    }
}