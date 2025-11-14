package IngS.tarea.controller;

import IngS.tarea.model.Variante;
import IngS.tarea.service.impll.VarianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/variantes")
public class VarianteController {


    @Autowired
    private VarianteService varianteService;
    //CREATE
    @PostMapping
    public ResponseEntity<Variante> crearVariante(@RequestBody Variante variante) {
        Variante nuevaVariante = varianteService.crearVariante(variante);
        return new ResponseEntity<>(nuevaVariante, HttpStatus.CREATED);
    }

    //READ
    @GetMapping
    public ResponseEntity<List<Variante>> listarVariantes() {
        List<Variante> variantes = varianteService.listarVariantes();
        return new ResponseEntity<>(variantes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarVariantePorId(@PathVariable Long id) {
        Optional<Variante> varianteOpt = varianteService.buscarVariantePorId(id);

        if (varianteOpt.isPresent()) {
            return new ResponseEntity<>(varianteOpt.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Variante con ID " + id + " no encontrada");
        }
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> buscarVariantePorNombre(@PathVariable String nombre) {
        Optional<Variante> varianteOpt = varianteService.buscarVariantePorNombre(nombre);

        if (varianteOpt.isPresent()) {
            return new ResponseEntity<>(varianteOpt.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Variante con nombre '" + nombre + "' no encontrada");
        }
    }

    @GetMapping("/calcular-precio")
    public ResponseEntity<BigDecimal> calcularPrecioConVariante(
            @RequestParam BigDecimal precioBase,
            @RequestParam(required = false) Long varianteId) {

        BigDecimal precioFinal = varianteService.calcularPrecioConVariante(precioBase, varianteId);
        return ResponseEntity.ok(precioFinal);
    }

    @GetMapping("/normal")
    public ResponseEntity<Variante> obtenerVarianteNormal() {
        Variante normal = varianteService.obtenerVarianteNormal();
        return ResponseEntity.ok(normal);
    }
    //UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVariante(@PathVariable Long id, @RequestBody Variante variante) {
        Variante varianteActualizada = varianteService.actualizarVariante(id, variante);

        if (varianteActualizada != null) {
            return new ResponseEntity<>(varianteActualizada, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Variante con ID " + id + " no encontrada");
        }
    }
    //"DELETE"
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarVariante(@PathVariable Long id) {
        boolean eliminado = varianteService.eliminarVariante(id);

        if (eliminado) {
            return ResponseEntity.ok("Variante con ID " + id + " eliminada correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Variante con ID " + id + " no encontrada");
        }
    }
}
