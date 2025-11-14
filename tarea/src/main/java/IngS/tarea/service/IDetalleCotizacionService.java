package IngS.tarea.service;

import IngS.tarea.model.DetalleCotizacion;
import IngS.tarea.model.Mueble;
import IngS.tarea.model.Variante;
import java.math.BigDecimal;

public interface IDetalleCotizacionService {
    DetalleCotizacion crearDetalleCotizacion(Mueble mueble, Variante variante, Integer cantidad);
    BigDecimal calcularPrecioUnitario(Mueble mueble, Variante variante);
    BigDecimal calcularSubtotal(BigDecimal precioUnitario, Integer cantidad);
    DetalleCotizacion recalcularPrecios(DetalleCotizacion detalle);
}
