package dao;

import model.Reserva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.time.LocalDate;

public class ReservaDAO {

    private Connection conexion;

    public ReservaDAO() {
        conexion = Conexion.getInstancia().getConexion();
    }


    // Registrar reserva
    public boolean registrarReserva(Reserva reserva) {

        String sql = """
                INSERT INTO reservas
                (cliente_id, habitacion_id, check_in, check_out, estado)
                VALUES (?, ?, ?, ?, ?)
                """;


        try (PreparedStatement ps = conexion.prepareStatement(sql)) {


            ps.setLong(1, reserva.getClienteId());
            ps.setLong(2, reserva.getHabitacionId());
            ps.setDate(3, java.sql.Date.valueOf(reserva.getCheckIn()));
            ps.setDate(4, java.sql.Date.valueOf(reserva.getCheckOut()));
            ps.setString(5, "Pendiente");


            return ps.executeUpdate() > 0;


        } catch (Exception e) {

            System.out.println("Error al registrar reserva.");
            e.printStackTrace();

        }

        return false;
    }



    // Listar reservas para la JTable
    public ArrayList<Reserva> listarReservas() {

        ArrayList<Reserva> lista = new ArrayList<>();


        String sql = """
                SELECT 
                    r.id,
                    r.cliente_id,
                    r.habitacion_id,
                    r.fecha_registro,
                    r.check_in,
                    r.check_out,
                    r.estado,
                    c.nombres || ' ' || c.apellidos AS cliente,
                    h.numero AS habitacion

                FROM reservas r

                INNER JOIN clientes c
                ON r.cliente_id = c.id

                INNER JOIN habitaciones h
                ON r.habitacion_id = h.id

                ORDER BY r.id DESC
                """;


        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {


            while (rs.next()) {


                Reserva reserva = new Reserva();


                reserva.setId(rs.getLong("id"));
                reserva.setClienteId(rs.getLong("cliente_id"));
                reserva.setHabitacionId(rs.getLong("habitacion_id"));

                if(rs.getTimestamp("fecha_registro") != null){
                    reserva.setFechaRegistro(
                        rs.getTimestamp("fecha_registro").toLocalDateTime()
                    );
                }


                reserva.setCheckIn(
                    rs.getDate("check_in").toLocalDate()
                );


                reserva.setCheckOut(
                    rs.getDate("check_out").toLocalDate()
                );


                reserva.setEstado(
                    rs.getString("estado")
                );


                // Datos para mostrar en JTable
                reserva.setNombreCliente(
                    rs.getString("cliente")
                );


                reserva.setNumeroHabitacion(
                    rs.getString("habitacion")
                );


                lista.add(reserva);

            }


        } catch(Exception e){

            System.out.println("Error al listar reservas.");
            e.printStackTrace();

        }


        return lista;

    }



    // Confirmar reserva
    public boolean confirmarReserva(long idReserva){

        return cambiarEstado(idReserva, "Confirmada");

    }



    // Cancelar reserva
    public boolean cancelarReserva(long idReserva){

        return cambiarEstado(idReserva, "Cancelada");

    }



    // Finalizar reserva
    public boolean finalizarReserva(long idReserva){

        return cambiarEstado(idReserva, "Finalizada");

    }



    // Método privado para actualizar estado
    private boolean cambiarEstado(long idReserva, String estado){


        String sql = """
                UPDATE reservas
                SET estado = ?
                WHERE id = ?
                """;


        try(PreparedStatement ps = conexion.prepareStatement(sql)){


            ps.setString(1, estado);
            ps.setLong(2, idReserva);


            return ps.executeUpdate() > 0;


        }catch(Exception e){

            System.out.println("Error al cambiar estado.");
            e.printStackTrace();

        }


        return false;

    }
    
    // Obtener todos los datos necesarios para la boleta y el correo
public Reserva obtenerReservaCompleta(long idReserva) {

    String sql = """
            SELECT
                r.id,
                r.cliente_id,
                r.habitacion_id,
                r.fecha_registro,
                r.check_in,
                r.check_out,
                r.estado,

                c.nombres || ' ' || c.apellidos AS cliente,
                c.correo AS correo_cliente,

                h.numero AS habitacion,
                h.tipo AS tipo_habitacion,
                h.precio AS precio_por_noche

            FROM reservas r

            INNER JOIN clientes c
                ON r.cliente_id = c.id

            INNER JOIN habitaciones h
                ON r.habitacion_id = h.id

            WHERE r.id = ?
            """;

    try (PreparedStatement ps = conexion.prepareStatement(sql)) {

        ps.setLong(1, idReserva);

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                Reserva reserva = new Reserva();

                reserva.setId(rs.getLong("id"));
                reserva.setClienteId(rs.getLong("cliente_id"));
                reserva.setHabitacionId(rs.getLong("habitacion_id"));

                if (rs.getTimestamp("fecha_registro") != null) {
                    reserva.setFechaRegistro(
                            rs.getTimestamp("fecha_registro")
                                    .toLocalDateTime()
                    );
                }

                if (rs.getDate("check_in") != null) {
                    reserva.setCheckIn(
                            rs.getDate("check_in").toLocalDate()
                    );
                }

                if (rs.getDate("check_out") != null) {
                    reserva.setCheckOut(
                            rs.getDate("check_out").toLocalDate()
                    );
                }

                reserva.setEstado(rs.getString("estado"));
                reserva.setNombreCliente(rs.getString("cliente"));
                reserva.setCorreoCliente(rs.getString("correo_cliente"));
                reserva.setNumeroHabitacion(rs.getString("habitacion"));
                reserva.setTipoHabitacion(rs.getString("tipo_habitacion"));
                reserva.setPrecioPorNoche(rs.getDouble("precio_por_noche"));

                return reserva;
            }

        }

    } catch (Exception e) {

        System.out.println(
                "Error al obtener los datos completos de la reserva."
        );

        e.printStackTrace();
    }

    return null;
}

}