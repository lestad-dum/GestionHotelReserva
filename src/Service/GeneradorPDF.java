/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.Color;

import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

public class GeneradorPDF {

    private final Font fuenteTitulo = new Font(
            Font.HELVETICA,
            18,
            Font.BOLD
    );

    private final Font fuenteSubtitulo = new Font(
            Font.HELVETICA,
            12,
            Font.BOLD
    );

    private final Font fuenteNormal = new Font(
            Font.HELVETICA,
            10,
            Font.NORMAL
    );

    private final Font fuenteNegrita = new Font(
            Font.HELVETICA,
            10,
            Font.BOLD
    );

    public File generarBoletaPDF(BoletaPDFDecorator boleta) {

        File carpeta = new File("boletas");

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        String nombreArchivo =
                "Boleta_Reserva_"
                + boleta.getReserva().getId()
                + "_"
                + boleta.getReserva().getEstado()
                + ".pdf";

        File archivo = new File(carpeta, nombreArchivo);

        Document documento = new Document(
                PageSize.A4,
                40,
                40,
                35,
                35
        );

        try {

            PdfWriter.getInstance(
                    documento,
                    new FileOutputStream(archivo)
            );

            documento.open();

            agregarEncabezado(documento, boleta);
            agregarTitulo(documento, boleta);
            agregarDatosCliente(documento, boleta);
            agregarDatosHospedaje(documento, boleta);
            agregarDetallePago(documento, boleta);
            agregarTotales(documento, boleta);
            agregarPiePagina(documento, boleta);
            documento.close();

            System.out.println(
                    "Boleta PDF generada correctamente: "
                    + archivo.getAbsolutePath()
            );

            return archivo;

        } catch (Exception ex) {

            System.err.println(
                    "Error al generar la boleta PDF: "
                    + ex.getMessage()
            );

            ex.printStackTrace();

            if (documento.isOpen()) {
                documento.close();
            }

            return null;
        }
    }

    private void agregarEncabezado(
            Document documento,
            BoletaPDFDecorator boleta
    ) throws Exception {

        PdfPTable tablaEncabezado = new PdfPTable(2);

        tablaEncabezado.setWidthPercentage(100);
        tablaEncabezado.setWidths(new float[]{2.2f, 1f});

        PdfPCell celdaEmpresa = new PdfPCell();
        celdaEmpresa.setBorder(PdfPCell.NO_BORDER);
        celdaEmpresa.setPadding(8);

        Paragraph empresa = new Paragraph(
                Boleta.EMPRESA,
                fuenteSubtitulo
        );

        empresa.setSpacingAfter(4);

        celdaEmpresa.addElement(empresa);

        celdaEmpresa.addElement(
                new Paragraph(
                        "RUC: " + Boleta.RUC,
                        fuenteNormal
                )
        );

        celdaEmpresa.addElement(
                new Paragraph(
                        "Dirección: " + Boleta.DIRECCION,
                        fuenteNormal
                )
        );

        celdaEmpresa.addElement(
                new Paragraph(
                        "Teléfono: " + Boleta.TELEFONO,
                        fuenteNormal
                )
        );

        PdfPCell celdaDocumento = new PdfPCell();
        celdaDocumento.setPadding(8);
        celdaDocumento.setHorizontalAlignment(Element.ALIGN_CENTER);
        celdaDocumento.setVerticalAlignment(Element.ALIGN_MIDDLE);

        celdaDocumento.addElement(
                crearParrafoCentrado(
                        boleta.getTitulo(),
                        fuenteSubtitulo
                )
        );

        celdaDocumento.addElement(
                crearParrafoCentrado(
                        "Reserva N.° "
                        + boleta.getReserva().getId(),
                        fuenteNegrita
                )
        );

        celdaDocumento.addElement(
                crearParrafoCentrado(
                        "Fecha de emisión: "
                        + LocalDate.now().format(
                                DateTimeFormatter.ofPattern(
                                        "dd/MM/yyyy"
                                )
                        ),
                        fuenteNormal
                )
        );

        tablaEncabezado.addCell(celdaEmpresa);
        tablaEncabezado.addCell(celdaDocumento);

        documento.add(tablaEncabezado);
    }

