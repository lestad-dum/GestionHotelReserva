/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.HabitacionDAO;
import dao.ReservaDAO;
import model.Reserva;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReservaService {

    private ReservaDAO reservaDAO;
    private HabitacionDAO habitacionDAO;


    public ReservaService() {

        reservaDAO = new ReservaDAO();
        habitacionDAO = new HabitacionDAO();

    }


    // Registrar una reserva validando disponibilidad
    public boolean registrarReserva(Reserva reserva) {


        boolean disponible = habitacionDAO.verificarDisponibilidad(
                reserva.getHabitacionId(),
                reserva.getCheckIn(),
                reserva.getCheckOut()
        );


        if (!disponible) {

            return false;

        }


        // Estado inicial


        return reservaDAO.registrarReserva(reserva);

    }



    // Obtener todas las reservas
    public ArrayList<Reserva> listarReservas() {

        return reservaDAO.listarReservas();

    }



    // Confirmar reserva
    public boolean confirmarReserva(long idReserva) {

        return reservaDAO.confirmarReserva(idReserva);

    }



    // Cancelar reserva
    public boolean cancelarReserva(long idReserva) {

        return reservaDAO.cancelarReserva(idReserva);

    }



    // Finalizar reserva
    public boolean finalizarReserva(long idReserva) {

        return reservaDAO.finalizarReserva(idReserva);

    }


}