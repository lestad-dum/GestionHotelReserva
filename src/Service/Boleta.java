/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import model.Reserva;

public class Boleta {

    public String generarContenido(Reserva reserva) {

        String titulo;

        switch (reserva.getEstado()) {

            case "Confirmada":
                titulo = "CONFIRMACIÓN DE RESERVA";
                break;

            case "Cancelada":
                titulo = "CONSTANCIA DE CANCELACIÓN";
                break;

            case "Finalizada":
                titulo = "COMPROBANTE DE ESTADÍA FINALIZADA";
                break;

            default:
                titulo = "DETALLE DE RESERVA";
                break;
        }

        return titulo + "\n\n"
                + "Hotel Luna Rose\n"
                + "Reserva N.°: " + reserva.getId() + "\n"
                + "Estado: " + reserva.getEstado() + "\n\n"
                + "Gracias por elegir Hotel Luna Rose.";
    }
}