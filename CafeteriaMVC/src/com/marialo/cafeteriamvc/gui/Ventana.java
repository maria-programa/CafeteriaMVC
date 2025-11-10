package com.marialo.cafeteriamvc.gui;

import com.marialo.cafeteriamvc.base.TipoBebida;
import com.marialo.cafeteriamvc.base.TipoComida;

import javax.swing.*;

public class Ventana {
    private JPanel panel1;
    private JRadioButton bebidaRadioButton;
    private JRadioButton comidaRadioButton;
    private JComboBox comboBox1;
    private JTextField textField1;
    private JCheckBox enStockCheckBox;
    private JLabel tipoLabel;
    private JCheckBox conDescuentoCheckBox;
    private JRadioButton cincoRadioButton;
    private JRadioButton diezRadioButton;
    private JRadioButton quinceRadioButton;
    private JTextField ingredientesTxt;
    private JButton añadirButton;

    public JFrame frame;
    public Ventana() {
        frame = new JFrame("Cafetería");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        /*opciones();*/
        /*if (bebidaRadioButton.i) {
            comboBox1.setModel(new DefaultComboBoxModel(TipoBebida.values()));
        } else {
            comboBox1.setModel(new DefaultComboBoxModel(TipoComida.values()));
        }*/
    }

    /*private void opciones() {
        if (bebidaRadioButton.hasFocus()) {
            comboBox1.setModel(new DefaultComboBoxModel(TipoBebida.values()));
        } else {
            comboBox1.setModel(new DefaultComboBoxModel(TipoComida.values()));
        }
    }*/
}
