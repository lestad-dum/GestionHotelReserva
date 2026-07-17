/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;

public class Habitacion {

    private long id;
    private String numero;
    private String tipo;
    private BigDecimal precio;

    public Habitacion() {
    }

    public Habitacion(long id, String numero, String tipo, BigDecimal precio) {
        this.id = id;
        this.numero = numero;
        this.tipo = tipo;
        this.precio = precio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    @Override
      public String toString() {
       return numero + " - " + tipo;
      }

}