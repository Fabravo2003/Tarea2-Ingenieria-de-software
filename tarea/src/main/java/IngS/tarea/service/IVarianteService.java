package IngS.tarea.service;

import IngS.tarea.model.Variante;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IVarianteService {

    /*Variantes: Registrar variaciones (como por ejemplo: barniz premium, co-
    jines de seda o ruedas), esto con el fin de que modifiquen y aumenten el
    precio del producto. Estas variaciones si es que no hay variaciones del
    producto, se debe marcar como normal y se mantiene el precio base del
    muebl*/
    Variante crearVariante(Variante variante);
    List<Variante> listarVariantes();
    Optional<Variante> buscarVariantePorId(Long id);
    Optional<Variante> buscarVariantePorNombre(String nombre);
    BigDecimal calcularPrecioConVariante(BigDecimal precioBase, Long varianteId);
    Variante obtenerVarianteNormal();
    Variante actualizarVariante(Long id, Variante variante);
    boolean eliminarVariante(Long id);
}
