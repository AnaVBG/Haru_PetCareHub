package com.dam2.app.controller;

import com.dam2.app.dto.HistorialInsertarDTO;
import com.dam2.app.dto.HistorialMedicoDTO;
import com.dam2.app.service.HistorialMedicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class HistorialMedicoController {

    private final HistorialMedicoService historialService;

    public HistorialMedicoController(HistorialMedicoService historialService) {
        this.historialService = historialService;
    }

    @GetMapping("/mascota/{idMascota}")
    public ResponseEntity<List<HistorialMedicoDTO>> getHistorial(
            @PathVariable Long idMascota) {
        return ResponseEntity.ok(historialService.obtenerHistorialDeMascota(idMascota));
    }

    @PostMapping
    @PreAuthorize("hasRole('VETERINARIO') or hasRole('CLINICA')")
    public ResponseEntity<HistorialMedicoDTO> crearRegistro(
            @RequestBody HistorialInsertarDTO dto) {
        return ResponseEntity.ok(historialService.guardar(dto));
    }
}