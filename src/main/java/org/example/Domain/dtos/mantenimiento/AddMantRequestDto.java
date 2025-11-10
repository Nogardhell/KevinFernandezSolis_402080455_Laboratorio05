package org.example.Domain.dtos.mantenimiento;

import java.util.Date;

public class AddMantRequestDto {

    private Date fecha;
    private String descripcion;
    private String tipo;
    private Long carId;

    public AddMantRequestDto(Date fecha, String descripcion, String tipo, Long carId) {
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.carId = carId;
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

    public Long getCarId() {
        return carId;
    }

    public void setCar(Long carId) {
        this.carId = carId;
    }
}