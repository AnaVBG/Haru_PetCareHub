package com.dam2.app.repo;

import com.dam2.app.model.PinMapa;
import com.dam2.app.model.TipoPin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PinMapaRepository extends JpaRepository<PinMapa, Long> {

    // Todos los pines del mapa (el mapa comunitario los carga todos al iniciar)
    List<PinMapa> findAll();

    // Filtrar por tipo para que el usuario pueda ver solo "FUENTES", por ejemplo
    List<PinMapa> findByTipo(TipoPin tipo);

    // Pines creados por un usuario concreto (para que pueda borrar los suyos)
    List<PinMapa> findByUsuario_Id(Long idUsuario);
}