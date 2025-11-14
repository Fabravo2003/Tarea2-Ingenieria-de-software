package IngS.tarea;

import IngS.tarea.model.Mueble;
import IngS.tarea.repository.MuebleRep;
import IngS.tarea.service.impll.MuebleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//TEST 1 MuebleCRUD
@SpringBootTest
@Transactional
public class MuebleCRUDTest {

    @Autowired
    private MuebleService muebleService;

    @Autowired
    private MuebleRep muebleRepository;

    @Test
    public void testCrearMueble() {
        // Test CREATE: Crear un nuevo mueble

        Mueble nuevoMueble = new Mueble();
        nuevoMueble.setNombreMueble("Silla Moderna");
        nuevoMueble.setTipo("silla");
        nuevoMueble.setPrecioBase(new BigDecimal("150.00"));
        nuevoMueble.setStock(20);
        nuevoMueble.setEstado("activo");
        nuevoMueble.setTamanio("Mediano");
        nuevoMueble.setMaterial("Plástico");

        Mueble muebleCreado = muebleService.crearMueble(nuevoMueble);

        // Verificaciones
        assertNotNull(muebleCreado.getId(), "El ID debe ser asignado automáticamente");
        assertEquals("Silla Moderna", muebleCreado.getNombreMueble());
        assertEquals("silla", muebleCreado.getTipo());
        assertEquals(new BigDecimal("150.00"), muebleCreado.getPrecioBase());
        assertEquals(20, muebleCreado.getStock());
        assertEquals("activo", muebleCreado.getEstado());
        assertEquals("Mediano", muebleCreado.getTamanio());
        assertEquals("Plástico", muebleCreado.getMaterial());
    }

    @Test
    public void testCrearMuebleConEstadoPorDefecto() {
        //Si no se especifica estado, debe ser "activo" por defecto

        Mueble mueble = new Mueble();
        mueble.setNombreMueble("Mesa Test");
        mueble.setTipo("mesa");
        mueble.setPrecioBase(new BigDecimal("300.00"));
        mueble.setStock(10);

        Mueble muebleCreado = muebleService.crearMueble(mueble);

        assertEquals("activo", muebleCreado.getEstado(),
                "El estado debe ser 'activo' por defecto");
    }

    @Test
    public void testLeerMueblePorId() {
        // Test READ: Buscar un mueble por ID

        Mueble mueble = crearMuebleEjemplo("Estante Modular", "estante");
        Mueble muebleGuardado = muebleService.crearMueble(mueble);

        Optional<Mueble> muebleEncontrado = muebleService.buscarMueblePorId(muebleGuardado.getId());

        assertTrue(muebleEncontrado.isPresent(), "El mueble debe ser encontrado");
        assertEquals("Estante Modular", muebleEncontrado.get().getNombreMueble());
    }

    @Test
    public void testLeerMuebleInexistente() {
        // Test READ: Buscar un mueble que no existe

        Optional<Mueble> muebleNoEncontrado = muebleService.buscarMueblePorId(9999L);

        assertFalse(muebleNoEncontrado.isPresent(),
                "No debe encontrar un mueble con ID inexistente");
    }

    @Test
    public void testListarTodosLosMuebles() {
        // Test READ: Listar todos los muebles

        muebleService.crearMueble(crearMuebleEjemplo("Silla 1", "silla"));
        muebleService.crearMueble(crearMuebleEjemplo("Mesa 1", "mesa"));
        muebleService.crearMueble(crearMuebleEjemplo("Sillón 1", "sillon"));

        List<Mueble> muebles = muebleService.listarMuebles();

        assertTrue(muebles.size() >= 3,
                "Debe haber al menos 3 muebles en la lista");
    }

    @Test
    public void testListarMueblesActivos() {
        // Test READ: Listar solo muebles activos

        Mueble muebleActivo = crearMuebleEjemplo("Mueble Activo", "silla");
        muebleActivo.setEstado("activo");
        muebleService.crearMueble(muebleActivo);

        Mueble muebleInactivo = crearMuebleEjemplo("Mueble Inactivo", "mesa");
        muebleInactivo.setEstado("inactivo");
        muebleService.crearMueble(muebleInactivo);

        List<Mueble> mueblesActivos = muebleService.listarMueblesActivos();

        assertTrue(mueblesActivos.stream().allMatch(m -> "activo".equals(m.getEstado())),
                "Todos los muebles listados deben estar activos");
    }

    @Test
    public void testBuscarMueblesPorTipo() {
        // Test READ: Buscar muebles por tipo

        muebleService.crearMueble(crearMuebleEjemplo("Silla Tipo 1", "silla"));
        muebleService.crearMueble(crearMuebleEjemplo("Silla Tipo 2", "silla"));
        muebleService.crearMueble(crearMuebleEjemplo("Mesa Tipo 1", "mesa"));

        List<Mueble> sillas = muebleService.buscarPorTipo("silla");

        assertTrue(sillas.size() >= 2, "Debe haber al menos 2 sillas");
        assertTrue(sillas.stream().allMatch(m -> "silla".equals(m.getTipo())),
                "Todos los muebles deben ser del tipo 'silla'");
    }

