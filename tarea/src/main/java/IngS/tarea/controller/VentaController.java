package IngS.tarea.controller;

import IngS.tarea.exception.CotizacionNoEncontradaException;
import IngS.tarea.exception.CotizacionYaConfirmadaException;
import IngS.tarea.exception.StockInsuficienteException;
import IngS.tarea.model.Cotizacion;
import IngS.tarea.service.impll.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping("/confirmar/{cotizacionId}")
    public ResponseEntity<?> confirmarVenta(@PathVariable Long cotizacionId) {
        try {
            Cotizacion venta = ventaService.confirmarVenta(cotizacionId);
            return new ResponseEntity<>(venta, HttpStatus.OK);

        } catch (StockInsuficienteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Stock insuficiente: " + e.getMessage());

        } catch (CotizacionNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (CotizacionYaConfirmadaException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al confirmar venta: " + e.getMessage());
        }
    }

    @GetMapping("/verificar-stock/{cotizacionId}")
    public ResponseEntity<Boolean> verificarStockParaVenta(@PathVariable Long cotizacionId) {
        boolean stockDisponible = ventaService.verificarStockParaVenta(cotizacionId);
        return ResponseEntity.ok(stockDisponible);
    }

    @PostMapping("/cancelar/{ventaId}")
    public ResponseEntity<?> cancelarVenta(@PathVariable Long ventaId) {
        try {
            Cotizacion ventaCancelada = ventaService.cancelarVenta(ventaId);
            return ResponseEntity.ok(ventaCancelada);

        } catch (CotizacionNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al cancelar venta: " + e.getMessage());
        }
    }

    @GetMapping("/total-ingresos")
    public ResponseEntity<BigDecimal> calcularTotalVentas() {
        BigDecimal total = ventaService.calcularTotalVentas();
        return ResponseEntity.ok(total);
    }
}
