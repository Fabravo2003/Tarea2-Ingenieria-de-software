package IngS.tarea;

import IngS.tarea.model.DetalleCotizacion;
import IngS.tarea.model.Mueble;
import IngS.tarea.model.Variante;
import IngS.tarea.repository.MuebleRep;
import IngS.tarea.repository.VarianteRep;
import IngS.tarea.service.impll.DetalleCotizacionService;
import IngS.tarea.service.impll.VarianteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
//TEST 4 PreciosVariantes
@SpringBootTest
@Transactional
public class PreciosVariantesTest {

    @Autowired
    private VarianteService varianteService;

    @Autowired
    private DetalleCotizacionService detalleCotizacionService;

    @Autowired
    private VarianteRep varianteRepository;

    @Autowired
    private MuebleRep muebleRepository;

    private Mueble muebleBase;
    private Variante varianteNormal;
    private Variante barnizPremium;
    private Variante cojinesSeda;

    @BeforeEach
    public void setUp() {
        // Crear mueble base
        muebleBase = new Mueble();
        muebleBase.setNombreMueble("Silla Test");
        muebleBase.setTipo("silla");
        muebleBase.setPrecioBase(new BigDecimal("100.00"));
        muebleBase.setStock(10);
        muebleBase.setEstado("activo");
        muebleBase.setTamanio("Mediano");
        muebleBase.setMaterial("Madera");
        muebleBase = muebleRepository.save(muebleBase);

        // Crear variante normal
        varianteNormal = new Variante();
        varianteNormal.setNombreVariante("normal");
        varianteNormal.setPrecioAdicional(BigDecimal.ZERO);
        varianteNormal.setDescripcion("Sin variante - precio base");
        varianteNormal = varianteRepository.save(varianteNormal);

        // Crear variante barniz premium
        barnizPremium = new Variante();
        barnizPremium.setNombreVariante("barniz premium");
        barnizPremium.setPrecioAdicional(new BigDecimal("50.00"));
        barnizPremium.setDescripcion("Barniz de alta calidad");
        barnizPremium = varianteRepository.save(barnizPremium);

        // Crear variante cojines de seda
        cojinesSeda = new Variante();
        cojinesSeda.setNombreVariante("cojines de seda");
        cojinesSeda.setPrecioAdicional(new BigDecimal("75.00"));
        cojinesSeda.setDescripcion("Cojines premium de seda");
        cojinesSeda = varianteRepository.save(cojinesSeda);
    }

    @Test
    public void testPrecioConVarianteNormal() {
        // Test: Precio con variante "normal" debe ser igual al precio base
        BigDecimal precioBase = new BigDecimal("100.00");

        BigDecimal precioCalculado = varianteService.calcularPrecioConVariante(
                precioBase, varianteNormal.getId());

        assertEquals(new BigDecimal("100.00"), precioCalculado,
                "El precio con variante normal debe ser igual al precio base");
    }

    @Test
    public void testPrecioSinVariante() {
        // Test: Precio sin variante (null) debe ser igual al precio base
        BigDecimal precioBase = new BigDecimal("100.00");

        BigDecimal precioCalculado = varianteService.calcularPrecioConVariante(
                precioBase, null);

        assertEquals(new BigDecimal("100.00"), precioCalculado,
                "El precio sin variante debe ser igual al precio base");
    }

    @Test
    public void testPrecioConBarnizPremium() {
        // Test: Precio con barniz premium debe sumar $50
        BigDecimal precioBase = new BigDecimal("100.00");

        BigDecimal precioCalculado = varianteService.calcularPrecioConVariante(
                precioBase, barnizPremium.getId());

        assertEquals(new BigDecimal("150.00"), precioCalculado,
                "El precio con barniz premium debe ser precio base + $50");
    }

    @Test
    public void testPrecioConCojinesSeda() {
        // Test: Precio con cojines de seda debe sumar $75
        BigDecimal precioBase = new BigDecimal("100.00");

        BigDecimal precioCalculado = varianteService.calcularPrecioConVariante(
                precioBase, cojinesSeda.getId());

        assertEquals(new BigDecimal("175.00"), precioCalculado,
                "El precio con cojines de seda debe ser precio base + $75");
    }

    @Test
    public void testCalculoPrecioUnitarioEnDetalle() {
        // Test: DetalleCotizacionService calcula correctamente el precio unitario

        BigDecimal precioUnitario = detalleCotizacionService.calcularPrecioUnitario(
                muebleBase, barnizPremium);

        assertEquals(new BigDecimal("150.00"), precioUnitario,
                "El precio unitario debe ser precio base + precio adicional de variante");
    }

    @Test
    public void testCalculoSubtotal() {
        // Test: Calcular subtotal correctamente (precio unitario × cantidad)
        BigDecimal precioUnitario = new BigDecimal("150.00");
        Integer cantidad = 3;

        BigDecimal subtotal = detalleCotizacionService.calcularSubtotal(
                precioUnitario, cantidad);

        assertEquals(new BigDecimal("450.00"), subtotal,
                "El subtotal debe ser precio unitario × cantidad");
    }

    @Test
    public void testDetalleCotizacionConVariante() {
        // Test: DetalleCotizacion calcula correctamente todos los precios
        Integer cantidad = 2;

        DetalleCotizacion detalle = detalleCotizacionService.crearDetalleCotizacion(
                muebleBase, barnizPremium, cantidad);

        // Verificar precio unitario (100 + 50 = 150)
        assertEquals(new BigDecimal("150.00"), detalle.getPrecioUnitario(),
                "El precio unitario debe ser $150");

        // Verificar subtotal (150 × 2 = 300)
        assertEquals(new BigDecimal("300.00"), detalle.getSubtotal(),
                "El subtotal debe ser $300");
    }

    @Test
    public void testDetalleCotizacionSinVariante() {
        // Test: DetalleCotizacion sin variante usa solo precio base
        Integer cantidad = 4;

        DetalleCotizacion detalle = detalleCotizacionService.crearDetalleCotizacion(
                muebleBase, null, cantidad);

        // Verificar precio unitario (100)
        assertEquals(new BigDecimal("100.00"), detalle.getPrecioUnitario(),
                "El precio unitario sin variante debe ser $100");

        // Verificar subtotal (100 × 4 = 400)
        assertEquals(new BigDecimal("400.00"), detalle.getSubtotal(),
                "El subtotal debe ser $400");
    }

    @Test
    public void testRecalcularPreciosAlCambiarCantidad() {
        // Test: Recalcular precios cuando cambia la cantidad
        DetalleCotizacion detalle = detalleCotizacionService.crearDetalleCotizacion(
                muebleBase, cojinesSeda, 2);

        detalle.setCantidad(5);

        detalle = detalleCotizacionService.recalcularPrecios(detalle);

        assertEquals(new BigDecimal("875.00"), detalle.getSubtotal(),
                "El subtotal recalculado debe ser $875");
    }

    @Test
    public void testObtenerVarianteNormal() {
        // Test: Obtener o crear variante "normal"
        Variante normal = varianteService.obtenerVarianteNormal();

        assertNotNull(normal, "La variante normal debe existir");
        assertEquals("normal", normal.getNombreVariante(),
                "El nombre debe ser 'normal'");
        assertEquals(BigDecimal.ZERO, normal.getPrecioAdicional(),
                "El precio adicional debe ser 0");
    }
}
