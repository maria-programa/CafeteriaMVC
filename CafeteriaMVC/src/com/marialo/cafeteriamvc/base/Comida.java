package com.marialo.cafeteriamvc.base;

import java.time.LocalDate;
import java.util.Arrays;

public class Comida extends Producto {
    private TipoComida tipoComida;
    private String[] ingredientes;

    public Comida() {}

    public Comida(TipoComida tipoComida, String[] ingredientes) {
        this.tipoComida = tipoComida;
        this.ingredientes = ingredientes;
    }


    public Comida(String factura, String nombre, double precio, LocalDate fechaCaducidad, TipoComida tipoComida, String[] ingredientes) {
        super(factura, nombre, precio, fechaCaducidad);
        this.tipoComida = tipoComida;
        this.ingredientes = ingredientes;
    }

    public Comida(String factura, String nombre, double precio, LocalDate fechaCaducidad, boolean conDescuento, int descuento, TipoComida tipoComida, String[] ingredientes) {
        super(factura, nombre, precio, fechaCaducidad, conDescuento, descuento);
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

    @Override
    public String toString() {
        return tipoComida + " " + super.toString() + "\nIngredientes: " + Arrays.toString(ingredientes);
    }
}