    private void agregarTitulo(
            Document documento,
            BoletaPDFDecorator boleta
    ) throws Exception {

        Paragraph espacio = new Paragraph(" ");
        espacio.setSpacingBefore(4);
        documento.add(espacio);

        Paragraph titulo = new Paragraph(
                boleta.getTitulo(),
                fuenteTitulo
        );

        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingBefore(10);
        titulo.setSpacingAfter(14);

        documento.add(titulo);
    }

    private void agregarDatosCliente(
        Document documento,
        BoletaPDFDecorator boleta
) throws Exception {

    Paragraph subtitulo = new Paragraph(
            "DATOS DEL CLIENTE",
            fuenteSubtitulo
    );

    subtitulo.setSpacingBefore(8);
    subtitulo.setSpacingAfter(6);

    documento.add(subtitulo);

    PdfPTable tablaCliente = new PdfPTable(2);

    tablaCliente.setWidthPercentage(100);
    tablaCliente.setWidths(new float[]{1f, 3f});
    tablaCliente.setSpacingAfter(14);

    agregarFilaDato(
            tablaCliente,
            "Cliente:",
            boleta.getReserva().getNombreCliente()
    );

    agregarFilaDato(
            tablaCliente,
            "Correo:",
            boleta.getReserva().getCorreoCliente()
    );

    agregarFilaDato(
            tablaCliente,
            "Código de cliente:",
            String.valueOf(
                    boleta.getReserva().getClienteId()
            )
    );

    documento.add(tablaCliente);
}

private void agregarDatosHospedaje(
        Document documento,
        BoletaPDFDecorator boleta
) throws Exception {

    Paragraph subtitulo = new Paragraph(
            "DETALLE DEL HOSPEDAJE",
            fuenteSubtitulo
    );

    subtitulo.setSpacingAfter(6);

    documento.add(subtitulo);

    PdfPTable tablaHospedaje = new PdfPTable(2);

    tablaHospedaje.setWidthPercentage(100);
    tablaHospedaje.setWidths(new float[]{1f, 3f});
    tablaHospedaje.setSpacingAfter(14);

    agregarFilaDato(
            tablaHospedaje,
            "Habitación:",
            boleta.getReserva().getNumeroHabitacion()
    );

    agregarFilaDato(
            tablaHospedaje,
            "Tipo:",
            boleta.getReserva().getTipoHabitacion()
    );

    agregarFilaDato(
            tablaHospedaje,
            "Fecha de ingreso:",
            formatearFecha(
                    boleta.getReserva().getCheckIn()
            )
    );

    agregarFilaDato(
            tablaHospedaje,
            "Fecha de salida:",
            formatearFecha(
                    boleta.getReserva().getCheckOut()
            )
    );

    agregarFilaDato(
            tablaHospedaje,
            "Cantidad de noches:",
            boleta.getCantidadNoches() + " noche(s)"
    );

    agregarFilaDato(
            tablaHospedaje,
            "Estado:",
            boleta.getReserva().getEstado()
    );

    documento.add(tablaHospedaje);
}

private void agregarFilaDato(
        PdfPTable tabla,
        String etiqueta,
        String valor
) {

    PdfPCell celdaEtiqueta = new PdfPCell(
            new Phrase(etiqueta, fuenteNegrita)
    );

    celdaEtiqueta.setPadding(6);
    celdaEtiqueta.setBackgroundColor(
        new Color(240, 240, 240)
);

    PdfPCell celdaValor = new PdfPCell(
            new Phrase(
                    valor != null ? valor : "-",
                    fuenteNormal
            )
    );

    celdaValor.setPadding(6);

    tabla.addCell(celdaEtiqueta);
    tabla.addCell(celdaValor);
}

private String formatearFecha(
        java.time.LocalDate fecha
) {

    if (fecha == null) {
        return "-";
    }

    return fecha.format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
    );
}

