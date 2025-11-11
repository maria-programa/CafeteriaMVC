package com.marialo.cafeteriamvc.gui;

import com.marialo.cafeteriamvc.base.*;

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

    public void altaBebida(String factura, String nombre, double precio, LocalDate fechaCaducidad, boolean conDescuento, int descuento, TipoBebida tipoBebida) {
        Bebida nuevaBebida;
        if (conDescuento) {
            nuevaBebida = new Bebida(factura, nombre, precio, fechaCaducidad, conDescuento, descuento, tipoBebida);
        } else {
            nuevaBebida = new Bebida(factura, nombre, precio, fechaCaducidad, tipoBebida);
        }
        listaProductos.add(nuevaBebida);
    }

    public void altaComida(String factura, String nombre, double precio, LocalDate fechaCaducidad, boolean conDescuento, int descuento, TipoComida tipoComida, String[] ingredientes) {
        Comida nuevaComida;
        if (conDescuento) {
            nuevaComida = new Comida(factura, nombre, precio, fechaCaducidad, conDescuento, descuento, tipoComida, ingredientes);
        } else {
            nuevaComida = new Comida(factura, nombre, precio, fechaCaducidad, tipoComida, ingredientes);
        }
    }
}
