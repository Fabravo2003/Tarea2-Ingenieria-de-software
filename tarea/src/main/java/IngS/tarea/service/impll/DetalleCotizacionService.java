package IngS.tarea.service.impll;

import IngS.tarea.model.DetalleCotizacion;
import IngS.tarea.model.Mueble;
import IngS.tarea.model.Variante;
import IngS.tarea.service.IDetalleCotizacionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DetalleCotizacionService implements IDetalleCotizacionService {

    @Override
    public DetalleCotizacion crearDetalleCotizacion(Mueble mueble, Variante variante, Integer cantidad) {
        DetalleCotizacion detalle = new DetalleCotizacion(mueble, variante, cantidad);

        // Calcular precios
        BigDecimal precioUnitario = calcularPrecioUnitario(mueble, variante);
        detalle.setPrecioUnitario(precioUnitario);

        BigDecimal subtotal = calcularSubtotal(precioUnitario, cantidad);
        detalle.setSubtotal(subtotal);

        return detalle;
    }

    @Override
    public BigDecimal calcularPrecioUnitario(Mueble mueble, Variante variante) {
        BigDecimal precioBase = mueble.getPrecioBase();

        if (variante != null) {
            return precioBase.add(variante.getPrecioAdicional());
        }

        return precioBase;
    }

    @Override
    public BigDecimal calcularSubtotal(BigDecimal precioUnitario, Integer cantidad) {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    @Override
    public DetalleCotizacion recalcularPrecios(DetalleCotizacion detalle) {
        BigDecimal precioUnitario = calcularPrecioUnitario(detalle.getMueble(), detalle.getVariante());
        detalle.setPrecioUnitario(precioUnitario);

        BigDecimal subtotal = calcularSubtotal(precioUnitario, detalle.getCantidad());
        detalle.setSubtotal(subtotal);

        return detalle;
    }
}
