package com.marialo.cafeteriamvc.base;

public enum TipoBebida {
    CAFE("Café"),
    TE("Té"),
    INFUSION("Infusión"),
    ZUMO("Zumo"),
    REFRESCO("Refresco");

    private final String nombre;

    TipoBebida(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public static TipoBebida fromString(String texto) {
        for (TipoBebida tipo : TipoBebida.values()) {
            if (tipo.nombre.equalsIgnoreCase(texto)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de bebida no válido: " + texto);
    }
}
