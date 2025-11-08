package com.marialo.cafeteriamvc.base;

import java.time.LocalDate;

public class Bebida extends Producto {
    private TipoBebida tipoBebida;

    public Bebida() {

    }

    public Bebida(String nombre, double precio, LocalDate fechaCaducidad, boolean enStock, boolean conDescuento, TipoBebida tipoBebida) {
        super(nombre, precio, fechaCaducidad, enStock, conDescuento);
        this.tipoBebida = tipoBebida;
    }

    public TipoBebida getTipoBebida() {
        return tipoBebida;
    }

    public void setTipoBebida(TipoBebida tipoBebida) {
        this.tipoBebida = tipoBebida;
    }
}
