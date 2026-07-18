/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.File;
import java.nio.charset.StandardCharsets;
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

    public void enviarNotificacionReserva(
            Reserva reserva,
            File archivoPDF
    ) {

        if (reserva == null) {

            System.err.println(
                    "No se puede enviar el correo porque la reserva es nula."
            );

            return;
        }

        if (correoEmisor == null
                || correoEmisor.isBlank()
                || claveAplicacion == null
                || claveAplicacion.isBlank()) {

            System.err.println(
                    "No se encontraron EMAIL_USER o EMAIL_PASSWORD "
                    + "en el archivo .env."
            );

            return;
        }

        String correoCliente = reserva.getCorreoCliente();

        if (correoCliente == null || correoCliente.isBlank()) {

            System.err.println(
                    "La reserva número "
                    + reserva.getId()
                    + " no tiene un correo asociado."
            );

            return;
        }

        if (archivoPDF == null || !archivoPDF.exists()) {

            System.err.println(
                    "No se encontró el PDF de la reserva "
                    + reserva.getId()
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
                    protected PasswordAuthentication
                            getPasswordAuthentication() {

                        return new PasswordAuthentication(
                                correoEmisor.trim(),
                                claveAplicacion.replace(" ", "")
                        );
                    }
                }
        );

        try {

            MimeMessage mensaje = new MimeMessage(sesion);

            mensaje.setFrom(
                    new InternetAddress(
                            correoEmisor.trim(),
                            "Hotel Luna Rose",
                            StandardCharsets.UTF_8.name()
                    )
            );

            mensaje.setRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(
                            correoCliente.trim()
                    )
            );

            mensaje.setSubject(
                    crearAsunto(reserva),
                    StandardCharsets.UTF_8.name()
            );

            /*
             * Primera parte del correo:
             * contenido de texto.
             */
            MimeBodyPart cuerpoMensaje = new MimeBodyPart();

            cuerpoMensaje.setText(
                    crearContenido(reserva),
                    StandardCharsets.UTF_8.name()
            );

            /*
             * Segunda parte del correo:
             * PDF adjunto.
             */
            MimeBodyPart archivoAdjunto = new MimeBodyPart();

            FileDataSource fuenteArchivo =
                    new FileDataSource(archivoPDF);

            archivoAdjunto.setDataHandler(
                    new DataHandler(fuenteArchivo)
            );

            archivoAdjunto.setFileName(
                    archivoPDF.getName()
            );

            /*
             * Se unen el texto y el archivo PDF.
             */
            Multipart contenidoCompleto =
                    new MimeMultipart();

            contenidoCompleto.addBodyPart(cuerpoMensaje);
            contenidoCompleto.addBodyPart(archivoAdjunto);

            mensaje.setContent(contenidoCompleto);

            Transport.send(mensaje);

            System.out.println(
                    "Correo enviado correctamente a: "
                    + correoCliente.trim()
            );

            System.out.println(
                    "PDF adjuntado correctamente: "
                    + archivoPDF.getName()
            );

        } catch (Exception ex) {

            System.err.println(
                    "Error al enviar el correo de la reserva "
                    + reserva.getId()
                    + ": "
                    + ex.getMessage()
            );

            ex.printStackTrace();
        }
    }

    private String crearAsunto(Reserva reserva) {

        String estado = reserva.getEstado();

        if (estado == null || estado.isBlank()) {
            estado = "Actualizada";
        }

        return "Reserva "
                + reserva.getId()
                + " "
                + estado
                + " - Hotel Luna Rose";
    }

    private String crearContenido(Reserva reserva) {

        String nombreCliente = reserva.getNombreCliente();

        if (nombreCliente == null || nombreCliente.isBlank()) {
            nombreCliente = "cliente";
        }

        String estado = reserva.getEstado();

        if (estado == null || estado.isBlank()) {
            estado = "Actualizada";
        }

        StringBuilder contenido = new StringBuilder();

        contenido.append("Estimado/a ")
                .append(nombreCliente)
                .append(":\n\n");

        contenido.append("Le informamos que su reserva número ")
                .append(reserva.getId())
                .append(" cambió al estado: ")
                .append(estado.toUpperCase())
                .append(".\n\n");

        if (reserva.getNumeroHabitacion() != null) {

            contenido.append("Habitación: ")
                    .append(reserva.getNumeroHabitacion())
                    .append("\n");
        }

        if (reserva.getTipoHabitacion() != null) {

            contenido.append("Tipo de habitación: ")
                    .append(reserva.getTipoHabitacion())
                    .append("\n");
        }

        if (reserva.getCheckIn() != null) {

            contenido.append("Fecha de ingreso: ")
                    .append(reserva.getCheckIn())
                    .append("\n");
        }

        if (reserva.getCheckOut() != null) {

            contenido.append("Fecha de salida: ")
                    .append(reserva.getCheckOut())
                    .append("\n");
        }

        contenido.append("\n")
                .append(crearMensajeSegunEstado(estado))
                .append("\n\n");

        contenido.append(
                "Se adjunta el documento PDF correspondiente "
                + "a su reserva.\n\n"
        );

        contenido.append("Gracias por elegir Hotel Luna Rose.\n")
                .append("EMPRESA HOTELERA LUNA S.A.C.");

        return contenido.toString();
    }

    private String crearMensajeSegunEstado(String estado) {

        if (estado.equalsIgnoreCase("Confirmada")) {

            return "Su reserva fue confirmada satisfactoriamente.";
        }

        if (estado.equalsIgnoreCase("Cancelada")) {

            return "Su reserva fue cancelada. "
                    + "Se aplicó una penalidad del 20 %.";
        }

        if (estado.equalsIgnoreCase("Finalizada")) {

            return "Su estadía fue finalizada satisfactoriamente. "
                    + "Esperamos recibirle nuevamente.";
        }

        return "Su reserva fue actualizada correctamente.";
    }
}