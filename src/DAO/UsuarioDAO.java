/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {

    private Connection conexion;

    public UsuarioDAO() {
        conexion = Conexion.getInstancia().getConexion();
    }


    public Usuario validarLogin(String usuario, String contrasena) {

        Usuario usuarioEncontrado = null;

        String sql = """
                SELECT id, usuario, contrasena
                FROM usuarios
                WHERE usuario = ?
                AND contrasena = ?
                """;


        try (PreparedStatement ps = conexion.prepareStatement(sql)) {


            ps.setString(1, usuario);
            ps.setString(2, contrasena);


            ResultSet rs = ps.executeQuery();


            if (rs.next()) {

                usuarioEncontrado = new Usuario();

                usuarioEncontrado.setId(rs.getLong("id"));
                usuarioEncontrado.setUsuario(rs.getString("usuario"));
                usuarioEncontrado.setContrasena(rs.getString("contrasena"));

            }


        } catch (Exception e) {

            System.out.println("Error al validar usuario.");
            e.printStackTrace();

        }


        return usuarioEncontrado;
    }

}