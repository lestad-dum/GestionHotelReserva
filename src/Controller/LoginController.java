/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.UsuarioDAO;
import model.Usuario;
import view.LoginFrame;
import controller.RegistrosController;
import javax.swing.JOptionPane;

public class LoginController {

    private final LoginFrame vista;
    private final UsuarioDAO usuarioDAO;

    public LoginController(LoginFrame vista) {

        this.vista = vista;
        this.usuarioDAO = new UsuarioDAO();

        iniciarEventos();
    }

    private void iniciarEventos() {

        vista.getButtonIngresar().addActionListener(e -> iniciarSesion());
    }

    private void iniciarSesion() {

        String nombreUsuario =
                vista.getTextUsuario().getText().trim();

        String contrasena =
                new String(
                        vista.getPasswordContrasena().getPassword()
                ).trim();

        if (nombreUsuario.isEmpty() || contrasena.isEmpty()) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Complete el usuario y la contraseña.",
                    "Campos vacíos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Usuario usuario =
                usuarioDAO.validarLogin(nombreUsuario, contrasena);

      if (usuario != null) {

    JOptionPane.showMessageDialog(
            vista,
            "Bienvenido al sistema."
    );

    view.Registros registros = new view.Registros();

new RegistrosController(registros);

registros.setLocationRelativeTo(null);
registros.setVisible(true);

javax.swing.SwingUtilities
        .getWindowAncestor(vista)
        .dispose();

    javax.swing.SwingUtilities
            .getWindowAncestor(vista)
            .dispose();



        } else {

            JOptionPane.showMessageDialog(
                    vista,
                    "Usuario o contraseña incorrectos.",
                    "Error de acceso",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}