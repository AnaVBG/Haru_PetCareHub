package com.dam2.app.dto;

public record UsuarioDTO(Long id, String nombre, String email, String rol, String telefono, Integer totalMascotas) {
}