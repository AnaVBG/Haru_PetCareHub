package com.dam2.app.dto;

/**
 * Lo que envía el botón de emergencia desde Android.
 * Las coordenadas las recoge la app del GPS del dispositivo.
 */
public record AlertaInsertarDTO(
		Double ultimaUbicacionLat,
	    Double ultimaUbicacionLng,
	    String mensajeAdicional,
	    Long idMascota,
	    Long idUsuario
		) {

}
