/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static Conexion instancia;
    private Connection conexion;

    private Conexion() {

        try {

            Dotenv dotenv = Dotenv.load();

            String host = dotenv.get("DB_HOST");
            String puerto = dotenv.get("DB_PORT");
            String bd = dotenv.get("DB_NAME");
            String usuario = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");

            String url = "jdbc:postgresql://" + host + ":" + puerto + "/" + bd;

            conexion = DriverManager.getConnection(url, usuario, password);

            System.out.println("Conexión exitosa con Supabase.");

        } catch (SQLException e) {

            System.out.println("Error al conectar.");
            e.printStackTrace();

        }

    }

    public static Conexion getInstancia() {

        if (instancia == null) {
            instancia = new Conexion();
        }

        return instancia;

    }

    public Connection getConexion() {
        return conexion;
    }

}