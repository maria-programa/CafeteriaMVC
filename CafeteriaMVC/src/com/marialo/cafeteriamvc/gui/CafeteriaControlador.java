package com.marialo.cafeteriamvc.gui;

import com.marialo.cafeteriamvc.base.Producto;
import com.marialo.cafeteriamvc.base.TipoBebida;
import com.marialo.cafeteriamvc.base.TipoComida;
import com.marialo.cafeteriamvc.util.Util;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

public class CafeteriaControlador implements ActionListener, ListSelectionListener, WindowListener {
    private Ventana vista;
    private CafeteriaModelo modelo;
    private File ultimaRutaExportada;

    public CafeteriaControlador(Ventana vista, CafeteriaModelo modelo) {
        this.vista = vista;
        this.modelo = modelo;

        try {
            cargarDatosConfiguración();
        } catch (IOException e) {
            System.out.println("No existe fichero de configuración");
        }

        addActionListener(this);
        addWindowListener(this);
        addListSelectionListener(this);

        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(vista.bebidaRadioButton);
        grupoTipo.add(vista.comidaRadioButton);
        vista.bebidaRadioButton.setSelected(true);

        ButtonGroup grupoDescuento = new ButtonGroup();
        grupoDescuento.add(vista.cincoRadioButton);
        grupoDescuento.add(vista.diezRadioButton);
        grupoDescuento.add(vista.quinceRadioButton);

        actualizarVisibilidadIngredientes();
        actualizarVisibilidadDescuentos();
        actualizarComboBox();
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
        vista.facturaTxt.setText("");
        vista.nombreTxt.setText("");
        vista.precioTxt.setText("");
        vista.fechaCaducidadDatePicker.clear();
        vista.conDescuentoCheckBox.setSelected(false);
        vista.ingredientesTxt.setText("");
        vista.dlmIngredientes.clear();

        ButtonGroup grupoDescuento = new ButtonGroup();
        grupoDescuento.add(vista.cincoRadioButton);
        grupoDescuento.add(vista.diezRadioButton);
        grupoDescuento.add(vista.quinceRadioButton);
        grupoDescuento.clearSelection();
    }

    private void addActionListener(ActionListener listener) {
        vista.itemExportarXML.addActionListener(listener);
        vista.itemImportarXML.addActionListener(listener);

        vista.bebidaRadioButton.setActionCommand("bebida");
        vista.comidaRadioButton.setActionCommand("comida");
        vista.bebidaRadioButton.addActionListener(listener);
        vista.comidaRadioButton.addActionListener(listener);

        vista.conDescuentoCheckBox.setActionCommand("conDescuento");
        vista.conDescuentoCheckBox.addActionListener(listener);

        vista.cincoRadioButton.setActionCommand("descuento");
        vista.diezRadioButton.setActionCommand("descuento");
        vista.quinceRadioButton.setActionCommand("descuento");
        vista.cincoRadioButton.addActionListener(listener);
        vista.diezRadioButton.addActionListener(listener);
        vista.quinceRadioButton.addActionListener(listener);

        vista.nuevoButton.addActionListener(listener);
        vista.editarButton.addActionListener(listener);
        vista.eliminarButton.addActionListener(listener);
        vista.annadirButton.addActionListener(listener);
    }

    private void addWindowListener(WindowListener listener) {
        vista.frame.addWindowListener(listener);
    }

    private void addListSelectionListener(ListSelectionListener listener) {
        vista.listaProductos.addListSelectionListener(listener);
        vista.listaIngredientes.addListSelectionListener(listener);
    }

    public void refrescar() {
        vista.dlmProducto.clear();
        for (Producto p : modelo.obtenerProductos()) {
            vista.dlmProducto.addElement(p);
        }
    }

    private void cargarDatosConfiguración() throws IOException {
        Properties configuracion = new Properties();
        configuracion.load(new FileReader("productos.conf"));
        ultimaRutaExportada = new File(configuracion.getProperty("ultimaRutaExportada"));
    }

    private void guardarConfiguracion() throws IOException {
        Properties configuracion = new Properties();
        configuracion.setProperty("ultimaRutaExportada", ultimaRutaExportada.getAbsolutePath());
        configuracion.store(new PrintWriter("productos.conf"), "Datos configuracion vehiculos");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            case "Nuevo":
                altaProducto();
                break;
            case "Editar":
                editarProducto();
                break;
            case "Eliminar":
                eliminarProducto();
                break;
            case "+":
                annadirIngrediente();
                break;
            case "exportarxml":
                try {
                    modelo.exportarXML(ultimaRutaExportada);
                } catch (ParserConfigurationException ex) {
                    ex.printStackTrace();
                } catch (TransformerException ex) {
                    ex.printStackTrace();
                }
                break;
            case "importarxml":
                try {
                    modelo.importarXML(ultimaRutaExportada);
                } catch (ParserConfigurationException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (SAXException ex) {
                    ex.printStackTrace();
                }
                break;
            case "bebida":
            case "comida":
                actualizarVisibilidadIngredientes();
                actualizarComboBox();
                break;
            case "conDescuento":
                actualizarVisibilidadDescuentos();
                break;
        }
    }

