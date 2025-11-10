package com.marialo.cafeteriamvc;

import com.marialo.cafeteriamvc.gui.CafeteriaControlador;
import com.marialo.cafeteriamvc.gui.CafeteriaModelo;
import com.marialo.cafeteriamvc.gui.Ventana;

public class Principal {
    public static void main(String[] args) {
        Ventana vista = new Ventana();
        CafeteriaModelo modelo = new CafeteriaModelo();
        CafeteriaControlador controlador = new CafeteriaControlador(vista, modelo);
    }
}
