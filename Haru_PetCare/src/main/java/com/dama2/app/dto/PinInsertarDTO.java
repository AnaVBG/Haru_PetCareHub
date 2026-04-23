package com.dama2.app.dto;

public record PinInsertarDTO(
		String tipo,
	    Double latitud,
	    Double longitud,
	    String descripcion,
	    Long idUsuario
		) {

}
