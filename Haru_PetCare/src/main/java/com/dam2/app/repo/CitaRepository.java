package com.dam2.app.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.dam2.app.model.Cita;
import com.dam2.app.model.EstadoCita;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByVeterinario_IdAndEstado(Long idVeterinario, EstadoCita estado);

    List<Cita> findByDueno_Id(Long idDueno);

    // Todas las citas de los veterinarios de una clínica
    List<Cita> findByVeterinario_Clinica_Id(Long idClinica);

    @Query("""
            SELECT c FROM Cita c
            WHERE c.mascota.id = :idMascota
            AND c.fechaCita >= CURRENT_TIMESTAMP
            ORDER BY c.fechaCita ASC
            """)
    List<Cita> findProximasCitasMascota(@Param("idMascota") Long idMascota);
}