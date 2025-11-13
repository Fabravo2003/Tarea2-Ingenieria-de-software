package IngS.tarea.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "mueble")
public class Mueble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_mueble")
    private Long id;

    @Column(name = "nombre_mueble", nullable = false, length = 100)
    private String nombreMueble;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo; // sillas, sillones, mesas, estantes, cajones

    @Column(name = "precio_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioBase;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado; // activo, inactivo

    @Column(name = "tamanio", length = 20)
    private String tamanio; // Grande, Mediano, Pequeño

    @Column(name = "material", length = 100)
    private String material;

    @OneToMany(mappedBy = "mueble", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCotizacion> detallesCotizacion;

    // Constructores
    public Mueble() {
    }

    public Mueble(String nombreMueble, String tipo, BigDecimal precioBase, Integer stock, String estado, String tamanio, String material) {
        this.nombreMueble = nombreMueble;
        this.tipo = tipo;
        this.precioBase = precioBase;
        this.stock = stock;
        this.estado = estado;
        this.tamanio = tamanio;
        this.material = material;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreMueble() {
        return nombreMueble;
    }

    public void setNombreMueble(String nombreMueble) {
        this.nombreMueble = nombreMueble;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(BigDecimal precioBase) {
        this.precioBase = precioBase;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public List<DetalleCotizacion> getDetallesCotizacion() {
        return detallesCotizacion;
    }

    public void setDetallesCotizacion(List<DetalleCotizacion> detallesCotizacion) {
        this.detallesCotizacion = detallesCotizacion;
    }
}
