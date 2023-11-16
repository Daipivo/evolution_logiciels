package com.example.TPLogging.repositories;

import com.example.TPLogging.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IUserRepository extends MongoRepository<User,String> {
}
