package com.dam2.app.dto;

import java.time.LocalDateTime;

public record CitaDTO(
	    Long id,
	    LocalDateTime fechaCita,
	    String motivo,
	    String estado,
	    Long idMascota,
	    String nombreMascota,
	    String nombreVeterinario,
	    Long idDueno
	) {}