package com.dam2.app.repo;

import com.dam2.app.model.RolUsuario;
import com.dam2.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Necesario para el login: buscar por email
    Optional<Usuario> findByEmail(String email);

    // Listar todos los veterinarios para el selector de citas en Android
    List<Usuario> findByRol(RolUsuario rol);

    // Para el sistema de alertas FCM: obtener tokens de usuarios cercanos
    // La lógica de distancia (5km) la haremos en el Service con Haversine
    List<Usuario> findByTokenFcmIsNotNull();
}