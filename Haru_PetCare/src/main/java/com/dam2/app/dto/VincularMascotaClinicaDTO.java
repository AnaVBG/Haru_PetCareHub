package com.dam2.app.dto;

/**
 * Petición para vincular una o todas las mascotas de un dueño a una clínica. Si
 * idMascota es null, se vinculan TODAS las mascotas del dueño.
 */
public record VincularMascotaClinicaDTO(Long idDueno, Long idMascota, Long idClinica) {
}