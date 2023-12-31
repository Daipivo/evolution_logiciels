package com.example.TPLogging.controller;
import com.example.TPLogging.model.Product;
import com.example.TPLogging.repositories.IProductRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/products")
public class ProductController {
    Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final IProductRepository productRepository;

    public ProductController(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(@RequestHeader("UserId")
                                                        String userId) {
        logger.info("{\"timestamp\": \"{}\", \"action\": \"read\", \"event\": \"getAll\", \"userId\": \"{}\"}", new java.util.Date(), userId);;
        List<Product> productList = productRepository.findAll();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable
                                                  String productId, @RequestHeader("UserId")
                                                  String userId) {
        logger.info("{\"timestamp\": \"{}\", \"action\": \"read\", \"event\": \"getById\", \"userId\": \"{}\", \"Id\": \"" + productId + "\"}", new java.util.Date(), userId);;
        Optional<Product> product = productRepository.findById(productId);
        return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody
                                              Product product, @RequestHeader("UserId")
                                              String userId) {
        logger.info("{\"timestamp\": \"{}\", \"action\": \"write\", \"event\": \"add\", \"userId\": \"{}\"}", new java.util.Date(), userId);;
        Product createdProduct = productRepository.save(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable
                                              String productId, @RequestHeader("UserId")
                                              String userId) {
        logger.info("{\"timestamp\": \"{}\", \"action\": \"write\", \"event\": \"delete\", \"userId\": \"{}\", \"Id\": \"" + productId + "\"}", new java.util.Date(), userId);;
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException(("Product with ID " + productId) + " not found.");
        }
        productRepository.deleteById(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable
                                                 String productId, @RequestBody
                                                 Product updatedProduct, @RequestHeader("UserId")
                                                 String userId) {
        logger.info("{\"timestamp\": \"{}\", \"action\": \"write\", \"event\": \"update\", \"userId\": \"{}\", \"Id\": \"" + productId + "\"}", new java.util.Date(), userId);;
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException(("Product with ID " + productId) + " not found.");
        }
        updatedProduct.setId(productId);
        Product savedProduct = productRepository.save(updatedProduct);
        return new ResponseEntity<>(savedProduct, HttpStatus.OK);
    }
}