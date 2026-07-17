/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Habitacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.time.LocalDate;

public class HabitacionDAO {

    private Connection conexion;

    public HabitacionDAO() {
        conexion = Conexion.getInstancia().getConexion();
    }

    // Listar habitaciones
    public ArrayList<Habitacion> listarHabitaciones() {

        ArrayList<Habitacion> lista = new ArrayList<>();

        String sql = """
                SELECT id, numero, tipo, precio
                FROM habitaciones
                ORDER BY numero
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Habitacion habitacion = new Habitacion();

                habitacion.setId(rs.getLong("id"));
                habitacion.setNumero(rs.getString("numero"));
                habitacion.setTipo(rs.getString("tipo"));
                habitacion.setPrecio(rs.getBigDecimal("precio"));

                lista.add(habitacion);

            }

        } catch (Exception e) {

            System.out.println("Error al listar habitaciones.");
            e.printStackTrace();

        }

        return lista;

    }

    // Verificar disponibilidad
    public boolean verificarDisponibilidad(long habitacionId,
                                           LocalDate checkIn,
                                           LocalDate checkOut) {

        String sql = """
                SELECT COUNT(*)
                FROM reservas
                WHERE habitacion_id = ?
                AND estado IN ('Pendiente','Confirmada')
                AND check_in < ?
                AND check_out > ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setLong(1, habitacionId);
            ps.setDate(2, java.sql.Date.valueOf(checkOut));
            ps.setDate(3, java.sql.Date.valueOf(checkIn));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getInt(1) == 0;

            }

        } catch (Exception e) {

            System.out.println("Error al verificar disponibilidad.");
            e.printStackTrace();

        }

        return false;

    }

}