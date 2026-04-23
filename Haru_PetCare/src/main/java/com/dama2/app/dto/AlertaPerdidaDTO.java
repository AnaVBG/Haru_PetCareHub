package com.dama2.app.dto;

import java.time.LocalDateTime;

/**
 * Alerta de mascota perdida que se muestra en el mapa y en las notificaciones.
 * Contiene las coordenadas exactas para calcular el radio de 5km en el backend.
 */
public record AlertaPerdidaDTO(
		Long id,
	    Double ultimaUbicacionLat,
	    Double ultimaUbicacionLng,
	    String mensajeAdicional,
	    Boolean activa,
	    LocalDateTime fechaAlerta,
	    String nombreMascota,
	    String fotoUrlMascota,   // Para mostrar la foto en la notificación push
	    String nombreDueno,
	    String telefonoDueno     // Para que los vecinos puedan llamar directamente
		) {

}
