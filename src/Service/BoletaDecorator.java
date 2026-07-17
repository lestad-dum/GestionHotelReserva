/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import model.Reserva;

public abstract class BoletaDecorator {

    protected Boleta boleta;

    public BoletaDecorator(Boleta boleta) {
        this.boleta = boleta;
    }

    public Reserva getReserva() {
        return boleta.getReserva();
    }

    public long getCantidadNoches() {
        return boleta.getCantidadNoches();
    }

    public double getSubtotal() {
        return boleta.getSubtotal();
    }

    public double getPenalidad() {
        return boleta.getPenalidad();
    }

    public double getTotal() {
        return boleta.getTotal();
    }

    public String getTitulo() {
        return boleta.getTitulo();
    }
}