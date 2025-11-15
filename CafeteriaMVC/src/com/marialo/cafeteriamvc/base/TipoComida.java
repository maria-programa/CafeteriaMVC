package com.marialo.cafeteriamvc.base;

public enum TipoComida {
    DULCE("Dulce"),
    SALADO("Salado");

    private final String nombre;

    TipoComida(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public static TipoComida fromString(String texto) {
        for (TipoComida tipo : TipoComida.values()) {
            if (tipo.nombre.equalsIgnoreCase(texto)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de comida no válido: " + texto);
    }
}
