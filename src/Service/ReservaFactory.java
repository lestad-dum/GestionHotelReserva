/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import model.Reserva;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservaFactory {


    public static Reserva crearReserva(long clienteId,
                                        long habitacionId,
                                        LocalDate checkIn,
                                        LocalDate checkOut) {


        Reserva reserva = new Reserva();


        reserva.setClienteId(clienteId);

        reserva.setHabitacionId(habitacionId);

        reserva.setFechaRegistro(LocalDateTime.now());

        reserva.setCheckIn(checkIn);

        reserva.setCheckOut(checkOut);

        reserva.setEstado("Pendiente");


        return reserva;

    }

}