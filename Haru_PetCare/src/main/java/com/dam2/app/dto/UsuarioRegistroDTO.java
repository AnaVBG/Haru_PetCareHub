package com.dam2.app.dto;

public record UsuarioRegistroDTO(
    String nombre,
    String email,
    String password,
    String rol,
    String telefono,
    Long idClinica   // nullable, solo para VETERINARIO
) {}