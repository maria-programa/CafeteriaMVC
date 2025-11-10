package com.marialo.cafeteriamvc.base;

import java.time.LocalDate;

public class Comida extends Producto {
    private TipoComida tipoComida;
    private String[] ingredientes;

    public Comida() {}

    public Comida(TipoComida tipoComida, String[] ingredientes) {
        this.tipoComida = tipoComida;
        this.ingredientes = ingredientes;
    }

    public Comida(String factura, String nombre, double precio, TipoComida tipoComida, String[] ingredientes) {
        super(factura, nombre, precio);
        this.tipoComida = tipoComida;
        this.ingredientes = ingredientes;
    }

    public Comida(String factura, String nombre, double precio, boolean enStock, LocalDate fechaCaducidad, TipoComida tipoComida, String[] ingredientes) {
        super(factura, nombre, precio, enStock, fechaCaducidad);
        this.tipoComida = tipoComida;
        this.ingredientes = ingredientes;
    }

    public Comida(String factura, String nombre, double precio, boolean enStock, LocalDate fechaCaducidad, boolean conDescuento, int descuento, TipoComida tipoComida, String[] ingredientes) {
        super(factura, nombre, precio, enStock, fechaCaducidad, conDescuento, descuento);
        this.tipoComida = tipoComida;
        this.ingredientes = ingredientes;
    }

    public TipoComida getTipoComida() {
        return tipoComida;
    }

    public void setTipoComida(TipoComida tipoComida) {
        this.tipoComida = tipoComida;
    }

    public String[] getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String[] ingredientes) {
        this.ingredientes = ingredientes;
    }
}
