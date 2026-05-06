package com.dam2.app.controller;

import com.dam2.app.dto.AlertaInsertarDTO;
import com.dam2.app.dto.AlertaPerdidaDTO;
import com.dam2.app.service.AlertaPerdidaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alertas")
public class AlertaPerdidaController {

    private final AlertaPerdidaService alertaService;

    public AlertaPerdidaController(AlertaPerdidaService alertaService) {
        this.alertaService = alertaService;
    }

    /** El mapa de alertas activas (mascotas perdidas ahora mismo) */
    @GetMapping("/activas")
    public ResponseEntity<List<AlertaPerdidaDTO>> getActivas() {
        return ResponseEntity.ok(alertaService.obtenerAlertasActivas());
    }

    /**
     * El botón de emergencia de Android llama a este endpoint.
     * Crea la alerta Y dispara las notificaciones FCM a los vecinos.
     */
    @PostMapping
    public ResponseEntity<AlertaPerdidaDTO> crearAlerta(
            @RequestBody AlertaInsertarDTO dto) {
        return ResponseEntity.ok(alertaService.crearAlerta(dto));
    }

    /** El dueño pulsa "Mascota encontrada" */
    @PutMapping("/{id}/resolver")
    public ResponseEntity<Void> resolver(@PathVariable Long id) {
        alertaService.resolverAlerta(id);
        return ResponseEntity.ok().build();
    }
}