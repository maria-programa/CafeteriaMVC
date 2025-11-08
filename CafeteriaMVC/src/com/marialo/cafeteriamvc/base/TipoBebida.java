package com.marialo.cafeteriamvc.base;

public enum TipoBebida {
    CAFE("Cafe"),
    TE("Té"),
    INFUSION("Infusion"),
    ZUMO("Zumo"),
    REFRESCO("Refresco");

    private final String nombre;

    TipoBebida(String nombre) {
        this.nombre = nombre;
    }
}
