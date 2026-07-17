/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reserva {

    private long id;
    private long clienteId;
    private long habitacionId;
    private LocalDateTime fechaRegistro;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String estado;
    private String nombreCliente;
    private String numeroHabitacion;

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNumeroHabitacion() {
        return numeroHabitacion;
    }

    public void setNumeroHabitacion(String numeroHabitacion) {
        this.numeroHabitacion = numeroHabitacion;
    }
    
    public Reserva() {
    }

    public Reserva(long id, long clienteId, long habitacionId,
            LocalDateTime fechaRegistro,
            LocalDate checkIn,
            LocalDate checkOut,
            String estado) {

        this.id = id;
        this.clienteId = clienteId;
        this.habitacionId = habitacionId;
        this.fechaRegistro = fechaRegistro;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.estado = estado;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClienteId() {
        return clienteId;
    }

    public void setClienteId(long clienteId) {
        this.clienteId = clienteId;
    }

    public long getHabitacionId() {
        return habitacionId;
    }

    public void setHabitacionId(long habitacionId) {
        this.habitacionId = habitacionId;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}