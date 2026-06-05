package com.dam2.app.controller;

import com.dam2.app.dto.ClinicaDTO;
import com.dam2.app.dto.UbicacionDTO;
import com.dam2.app.dto.UsuarioDTO;
import com.dam2.app.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/clinicas")
    public ResponseEntity<List<ClinicaDTO>> getClinicas() {
        return ResponseEntity.ok(usuarioService.obtenerClinicas());
    }

    @GetMapping("/buscar")
    public ResponseEntity<UsuarioDTO> buscarPorEmail(@RequestParam String email) {
        return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }

    @GetMapping("/veterinarios-clinica/{idClinica}")
    public ResponseEntity<List<UsuarioDTO>> getVeterinariosDeClinica(@PathVariable Long idClinica) {
        return ResponseEntity.ok(usuarioService.obtenerVeterinariosDeClinica(idClinica));
    }

    @PutMapping("/{id}/ubicacion")
    public ResponseEntity<Void> actualizarUbicacion(@PathVariable Long id,
                                                     @RequestBody UbicacionDTO dto) {
        usuarioService.actualizarUbicacion(id, dto.lat(), dto.lng());
        return ResponseEntity.ok().build();
    }
}