    @Test
    public void testActualizarMueble() {
        // Test UPDATE: Actualizar datos de un mueble

        Mueble muebleOriginal = crearMuebleEjemplo("Silla Original", "silla");
        muebleOriginal.setPrecioBase(new BigDecimal("100.00"));
        muebleOriginal.setStock(10);
        Mueble muebleGuardado = muebleService.crearMueble(muebleOriginal);

        Mueble datosActualizados = new Mueble();
        datosActualizados.setNombreMueble("Silla Actualizada");
        datosActualizados.setPrecioBase(new BigDecimal("150.00"));
        datosActualizados.setStock(15);
        datosActualizados.setMaterial("Madera Premium");

        Mueble muebleActualizado = muebleService.actualizarMueble(
                muebleGuardado.getId(), datosActualizados);

        // verificaciones
        assertNotNull(muebleActualizado);
        assertEquals("Silla Actualizada", muebleActualizado.getNombreMueble());
        assertEquals(new BigDecimal("150.00"), muebleActualizado.getPrecioBase());
        assertEquals(15, muebleActualizado.getStock());
        assertEquals("Madera Premium", muebleActualizado.getMaterial());
        assertEquals("silla", muebleActualizado.getTipo(),
                "El tipo no debe cambiar si no se especifica");
    }

    @Test
    public void testActualizarMuebleInexistente() {
        // Test UPDATE: intentar actualizar un mueble que no existe

        Mueble datosActualizados = new Mueble();
        datosActualizados.setNombreMueble("Nombre nuevo");

        Mueble resultado = muebleService.actualizarMueble(9999L, datosActualizados);

        assertNull(resultado, "Debe retornar null si el mueble no existe");
    }

    @Test
    public void testDesactivarMueble() {
        // Test DELETE: desactivar un mueble

        Mueble mueble = crearMuebleEjemplo("Mueble a Desactivar", "silla");
        mueble.setEstado("activo");
        Mueble muebleGuardado = muebleService.crearMueble(mueble);

        boolean desactivado = muebleService.desactivarMueble(muebleGuardado.getId());

        assertTrue(desactivado, "El mueble debe ser desactivado exitosamente");

        // verificar que el estado cambió a "inactivo"
        Optional<Mueble> muebleVerificado = muebleService.buscarMueblePorId(muebleGuardado.getId());
        assertTrue(muebleVerificado.isPresent(), "El mueble debe seguir existiendo");
        assertEquals("inactivo", muebleVerificado.get().getEstado(),
                "El estado debe ser 'inactivo'");
    }

    @Test
    public void testDesactivarMuebleInexistente() {
        // Test DELETE: Intentar desactivar un mueble que no existe

        boolean resultado = muebleService.desactivarMueble(9999L);

        assertFalse(resultado, "No debe poder desactivar un mueble inexistente");
    }

    @Test
    public void testVerificarStockDisponible() {
        // Test: Verificar disponibilidad de stock

        Mueble mueble = crearMuebleEjemplo("Mueble Stock", "mesa");
        mueble.setStock(10);
        Mueble muebleGuardado = muebleService.crearMueble(mueble);

        boolean hayStock = muebleService.verificarStockDisponible(muebleGuardado.getId(), 5);
        assertTrue(hayStock, "Debe haber stock para 5 unidades cuando hay 10");

       boolean stockExacto = muebleService.verificarStockDisponible(muebleGuardado.getId(), 10);
        assertTrue(stockExacto, "Debe haber stock para 10 unidades cuando hay 10");

         boolean noHayStock = muebleService.verificarStockDisponible(muebleGuardado.getId(), 15);
        assertFalse(noHayStock, "No debe haber stock para 15 unidades cuando solo hay 10");
    }

    @Test
    public void testActualizarStock() {
        // Test: Actualizar stock (incrementar/decrementar)

        Mueble mueble = crearMuebleEjemplo("Mueble Stock", "cajon");
        mueble.setStock(10);
        Mueble muebleGuardado = muebleService.crearMueble(mueble);

        Mueble muebleIncrementado = muebleService.actualizarStock(muebleGuardado.getId(), 5);
        assertEquals(15, muebleIncrementado.getStock(),
                "El stock debe incrementarse a 15");

        Mueble muebleDecrementado = muebleService.actualizarStock(muebleGuardado.getId(), -3);
        assertEquals(12, muebleDecrementado.getStock(),
                "El stock debe decrementarse a 12");
    }

   private Mueble crearMuebleEjemplo(String nombre, String tipo) {
        Mueble mueble = new Mueble();
        mueble.setNombreMueble(nombre);
        mueble.setTipo(tipo);
        mueble.setPrecioBase(new BigDecimal("100.00"));
        mueble.setStock(10);
        mueble.setEstado("activo");
        mueble.setTamanio("Mediano");
        mueble.setMaterial("Madera");
        return mueble;
    }
}
