package com.dam2.app.dto;

/**
 * Lo que envía Android al hacer POST /api/auth/login.
 * Solo email y password — nunca el objeto Usuario completo.
 */
public record LoginRequestDTO(
		String email,
	    String password
		) {

}
