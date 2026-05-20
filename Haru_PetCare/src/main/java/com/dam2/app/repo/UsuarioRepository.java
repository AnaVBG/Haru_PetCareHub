package com.dam2.app.repo;

import com.dam2.app.model.RolUsuario;
import com.dam2.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByRol(RolUsuario rol);

    List<Usuario> findByTokenFcmIsNotNull();

    List<Usuario> findByClinica_Id(Long idClinica);
}