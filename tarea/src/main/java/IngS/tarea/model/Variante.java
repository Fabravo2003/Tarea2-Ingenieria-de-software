package IngS.tarea.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "variante")
public class Variante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_variante")
    private Long id;

    @Column(name = "nombre_variante", nullable = false, length = 100)
    private String nombreVariante;

    @Column(name = "precio_adicional", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioAdicional;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @OneToMany(mappedBy = "variante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCotizacion> detallesCotizacion;

    public Variante() {
    }

    public Variante(String nombreVariante, BigDecimal precioAdicional, String descripcion) {
        this.nombreVariante = nombreVariante;
        this.precioAdicional = precioAdicional;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreVariante() {
        return nombreVariante;
    }

    public void setNombreVariante(String nombreVariante) {
        this.nombreVariante = nombreVariante;
    }

    public BigDecimal getPrecioAdicional() {
        return precioAdicional;
    }

    public void setPrecioAdicional(BigDecimal precioAdicional) {
        this.precioAdicional = precioAdicional;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<DetalleCotizacion> getDetallesCotizacion() {
        return detallesCotizacion;
    }

    public void setDetallesCotizacion(List<DetalleCotizacion> detallesCotizacion) {
        this.detallesCotizacion = detallesCotizacion;
    }
}
