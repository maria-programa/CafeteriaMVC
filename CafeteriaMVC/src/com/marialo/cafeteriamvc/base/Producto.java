package com.marialo.cafeteriamvc.base;

import java.time.LocalDate;

public class Producto {
    private String factura;
    private String nombre;
    private double precio;
    private boolean enStock;
    private LocalDate fechaCaducidad;
    private boolean conDescuento;
    private int descuento;

    public Producto() {

    }

    public Producto(String factura, String nombre, double precio) {
        this.factura = factura;
        this.nombre = nombre;
        this.precio = precio;
    }

    public Producto(String factura, String nombre, double precio, boolean enStock, LocalDate fechaCaducidad) {
        this.factura = factura;
        this.nombre = nombre;
        this.precio = precio;
        this.enStock = enStock;
        this.fechaCaducidad = fechaCaducidad;
    }

    public Producto(String factura, String nombre, double precio, boolean enStock, LocalDate fechaCaducidad, boolean conDescuento, int descuento) {
        this.factura = factura;
        this.nombre = nombre;
        this.precio = precio;
        this.enStock = enStock;
        this.fechaCaducidad = fechaCaducidad;
        this.conDescuento = conDescuento;
        this.descuento = descuento;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public boolean isEnStock() {
        return enStock;
    }

    public void setEnStock(boolean enStock) {
        this.enStock = enStock;
    }

    public boolean isConDescuento() {
        return conDescuento;
    }

    public void setConDescuento(boolean conDescuento) {
        this.conDescuento = conDescuento;
    }

    public int getDescuento() {
        return descuento;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", fechaCaducidad=" + fechaCaducidad +
                ", enStock=" + (enStock ? " sí " : " no ") +
                ", descuento=" + (conDescuento ? " sí " : " no ") +
                '}';
    }
}
