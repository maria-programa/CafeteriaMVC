package com.marialo.cafeteriamvc.base;

import java.time.LocalDate;

public class Comida extends Producto {
    private TipoComida tipoComida;
    private String[] ingredientes;

    public Comida() {

    }

    public Comida(String nombre, double precio, LocalDate fechaCaducidad, boolean enStock, boolean conDescuento, TipoComida tipoComida, String[] ingredientes) {
        super(nombre, precio, fechaCaducidad, enStock, conDescuento);
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
