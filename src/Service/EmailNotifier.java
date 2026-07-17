/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import model.Reserva;

public class EmailNotifier implements Observer {

    private final EmailService emailService;

    public EmailNotifier() {
        this.emailService = new EmailService();
    }

    @Override
    public void actualizar(Reserva reserva) {
        emailService.enviarNotificacionReserva(reserva);
    }
}