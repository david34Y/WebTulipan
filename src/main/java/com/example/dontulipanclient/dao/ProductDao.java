package com.example.dontulipanclient.dao;

import com.example.dontulipanclient.dto.ProductDto;
import com.example.dontulipanclient.entity.Product;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class ProductDao {

    private static final String API_URL = "http://127.0.0.1:5001/proyecto-4f73f/us-central1/api/plantas";
    public List<Product> listarProductos() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ProductDto> response = restTemplate.getForEntity(
                API_URL, ProductDto.class);

        return Arrays.asList(response.getBody().getData());
    }

    public void crearProducto(Product product, MultipartFile[] plantPhotos, MultipartFile profilePhoto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("name", product.getName());
        body.add("stock", product.getStock());
        body.add("price", product.getPrice());
        body.add("recommendedEnvironment", product.getRecommendedEnvironment());
        // Add other fields as needed

        List<Resource> plantPhotoResources = new ArrayList<>();

        for (MultipartFile photo : plantPhotos) {
            try {
                InputStream inputStream = photo.getInputStream();
                ByteArrayResource resource = new ByteArrayResource(inputStream.readAllBytes()) {
                    @Override
                    public String getFilename() {
                        return photo.getOriginalFilename();
                    }
                };
                plantPhotoResources.add((Resource) resource);
            } catch (IOException e) {
                System.out.println("Error occurred while reading plant photo: " + e.getMessage());
            }
        }

        if (profilePhoto != null) {
            try {
                InputStream inputStream = profilePhoto.getInputStream();
                ByteArrayResource resource = new ByteArrayResource(inputStream.readAllBytes()) {
                    @Override
                    public String getFilename() {
                        return profilePhoto.getOriginalFilename();
                    }
                };
                body.add("profilePhoto", resource);
            } catch (IOException e) {
                System.out.println("Error occurred while reading profile photo: " + e.getMessage());
            }
        }

        for (Resource resource : plantPhotoResources) {
            body.add("plantPhotos", resource);
        }

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Product> responseEntity = restTemplate.exchange(API_URL, HttpMethod.POST, httpEntity, Product.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Product createdPlant = responseEntity.getBody();
                System.out.println("Created plant: " + createdPlant);
            } else {
                System.out.println("Error occurred while processing the request: " + responseEntity.getStatusCodeValue());
            }
        } catch (RestClientException e) {
            System.out.println("Error occurred while processing the request: " + e.getMessage());
        }
    }

    public void actualizarPlanta(String plantaId, Product updatedPlanta) {
        try {
            String url = API_URL + "/" + plantaId;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Product> requestEntity = new HttpEntity<>(updatedPlanta, headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Product> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Product.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Product updatedPlant = responseEntity.getBody();
                System.out.println("Planta actualizada exitosamente: " + updatedPlant);
            } else {
                System.out.println("Error al actualizar la planta: " + responseEntity.getStatusCodeValue());
            }
        } catch (RestClientException e) {
            System.out.println("Error al actualizar la planta: " + e.getMessage());
        }
    }

    public void updatePlant(String id, Product updatedPlanta, MultipartFile[] plantPhotos, MultipartFile profilePhoto) {
        String url = API_URL+ "200/"+ id;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("stock", updatedPlanta.getStock());

        // Add plant photos
        if (plantPhotos != null && plantPhotos.length > 0) {
            for (MultipartFile plantPhoto : plantPhotos) {
                if (!plantPhoto.isEmpty()) {
                    try {
                        Path tempFile = Files.createTempFile("plantPhoto", null);
                        Files.copy(plantPhoto.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
                        body.add("plantPhotos", new FileSystemResource(tempFile.toFile()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Add profile photo
        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            try {
                Path tempFile = Files.createTempFile("profilePhoto", null);
                Files.copy(profilePhoto.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
                body.add("profilePhoto", new FileSystemResource(tempFile.toFile()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Plant updated successfully");
        } else {
            System.out.println("Failed to update plant");
        }
    }

    public Product obtenerProductoPorId(String id) {
        RestTemplate restTemplate = new RestTemplate();

        String url = API_URL +"/"+id;

        ResponseEntity<ProductDto> response = restTemplate.getForEntity(url, ProductDto.class);
        return Objects.requireNonNull(response.getBody()).getData()[0];
    }


    public void borrarProducto(String id) {
        RestTemplate restTemplate = new RestTemplate();

        String url = API_URL +"/"+id;

        restTemplate.delete(url);
    }





}
