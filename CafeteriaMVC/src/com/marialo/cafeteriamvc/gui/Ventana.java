package com.marialo.cafeteriamvc.gui;

import com.marialo.cafeteriamvc.base.Producto;

import javax.swing.*;
import java.util.ArrayList;

public class Ventana {
    private JPanel panel1;
    public JRadioButton bebidaRadioButton;
    public JRadioButton comidaRadioButton;
    public JComboBox comboBox1;
    public JTextField textField1;
    public JLabel tipoLabel;
    public JCheckBox conDescuentoCheckBox;
    public JRadioButton cincoRadioButton;
    public JRadioButton diezRadioButton;
    public JRadioButton quinceRadioButton;
    public JTextField ingredientesTxt;
    public JButton annadirButton;
    public JButton nuevoButton;
    public JButton editarButton;
    public JList list1;
    private JTextField textField2;
    private JButton borrarButton;
    private JList list2;

    public JFrame frame;
    JMenuItem itemExportar;
    JMenuItem itemImportar;
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
        itemExportar = new JMenuItem("Exportar XML");
        itemImportar = new JMenuItem("Importar XML");
        //me permitirá reconocer el botón
        itemExportar.setActionCommand("exportar");
        itemImportar.setActionCommand("importar");

        menu.add(itemImportar);
        menu.add(itemExportar);
        barra.add(menu);
        frame.setJMenuBar(barra);
    }

    public void initComponents() {
        dlmProducto = new DefaultListModel<Producto>();
        list1.setModel(dlmProducto);

        dlmIngredientes = new DefaultListModel<ArrayList<String>>();
        list2.setModel(dlmIngredientes);
    }
}
