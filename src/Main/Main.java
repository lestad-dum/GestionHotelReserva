/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import controller.LoginController;
import view.LoginFrame;

import javax.swing.JFrame;


public class Main {

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(() -> {

            LoginFrame loginPanel = new LoginFrame();
           

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