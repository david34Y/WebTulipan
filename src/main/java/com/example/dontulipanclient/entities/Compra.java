package com.example.dontulipanclient.entities;

import javax.persistence.*;

@Entity
@Table(name = "compra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCarrito;

    private byte estado;

    private int numPlantas;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Plantas plantas;

    // Constructor vacío
    public Compra() {}

    // Constructor con todos los campos
    public Compra(byte estado, int numPlantas, Usuario usuario, Plantas plantas) {
        this.estado = estado;
        this.numPlantas = numPlantas;
        this.usuario = usuario;
        this.plantas = plantas;
    }

    // Getters y Setters
    public int getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public byte getEstado() {
        return estado;
    }

    public void setEstado(byte estado) {
        this.estado = estado;
    }

    public int getNumPlantas() {
        return numPlantas;
    }

    public void setNumPlantas(int numPlantas) {
        this.numPlantas = numPlantas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Plantas getPlantas() {
        return plantas;
    }

    public void setPlantas(Plantas plantas) {
        this.plantas = plantas;
    }

    // Método toString para imprimir el objeto
    @Override
    public String toString() {
        return "Compra [idCarrito=" + idCarrito + ", estado=" + estado + ", numPlantas=" + numPlantas + ", usuario=" + usuario + ", plantas=" + plantas + "]";
    }

}
