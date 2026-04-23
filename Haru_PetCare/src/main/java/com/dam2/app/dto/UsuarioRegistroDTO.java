package com.dam2.app.dto;

/**
 * Datos que un nuevo usuario envía al registrarse.
 * No incluye 'id' ni 'fechaRegistro' — el servidor los genera.
 */
public record UsuarioRegistroDTO(
		String nombre,
	    String email,
	    String password,
	    String rol,       // "DUENO" o "VETERINARIO"
	    String telefono
		) {

}
