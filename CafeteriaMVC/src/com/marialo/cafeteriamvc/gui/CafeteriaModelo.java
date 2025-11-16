package com.marialo.cafeteriamvc.gui;

import com.marialo.cafeteriamvc.base.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
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

    public String generarCodigoVenta() {
        return "VTA-" + (listaProductos.size() + 1);
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
        listaProductos.add(nuevaComida);
    }

    public Producto obtenerUnProducto(String factura) {
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
        Bebida producto = (Bebida) obtenerUnProducto(factura);
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setFechaCaducidad(fechaCaducidad);
        producto.setConDescuento(conDescuento);
        producto.setDescuento(descuento);
        producto.setTipoBebida(tipoBebida);
    }

    public void editarComida(String factura, String nombre, double precio, LocalDate fechaCaducidad, boolean conDescuento
            , int descuento, TipoComida tipoComida, ArrayList<String> ingredientes) {
        Comida producto = (Comida) obtenerUnProducto(factura);
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setFechaCaducidad(fechaCaducidad);
        producto.setConDescuento(conDescuento);
        producto.setDescuento(descuento);
        producto.setTipoComida(tipoComida);
        producto.setIngredientes(ingredientes);
    }

    public void exportarXML(File fichero) throws ParserConfigurationException, TransformerException {
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

        Source source = new DOMSource(documento);
        Result result = new StreamResult(fichero);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, result);
    }

    public void importarXML(File fichero) throws ParserConfigurationException, IOException, SAXException {
        listaProductos = new ArrayList<Producto>();
        Bebida nuevaBebida = null;
        Comida nuevaComida = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document documento = builder.parse(fichero);

        NodeList listaElementos = documento.getElementsByTagName("*");

        for (int i = 0; i < listaElementos.getLength(); i++) {
            Element nodoProducto = (Element) listaElementos.item(i);

            if (nodoProducto.getTagName().equals("bebida")) {
                nuevaBebida = new Bebida();
                nuevaBebida.setFactura(nodoProducto.getChildNodes().item(0).getTextContent());
                nuevaBebida.setNombre(nodoProducto.getChildNodes().item(1).getTextContent());
                nuevaBebida.setPrecio(Double.parseDouble(nodoProducto.getChildNodes().item(2).getTextContent()));
                nuevaBebida.setFechaCaducidad(LocalDate.parse(nodoProducto.getChildNodes().item(3).getTextContent()));
                nuevaBebida.setConDescuento(Boolean.parseBoolean(nodoProducto.getChildNodes().item(4).getTextContent()));
                nuevaBebida.setDescuento(Integer.parseInt(nodoProducto.getChildNodes().item(5).getTextContent()));
                nuevaBebida.setTipoBebida(TipoBebida.valueOf(nodoProducto.getChildNodes().item(6).getTextContent()));

                listaProductos.add(nuevaBebida);
            } else if (nodoProducto.getTagName().equals("comida")) {
                nuevaComida = new Comida();
                nuevaComida.setFactura(nodoProducto.getChildNodes().item(0).getTextContent());
                nuevaComida.setNombre(nodoProducto.getChildNodes().item(1).getTextContent());
                nuevaComida.setPrecio(Double.parseDouble(nodoProducto.getChildNodes().item(2).getTextContent()));
                nuevaComida.setFechaCaducidad(LocalDate.parse(nodoProducto.getChildNodes().item(3).getTextContent()));
                nuevaComida.setConDescuento(Boolean.parseBoolean(nodoProducto.getChildNodes().item(4).getTextContent()));
                nuevaComida.setDescuento(Integer.parseInt(nodoProducto.getChildNodes().item(5).getTextContent()));
                nuevaComida.setTipoComida(TipoComida.valueOf(nodoProducto.getChildNodes().item(6).getTextContent()));

                NodeList ingredientes = nodoProducto.getElementsByTagName("ingrediente");
                ArrayList<String> listaIngredientes = new ArrayList<>();
                for (int j = 0; j < ingredientes.getLength(); j++) {
                    listaIngredientes.add(ingredientes.item(j).getTextContent());
                }
                nuevaComida.setIngredientes(listaIngredientes);

                listaProductos.add(nuevaComida);
            }
        }
    }
}
