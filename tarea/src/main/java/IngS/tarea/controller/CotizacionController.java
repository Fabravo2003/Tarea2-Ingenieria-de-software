package IngS.tarea.controller;

import IngS.tarea.model.Cotizacion;
import IngS.tarea.model.DetalleCotizacion;
import IngS.tarea.service.impll.CotizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cotizacion")
public class CotizacionController {

    @Autowired
    private CotizacionService cotizacionService;
    
    
    //CREATE
    @PostMapping
    public ResponseEntity<Cotizacion> crearCotizacion() {
        Cotizacion cotizacion = cotizacionService.crearCotizacion();
        return new ResponseEntity<>(cotizacion, HttpStatus.CREATED);
    }


    @PostMapping("/{cotizacionId}/agregar-mueble")
    public ResponseEntity<?> agregarMueble(
            @PathVariable Long cotizacionId,
            @RequestBody Map<String, Object> request) {

        try {
            Long muebleId = Long.valueOf(request.get("muebleId").toString());
            Long varianteId = request.get("varianteId") != null ? Long.valueOf(request.get("varianteId").toString()) : null;
            Integer cantidad = Integer.valueOf(request.get("cantidad").toString());

            Cotizacion cotizacion = cotizacionService.agregarMuebleACotizacion(cotizacionId, muebleId, varianteId, cantidad);
            return new ResponseEntity<>(cotizacion, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    //READ
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCotizacion(@PathVariable Long id) {
        Optional<Cotizacion> cotizacionOpt = cotizacionService.obtenerCotizacionPorId(id);

        if (cotizacionOpt.isPresent()) {
            return new ResponseEntity<>(cotizacionOpt.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cotización con ID " + id + " no encontrada");
        }
    }

    @GetMapping
    public ResponseEntity<List<Cotizacion>> listarCotizaciones() {
        List<Cotizacion> cotizaciones = cotizacionService.listarCotizaciones();
        return ResponseEntity.ok(cotizaciones);
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<Cotizacion>> listarCotizacionesPendientes() {
        List<Cotizacion> cotizaciones = cotizacionService.listarCotizacionesPendientes();
        return ResponseEntity.ok(cotizaciones);
    }

    @GetMapping("/ventas")
    public ResponseEntity<List<Cotizacion>> listarVentas() {
        List<Cotizacion> ventas = cotizacionService.listarVentas();
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/{id}/detalles")
    public ResponseEntity<List<DetalleCotizacion>> obtenerDetalles(@PathVariable Long id) {
        List<DetalleCotizacion> detalles = cotizacionService.obtenerDetallesCotizacion(id);
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/{id}/total")
    public ResponseEntity<BigDecimal> calcularTotal(@PathVariable Long id) {
        BigDecimal total = cotizacionService.calcularTotalCotizacion(id);
        return ResponseEntity.ok(total);
    }

    @DeleteMapping("/{cotizacionId}/detalles/{detalleId}")
    public ResponseEntity<?> eliminarMueble(@PathVariable Long cotizacionId, @PathVariable Long detalleId) {

        Cotizacion cotizacion = cotizacionService.eliminarMuebleDeCotizacion(cotizacionId, detalleId);

        if (cotizacion != null) {
            return ResponseEntity.ok(cotizacion);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo eliminar el detalle");
        }
    }

    @PutMapping("/detalles/{detalleId}")
    public ResponseEntity<?> actualizarCantidad(@PathVariable Long detalleId, @RequestBody Map<String, Integer> request) {

        Integer nuevaCantidad = request.get("cantidad");

        if (nuevaCantidad == null || nuevaCantidad <= 0) {
            return ResponseEntity.badRequest().body("La cantidad debe ser mayor a 0");
        }

        DetalleCotizacion detalle = cotizacionService.actualizarCantidad(detalleId, nuevaCantidad);

        if (detalle != null) {
            return ResponseEntity.ok(detalle);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Detalle con ID " + detalleId + " no encontrado");
        }
    }
}
