package com.example.TPLogging.repositories;

import com.example.TPLogging.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IUserRepository extends MongoRepository<User,String> {
    Optional<User> findByEmailAndPassword(String email, String password);
}
