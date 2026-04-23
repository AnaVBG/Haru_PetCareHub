package com.dam2.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dam2.app.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
