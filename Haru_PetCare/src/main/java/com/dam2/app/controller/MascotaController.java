package com.dam2.app.controller;

import com.dam2.app.service.MascotaService;
import com.dam2.app.dto.MascotaDTO;
import com.dam2.app.dto.MascotaInsertarDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @GetMapping("/dueno/{id}")
    public ResponseEntity<List<MascotaDTO>> getMascotasPorDueno(@PathVariable Long id) {
        return ResponseEntity.ok(mascotaService.obtenerMascotasPorDueno(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaDTO> getMascotaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mascotaService.obtenerPorId(id));
    }

    @PostMapping("/inserta")
    public ResponseEntity<MascotaDTO> insertar(@RequestBody MascotaInsertarDTO dto) {
        MascotaDTO creada = mascotaService.guardarDesdeDTO(dto);
        return ResponseEntity.status(201).body(creada);
    }

    @PostMapping(value = "/{id}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MascotaDTO> subirFoto(
            @PathVariable Long id,
            @RequestParam("foto") MultipartFile foto) {
        return ResponseEntity.ok(mascotaService.subirFoto(id, foto));
    }
}