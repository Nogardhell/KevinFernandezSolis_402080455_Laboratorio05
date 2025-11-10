package org.example.Domain.dtos.mantenimiento;

import java.util.List;

public class ListMantResponseDto {
    private List<MantResponseDto> mantenimientos;

    public ListMantResponseDto() {
    }

    public ListMantResponseDto(List<MantResponseDto> mantenimientos) {
        this.mantenimientos = mantenimientos;
    }

    public List<MantResponseDto> getMantenimientos() {
        return mantenimientos;
    }

    public void setMantenimientos(List<MantResponseDto> mantenimientos) {
        this.mantenimientos = mantenimientos;
    }
}