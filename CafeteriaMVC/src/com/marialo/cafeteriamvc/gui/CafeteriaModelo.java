package com.marialo.cafeteriamvc.gui;

import com.marialo.cafeteriamvc.base.*;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

public class CafeteriaModelo {
    private ArrayList<Producto> listaProductos;

    public CafeteriaModelo() {
        listaProductos = new ArrayList<Producto>();
    }

    public ArrayList<Producto> obtenerProductos() {
        return listaProductos;
    }

    public void altaBebida(String factura, String nombre, double precio, LocalDate fechaCaducidad, boolean conDescuento
            , int descuento, TipoBebida tipoBebida) {
        Bebida nuevaBebida;
        if (conDescuento) {
            nuevaBebida = new Bebida(factura, nombre, precio, fechaCaducidad, conDescuento, descuento, tipoBebida);
        } else {
            nuevaBebida = new Bebida(factura, nombre, precio, fechaCaducidad, tipoBebida);
        }
        listaProductos.add(nuevaBebida);
    }

    public void altaComida(String factura, String nombre, double precio, LocalDate fechaCaducidad, boolean conDescuento
            , int descuento, TipoComida tipoComida, ArrayList<String> ingredientes) {
        Comida nuevaComida;
        if (conDescuento) {
            nuevaComida = new Comida(factura, nombre, precio, fechaCaducidad, conDescuento, descuento, tipoComida, ingredientes);
        } else {
            nuevaComida = new Comida(factura, nombre, precio, fechaCaducidad, tipoComida, ingredientes);
        }
    }

    public Producto existeProducto(String factura) {
        for (Producto p : listaProductos) {
            if (p.getFactura().equals(factura)) {
                return p;
            }
        }
        return null;
    }

    public void eliminarProducto(Producto producto) {
        listaProductos.remove(producto);
    }

    public void editarBebida(String factura, String nombre, double precio, LocalDate fechaCaducidad, boolean conDescuento
            , int descuento, TipoBebida tipoBebida) {
        Bebida producto = (Bebida) existeProducto(factura);
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setFechaCaducidad(fechaCaducidad);
        producto.setConDescuento(conDescuento);
        producto.setDescuento(descuento);
        producto.setTipoBebida(tipoBebida);
    }

    public void editarComida(String factura, String nombre, double precio, LocalDate fechaCaducidad, boolean conDescuento
            , int descuento, TipoComida tipoComida, ArrayList<String> ingredientes) {
        Comida producto = (Comida) existeProducto(factura);
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setFechaCaducidad(fechaCaducidad);
        producto.setConDescuento(conDescuento);
        producto.setDescuento(descuento);
        producto.setTipoComida(tipoComida);
        producto.setIngredientes(ingredientes);
    }

    public void exportarXML(File fichero) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation dom = builder.getDOMImplementation();
        Document documento = dom.createDocument(null, "xml", null);

        Element raiz = documento.createElement("productos");
        documento.getDocumentElement().appendChild(raiz);

        Element nodoProducto = null;
        Element nodoDatos = null;
        Text texto = null;

        for (Producto p : listaProductos) {
            if (p instanceof  Bebida) {
                nodoProducto = documento.createElement("bebida");
            } else {
                nodoProducto = documento.createElement("comida");
            }
            raiz.appendChild(nodoProducto);

            nodoDatos = documento.createElement("factura");
            nodoProducto.appendChild(nodoDatos);
            texto = documento.createTextNode(p.getFactura());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("nombre");
            nodoProducto.appendChild(nodoDatos);
            texto = documento.createTextNode(p.getNombre());
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("precio");
            nodoProducto.appendChild(nodoDatos);
            texto = documento.createTextNode(String.valueOf(p.getPrecio()));
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("fecha-caducidad");
            nodoProducto.appendChild(nodoDatos);
            texto = documento.createTextNode(String.valueOf(p.getFechaCaducidad()));
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("tiene-descuento");
            nodoProducto.appendChild(nodoDatos);
            texto = documento.createTextNode(String.valueOf(p.isConDescuento()));
            nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("descuento");
            nodoProducto.appendChild(nodoDatos);
            texto = documento.createTextNode(String.valueOf(p.getDescuento()));
            nodoDatos.appendChild(texto);

            if (p instanceof Bebida) {
                nodoDatos = documento.createElement("tipo-bebida");
                nodoProducto.appendChild(nodoDatos);
                texto = documento.createTextNode(String.valueOf(((Bebida) p).getTipoBebida()));
                nodoDatos.appendChild(texto);
            } else {
                nodoDatos = documento.createElement("tipo-comida");
                nodoProducto.appendChild(nodoDatos);
                texto = documento.createTextNode(String.valueOf(((Comida) p).getTipoComida()));
                nodoDatos.appendChild(texto);

                ArrayList<String> listaIngredientes = ((Comida) p).getIngredientes();

                for (String ingrediente : listaIngredientes) {
                    nodoDatos = documento.createElement("ingrediente");
                    nodoProducto.appendChild(nodoDatos);
                    texto = documento.createTextNode(ingrediente);
                    nodoDatos.appendChild(texto);
                }
            }
        }
    }
}
