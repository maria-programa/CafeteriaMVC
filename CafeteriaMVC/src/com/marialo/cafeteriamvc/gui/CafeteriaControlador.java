package com.marialo.cafeteriamvc.gui;

import com.marialo.cafeteriamvc.base.*;
import com.marialo.cafeteriamvc.util.Util;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

public class CafeteriaControlador implements ActionListener, ListSelectionListener, WindowListener, MouseListener {
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
        addMouseListener(this);

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
        vista.eliminarIngredienteBtn.addActionListener(listener);
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

    private void addMouseListener(MouseListener listener) {
        vista.listaIngredientes.addMouseListener(listener);
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
            case "Actualizar":
                annadirIngrediente();
                break;
            case "-":
                eliminarIngrediente();
                break;
            case "exportarxml":
                exportarXML();
                break;
            case "importarxml":
                importarXML();
                break;
            case "Bebida":
            case "Comida":
                actualizarVisibilidadIngredientes();
                actualizarComboBox();
                limpiarCampos();
                break;
            case "Descuento":
                actualizarVisibilidadDescuentos();
                break;
        }
    }

    private void altaProducto() {
        crearProducto(false);
    }

    private void editarProducto() {
        Producto productoSeleccionado = (Producto) vista.listaProductos.getSelectedValue();
        if (productoSeleccionado == null) {
            Util.mensajeError("Seleccione un producto para editar");
            return;
        }
        cargarDatosProducto(productoSeleccionado);
        actualizarVisibilidadBotones(false);
        vista.cardPanel.setVisible(true);
    }

    private void cargarDatosProducto(Producto producto) {
        if (producto instanceof Bebida) {
            vista.bebidaRadioButton.setSelected(true);
        } else if (producto instanceof Comida) {
            vista.comidaRadioButton.setSelected(true);
        }

        actualizarVisibilidadIngredientes();
        actualizarVisibilidadDescuentos();
        actualizarComboBox();

        vista.facturaTxt.setText(producto.getFactura());
        vista.nombreTxt.setText(producto.getNombre());
        vista.precioTxt.setText(String.valueOf(producto.getPrecio()));
        vista.fechaCaducidadDatePicker.setDate(producto.getFechaCaducidad());
        vista.conDescuentoCheckBox.setSelected(producto.isConDescuento());

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

            Bebida bebida = (Bebida) producto;
            vista.comboBox1.setSelectedItem(bebida.getTipoBebida().getNombre());

            vista.dlmIngredientes.clear();

        } else if (producto instanceof Comida) {

            Comida comida = (Comida) producto;
            vista.comboBox1.setSelectedItem(comida.getTipoComida().getNombre());

            vista.dlmIngredientes.clear();
            for (String ingrediente : comida.getIngredientes()) {
                ArrayList<String> ingredienteList = new ArrayList<>();
                ingredienteList.add(ingrediente);
                vista.dlmIngredientes.addElement(ingredienteList);
            }
        }
    }

    private void aceptar() {
        crearProducto(true);
        actualizarVisibilidadBotones(true);
    }

    private void cancelar() {
        Util.mensajeInfo("Acción cancelada");
        vista.listaProductos.clearSelection();
        actualizarVisibilidadBotones(true);
    }

    private void actualizarVisibilidadBotones(boolean visible) {
            vista.nuevoButton.setVisible(visible);
            vista.editarButton.setVisible(visible);
            vista.eliminarButton.setVisible(visible);
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

    private void crearProducto(boolean editando) {
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

            if (!editando && modelo.existeProducto(factura) != null) {
                Util.mensajeError("El código de venta " + factura + " ya existe");
                return;
            }

            if (vista.bebidaRadioButton.isSelected()) {
                TipoBebida tipoBebida = TipoBebida.fromString(vista.comboBox1.getSelectedItem().toString());
                if (editando) {
                    modelo.editarBebida(factura, nombre, precio, fechaCaducidad, conDescuento, descuento, tipoBebida);
                } else {
                    modelo.altaBebida(factura, nombre, precio, fechaCaducidad, conDescuento, descuento, tipoBebida);
                }
            } else {
                TipoComida tipoComida = TipoComida.fromString(vista.comboBox1.getSelectedItem().toString());
                ArrayList<String> ingredientes = obtenerIngredientesDeLista();

                if (ingredientes.isEmpty()) {
                    Util.mensajeError("Debe añadir al menos un ingrediente");
                    return;
                }

                if (editando) {
                    modelo.editarComida(factura, nombre, precio, fechaCaducidad, conDescuento, descuento, tipoComida, ingredientes);
                } else {
                    modelo.altaComida(factura, nombre, precio, fechaCaducidad, conDescuento, descuento, tipoComida, ingredientes);
                }
            }

            refrescar();
            limpiarCampos();
            Util.mensajeInfo("Producto " + (editando ? "editado" : "añadido") + " correctamente");

        } catch (NumberFormatException ex) {
            Util.mensajeError("El precio debe ser un número válido");
        } catch (Exception ex) {
            Util.mensajeError("Error al " + (editando ? "editar" : "añadir") + " producto: " + ex.getMessage());
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
        if (ingrediente.isEmpty()) {
            Util.mensajeError("El ingrediente no puede estar vacío");
            return;
        }

        int indiceSeleccionado = vista.listaIngredientes.getSelectedIndex();

        if (indiceSeleccionado != -1) {
            ArrayList<String> ingredienteActualizado = new ArrayList<>();
            ingredienteActualizado.add(ingrediente);
            vista.dlmIngredientes.set(indiceSeleccionado, ingredienteActualizado);
        } else {
            for (int i = 0; i < vista.dlmIngredientes.getSize(); i++) {
                Object elemento = vista.dlmIngredientes.getElementAt(i);
                if (elemento instanceof ArrayList) {
                    ArrayList<String> lista = (ArrayList<String>) elemento;
                    if (!lista.isEmpty() && lista.get(0).equalsIgnoreCase(ingrediente)) {
                        Util.mensajeError("Este ingrediente ya existe en la lista");
                        return;
                    }
                }
            }

            ArrayList<String> nuevoIngrediente = new ArrayList<>();
            nuevoIngrediente.add(ingrediente);
            vista.dlmIngredientes.addElement(nuevoIngrediente);
        }

        vista.ingredientesTxt.setText("");
        vista.listaIngredientes.clearSelection();
    }

    private void eliminarIngrediente() {
        int indiceSeleccionado = vista.listaIngredientes.getSelectedIndex();
        if (indiceSeleccionado != -1) {
            int respuesta = Util.mensajeConfirmacion(
                    "¿Está seguro de eliminar este ingrediente?",
                    "Confirmar eliminación"
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                vista.dlmIngredientes.remove(indiceSeleccionado);
                vista.ingredientesTxt.setText("");
            }
        } else {
            Util.mensajeError("Seleccione un ingrediente para eliminar");
        }
    }

    private void actualizarVisibilidadIngredientes() {
                boolean esComida = vista.comidaRadioButton.isSelected();

        vista.ingredientesTxt.setVisible(esComida);
        vista.annadirButton.setVisible(esComida);
        vista.listaIngredientes.setVisible(esComida);
        vista.ingredientesScroll.setVisible(esComida);
        vista.ingredientesLbl.setVisible(esComida);
        vista.eliminarIngredienteBtn.setVisible(esComida);
        vista.eliminarIngredienteLbl.setVisible(esComida);

        vista.panel1.revalidate();
        vista.panel1.repaint();

        vista.frame.pack();
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
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            ArrayList<String> ingredienteSeleccionado = (ArrayList<String>) vista.listaIngredientes.getSelectedValue();
            if (ingredienteSeleccionado != null && !ingredienteSeleccionado.isEmpty()) {
                vista.ingredientesTxt.setText(ingredienteSeleccionado.get(0));
                vista.annadirButton.setText("Actualizar");
                vista.ingredientesTxt.requestFocus();
            }
        }
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
    public void windowOpened(WindowEvent e) {

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

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
