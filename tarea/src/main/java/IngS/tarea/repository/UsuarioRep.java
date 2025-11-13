package IngS.tarea.repository;

import IngS.tarea.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRep extends JpaRepository<Usuario, Long> {
}
