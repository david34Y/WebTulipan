package com.example.dontulipanclient.dao;

import com.example.dontulipanclient.dto.ProductDto;
import com.example.dontulipanclient.entity.Product;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class ProductDao {
    public List<Product> listarProductos() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Product[]> response = restTemplate.getForEntity(
                "http://localhost:8080/product/list", Product[].class);

        return Arrays.asList(response.getBody());
    }


    public void guardarProducto(Product product) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = "http://localhost:8080/product/save/";
        HttpEntity<Product> httpEntity = new HttpEntity<>(product, headers);
        String uri;
        RestTemplate restTemplate = new RestTemplate();
        if (product.getId() != null) {
            uri = url + product.getId();
        } else {
            uri = url +"null";
        }

        try {
            ResponseEntity<Product> responseEntity = restTemplate.postForEntity(uri, httpEntity, Product.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Product responseProduct = responseEntity.getBody();
                System.out.println("Response product: " + responseProduct);
            } else {
                System.out.println("Error occurred while processing the request: " + responseEntity.getStatusCodeValue());
            }
        } catch (RestClientException e) {
            System.out.println("Error occurred while processing the request: " + e.getMessage());
        }


    }

    public Product obtenerProductoPorId(String id) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/product/find?id=" + id;

        ResponseEntity<Product> responseMap = restTemplate.getForEntity(url, Product.class);

        return responseMap.getBody();
    }


    public void borrarProducto(String id) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete("http://localhost:8080/product/delete/" + id);
    }





}
