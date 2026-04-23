package com.dam2.app.dto;

import java.time.LocalDateTime;

/**
 * Pin colaborativo del mapa.
 * Incluye latitud, longitud y tipo para que el SDK de Google Maps
 * pueda renderizar el icono correcto (fuente, papelera, peligro...).
 */
public record PinMapaDTO(
		Long id,
	    String tipo,
	    Double latitud,
	    Double longitud,
	    String descripcion,
	    LocalDateTime fechaCreacion,
	    Long idUsuario,
	    String nombreUsuario   // Para mostrar "Colocado por: Ana" en el mapa
		) {

}
