package com.dam2.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dam2.app.model.HistorialMedico;

public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Long>{

	// Obtener todo el historial de una mascota, ordenado del más reciente al más antiguo
    // Requisito: sección PDF Export — necesitamos todos los registros en orden
    List<HistorialMedico> findByMascota_IdOrderByFechaRegistroDesc(Long idMascota);
	
}
