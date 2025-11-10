package org.example.Domain.dtos.mantenimiento;

import org.example.Domain.dtos.cars.CarResponseDto;

import java.util.Date;

public class MantResponseDto {

    private Long id;
    private Date fecha;
    private String descripcion;
    private String tipo;
    private CarResponseDto carId;

    public MantResponseDto() {
    }

    public MantResponseDto(Long id, Date fecha, String descripcion, String tipo, CarResponseDto carId) {
        this.id = id;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.carId = carId;
    }

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

    public CarResponseDto getCarId() {
        return carId;
    }

    public void setCarId(CarResponseDto carId) {
        this.carId = carId;
    }
}
