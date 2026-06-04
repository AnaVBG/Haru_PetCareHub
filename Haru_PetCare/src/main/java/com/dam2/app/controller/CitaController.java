package com.dam2.app.controller;

import com.dam2.app.dto.CitaDTO;
import com.dam2.app.dto.CitaInsertarDTO;
import com.dam2.app.dto.CitaActualizarDTO;
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
    @PreAuthorize("hasRole('VETERINARIO') or hasRole('CLINICA')")
    public ResponseEntity<List<CitaDTO>> agendaVeterinario(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.obtenerAgendaVeterinario(id));
    }

    @GetMapping("/dueno/{id}")
    public ResponseEntity<List<CitaDTO>> citasDueno(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.obtenerCitasDeDueno(id));
    }

    /** Todas las citas de los veterinarios de una clínica */
    @GetMapping("/clinica/{idClinica}")
    @PreAuthorize("hasRole('CLINICA')")
    public ResponseEntity<List<CitaDTO>> citasClinica(@PathVariable Long idClinica) {
        return ResponseEntity.ok(citaService.obtenerCitasDeClinica(idClinica));
    }

    @PostMapping
    @PreAuthorize("hasRole('VETERINARIO') or hasRole('CLINICA')")
    public ResponseEntity<CitaDTO> crearCita(@RequestBody CitaInsertarDTO dto) {
        return ResponseEntity.ok(citaService.crearCita(dto));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('VETERINARIO') or hasRole('CLINICA')")
    public ResponseEntity<CitaDTO> actualizarCita(@PathVariable Long id,
                                                   @RequestBody CitaActualizarDTO dto) {
        return ResponseEntity.ok(citaService.actualizarCita(id, dto));
    }
}