package org.example.Domain.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "mantenimiento")
public class Mantenimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha", updatable = false)
    private Date fecha;

    @Column(nullable = false, length = 50)
    private String descripcion;   // e.g., Toyota, Subaru, Honda

    @Column(nullable = false, length = 50)
    private String  tipo ;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // Each car belongs to one user
    @JoinColumn(name = "car_id", nullable = false, foreignKey = @ForeignKey(name = "fk_mantenimiento_car"))
    private Car car;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
