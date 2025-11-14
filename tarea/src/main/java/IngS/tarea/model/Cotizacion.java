package IngS.tarea.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cotizacion")
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_cotizacion")
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCotizacion> detalles = new ArrayList<>();

    public Cotizacion() {
        this.fecha = LocalDateTime.now();
        this.estado = "pendiente";
        this.total = BigDecimal.ZERO;
    }

    public Cotizacion(String estado) {
        this.fecha = LocalDateTime.now();
        this.estado = estado;
        this.total = BigDecimal.ZERO;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<DetalleCotizacion> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCotizacion> detalles) {
        this.detalles = detalles;
    }
}
