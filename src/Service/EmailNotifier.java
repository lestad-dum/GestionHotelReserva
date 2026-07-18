/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.io.File;
import model.Reserva;

public class EmailNotifier implements Observer {

    private final EmailService emailService;

    public EmailNotifier() {
        this.emailService = new EmailService();
    }

    @Override
    public void actualizar(Reserva reserva) {

        if (reserva == null) {

            System.err.println(
                    "No se puede generar la notificación: "
                    + "la reserva es nula."
            );

            return;
        }

        /*
         * Se crea la boleta con todos los datos
         * de la reserva.
         */
        Boleta boleta = new Boleta(reserva);

        /*
         * El decorador genera el archivo PDF.
         */
        BoletaPDFDecorator boletaPDF =
                new BoletaPDFDecorator(boleta);

        File archivoPDF = boletaPDF.generarPDF();

        if (archivoPDF == null || !archivoPDF.exists()) {

            System.err.println(
                    "No se pudo generar el PDF de la reserva "
                    + reserva.getId()
            );

            return;
        }

        /*
         * Se envía el correo al cliente
         * con el PDF adjunto.
         */
        emailService.enviarNotificacionReserva(
                reserva,
                archivoPDF
        );

        /*
         * También se abre el PDF en la computadora.
         */
        GeneradorPDF generadorPDF =
                new GeneradorPDF();

        generadorPDF.abrirPDF(archivoPDF);
    }
}