package com.marialo.cafeteriamvc.base;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class Comida extends Producto {
    private TipoComida tipoComida;
    private ArrayList<String> ingredientes;

    public Comida() {}

    public Comida(TipoComida tipoComida, ArrayList<String> ingredientes) {
        this.tipoComida = tipoComida;
        this.ingredientes = ingredientes;
    }


    public Comida(String factura, String nombre, double precio, LocalDate fechaCaducidad, TipoComida tipoComida, ArrayList<String> ingredientes) {
        super(factura, nombre, precio, fechaCaducidad);
        this.tipoComida = tipoComida;
        this.ingredientes = ingredientes;
    }

    public Comida(String factura, String nombre, double precio, LocalDate fechaCaducidad, boolean conDescuento, int descuento, TipoComida tipoComida, ArrayList<String> ingredientes) {
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

    public ArrayList<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(ArrayList<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    @Override
    public String toString() {
        return tipoComida + " " + super.toString() + " Ingredientes: " + ingredientes;
    }
}
