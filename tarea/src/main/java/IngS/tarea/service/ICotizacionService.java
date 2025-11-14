package IngS.tarea.service;

import IngS.tarea.model.Cotizacion;
import IngS.tarea.model.DetalleCotizacion;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ICotizacionService {
    /*5. Cotizaciones y ventas: Crear una cotizaci´on de uno o m´as muebles (mue-
ble, variante y cantidad). Importante confirmar una cotizaci´on como
venta, decrementando stock.*/

    Cotizacion crearCotizacion();
    Cotizacion agregarMuebleACotizacion(Long cotizacionId, Long muebleId, Long varianteId, Integer cantidad);
    BigDecimal calcularTotalCotizacion(Long cotizacionId);
    Optional<Cotizacion> obtenerCotizacionPorId(Long id);
    List<Cotizacion> listarCotizaciones();
    List<Cotizacion> listarCotizacionesPendientes();
    List<Cotizacion> listarVentas();
    List<DetalleCotizacion> obtenerDetallesCotizacion(Long cotizacionId);
    Cotizacion eliminarMuebleDeCotizacion(Long cotizacionId, Long detalleId);
    DetalleCotizacion actualizarCantidad(Long detalleId, Integer nuevaCantidad);
}
