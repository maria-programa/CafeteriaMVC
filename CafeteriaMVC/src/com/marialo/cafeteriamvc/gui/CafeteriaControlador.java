package com.marialo.cafeteriamvc.gui;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

public class CafeteriaControlador implements ActionListener, ListSelectionListener, WindowListener {
    private Ventana vista;
    private CafeteriaModelo modelo;
    private File ultimaRutaExportada;

    public CafeteriaControlador(Ventana vista, CafeteriaModelo modelo) {
        this.vista = vista;
        this.modelo = modelo;
    }

    private boolean hayCamposVacios() {
        if (vista.nombreTxt.getText().isEmpty()
        || vista.facturaTxt.getText().isEmpty()
        || vista.fechaCaducidadDatePicker.getText().isEmpty()) {
            return true;
        }
        return false;
    }

    private void limpiarCampos() {
        vista.nombreTxt.setText(null);
        vista.fechaCaducidadDatePicker.setText(null);
        vista.ingredientesTxt.setText(null);
        vista.conDescuentoCheckBox.setSelected(false);
    }

    private void addActionListener(ActionListener listener) {
        vista.itemExportarXML.addActionListener(listener);
        vista.itemImportarXML.addActionListener(listener);

        vista.bebidaRadioButton.addActionListener(listener);
        vista.comidaRadioButton.addActionListener(listener);

        vista.conDescuentoCheckBox.addActionListener(listener);
        vista.cincoRadioButton.addActionListener(listener);
        vista.diezRadioButton.addActionListener(listener);
        vista.quinceRadioButton.addActionListener(listener);

        vista.nuevoButton.addActionListener(listener);
        vista.editarButton.addActionListener(listener);
        vista.eliminarButton.addActionListener(listener);
        vista.annadirButton.addActionListener(listener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}