    private void altaProducto() {
        if (hayCamposVacios()) {
            Util.mensajeError("Hay campos vacíos");
            return;
        }

        if (modelo.existeProducto(vista.facturaTxt.getText()) != null) {
            Util.mensajeError("El código de venta " + vista.facturaTxt.getText() + " ya existe");
            return;
        }

        try {
            String factura = vista.facturaTxt.getText();
            String nombre = vista.nombreTxt.getText();
            double precio = Double.parseDouble(vista.precioTxt.getText());
            LocalDate fechaCaducidad = vista.fechaCaducidadDatePicker.getDate();
            boolean conDescuento = vista.conDescuentoCheckBox.isSelected();
            int descuento = obtenerDescuento();

            if (vista.bebidaRadioButton.isSelected()) {
                // Alta de bebida
                TipoBebida tipoBebida = TipoBebida.fromString(vista.comboBox1.getSelectedItem().toString());
                modelo.altaBebida(factura, nombre, precio, fechaCaducidad, conDescuento, descuento, tipoBebida);
            } else {
                // Alta de comida
                TipoComida tipoComida = TipoComida.fromString(vista.comboBox1.getSelectedItem().toString());

                // Obtener ingredientes de la lista
                ArrayList<String> ingredientes = obtenerIngredientesDeLista();

                if (ingredientes.isEmpty()) {
                    Util.mensajeError("Debe añadir al menos un ingrediente");
                    return;
                }

                modelo.altaComida(factura, nombre, precio, fechaCaducidad, conDescuento, descuento, tipoComida, ingredientes);
            }

            refrescar();
            limpiarCampos();
            Util.mensajeInfo("Producto añadido correctamente");

        } catch (NumberFormatException ex) {
            Util.mensajeError("El precio debe ser un número válido");
        } catch (Exception ex) {
            Util.mensajeError("Error al añadir producto: " + ex.getMessage());
        }
    }

    private int obtenerDescuento() {
        if (vista.cincoRadioButton.isSelected()) return 5;
        if (vista.diezRadioButton.isSelected()) return 10;
        if (vista.quinceRadioButton.isSelected()) return 15;
        return 0;
    }

    private ArrayList<String> obtenerIngredientesDeLista() {
        ArrayList<String> ingredientes = new ArrayList<>();
        for (int i = 0; i < vista.dlmIngredientes.getSize(); i++) {
            // Como dlmIngredientes es de ArrayList<String>, obtenemos el primer elemento
            Object elemento = vista.dlmIngredientes.getElementAt(i);
            if (elemento instanceof ArrayList) {
                @SuppressWarnings("unchecked")
                ArrayList<String> lista = (ArrayList<String>) elemento;
                if (!lista.isEmpty()) {
                    ingredientes.add(lista.get(0));
                }
            }
        }
        return ingredientes;
    }

    private void annadirIngrediente() {
        String ingrediente = vista.ingredientesTxt.getText().trim();
        if (!ingrediente.isEmpty()) {
            ArrayList<String> ingredienteList = new ArrayList<>();
            ingredienteList.add(ingrediente);
            vista.dlmIngredientes.addElement(ingredienteList);
            vista.ingredientesTxt.setText("");
        } else {
            Util.mensajeError("El ingrediente no puede estar vacío");
        }
    }

    private void actualizarVisibilidadIngredientes() {
        boolean esComida = vista.comidaRadioButton.isSelected();

        vista.ingredientesTxt.setVisible(esComida);
        vista.annadirButton.setVisible(esComida);
        vista.listaIngredientes.setVisible(esComida);
        vista.ingredientesScroll.setVisible(esComida);
        vista.ingredientesLbl.setVisible(esComida);

        vista.panel1.revalidate();
        vista.panel1.repaint();
        // Limpiar ingredientes cuando se cambia a bebida
        if (!esComida) {
            vista.dlmIngredientes.clear();
        }
    }

    private void actualizarComboBox() {
        vista.comboBox1.removeAllItems();

        if (vista.comidaRadioButton.isSelected()) {
            // Agregar tipos de comida
            for (TipoComida tipo : TipoComida.values()) {
                vista.comboBox1.addItem(tipo.getNombre());
            }
        } else {
            // Agregar tipos de bebida
            for (TipoBebida tipo : TipoBebida.values()) {
                vista.comboBox1.addItem(tipo.getNombre());
            }
        }
    }

    private void actualizarVisibilidadDescuentos() {
        boolean conDescuento = vista.conDescuentoCheckBox.isSelected();
        vista.cincoRadioButton.setVisible(conDescuento);
        vista.diezRadioButton.setVisible(conDescuento);
        vista.quinceRadioButton.setVisible(conDescuento);

        // Seleccionar un descuento por defecto si se activa
        if (conDescuento && !vista.cincoRadioButton.isSelected() &&
                !vista.diezRadioButton.isSelected() && !vista.quinceRadioButton.isSelected()) {
            vista.cincoRadioButton.setSelected(true);
        }
    }

    private void editarProducto() {
        Producto productoSeleccionado = (Producto) vista.listaProductos.getSelectedValue();
        if (productoSeleccionado == null) {
            Util.mensajeError("Seleccione un producto para editar");
            return;
        }
    }

    private void eliminarProducto() {
        Producto productoSeleccionado = (Producto) vista.listaProductos.getSelectedValue();
        if (productoSeleccionado == null) {
            Util.mensajeError("Seleccione un producto para eliminar");
            return;
        }

        int respuesta = Util.mensajeConfirmacion(
                "¿Está seguro de eliminar el producto: " + productoSeleccionado.getNombre() + "?",
                "Confirmar eliminación"
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            modelo.eliminarProducto(productoSeleccionado);
            refrescar();
            Util.mensajeInfo("Producto eliminado correctamente");
        }
    }


    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        int resp = Util.mensajeConfirmacion("¿Desea cerrar la ventana?", "Salir");
        if (resp == JOptionPane.OK_OPTION) {
            try {
                if (ultimaRutaExportada != null) {
                    guardarConfiguracion();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        }
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
