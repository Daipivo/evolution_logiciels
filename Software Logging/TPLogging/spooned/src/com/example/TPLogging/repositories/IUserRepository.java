package com.example.TPLogging.repositories;
import com.example.TPLogging.model.User;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface IUserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmailAndPassword(String email, String password);
}