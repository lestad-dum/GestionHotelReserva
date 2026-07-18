/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import model.Reserva;

public class EmailService {

    private final String correoEmisor;
    private final String claveAplicacion;

    public EmailService() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        correoEmisor = dotenv.get("EMAIL_USER");
        claveAplicacion = dotenv.get("EMAIL_PASSWORD");
    }

    public void enviarNotificacionReserva(Reserva reserva) {

        if (correoEmisor == null || correoEmisor.isBlank()
                || claveAplicacion == null || claveAplicacion.isBlank()) {

            System.err.println(
                    "No se encontraron EMAIL_USER o EMAIL_PASSWORD en el archivo .env."
            );
            return;
        }

        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.starttls.required", "true");
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");

        Session sesion = Session.getInstance(
                propiedades,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                correoEmisor,
                                claveAplicacion.replace(" ", "")
                        );
                    }
                }
        );

        try {
            MimeMessage mensaje = new MimeMessage(sesion);

            mensaje.setFrom(
                    new InternetAddress(
                            correoEmisor,
                            "Hotel Luna Rose"
                    )
            );

          if (reserva.getCorreoCliente() == null
        || reserva.getCorreoCliente().isBlank()) {

    System.err.println(
            "La reserva no tiene un correo de cliente asociado."
    );

    return;
}

mensaje.setRecipient(
        Message.RecipientType.TO,
        new InternetAddress(
                reserva.getCorreoCliente().trim()
        )
);

            mensaje.setSubject(
                    "Actualización de reserva - Hotel Luna Rose",
                    "UTF-8"
            );

            String contenido
                    = "Estimado cliente:\n\n"
                    + "La reserva número " + reserva.getId()
                    + " cambió al estado: " + reserva.getEstado()
                    + ".\n\n"
                    + "Gracias por elegir Hotel Luna Rose.";

            mensaje.setText(contenido, "UTF-8");

            Transport.send(mensaje);

            System.out.println(
        "Correo enviado correctamente a: "
        + reserva.getCorreoCliente()
);

        } catch (Exception ex) {
            System.err.println(
                    "Error al enviar el correo: "
                    + ex.getMessage()
            );

            ex.printStackTrace();
        }
    }
}