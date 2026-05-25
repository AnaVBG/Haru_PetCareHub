package com.dam2.app.dto;

public record LoginResponseDTO(
        Long   idUsuario,
        String nombre,
        String email,
        String rol,
        String telefono,
        String token
) {}