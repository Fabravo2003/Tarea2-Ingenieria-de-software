package IngS.tarea.service;

import IngS.tarea.model.Mueble;
import java.util.List;
import java.util.Optional;

public interface IMuebleService {

    /*
    3. Gesti´on de cat´alogo: la tabla mueble contiene estos atributos: ID mueble,
    nombre mueble, tipo, precio base, stock, estado (activo, inactivo),
    tama~no (Grande, Mediano, Peque~no) y material, esto con el fin de
    poder crear, listar (leer), actualizar y desactivar los muebles del cat´alogo
    (CRUD).*/
    Mueble crearMueble(Mueble mueble);
    List<Mueble> listarMuebles();
    List<Mueble> listarMueblesActivos();
    Optional<Mueble> buscarMueblePorId(Long id);
    Mueble actualizarMueble(Long id, Mueble mueble);
    boolean desactivarMueble(Long id);
    List<Mueble> buscarPorTipo(String tipo);
    boolean verificarStockDisponible(Long id, Integer cantidad);
    Mueble actualizarStock(Long id, Integer cantidad);
}