private void agregarDetallePago(
        Document documento,
        BoletaPDFDecorator boleta
) throws Exception {

    Paragraph subtitulo = new Paragraph(
            "DETALLE DEL PAGO",
            fuenteSubtitulo
    );

    subtitulo.setSpacingAfter(6);
    documento.add(subtitulo);

    PdfPTable tablaDetalle = new PdfPTable(4);

    tablaDetalle.setWidthPercentage(100);
    tablaDetalle.setWidths(
            new float[]{3f, 1.2f, 1.5f, 1.5f}
    );

    agregarCeldaCabecera(tablaDetalle, "Concepto");
    agregarCeldaCabecera(tablaDetalle, "Cantidad");
    agregarCeldaCabecera(tablaDetalle, "Precio unitario");
    agregarCeldaCabecera(tablaDetalle, "Importe");

    agregarCeldaDetalle(
            tablaDetalle,
            "Hospedaje en habitación "
            + boleta.getReserva().getNumeroHabitacion()
            + " - "
            + boleta.getReserva().getTipoHabitacion(),
            Element.ALIGN_LEFT
    );

    agregarCeldaDetalle(
            tablaDetalle,
            boleta.getCantidadNoches() + " noche(s)",
            Element.ALIGN_CENTER
    );

    agregarCeldaDetalle(
            tablaDetalle,
            formatearMoneda(
                    boleta.getReserva().getPrecioPorNoche()
            ),
            Element.ALIGN_RIGHT
    );

    agregarCeldaDetalle(
            tablaDetalle,
            formatearMoneda(boleta.getSubtotal()),
            Element.ALIGN_RIGHT
    );

    tablaDetalle.setSpacingAfter(12);
    documento.add(tablaDetalle);
}

private void agregarTotales(
        Document documento,
        BoletaPDFDecorator boleta
) throws Exception {

    PdfPTable tablaTotales = new PdfPTable(2);

    tablaTotales.setWidthPercentage(45);
    tablaTotales.setHorizontalAlignment(Element.ALIGN_RIGHT);
    tablaTotales.setWidths(new float[]{2f, 1.3f});
    tablaTotales.setSpacingBefore(4);
    tablaTotales.setSpacingAfter(16);

    agregarFilaTotal(
            tablaTotales,
            "Subtotal:",
            formatearMoneda(boleta.getSubtotal()),
            false
    );

    if (boleta.getReserva()
            .getEstado()
            .equalsIgnoreCase("Cancelada")) {

        agregarFilaTotal(
                tablaTotales,
                "Penalidad (20 %):",
                formatearMoneda(boleta.getPenalidad()),
                false
        );

        agregarFilaTotal(
                tablaTotales,
                "Monto a devolver:",
                formatearMoneda(boleta.getTotal()),
                true
        );

    } else {

        agregarFilaTotal(
                tablaTotales,
                "TOTAL:",
                formatearMoneda(boleta.getTotal()),
                true
        );
    }

    documento.add(tablaTotales);
}

