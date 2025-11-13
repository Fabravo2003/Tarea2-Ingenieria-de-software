package IngS.tarea.repository;

import IngS.tarea.model.Mueble;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MuebleRep extends JpaRepository<Mueble, Long> {

    // Métodos de búsqueda personalizados
    List<Mueble> findByEstado(String estado);

    List<Mueble> findByTipo(String tipo);

    List<Mueble> findByEstadoAndTipo(String estado, String tipo);
}
