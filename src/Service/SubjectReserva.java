/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.ArrayList;
import java.util.List;
import model.Reserva;

public class SubjectReserva {

    private final List<Observer> observers;

    public SubjectReserva() {
        observers = new ArrayList<>();
    }

    public void agregarObserver(Observer observer) {
        observers.add(observer);
    }

    public void eliminarObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notificarObservers(Reserva reserva) {
        for (Observer observer : observers) {
            observer.actualizar(reserva);
        }
    }
}