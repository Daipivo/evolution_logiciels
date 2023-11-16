package com.example.TPLogging.controller;

import com.example.TPLogging.model.User;
import com.example.TPLogging.repositories.IUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/users")
@RestController
public class UserController {
    private final IUserRepository userRepository;

    public UserController(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = this.userRepository.save(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/test")
    public  ResponseEntity<String> testkely(){
        return new ResponseEntity<>("Hello world",HttpStatus.ACCEPTED);
    }
}
