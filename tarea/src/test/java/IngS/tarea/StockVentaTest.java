package IngS.tarea;

import IngS.tarea.exception.StockInsuficienteException;
import IngS.tarea.model.Cotizacion;
import IngS.tarea.model.Mueble;
import IngS.tarea.repository.MuebleRep;
import IngS.tarea.service.impll.CotizacionService;
import IngS.tarea.service.impll.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

//TEST 5 StockVenta
@SpringBootTest
@Transactional
public class StockVentaTest {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private CotizacionService cotizacionService;

    @Autowired
    private MuebleRep muebleRepository;

    private Mueble muebleConStock;
    private Mueble muebleSinStock;

    @BeforeEach
    public void setUp() {
        // crear mueble con stock suficiente
        muebleConStock = new Mueble();
        muebleConStock.setNombreMueble("Mesa con stock");
        muebleConStock.setTipo("mesa");
        muebleConStock.setPrecioBase(new BigDecimal("200.00"));
        muebleConStock.setStock(10);  // Stock suficiente
        muebleConStock.setEstado("activo");
        muebleConStock.setTamanio("Grande");
        muebleConStock.setMaterial("Madera");
        muebleConStock = muebleRepository.save(muebleConStock);

        // Crear mueble con stock insuficiente
        muebleSinStock = new Mueble();
        muebleSinStock.setNombreMueble("Silla sin stock");
        muebleSinStock.setTipo("silla");
        muebleSinStock.setPrecioBase(new BigDecimal("100.00"));
        muebleSinStock.setStock(2);  // Stock insuficiente para el test
        muebleSinStock.setEstado("activo");
        muebleSinStock.setTamanio("Mediano");
        muebleSinStock.setMaterial("Metal");
        muebleSinStock = muebleRepository.save(muebleSinStock);
    }

    @Test
    public void testVentaConStockInsuficiente() {
       
        // Crear cotización
        Cotizacion cotizacion = cotizacionService.crearCotizacion();

        // Agregar mueble que tiene solo 2 unidades de stock, pero pedir 5
        cotizacion = cotizacionService.agregarMuebleACotizacion(
                cotizacion.getId(),
                muebleSinStock.getId(),
                null,  
                5      
        );

        Long cotizacionId = cotizacion.getId();

        assertThrows(StockInsuficienteException.class, () -> {
            ventaService.confirmarVenta(cotizacionId);
        }, "Debe lanzar StockInsuficienteException cuando no hay stock suficiente");

        // Verificar que el stock NO se decrementó (la venta no se realizó)
        Mueble muebleVerificado = muebleRepository.findById(muebleSinStock.getId()).get();
        assertEquals(2, muebleVerificado.getStock(),
                "El stock no debe cambiar si la venta falla");

        // Verificar que la cotización sigue en estado "pendiente"
        Cotizacion cotizacionVerificada = cotizacionService.obtenerCotizacionPorId(cotizacionId).get();
        assertEquals("pendiente", cotizacionVerificada.getEstado(),
                "La cotización debe seguir pendiente si la venta falla");
    }

        //test venta exitosa decrementa stock
    @Test
    public void testVentaExitosaDecrementaStock() throws Exception {
        

        int stockInicial = muebleConStock.getStock();
        assertEquals(10, stockInicial, "El stock inicial debe ser 10");

        Cotizacion cotizacion = cotizacionService.crearCotizacion();
        cotizacion = cotizacionService.agregarMuebleACotizacion(
                cotizacion.getId(),
                muebleConStock.getId(),
                null,
                3  
        );

        
        Cotizacion ventaConfirmada = ventaService.confirmarVenta(cotizacion.getId());

        assertEquals("confirmada", ventaConfirmada.getEstado(),
                "El estado debe cambiar a 'confirmada'");

        Mueble muebleActualizado = muebleRepository.findById(muebleConStock.getId()).get();
        assertEquals(7, muebleActualizado.getStock(),
                "El stock debe decrementarse de 10 a 7");
    }

