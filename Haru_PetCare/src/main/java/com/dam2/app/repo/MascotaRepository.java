package com.dam2.app.repo;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.dam2.app.model.Mascota;

public interface MascotaRepository extends JpaRepository<Mascota, Long>{

	// Requisito: Obtener todas las mascotas de un dueño en concreto
    List<Mascota> findByDueno_Id(Long idDueno);
    
    // Spring lo traduce a: Buscar mascotas de una especie (ej. "Perro")
    List<Mascota> findByEspecieIgnoreCase(String especie);
    
    // Consulta personalizada: Buscar mascotas de un dueño nacidas después de una fecha
    @Query("""
            SELECT m FROM Mascota m 
            WHERE m.dueno.id = :duenoId 
            AND m.fechaNacimiento > :fechaLimite
            """)
    List<Mascota> findCachorrosDeUsuario(
            @Param("duenoId") Long duenoId, 
            @Param("fechaLimite") LocalDate fechaLimite
    );
	
}
