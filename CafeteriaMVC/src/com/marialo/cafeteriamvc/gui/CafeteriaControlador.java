package com.marialo.cafeteriamvc.gui;

import com.marialo.cafeteriamvc.base.*;
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

        vista.bebidaRadioButton.setSelected(true);
        vista.cardPanel.setVisible(false);

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
        vista.ingredientesTxt.setText("");
        vista.dlmIngredientes.clear();
        vista.conDescuentoCheckBox.setSelected(false);
        actualizarVisibilidadDescuentos();
        vista.cardPanel.setVisible(false);
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
        vista.aceptarButton.addActionListener(listener);
        vista.cancelarButton.addActionListener(listener);
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
            case "Aceptar":
                aceptar();
                limpiarCampos();
                break;
            case "Cancelar":
                cancelar();
                limpiarCampos();
                break;
            case "Eliminar":
                eliminarProducto();
                break;
            case "+":
                annadirIngrediente();
                break;
            case "exportarxml":
                exportarXML();
                break;
            case "importarxml":
                importarXML();
                break;
            case "bebida":
            case "comida":
                actualizarVisibilidadIngredientes();
                actualizarComboBox();
                limpiarCampos();
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
                TipoBebida tipoBebida = TipoBebida.fromString(vista.comboBox1.getSelectedItem().toString());
                modelo.altaBebida(factura, nombre, precio, fechaCaducidad, conDescuento, descuento, tipoBebida);
            } else {
                TipoComida tipoComida = TipoComida.fromString(vista.comboBox1.getSelectedItem().toString());

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
            Object elemento = vista.dlmIngredientes.getElementAt(i);
            if (elemento instanceof ArrayList) {
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

        if (!esComida) {
            vista.dlmIngredientes.clear();
        }
    }

    private void actualizarComboBox() {
        vista.comboBox1.removeAllItems();

        if (vista.comidaRadioButton.isSelected()) {
            for (TipoComida tipo : TipoComida.values()) {
                vista.comboBox1.addItem(tipo.getNombre());
            }
        } else {
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

        if (conDescuento) {
            vista.cincoRadioButton.setSelected(true);
        }
    }

    private void editarProducto() {
        Producto productoSeleccionado = (Producto) vista.listaProductos.getSelectedValue();
        if (productoSeleccionado == null) {
            Util.mensajeError("Seleccione un producto para editar");
            return;
        }
        cargarDatosProducto(productoSeleccionado);
        vista.cardPanel.setVisible(true);
    }

    private void cargarDatosProducto(Producto producto) {
        // Cargar datos básicos
        vista.facturaTxt.setText(producto.getFactura());
        vista.nombreTxt.setText(producto.getNombre());
        vista.precioTxt.setText(String.valueOf(producto.getPrecio()));
        vista.fechaCaducidadDatePicker.setDate(producto.getFechaCaducidad());
        vista.conDescuentoCheckBox.setSelected(producto.isConDescuento());

        // Cargar descuento
        if (producto.isConDescuento()) {
            if (producto.getDescuento() == 5) {
                vista.cincoRadioButton.setSelected(true);
            } else if (producto.getDescuento() == 10) {
                vista.diezRadioButton.setSelected(true);
            } else if (producto.getDescuento() == 15) {
                vista.quinceRadioButton.setSelected(true);
            }
        }

        if (producto instanceof Bebida) {
            vista.bebidaRadioButton.setSelected(true);
            Bebida bebida = (Bebida) producto;
            vista.comboBox1.setSelectedItem(bebida.getTipoBebida().getNombre());

            vista.dlmIngredientes.clear();

        } else if (producto instanceof Comida) {
            vista.comidaRadioButton.setSelected(true);
            Comida comida = (Comida) producto;
            vista.comboBox1.setSelectedItem(comida.getTipoComida().getNombre());

            vista.dlmIngredientes.clear();
            for (String ingrediente : comida.getIngredientes()) {
                ArrayList<String> ingredienteList = new ArrayList<>();
                ingredienteList.add(ingrediente);
                vista.dlmIngredientes.addElement(ingredienteList);
            }
        }

        actualizarVisibilidadIngredientes();
        actualizarVisibilidadDescuentos();
    }

    private void aceptar() {
        Producto productoSeleccionado = (Producto) vista.listaProductos.getSelectedValue();
        if (hayCamposVacios()) {
            Util.mensajeError("Hay campos vacíos");
            return;
        }

        try {
            String factura = vista.facturaTxt.getText();
            String nombre = vista.nombreTxt.getText();
            double precio = Double.parseDouble(vista.precioTxt.getText());
            LocalDate fechaCaducidad = vista.fechaCaducidadDatePicker.getDate();
            boolean conDescuento = vista.conDescuentoCheckBox.isSelected();
            int descuento = obtenerDescuento();

            Producto productoExistente = modelo.existeProducto(factura);
            if (productoExistente != null && !productoExistente.getFactura().equals(productoSeleccionado.getFactura())) {
                Util.mensajeError("El código de venta " + factura + " ya existe en otro producto");
                return;
            }

            if (productoSeleccionado instanceof Bebida && vista.bebidaRadioButton.isSelected()) {
                TipoBebida tipoBebida = TipoBebida.fromString(vista.comboBox1.getSelectedItem().toString());
                modelo.editarBebida(factura, nombre, precio, fechaCaducidad, conDescuento, descuento, tipoBebida);

            } else if (productoSeleccionado instanceof Comida && vista.comidaRadioButton.isSelected()) {
                TipoComida tipoComida = TipoComida.fromString(vista.comboBox1.getSelectedItem().toString());
                ArrayList<String> ingredientes = obtenerIngredientesDeLista();

                if (ingredientes.isEmpty()) {
                    Util.mensajeError("Debe añadir al menos un ingrediente");
                    return;
                }

                modelo.editarComida(factura, nombre, precio, fechaCaducidad, conDescuento, descuento, tipoComida, ingredientes);

            }
            refrescar();
            Util.mensajeInfo("Producto editado correctamente");

        } catch (NumberFormatException ex) {
            Util.mensajeError("El precio debe ser un número válido");
        } catch (Exception ex) {
            Util.mensajeError("Error al editar producto: " + ex.getMessage());
        }
    }

    private void cancelar() {
        Util.mensajeInfo("Acción cancelada");
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

    private void exportarXML() {
        try {
            JFileChooser selector = Util.crearSelectorFichero(ultimaRutaExportada, "Archivos XML", "xml");
            int resultado = selector.showSaveDialog(vista.frame);

            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivo = selector.getSelectedFile();
                if (!archivo.getName().toLowerCase().endsWith(".xml")) {
                    archivo = new File(archivo.getAbsolutePath() + ".xml");
                }

                modelo.exportarXML(archivo);
                ultimaRutaExportada = archivo.getParentFile();
                Util.mensajeInfo("Datos exportados correctamente a " + archivo.getName());
            }
        } catch (Exception ex) {
            Util.mensajeError("Error al exportar: " + ex.getMessage());
        }
    }

    private void importarXML() {
        try {
            JFileChooser selector = Util.crearSelectorFichero(ultimaRutaExportada, "Archivos XML", "xml");
            int resultado = selector.showOpenDialog(vista.frame);

            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivo = selector.getSelectedFile();
                modelo.importarXML(archivo);
                ultimaRutaExportada = archivo.getParentFile();
                refrescar();
                Util.mensajeInfo("Datos importados correctamente desde " + archivo.getName());
            }
        } catch (Exception ex) {
            Util.mensajeError("Error al importar: " + ex.getMessage());
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
