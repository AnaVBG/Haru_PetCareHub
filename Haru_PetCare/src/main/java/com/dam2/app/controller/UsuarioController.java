package com.dam2.app.controller;

import com.dam2.app.dto.ClinicaDTO;
import com.dam2.app.model.RolUsuario;
import com.dam2.app.repo.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepo;

    public UsuarioController(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @GetMapping("/clinicas")
    public ResponseEntity<List<ClinicaDTO>> listarClinicas() {
        List<ClinicaDTO> clinicas = usuarioRepo.findByRol(RolUsuario.CLINICA)
                .stream()
                .map(u -> new ClinicaDTO(u.getId(), u.getNombre(), u.getTelefono()))
                .toList();
        return ResponseEntity.ok(clinicas);
    }
}