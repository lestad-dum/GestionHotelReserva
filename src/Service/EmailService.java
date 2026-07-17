/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import model.Reserva;

public class EmailService {

    public void enviarNotificacionReserva(Reserva reserva) {

        System.out.println(
                "Notificación de reserva enviada. Estado: "
                + reserva.getEstado()
        );
    }
}