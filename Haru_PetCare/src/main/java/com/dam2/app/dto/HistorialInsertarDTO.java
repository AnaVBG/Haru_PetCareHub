package com.dam2.app.dto;

/**
 * Lo que envía el veterinario al crear un nuevo registro médico.
 */
public record HistorialInsertarDTO(
		String tipoRegistro,
	    String descripcion,
	    Long idMascota
		) {

}
