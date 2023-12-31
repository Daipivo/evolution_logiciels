package com.loggin.cli.service;

import com.loggin.cli.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.List;

@Service
public class ProductService {
    private final RestTemplate restTemplate;
    private final String PRODUCT_BASE_URL = "http://localhost:8080/api/products";

    @Autowired
    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpEntity<Object> createHttpEntityWithUserId(Object body, String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("UserId", userId); // Ajouter l'userId dans les en-têtes
        return new HttpEntity<>(body, headers);
    }

    public Product getProductById(String productId, String userId) {
        String url = PRODUCT_BASE_URL + "/" + productId;
        ResponseEntity<Product> response = restTemplate.exchange(
                url, HttpMethod.GET, createHttpEntityWithUserId(null, userId), Product.class);
        return response.getBody();
    }

    public Product addProduct(Product product, String userId) {
        HttpEntity<Object> entity = createHttpEntityWithUserId(product, userId);
        return restTemplate.postForObject(PRODUCT_BASE_URL, entity, Product.class);
    }

    public void deleteProduct(String productId, String userId) {
        String url = PRODUCT_BASE_URL + "/" + productId;
        restTemplate.exchange(url, HttpMethod.DELETE, createHttpEntityWithUserId(null, userId), Void.class);
    }

    public Product updateProduct(String productId, Product product, String userId) {
        HttpEntity<Object> entity = createHttpEntityWithUserId(product, userId);
        restTemplate.exchange(PRODUCT_BASE_URL + "/" + productId, HttpMethod.PUT, entity, Product.class);
        return product;
    }

    public List<Product> getAllProducts(String userId) {
        String url = PRODUCT_BASE_URL;
        ResponseEntity<List<Product>> response = restTemplate.exchange(
                url, HttpMethod.GET, createHttpEntityWithUserId(null, userId),
                new ParameterizedTypeReference<List<Product>>() {});
        return response.getBody();
    }
}

