package com.example.dontulipanclient.dto;

import com.example.dontulipanclient.entity.Product;
import lombok.Data;


@Data
public class ProductDto  {
    private String id;
    private Product product;
}