private void agregarPiePagina(
        Document documento,
        BoletaPDFDecorator boleta
) throws Exception {

    String mensaje;

    switch (boleta.getReserva().getEstado()) {

        case "Confirmada":
            mensaje =
                    "La reserva fue confirmada satisfactoriamente. "
                    + "Presente este documento al momento del ingreso.";
            break;

        case "Cancelada":
            mensaje =
                    "La reserva fue cancelada. Se aplicó una penalidad "
                    + "predeterminada del 20 % sobre el importe total.";
            break;

        case "Finalizada":
            mensaje =
                    "La estadía fue finalizada satisfactoriamente. "
                    + "Gracias por hospedarse en Hotel Luna Rose.";
            break;

        default:
            mensaje =
                    "Gracias por elegir Hotel Luna Rose.";
            break;
    }

    PdfPTable tablaEstado = new PdfPTable(1);
    tablaEstado.setWidthPercentage(100);
    tablaEstado.setSpacingBefore(5);

    PdfPCell celdaEstado = new PdfPCell();

    celdaEstado.setPadding(12);
    celdaEstado.setHorizontalAlignment(
            Element.ALIGN_CENTER
    );

    Paragraph estado = new Paragraph(
            "ESTADO: "
            + boleta.getReserva()
                    .getEstado()
                    .toUpperCase(),
            fuenteSubtitulo
    );

    estado.setAlignment(Element.ALIGN_CENTER);
    celdaEstado.addElement(estado);

    Paragraph nota = new Paragraph(
            mensaje,
            fuenteNormal
    );

    nota.setAlignment(Element.ALIGN_CENTER);
    nota.setSpacingBefore(5);

    celdaEstado.addElement(nota);
    tablaEstado.addCell(celdaEstado);

    documento.add(tablaEstado);

    Paragraph agradecimiento = new Paragraph(
            "EMPRESA HOTELERA LUNA S.A.C.\n"
            + "Gracias por su preferencia.",
            fuenteNegrita
    );

    agradecimiento.setAlignment(Element.ALIGN_CENTER);
    agradecimiento.setSpacingBefore(18);

    documento.add(agradecimiento);
}

private void agregarCeldaCabecera(
        PdfPTable tabla,
        String texto
) {

    PdfPCell celda = new PdfPCell(
            new Phrase(texto, fuenteNegrita)
    );

    celda.setPadding(7);
    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
    celda.setBackgroundColor(
        new Color(225, 225, 225)
);
    tabla.addCell(celda);
}

private void agregarCeldaDetalle(
        PdfPTable tabla,
        String texto,
        int alineacion
) {

    PdfPCell celda = new PdfPCell(
            new Phrase(
                    texto != null ? texto : "-",
                    fuenteNormal
            )
    );

    celda.setPadding(7);
    celda.setHorizontalAlignment(alineacion);
    celda.setVerticalAlignment(Element.ALIGN_MIDDLE);

    tabla.addCell(celda);
}

private void agregarFilaTotal(
        PdfPTable tabla,
        String etiqueta,
        String monto,
        boolean destacado
) {

    Font fuente = destacado
            ? fuenteSubtitulo
            : fuenteNegrita;

    PdfPCell celdaEtiqueta = new PdfPCell(
            new Phrase(etiqueta, fuente)
    );

    PdfPCell celdaMonto = new PdfPCell(
            new Phrase(monto, fuente)
    );

    celdaEtiqueta.setPadding(7);
    celdaMonto.setPadding(7);

    celdaEtiqueta.setHorizontalAlignment(
            Element.ALIGN_RIGHT
    );

    celdaMonto.setHorizontalAlignment(
            Element.ALIGN_RIGHT
    );

    if (destacado) {

        Color fondo = new Color(
                225,
                225,
                225
        );

        celdaEtiqueta.setBackgroundColor(fondo);
        celdaMonto.setBackgroundColor(fondo);
    }

    tabla.addCell(celdaEtiqueta);
    tabla.addCell(celdaMonto);
}

private String formatearMoneda(double monto) {

    return String.format(
            java.util.Locale.US,
            "S/ %.2f",
            monto
    );
}

    private Paragraph crearParrafoCentrado(
            String texto,
            Font fuente
    ) {

        Paragraph parrafo = new Paragraph(texto, fuente);
        parrafo.setAlignment(Element.ALIGN_CENTER);

        return parrafo;
    }

    public void abrirPDF(File archivo) {

        if (archivo == null || !archivo.exists()) {
            return;
        }

        try {

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(archivo);
            }

        } catch (Exception ex) {

            System.err.println(
                    "No se pudo abrir el PDF: "
                    + ex.getMessage()
            );
        }
    }
}