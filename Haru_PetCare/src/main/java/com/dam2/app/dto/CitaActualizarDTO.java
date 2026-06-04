package com.dam2.app.dto;

import java.time.LocalDateTime;

public record CitaActualizarDTO(
        LocalDateTime fechaCita,
        String motivo,
        String estado
) {}