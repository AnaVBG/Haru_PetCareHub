package com.dam2.app.controller;

import com.dam2.app.dto.PinInsertarDTO;
import com.dam2.app.dto.PinMapaDTO;
import com.dam2.app.service.PinMapaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pines")
public class PinMapaController {

    private final PinMapaService pinService;

    // Inyección por constructor — consistente con el resto del proyecto
    public PinMapaController(PinMapaService pinService) {
        this.pinService = pinService;
    }

    @GetMapping
    public ResponseEntity<List<PinMapaDTO>> getTodos() {
        return ResponseEntity.ok(pinService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<PinMapaDTO> crear(@RequestBody PinInsertarDTO dto) {
        return ResponseEntity.ok(pinService.crear(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        pinService.borrar(id);
        return ResponseEntity.noContent().build();
    }
}