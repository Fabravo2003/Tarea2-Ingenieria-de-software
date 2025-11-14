package IngS.tarea.service;

import IngS.tarea.model.Cotizacion;

public interface IVentaService {

    /*5. Cotizaciones y ventas: Crear una cotizaci´on de uno o m´as muebles (mue-
    ble, variante y cantidad). Importante confirmar una cotizaci´on como
    venta, decrementando stock.*/

    Cotizacion confirmarVenta(Long cotizacionId) throws Exception;
    boolean verificarStockParaVenta(Long cotizacionId);
    Cotizacion cancelarVenta(Long ventaId) throws Exception;
    java.math.BigDecimal calcularTotalVentas();
}
