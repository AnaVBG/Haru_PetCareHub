package com.dama2.app.dto;

import java.time.LocalDate;

/**
 * Ficha de mascota que el servidor devuelve a Android.
 * No incluye el objeto Usuario completo para evitar
 * bucles infinitos de serialización JSON.
 */
public record MascotaDTO(
		Long id,
	    String nombre,
	    String especie,
	    String raza,
	    LocalDate fechaNacimiento,
	    String fotoUrl,
	    Long duenoId     // Solo el ID, no el objeto Usuario entero
		) {

}
