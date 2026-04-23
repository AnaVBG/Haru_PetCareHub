package com.dam2.app.dto;

/**
 * Lo que devuelve el servidor tras un login exitoso.
 * Android guardará el token en SharedPreferences para enviarlo
 * en el header "Authorization" de todas las peticiones siguientes.
 */
public record LoginResponseDTO(
		Long   idUsuario,
	    String nombre,
	    String email,
	    String rol,      // "DUENO" o "VETERINARIO"
	    String token     // El JWT — caduca en 24h
		) {

}
