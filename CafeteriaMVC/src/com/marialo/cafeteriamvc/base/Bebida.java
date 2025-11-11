package com.marialo.cafeteriamvc.base;

import java.time.LocalDate;

public class Bebida extends Producto {
    private TipoBebida tipoBebida;

    public Bebida() {}

    public Bebida(TipoBebida tipoBebida) {
        this.tipoBebida = tipoBebida;
    }

    public Bebida(String factura, String nombre, double precio, LocalDate fechaCaducidad, TipoBebida tipoBebida) {
        super(factura, nombre, precio, fechaCaducidad);
        this.tipoBebida = tipoBebida;
    }

    public Bebida(String factura, String nombre, double precio, LocalDate fechaCaducidad, boolean conDescuento, int descuento, TipoBebida tipoBebida) {
        super(factura, nombre, precio, fechaCaducidad, conDescuento, descuento);
        this.tipoBebida = tipoBebida;
    }

    public TipoBebida getTipoBebida() {
        return tipoBebida;
    }

    public void setTipoBebida(TipoBebida tipoBebida) {
        this.tipoBebida = tipoBebida;
    }

    @Override
    public String toString() {
        return  tipoBebida.toString() + " " + super.toString();
    }
}
