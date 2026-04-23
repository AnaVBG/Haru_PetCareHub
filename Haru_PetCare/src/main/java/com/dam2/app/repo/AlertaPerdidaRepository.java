package com.dam2.app.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dam2.app.model.AlertaPerdida;

public interface AlertaPerdidaRepository extends JpaRepository<AlertaPerdida, Long>{

	// Requisito: Obtener solo las alertas que están activas ahora mismo en el mapa
    List<AlertaPerdida> findByActivaTrue();
	
}
