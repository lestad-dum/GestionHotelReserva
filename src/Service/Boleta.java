/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.time.temporal.ChronoUnit;
import model.Reserva;

public class Boleta {

    private final Reserva reserva;

    public static final String EMPRESA =
            "EMPRESA HOTELERA LUNA S.A.C.";

    public static final String RUC =
            "20601525784";

    public static final String TELEFONO =
            "952356845";

    public static final String DIRECCION =
            "Av. Larco 560, Miraflores, Lima";

    public Boleta(Reserva reserva) {
        this.reserva = reserva;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public long getCantidadNoches() {

        return ChronoUnit.DAYS.between(
                reserva.getCheckIn(),
                reserva.getCheckOut()
        );
    }

    public double getSubtotal() {

        return getCantidadNoches()
                * reserva.getPrecioPorNoche();
    }

    public double getPenalidad() {

        if (reserva.getEstado().equalsIgnoreCase("Cancelada")) {

            return getSubtotal() * 0.20;

        }

        return 0;
    }

    public double getTotal() {

        if (reserva.getEstado().equalsIgnoreCase("Cancelada")) {

            return getSubtotal() - getPenalidad();

        }

        return getSubtotal();
    }

    public String getTitulo() {

        switch (reserva.getEstado()) {

            case "Confirmada":
                return "CONFIRMACIÓN DE RESERVA";

            case "Cancelada":
                return "CONSTANCIA DE CANCELACIÓN";

            case "Finalizada":
                return "BOLETA DE HOSPEDAJE";

            default:
                return "BOLETA";
        }

    }

}