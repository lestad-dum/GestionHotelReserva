/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.ClienteDAO;
import view.BuscarCodigo;
import view.Registros;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class BuscarCodigoController {

    private final BuscarCodigo vistaBuscar;
    private final Registros vistaRegistros;
    private final ClienteDAO clienteDAO;
    private final JDialog ventana;

    public BuscarCodigoController(
            BuscarCodigo vistaBuscar,
            Registros vistaRegistros,
            JDialog ventana
    ) {

        this.vistaBuscar = vistaBuscar;
        this.vistaRegistros = vistaRegistros;
        this.ventana = ventana;
        this.clienteDAO = new ClienteDAO();

        iniciarEventos();
    }

    private void iniciarEventos() {

        vistaBuscar.getBtnBuscar().addActionListener(
                e -> buscarCliente()
        );

        vistaBuscar.getTxtDni().addActionListener(
                e -> buscarCliente()
        );
    }

    private void buscarCliente() {

        String dni =
                vistaBuscar.getTxtDni().getText().trim();

        if (dni.isEmpty()) {

            JOptionPane.showMessageDialog(
                    vistaBuscar,
                    "Ingrese el DNI del cliente.",
                    "Campo vacío",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!dni.matches("\\d{8}")) {

            JOptionPane.showMessageDialog(
                    vistaBuscar,
                    "El DNI debe contener exactamente 8 números.",
                    "DNI incorrecto",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        long idCliente =
                clienteDAO.buscarClientePorDni(dni);

        if (idCliente == -1) {

            JOptionPane.showMessageDialog(
                    vistaBuscar,
                    "No existe un cliente registrado con ese DNI.",
                    "Cliente no encontrado",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        vistaRegistros.getTxtCodigoCliente().setText(
                String.valueOf(idCliente)
        );

        JOptionPane.showMessageDialog(
                vistaBuscar,
                "Cliente encontrado correctamente.",
                "Búsqueda exitosa",
                JOptionPane.INFORMATION_MESSAGE
        );

        ventana.dispose();
    }
}