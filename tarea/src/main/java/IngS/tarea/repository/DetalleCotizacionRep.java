package IngS.tarea.repository;

import IngS.tarea.model.DetalleCotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleCotizacionRep extends JpaRepository<DetalleCotizacion, Long> {

    // Método para buscar detalles por cotización
    List<DetalleCotizacion> findByCotizacionId(Long cotizacionId);
}
