package com.example.dontulipanclient.entities;

import javax.persistence.*;

@Entity
@Table(name = "plantas")
public class Plantas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private double precio;
    private String recomendacion;
    private int stock;

    // Constructor vacío
    public Plantas() {}

    // Constructor con todos los campos
    public Plantas(String nombre, double precio, String recomendacion, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.recomendacion = recomendacion;
        this.stock = stock;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String isRecomendacion() {
        return recomendacion;
    }

    public void setRecomendacion(String recomendacion) {
        this.recomendacion = recomendacion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // Método toString para imprimir el objeto
    @Override
    public String toString() {
        return "Planta [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", recomendacion=" + recomendacion + ", stock=" + stock + "]";
    }

}