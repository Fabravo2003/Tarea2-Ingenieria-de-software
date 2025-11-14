package IngS.tarea.repository;

import IngS.tarea.model.Variante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VarianteRep extends JpaRepository<Variante, Long> {

    Optional<Variante> findByNombreVariante(String nombreVariante);
}
