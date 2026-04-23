package com.dam2.app.dto;

import java.time.LocalDateTime;

/**
 * Datos que envía el veterinario al crear una cita.
 */
public record CitaInsertarDTO(
		LocalDateTime fechaCita,
	    String motivo,
	    Long idMascota,
	    Long idVeterinario,
	    Long idDueno
		) {

}
