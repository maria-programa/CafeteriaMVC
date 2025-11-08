package com.marialo.cafeteriamvc.base;

public enum TipoComida {
    DULCE("Dulce"),
    SALADO("Salado");

    private final String nombre;

    TipoComida(String nombre) {
        this.nombre = nombre;
    }
}
