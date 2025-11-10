package Presentation.Models;

import Domain.Dtos.mantenimiento.MantResponseDto;
import Presentation.IObserver;
import Utilities.EventType;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MantenimientoTableModel extends AbstractTableModel implements IObserver {

    private final List<MantResponseDto> mants = new ArrayList<>();
    private final String[] columnNames = { "ID" , "Fecha" , "Descripcion" , "Tipo" , "Carro" };


    @Override
    public int getRowCount() {
        return mants.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MantResponseDto mant = mants.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> mant.getId();
            case 1 -> mant.getFecha();
            case 2 -> mant.getDescripcion();
            case 3 -> mant.getTipo();
            case 4 -> mant.getCarId().getId();
            default -> null;
        };
    }

    // -------------------------
    // Observer implementation
    // -------------------------


    @Override
    public void update(EventType eventType, Object data) {
        if (data == null) return;

        switch (eventType) {
            case CREATED -> {
                MantResponseDto newMant = (MantResponseDto) data;
                mants.add(newMant);
                fireTableRowsInserted(mants.size() - 1, mants.size() - 1);
            }
            case UPDATED -> {
                MantResponseDto updatedMant = (MantResponseDto) data;
                for (int i = 0; i < mants.size(); i++) {
                    if (mants.get(i).getId().equals(updatedMant.getId())) {
                        mants.set(i, updatedMant);
                        fireTableRowsUpdated(i, i);
                        break;
                    }
                }
            }
            case DELETED -> {
                Long deletedId = (Long) data;
                for (int i = 0; i < mants.size(); i++) {
                    if (mants.get(i).getId().equals(deletedId)) {
                        mants.remove(i);
                        fireTableRowsDeleted(i, i);
                        break;
                    }
                }
            }
        }
    }

    // -------------------------
    // Utility methods
    // -------------------------
    public List<MantResponseDto> getMants() {
        return new ArrayList<>(mants);
    }

    public void setMants(List<MantResponseDto> newCars) {
        mants.clear();
        if (newCars != null) mants.addAll(newCars);
        fireTableDataChanged();
    }

}
