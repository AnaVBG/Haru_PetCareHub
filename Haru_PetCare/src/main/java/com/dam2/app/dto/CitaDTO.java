package com.dam2.app.dto;

import java.time.LocalDateTime;

/**
 * Cita devuelta a Android, con nombres legibles en lugar de IDs.
 * Así la app puede mostrar "Luna — Dr. García" directamente
 * sin hacer peticiones adicionales.
 */
public record CitaDTO(
		Long id,
	    LocalDateTime fechaCita,
	    String motivo,
	    String estado,
	    String nombreMascota,
	    String nombreVeterinario,
	    Long idDueno
		) {

}
