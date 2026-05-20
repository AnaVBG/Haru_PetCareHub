package com.dam2.app.dto;

import java.time.LocalDate;

public record MascotaDTO(Long id, String nombre, String especie, String raza, LocalDate fechaNacimiento, String fotoUrl,
		Long duenoId, String nombreDueno, Long clinicaId
) {
}