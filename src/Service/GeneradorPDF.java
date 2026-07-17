/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.io.FileOutputStream;
import org.openpdf.text.Document;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfWriter;

public class GeneradorPDF {

    public void probarPDF() {

        Document documento = new Document();

        try {
            PdfWriter.getInstance(
                    documento,
                    new FileOutputStream("prueba_hotel.pdf")
            );

            documento.open();

            documento.add(
                    new Paragraph("Hotel Luna Rose")
            );

            documento.add(
                    new Paragraph("OpenPDF funciona correctamente.")
            );

            documento.close();

            System.out.println("PDF generado correctamente.");

        } catch (Exception ex) {
            System.err.println(
                    "Error al generar PDF: " + ex.getMessage()
            );
            ex.printStackTrace();
        }
    }
}