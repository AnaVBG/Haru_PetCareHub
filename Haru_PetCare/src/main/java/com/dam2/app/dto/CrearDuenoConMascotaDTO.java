package com.dam2.app.dto;

import java.time.LocalDate;

/**
 * Formulario completo para crear un dueño nuevo con su mascota directamente
 * desde la pantalla del veterinario.
 */
public record CrearDuenoConMascotaDTO(
		// Datos del dueño nuevo
		String nombre, String email, String password, String telefono,
		// Datos de la mascota
		String nombreMascota, String especie, String raza, LocalDate fechaNacimiento,
		// Clínica a la que se vincula
		Long idClinica) {
}