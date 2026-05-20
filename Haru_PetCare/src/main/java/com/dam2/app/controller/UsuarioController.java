package com.dam2.app.controller;

import com.dam2.app.dto.ClinicaDTO;
import com.dam2.app.dto.UsuarioDTO;
import com.dam2.app.model.RolUsuario;
import com.dam2.app.model.Usuario;
import com.dam2.app.repo.MascotaRepository;
import com.dam2.app.repo.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepo;
    private final MascotaRepository mascotaRepo;

    public UsuarioController(UsuarioRepository usuarioRepo,
                             MascotaRepository mascotaRepo) {
        this.usuarioRepo = usuarioRepo;
        this.mascotaRepo = mascotaRepo;
    }

    @GetMapping("/clinicas")
    public ResponseEntity<List<ClinicaDTO>> getClinicas() {
        List<ClinicaDTO> clinicas = usuarioRepo.findByRol(RolUsuario.CLINICA)
                .stream()
                .map(u -> new ClinicaDTO(u.getId(), u.getNombre(), u.getTelefono()))
                .toList();
        return ResponseEntity.ok(clinicas);
    }

    @GetMapping("/buscar")
    public ResponseEntity<UsuarioDTO> buscarPorEmail(@RequestParam String email) {
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        int totalMascotas = mascotaRepo.findByDueno_Id(usuario.getId()).size();
        return ResponseEntity.ok(new UsuarioDTO(
                usuario.getId(), usuario.getNombre(), usuario.getEmail(),
                usuario.getRol().name(), usuario.getTelefono(), totalMascotas));
    }

    @GetMapping("/veterinarios-clinica/{idClinica}")
    public ResponseEntity<List<UsuarioDTO>> getVeterinariosDeClinica(
            @PathVariable Long idClinica) {
        List<UsuarioDTO> vets = usuarioRepo.findByClinica_Id(idClinica)
                .stream()
                .map(u -> new UsuarioDTO(
                        u.getId(), u.getNombre(), u.getEmail(),
                        u.getRol().name(), u.getTelefono(), null))
                .toList();
        return ResponseEntity.ok(vets);
    }
}