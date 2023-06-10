package com.example.dontulipanclient.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Product {
    private String id;
    private String foto;
    private String name;
    private Float price;
    private Integer stock;
    private String recommendedEnvironment;
    private String profilePhoto;
    private String[] plantPhotos;
}
