package com.marialo.cafeteriamvc.base;

import java.time.LocalDate;

public class Producto {
    private String nombre;
    private double precio;
    private LocalDate fechaCaducidad;
    private boolean enStock;
    private boolean conDescuento;
    private int descuento;

    public Producto() {

    }

    public Producto(String nombre, double precio, LocalDate fechaCaducidad, boolean enStock, boolean conDescuento) {
        this.nombre = nombre;
        this.precio = precio;
        this.fechaCaducidad = fechaCaducidad;
        this.enStock = enStock;
        this.conDescuento = conDescuento;
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
