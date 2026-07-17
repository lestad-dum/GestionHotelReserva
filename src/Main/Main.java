/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import controller.LoginController;
import view.LoginFrame;

import javax.swing.JFrame;
import service.GeneradorPDF;

public class Main {

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(() -> {

            LoginFrame loginPanel = new LoginFrame();
            GeneradorPDF pdf = new GeneradorPDF();
            pdf.probarPDF();

            new LoginController(loginPanel);

            JFrame ventana = new JFrame("Hotel Luna Rose");

            ventana.setContentPane(loginPanel);
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.pack();
            ventana.setLocationRelativeTo(null);
            ventana.setResizable(false);
            ventana.setVisible(true);

        });
    }
}