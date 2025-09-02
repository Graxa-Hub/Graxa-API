package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
