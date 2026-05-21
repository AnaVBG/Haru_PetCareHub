package com.dam2.app.dto;

import java.time.LocalDate;

public record MascotaActualizarDTO(
        String nombre,
        String especie,
        String raza,
        LocalDate fechaNacimiento,
        String fotoUrl
) {}