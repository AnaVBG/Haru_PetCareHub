package com.dam2.app.controller;

import com.dam2.app.dto.CitaDTO;
import com.dam2.app.dto.CitaInsertarDTO;
import com.dam2.app.service.CitaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping("/veterinario/{id}")
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<List<CitaDTO>> agendaVeterinario(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.obtenerAgendaVeterinario(id));
    }

    @GetMapping("/dueno/{id}")
    public ResponseEntity<List<CitaDTO>> citasDueno(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.obtenerCitasDeDueno(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<CitaDTO> crearCita(@RequestBody CitaInsertarDTO dto) {
        return ResponseEntity.ok(citaService.crearCita(dto));
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<CitaDTO> cambiarEstado(@PathVariable Long id,
                                                  @RequestBody String nuevoEstado) {
        return ResponseEntity.ok(citaService.cambiarEstado(id, nuevoEstado));
    }
}