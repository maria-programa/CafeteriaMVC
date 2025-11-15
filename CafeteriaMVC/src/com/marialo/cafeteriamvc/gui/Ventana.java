package com.marialo.cafeteriamvc.gui;

import com.github.lgooddatepicker.components.DatePicker;
import com.marialo.cafeteriamvc.base.Producto;

import javax.swing.*;
import java.util.ArrayList;

public class Ventana {
    public JPanel panel1;
    public JRadioButton bebidaRadioButton;
    public JRadioButton comidaRadioButton;
    public JComboBox comboBox1;
    public JTextField nombreTxt;
    public JLabel tipoLabel;
    public JCheckBox conDescuentoCheckBox;
    public JRadioButton cincoRadioButton;
    public JRadioButton diezRadioButton;
    public JRadioButton quinceRadioButton;
    public JTextField ingredientesTxt;
    public JButton annadirButton;
    public JButton nuevoButton;
    public JButton editarButton;
    public JList listaProductos;
    public JTextField facturaTxt;
    public JButton eliminarButton;
    public JList listaIngredientes;
    public JScrollPane ingredientesScroll;
    public JLabel ingredientesLbl;
    public DatePicker fechaCaducidadDatePicker;
    public JTextField precioTxt;

    public JFrame frame;
    JMenuItem itemExportarXML;
    JMenuItem itemImportarXML;
    public DefaultListModel<Producto> dlmProducto;
    public DefaultListModel<ArrayList<String>> dlmIngredientes;

    public Ventana() {
        frame = new JFrame("Cafetería");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        crearBarraMenu();
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);

        initComponents();

    }

    public void crearBarraMenu() {
        JMenuBar barra = new JMenuBar();
        JMenu menu = new JMenu("Opciones");
        itemExportarXML = new JMenuItem("Exportar XML");
        itemImportarXML = new JMenuItem("Importar XML");
        //me permitirá reconocer el botón
        itemExportarXML.setActionCommand("exportarxml");
        itemImportarXML.setActionCommand("importarxml");

        menu.add(itemImportarXML);
        menu.add(itemExportarXML);
        barra.add(menu);
        frame.setJMenuBar(barra);
    }

    public void initComponents() {
        dlmProducto = new DefaultListModel<Producto>();
        listaProductos.setModel(dlmProducto);

        dlmIngredientes = new DefaultListModel<ArrayList<String>>();
        listaIngredientes.setModel(dlmIngredientes);
    }
}
