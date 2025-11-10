package org.example.Domain.dtos.mantenimiento;

public class DeleteMantRequestDto {

    private Long id;

    public DeleteMantRequestDto() {}
    public DeleteMantRequestDto(Long id) { this.id = id; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

}
