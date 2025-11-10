package Domain.Dtos.mantenimiento;

import java.util.Date;

public class UpdateMantRequestDto {

    private Date fecha;
    private String descripcion;
    private String tipo;
    private Long id;

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

    public UpdateMantRequestDto() {
    }

    public UpdateMantRequestDto(Date fecha, String descripcion, String tipo, Long id) {
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
