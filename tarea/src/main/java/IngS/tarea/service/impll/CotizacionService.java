package IngS.tarea.service.impll;

import IngS.tarea.model.Cotizacion;
import IngS.tarea.model.DetalleCotizacion;
import IngS.tarea.model.Mueble;
import IngS.tarea.model.Variante;
import IngS.tarea.repository.CotizacionRep;
import IngS.tarea.repository.DetalleCotizacionRep;
import IngS.tarea.repository.MuebleRep;
import IngS.tarea.repository.VarianteRep;
import IngS.tarea.service.ICotizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CotizacionService implements ICotizacionService {

    @Autowired
    private CotizacionRep cotizacionRepository;

    @Autowired
    private DetalleCotizacionRep detalleCotizacionRepository;

    @Autowired
    private MuebleRep muebleRepository;

    @Autowired
    private VarianteRep varianteRepository;

    @Autowired
    private DetalleCotizacionService detalleCotizacionService;

    @Override
    public Cotizacion crearCotizacion() {
        Cotizacion cotizacion = new Cotizacion();
        return cotizacionRepository.save(cotizacion);
    }

    @Override
    public Cotizacion agregarMuebleACotizacion(Long cotizacionId, Long muebleId, Long varianteId, Integer cantidad) {
        Optional<Cotizacion> cotizacionOpt = cotizacionRepository.findById(cotizacionId);
        if (!cotizacionOpt.isPresent()) {
            throw new RuntimeException("Cotización no encontrada con ID: " + cotizacionId);
        }

        Optional<Mueble> muebleOpt = muebleRepository.findById(muebleId);
        if (!muebleOpt.isPresent()) {
            throw new RuntimeException("Mueble no encontrado con ID: " + muebleId);
        }

        Variante variante = null;
        if (varianteId != null) {
            Optional<Variante> varianteOpt = varianteRepository.findById(varianteId);
            if (!varianteOpt.isPresent()) {
                throw new RuntimeException("Variante no encontrada con ID: " + varianteId);
            }
            variante = varianteOpt.get();
        }

        Cotizacion cotizacion = cotizacionOpt.get();
        Mueble mueble = muebleOpt.get();
        DetalleCotizacion detalle = detalleCotizacionService.crearDetalleCotizacion(mueble, variante, cantidad);
        detalle.setCotizacion(cotizacion);
        detalleCotizacionRepository.save(detalle);
        cotizacion = calcularYActualizarTotal(cotizacion);
        return cotizacion;
    }

    @Override
    public BigDecimal calcularTotalCotizacion(Long cotizacionId) {
        Optional<Cotizacion> cotizacionOpt = cotizacionRepository.findById(cotizacionId);

        if (cotizacionOpt.isPresent()) {
            Cotizacion cotizacion = cotizacionOpt.get();
            List<DetalleCotizacion> detalles = detalleCotizacionRepository.findByCotizacionId(cotizacionId);

            BigDecimal total = detalles.stream()
                    .map(DetalleCotizacion::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            cotizacion.setTotal(total);
            cotizacionRepository.save(cotizacion);

            return total;
        }

        return BigDecimal.ZERO;
    }

    @Override
    public Optional<Cotizacion> obtenerCotizacionPorId(Long id) {
        return cotizacionRepository.findById(id);
    }

    @Override
    public List<Cotizacion> listarCotizaciones() {
        return cotizacionRepository.findAll();
    }

    @Override
    public List<Cotizacion> listarCotizacionesPendientes() {
        return cotizacionRepository.findByEstado("pendiente");
    }

    @Override
    public List<Cotizacion> listarVentas() {
        return cotizacionRepository.findByEstado("confirmada");
    }

    @Override
    public List<DetalleCotizacion> obtenerDetallesCotizacion(Long cotizacionId) {
        return detalleCotizacionRepository.findByCotizacionId(cotizacionId);
    }

    @Override
    public Cotizacion eliminarMuebleDeCotizacion(Long cotizacionId, Long detalleId) {
        Optional<Cotizacion> cotizacionOpt = cotizacionRepository.findById(cotizacionId);
        Optional<DetalleCotizacion> detalleOpt = detalleCotizacionRepository.findById(detalleId);

        if (cotizacionOpt.isPresent() && detalleOpt.isPresent()) {
            DetalleCotizacion detalle = detalleOpt.get();

            if (detalle.getCotizacion().getId().equals(cotizacionId)) {
                detalleCotizacionRepository.deleteById(detalleId);
                Cotizacion cotizacion = cotizacionOpt.get();
                return calcularYActualizarTotal(cotizacion);
            }
        }

        return null;
    }

    @Override
    public DetalleCotizacion actualizarCantidad(Long detalleId, Integer nuevaCantidad) {
        Optional<DetalleCotizacion> detalleOpt = detalleCotizacionRepository.findById(detalleId);

        if (detalleOpt.isPresent()) {
            DetalleCotizacion detalle = detalleOpt.get();
            detalle.setCantidad(nuevaCantidad);

            detalle = detalleCotizacionService.recalcularPrecios(detalle);
            detalleCotizacionRepository.save(detalle);

            calcularYActualizarTotal(detalle.getCotizacion());

            return detalle;
        }

        return null;
    }

    private Cotizacion calcularYActualizarTotal(Cotizacion cotizacion) {
        List<DetalleCotizacion> detalles = detalleCotizacionRepository.findByCotizacionId(cotizacion.getId());

        BigDecimal total = detalles.stream()
                .map(DetalleCotizacion::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cotizacion.setTotal(total);
        return cotizacionRepository.save(cotizacion);
    }
}
