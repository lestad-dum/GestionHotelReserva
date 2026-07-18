/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.ReservaDAO;
import java.io.File;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Reserva;
import service.Boleta;
import service.BoletaPDFDecorator;
import view.GestionReserva;
import view.Registros;
import service.EmailNotifier;
import service.GeneradorPDF;
import service.SubjectReserva;

public class GestionReservaController {

    private final GestionReserva vista;
    private final ReservaDAO reservaDAO;
    private final SubjectReserva subjectReserva;
    
    public GestionReservaController(GestionReserva vista) {
        this.vista = vista;
        this.reservaDAO = new ReservaDAO();
        this.subjectReserva = new SubjectReserva();
        this.subjectReserva.agregarObserver(new EmailNotifier());

        iniciarEventos();
        cargarReservas();
    }

    private void iniciarEventos() {

        vista.getBotonConfirmar().addActionListener(e -> {
            cambiarEstado("Confirmada");
        });

        vista.getBotonCancelar().addActionListener(e -> {
            cambiarEstado("Cancelada");
        });

        vista.getBotonFinalizar().addActionListener(e -> {
            cambiarEstado("Finalizada");
            
        });
        
        vista.getBotonRegresar().addActionListener(e -> {
       regresar();
});
        
    }
    
    private void regresar() {

    Registros registros = new Registros();
    new RegistrosController(registros);

    registros.setLocationRelativeTo(null);
    registros.setVisible(true);

    javax.swing.SwingUtilities.getWindowAncestor(vista).dispose();
}

    private void cargarReservas() {

        DefaultTableModel modelo =
                (DefaultTableModel) vista.getTablaReservas().getModel();

        modelo.setRowCount(0);

        List<Reserva> reservas = reservaDAO.listarReservas();

        for (Reserva reserva : reservas) {

            modelo.addRow(new Object[]{
                reserva.getId(),
                reserva.getClienteId(),
                reserva.getHabitacionId(),
               reserva.getCheckIn(),
                reserva.getCheckOut(),
                reserva.getEstado()
            });
        }
    }

    private void cambiarEstado(String nuevoEstado) {

        int filaSeleccionada =
                vista.getTablaReservas().getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Seleccione una reserva de la tabla."
            );
            return;
        }

        long idReserva = Long.parseLong(
                vista.getTablaReservas()
                        .getValueAt(filaSeleccionada, 0)
                        .toString()
        );

        boolean resultado = false;

        switch (nuevoEstado) {

            case "Confirmada":
                resultado = reservaDAO.confirmarReserva(idReserva);
                break;

            case "Cancelada":
                resultado = reservaDAO.cancelarReserva(idReserva);
                break;

            case "Finalizada":
                resultado = reservaDAO.finalizarReserva(idReserva);
                break;
        }

       if (resultado) {

    Reserva reservaActualizada =
            reservaDAO.obtenerReservaCompleta(idReserva);

    if (reservaActualizada != null) {

        subjectReserva.notificarObservers(
                reservaActualizada
        );

        Boleta boleta = new Boleta(
                reservaActualizada
        );

        BoletaPDFDecorator boletaPDF =
                new BoletaPDFDecorator(boleta);

        File archivoPDF =
                boletaPDF.generarPDF();

        if (archivoPDF != null) {
            new GeneradorPDF().abrirPDF(
                    archivoPDF
            );
        }
    }

    JOptionPane.showMessageDialog(
            vista,
            "Reserva actualizada a: " + nuevoEstado
    );

    cargarReservas();
} else {

            JOptionPane.showMessageDialog(
                    vista,
                    "No se pudo actualizar la reserva."
            );
        }
    }
}