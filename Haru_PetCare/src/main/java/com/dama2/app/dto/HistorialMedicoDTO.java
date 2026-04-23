package com.dama2.app.dto;

import java.time.LocalDateTime;

/**
 * Un registro del historial clínico.
 * Incluye idMascota para que Android sepa a qué mascota pertenece
 * sin tener que deserializar el objeto Mascota entero.
 */
public record HistorialMedicoDTO(
		Long id,
	    String tipoRegistro,
	    String descripcion,
	    LocalDateTime fechaRegistro,
	    Long idMascota
		) {

}