    //Test verificar stock disponible
    @Test
    public void testVerificarStockDisponible() {
        Cotizacion cotizacionConStock = cotizacionService.crearCotizacion();
        cotizacionConStock = cotizacionService.agregarMuebleACotizacion(
                cotizacionConStock.getId(),
                muebleConStock.getId(),
                null,
                5  
        );

        boolean stockDisponible = ventaService.verificarStockParaVenta(cotizacionConStock.getId());
        assertTrue(stockDisponible,
                "Debe haber stock disponible para 5 unidades cuando hay 10 en stock");

        // Crear cotización SIN stock disponible
        Cotizacion cotizacionSinStock = cotizacionService.crearCotizacion();
        cotizacionSinStock = cotizacionService.agregarMuebleACotizacion(
                cotizacionSinStock.getId(),
                muebleSinStock.getId(),
                null,
                10  
        );

        boolean stockNoDisponible = ventaService.verificarStockParaVenta(cotizacionSinStock.getId());
        assertFalse(stockNoDisponible,
                "NO debe haber stock disponible para 10 unidades cuando solo hay 2 en stock");
    }

        //test venta multiples muebles con stock insuficiente
    @Test
    public void testVentaMultiplesMueblesConStockInsuficiente() {
        

        Cotizacion cotizacion = cotizacionService.crearCotizacion();

        cotizacion = cotizacionService.agregarMuebleACotizacion(
                cotizacion.getId(),
                muebleConStock.getId(),
                null,
                2
        );

        cotizacion = cotizacionService.agregarMuebleACotizacion(
                cotizacion.getId(),
                muebleSinStock.getId(),
                null,
                5  
        );

        Long cotizacionId = cotizacion.getId();

        assertThrows(StockInsuficienteException.class, () -> {
            ventaService.confirmarVenta(cotizacionId);
        });

        Mueble mueble1 = muebleRepository.findById(muebleConStock.getId()).get();
        Mueble mueble2 = muebleRepository.findById(muebleSinStock.getId()).get();

        assertEquals(10, mueble1.getStock(),
                "El stock del primer mueble no debe cambiar si la venta falla");
        assertEquals(2, mueble2.getStock(),
                "El stock del segundo mueble no debe cambiar");
    }

        //test venta decrementa stock exacto a 0
    @Test
    public void testVentaDecrementaStockExacto() throws Exception {
        
        Cotizacion cotizacion = cotizacionService.crearCotizacion();
        cotizacion = cotizacionService.agregarMuebleACotizacion(
                cotizacion.getId(),
                muebleSinStock.getId(),  
                null,
                2  
        );

        ventaService.confirmarVenta(cotizacion.getId());

        // Verificar que el stock quedó en 0
        Mueble mueble = muebleRepository.findById(muebleSinStock.getId()).get();
        assertEquals(0, mueble.getStock(),
                "El stock debe quedar en 0 después de vender todas las unidades");
    }

        //test stock no puede ser negativo
    @Test
    public void testStockNoPuedeSerNegativo() {
        Cotizacion cotizacion = cotizacionService.crearCotizacion();
        cotizacion = cotizacionService.agregarMuebleACotizacion(
                cotizacion.getId(),
                muebleSinStock.getId(),
                null,
                100  
        );

        Long cotizacionId = cotizacion.getId();

        assertThrows(StockInsuficienteException.class, () -> {
            ventaService.confirmarVenta(cotizacionId);
        });

        Mueble mueble = muebleRepository.findById(muebleSinStock.getId()).get();
        assertTrue(mueble.getStock() >= 0,
                "El stock nunca debe ser negativo");
        assertEquals(2, mueble.getStock(),
                "El stock debe mantenerse en su valor original");
    }

    
        //test mensaje de error stock insuficiente
    @Test
    public void testMensajeDeErrorStockInsuficiente() {

        Cotizacion cotizacion = cotizacionService.crearCotizacion();
        cotizacion = cotizacionService.agregarMuebleACotizacion(
                cotizacion.getId(),
                muebleSinStock.getId(),
                null,
                10
        );

        Long cotizacionId = cotizacion.getId();

        Exception exception = assertThrows(StockInsuficienteException.class, () -> {
            ventaService.confirmarVenta(cotizacionId);
        });

        String mensajeError = exception.getMessage();

        assertTrue(mensajeError.contains("Stock insuficiente"),
                "El mensaje debe contener 'Stock insuficiente'");
        assertTrue(mensajeError.contains("Silla sin stock"),
                "El mensaje debe contener el nombre del mueble");
    }
}
