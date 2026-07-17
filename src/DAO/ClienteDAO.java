/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClienteDAO {

    private Connection conexion;

    public ClienteDAO() {
        conexion = Conexion.getInstancia().getConexion();
    }

    // Validar si el DNI ya existe
public boolean existeDni(String dni) {

    String sql = """
            SELECT 1
            FROM clientes
            WHERE dni = ?
            """;

    try (PreparedStatement ps = conexion.prepareStatement(sql)) {

        ps.setString(1, dni);

        ResultSet rs = ps.executeQuery();

        return rs.next();

    } catch (Exception e) {

        System.out.println("Error al validar DNI.");
        e.printStackTrace();

    }

    return false;

}
    // Registrar cliente
    public boolean registrarCliente(Cliente cliente) {

        String sql = """
                INSERT INTO clientes
                (dni, nombres, apellidos, correo, telefono)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, cliente.getDni());
            ps.setString(2, cliente.getNombres());
            ps.setString(3, cliente.getApellidos());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getTelefono());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println("Error al registrar cliente.");
            e.printStackTrace();

        }

        return false;

    }

    // Buscar cliente por DNI
    public long buscarClientePorDni(String dni) {

        String sql = """
                SELECT id
                FROM clientes
                WHERE dni = ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, dni);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getLong("id");
            }

        } catch (Exception e) {

            System.out.println("Error al buscar cliente.");
            e.printStackTrace();

        }

        return -1;

    }

}