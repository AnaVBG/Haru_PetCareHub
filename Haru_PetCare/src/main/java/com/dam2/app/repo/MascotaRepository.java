package com.dam2.app.repo;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.dam2.app.model.Mascota;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {

	List<Mascota> findByDueno_Id(Long idDueno);

	List<Mascota> findByEspecieIgnoreCase(String especie);

	List<Mascota> findByClinica_Id(Long idClinica);

	@Query("""
			SELECT m FROM Mascota m
			WHERE m.dueno.id = :duenoId
			AND m.fechaNacimiento > :fechaLimite
			""")
	List<Mascota> findCachorrosDeUsuario(@Param("duenoId") Long duenoId, @Param("fechaLimite") LocalDate fechaLimite);

	// Buscar todas con filtros opcionales (para veterinario sin clínica)
	@Query("""
			SELECT m FROM Mascota m
			WHERE (:especie IS NULL OR LOWER(m.especie) = LOWER(:especie))
			AND (:buscar IS NULL
			    OR LOWER(m.nombre) LIKE LOWER(CONCAT('%', :buscar, '%'))
			    OR LOWER(m.dueno.nombre) LIKE LOWER(CONCAT('%', :buscar, '%')))
			ORDER BY m.nombre ASC
			""")
	List<Mascota> buscarTodas(@Param("especie") String especie, @Param("buscar") String buscar);

	// Buscar mascotas de una clínica concreta con filtros opcionales
	@Query("""
			SELECT m FROM Mascota m
			WHERE m.clinica.id = :idClinica
			AND (:especie IS NULL OR LOWER(m.especie) = LOWER(:especie))
			AND (:buscar IS NULL
			    OR LOWER(m.nombre) LIKE LOWER(CONCAT('%', :buscar, '%'))
			    OR LOWER(m.dueno.nombre) LIKE LOWER(CONCAT('%', :buscar, '%')))
			ORDER BY m.nombre ASC
			""")
	List<Mascota> buscarPorClinica(@Param("idClinica") Long idClinica, @Param("especie") String especie,
			@Param("buscar") String buscar);
}