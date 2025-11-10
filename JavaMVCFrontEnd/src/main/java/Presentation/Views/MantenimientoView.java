package Presentation.Views;

import Domain.Dtos.mantenimiento.MantResponseDto;
import Presentation.Models.MantenimientoTableModel;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class MantenimientoView {
    private JPanel ContentPanel;
    private JPanel InfoPanel;
    private JTable MantenimientoTabla;
    private JLabel LabelDescripcion;
    private JLabel LabelTipo;
    private JTextField DescripcionTextField;
    private JTextField TipoTextField;
    private JPanel DatePickerPanel;
    private JPanel ButtonPanel;
    private JButton AgregarButton;
    private JButton BorrarButton;
    private JButton UpdateButton;
    private JButton ClearButton;
    private JDateChooser dateChooser;

    private final MantenimientoTableModel tableModel;
    private final LoadingOverlay loadingOverlay;

    public MantenimientoView(JFrame parentFrame) {
        tableModel = new MantenimientoTableModel();
        MantenimientoTabla.setModel(tableModel);
        initDatePickers();
        loadingOverlay = new LoadingOverlay(parentFrame);
    }

    public void showLoading(boolean visible) {
        loadingOverlay.show(visible);
    }

    //getters
    public JPanel getContentPanel() {return ContentPanel;}
    public JTable getMantenimientoTabla() {return MantenimientoTabla;}
    public JTextField getDescripcionTextField() {return DescripcionTextField;}
    public JTextField getTipoTextField() {return TipoTextField;}
    public JButton getAgregarButton() {return AgregarButton;}
    public JButton getBorrarButton() {return BorrarButton;}
    public JButton getUpdateButton() {return UpdateButton;}
    public JButton getClearButton() {return ClearButton;}
    public MantenimientoTableModel getTableModel() {return tableModel;}

    // --- DatePicker getters and setters ---
    public Date getSelectedDate() {
        return dateChooser.getDate();
    }

    public void setSelectedDate(Date date) {
        dateChooser.setDate(date);
    }

    public void clearFields() {
        DescripcionTextField.setText("");
        TipoTextField.setText("");
        MantenimientoTabla.clearSelection();
        dateChooser.setDate(new Date()); // Reinicia la fecha al día actual
    }

    public void populateFields(MantResponseDto mant) {
        DescripcionTextField.setText(mant.getDescripcion());
        TipoTextField.setText(mant.getTipo());
        if (mant.getFecha() != null) {
            dateChooser.setDate(mant.getFecha());
        } else {
            dateChooser.setDate(new Date());
        }
    }

    private void initDatePickers() {
        DatePickerPanel.setLayout(new BorderLayout());
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setDate(new Date());
        DatePickerPanel.add(dateChooser, BorderLayout.CENTER);
        DatePickerPanel.revalidate();
        DatePickerPanel.repaint();
    }


}
