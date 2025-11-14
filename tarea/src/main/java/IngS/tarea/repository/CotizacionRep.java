package IngS.tarea.repository;

import IngS.tarea.model.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CotizacionRep extends JpaRepository<Cotizacion, Long> {

    List<Cotizacion> findByEstado(String estado);
}
