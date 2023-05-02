package com.example.dontulipanclient.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Product {
    private String id;
    private String foto;
    private String nombre;
    private Float precio;
    private Boolean stock;
}
