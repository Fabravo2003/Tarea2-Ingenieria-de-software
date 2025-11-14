package IngS.tarea.service.impll;

import IngS.tarea.exception.CotizacionNoEncontradaException;
import IngS.tarea.exception.CotizacionYaConfirmadaException;
import IngS.tarea.exception.StockInsuficienteException;
import IngS.tarea.model.Cotizacion;
import IngS.tarea.model.DetalleCotizacion;
import IngS.tarea.model.Mueble;
import IngS.tarea.repository.CotizacionRep;
import IngS.tarea.repository.DetalleCotizacionRep;
import IngS.tarea.repository.MuebleRep;
import IngS.tarea.service.IVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaService implements IVentaService {

    @Autowired
    private CotizacionRep cotizacionRepository;

    @Autowired
    private DetalleCotizacionRep detalleCotizacionRepository;

    @Autowired
    private MuebleRep muebleRepository;

    @Override
    public Cotizacion confirmarVenta(Long cotizacionId) throws Exception {
        Optional<Cotizacion> cotizacionOpt = cotizacionRepository.findById(cotizacionId);
        if (!cotizacionOpt.isPresent()) {
            throw new CotizacionNoEncontradaException("Cotización no encontrada con ID: " + cotizacionId);
        }

        Cotizacion cotizacion = cotizacionOpt.get();

        if (!"pendiente".equals(cotizacion.getEstado())) {
            throw new CotizacionYaConfirmadaException("La cotización ya ha sido confirmada o está en estado: " + cotizacion.getEstado());
        }
        List<DetalleCotizacion> detalles = detalleCotizacionRepository.findByCotizacionId(cotizacionId);

        if (detalles.isEmpty()) {
            throw new Exception("La cotización no tiene productos agregados");
        }
        for (DetalleCotizacion detalle : detalles) {
            Mueble mueble = detalle.getMueble();
            Integer cantidadRequerida = detalle.getCantidad();

            if (mueble.getStock() < cantidadRequerida) {
                throw new StockInsuficienteException(
                    "Stock insuficiente para: " + mueble.getNombreMueble() +
                    ". Stock disponible: " + mueble.getStock() +
                    ", Cantidad requerida: " + cantidadRequerida
                );
            }
        }

        for (DetalleCotizacion detalle : detalles) {
            Mueble mueble = detalle.getMueble();
            Integer cantidadRequerida = detalle.getCantidad();

            mueble.setStock(mueble.getStock() - cantidadRequerida);
            muebleRepository.save(mueble);
        }

        cotizacion.setEstado("confirmada");
        Cotizacion ventaConfirmada = cotizacionRepository.save(cotizacion);

        return ventaConfirmada;
    }

    @Override
    public boolean verificarStockParaVenta(Long cotizacionId) {
        Optional<Cotizacion> cotizacionOpt = cotizacionRepository.findById(cotizacionId);

        if (!cotizacionOpt.isPresent()) {
            return false;
        }

        List<DetalleCotizacion> detalles = detalleCotizacionRepository.findByCotizacionId(cotizacionId);

        for (DetalleCotizacion detalle : detalles) {
            Mueble mueble = detalle.getMueble();
            Integer cantidadRequerida = detalle.getCantidad();

            if (mueble.getStock() < cantidadRequerida) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Cotizacion cancelarVenta(Long ventaId) throws Exception {
        Optional<Cotizacion> ventaOpt = cotizacionRepository.findById(ventaId);

        if (!ventaOpt.isPresent()) {
            throw new CotizacionNoEncontradaException("Venta no encontrada con ID: " + ventaId);
        }

        Cotizacion venta = ventaOpt.get();

        if (!"confirmada".equals(venta.getEstado())) {
            throw new Exception("Solo se pueden cancelar ventas confirmadas. Estado actual: " + venta.getEstado());
        }

        List<DetalleCotizacion> detalles = detalleCotizacionRepository.findByCotizacionId(ventaId);

        for (DetalleCotizacion detalle : detalles) {
            Mueble mueble = detalle.getMueble();
            Integer cantidadDevolver = detalle.getCantidad();

            mueble.setStock(mueble.getStock() + cantidadDevolver);
            muebleRepository.save(mueble);
        }

        venta.setEstado("cancelada");
        return cotizacionRepository.save(venta);
    }

    @Override
    public BigDecimal calcularTotalVentas() {
        List<Cotizacion> ventas = cotizacionRepository.findByEstado("confirmada");

        return ventas.stream()
                .map(Cotizacion::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
