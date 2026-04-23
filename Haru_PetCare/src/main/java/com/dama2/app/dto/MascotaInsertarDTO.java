package com.dama2.app.dto;

import java.time.LocalDate;

/**
 * Datos mínimos para crear una mascota nueva desde Android.
 */
public record MascotaInsertarDTO(
		String nombre,
	    String especie,
	    String raza,
	    LocalDate fechaNacimiento,
	    Long duenoId
		) {

}
