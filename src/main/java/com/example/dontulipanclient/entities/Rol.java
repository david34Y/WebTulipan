package com.example.dontulipanclient.entities;

import javax.persistence.*;

@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRol;

    private String nombreRol;

    // Constructor vacío
    public Rol() {}

    // Constructor con todos los campos
    public Rol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    // Getters y Setters
    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    // Método toString para imprimir el objeto
    @Override
    public String toString() {
        return "Rol [idRol=" + idRol + ", nombreRol=" + nombreRol + "]";
    }

}