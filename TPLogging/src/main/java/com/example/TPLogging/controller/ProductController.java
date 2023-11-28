package com.example.TPLogging.controller;

import com.example.TPLogging.model.Product;
import com.example.TPLogging.repositories.IProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import  org.slf4j.Logger;
import  org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    Logger logger=LoggerFactory.getLogger(ProductController.class);
    private final IProductRepository productRepository;

    public ProductController(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        logger.info("READ");
        List<Product> productList = productRepository.findAll();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable String productId) {
        Optional<Product> product = productRepository.findById(productId);

        return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product createdProduct = productRepository.save(product);
        logger.info("Product added with ID: {}", createdProduct.getId());
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product with ID " + productId + " not found.");
        }

        productRepository.deleteById(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable String productId, @RequestBody Product updatedProduct) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product with ID " + productId + " not found.");
        }

        updatedProduct.setId(productId);
        Product savedProduct = productRepository.save(updatedProduct);
        return new ResponseEntity<>(savedProduct, HttpStatus.OK);
    }
}
