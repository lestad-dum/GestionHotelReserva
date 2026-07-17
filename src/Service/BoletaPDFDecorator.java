/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package service;

import java.io.File;

public class BoletaPDFDecorator extends BoletaDecorator {

    private final GeneradorPDF generadorPDF;

    public BoletaPDFDecorator(Boleta boleta) {
        super(boleta);
        this.generadorPDF = new GeneradorPDF();
    }

    public File generarPDF() {
        return generadorPDF.generarBoletaPDF(this);
    }
}