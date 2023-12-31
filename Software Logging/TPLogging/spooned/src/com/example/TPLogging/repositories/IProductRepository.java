package com.example.TPLogging.repositories;
import com.example.TPLogging.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface IProductRepository extends MongoRepository<Product, String> {}