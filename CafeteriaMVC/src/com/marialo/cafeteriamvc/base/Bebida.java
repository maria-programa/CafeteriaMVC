package com.marialo.cafeteriamvc.base;

import java.time.LocalDate;

public class Bebida extends Producto {
    private TipoBebida tipoBebida;

    public Bebida() {}

    public Bebida(TipoBebida tipoBebida) {
        this.tipoBebida = tipoBebida;
    }

    public Bebida(String factura, String nombre, double precio, TipoBebida tipoBebida) {
        super(factura, nombre, precio);
        this.tipoBebida = tipoBebida;
    }

    public Bebida(String factura, String nombre, double precio, boolean enStock, LocalDate fechaCaducidad, TipoBebida tipoBebida) {
        super(factura, nombre, precio, enStock, fechaCaducidad);
        this.tipoBebida = tipoBebida;
    }

    public Bebida(String factura, String nombre, double precio, boolean enStock, LocalDate fechaCaducidad, boolean conDescuento, int descuento, TipoBebida tipoBebida) {
        super(factura, nombre, precio, enStock, fechaCaducidad, conDescuento, descuento);
        this.tipoBebida = tipoBebida;
    }

    public TipoBebida getTipoBebida() {
        return tipoBebida;
    }

    public void setTipoBebida(TipoBebida tipoBebida) {
        this.tipoBebida = tipoBebida;
    }
}
