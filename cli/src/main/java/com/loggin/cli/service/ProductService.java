package com.loggin.cli.service;

import com.loggin.cli.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Product getProductById(String productId) {
        return restTemplate.getForObject(PRODUCT_BASE_URL + "/" + productId, Product.class);
    }

    public Product addProduct(Product product) {
        return restTemplate.postForObject(PRODUCT_BASE_URL, product, Product.class);
    }

    public void deleteProduct(String productId) {
        restTemplate.delete(PRODUCT_BASE_URL + "/" + productId);
    }

    public Product updateProduct(String productId, Product product) {
        restTemplate.put(PRODUCT_BASE_URL + "/" + productId, product);
        return product;
    }
    public List<Product> getAllProducts() {
        ResponseEntity<List<Product>> response = restTemplate.exchange(
                PRODUCT_BASE_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {}
        );
        return response.getBody();
    }
}

