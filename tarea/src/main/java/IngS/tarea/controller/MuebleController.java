package IngS.tarea.controller;

import IngS.tarea.model.Mueble;
import IngS.tarea.service.impll.MuebleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/muebles")
public class MuebleController {

    @Autowired
    private MuebleService muebleService;

    //Create
    @PostMapping
    public ResponseEntity<Mueble> crearMueble(@RequestBody Mueble mueble) {
        Mueble nuevoMueble = muebleService.crearMueble(mueble);
        return new ResponseEntity<>(nuevoMueble, HttpStatus.CREATED);
    }

    //READ
    @GetMapping
    public ResponseEntity<List<Mueble>> listarMuebles() {
        List<Mueble> muebles = muebleService.listarMuebles();
        return new ResponseEntity<>(muebles, HttpStatus.OK);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Mueble>> listarMueblesActivos() {
        List<Mueble> muebles = muebleService.listarMueblesActivos();
        return new ResponseEntity<>(muebles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarMueblePorId(@PathVariable Long id) {
        Optional<Mueble> muebleOpt = muebleService.buscarMueblePorId(id);

        if (muebleOpt.isPresent()) {
            return new ResponseEntity<>(muebleOpt.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mueble no encontrado");
        }
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Mueble>> buscarPorTipo(@PathVariable String tipo) {
        List<Mueble> muebles = muebleService.buscarPorTipo(tipo);
        return new ResponseEntity<>(muebles, HttpStatus.OK);
    }

    //UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMueble(@PathVariable Long id, @RequestBody Mueble mueble) {
        Mueble muebleActualizado = muebleService.actualizarMueble(id, mueble);

        if (muebleActualizado != null) {
            return new ResponseEntity<>(muebleActualizado, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mueble no encontrado");
        }
    }

    //"DELETE" en verdad es desactivar 
    @DeleteMapping("/{id}")
    public ResponseEntity<String> desactivarMueble(@PathVariable Long id) {
        boolean desactivado = muebleService.desactivarMueble(id);

        if (desactivado) {
            return ResponseEntity.ok("Mueble desactivado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mueble noencontrado");
        }
    }

    @GetMapping("/{id}/stock/{cantidad}")
    public ResponseEntity<Boolean> verificarStock(@PathVariable Long id, @PathVariable Integer cantidad) {
        boolean stockDisponible = muebleService.verificarStockDisponible(id, cantidad);
        return ResponseEntity.ok(stockDisponible);
    }
}
