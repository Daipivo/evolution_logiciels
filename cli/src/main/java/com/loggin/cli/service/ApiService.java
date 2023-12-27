package com.loggin.cli.service;

import com.loggin.cli.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {
    private final String BASE_URL = "http://localhost:8080/api/users"; // Remplacez avec l'URL de votre API
    private final RestTemplate restTemplate;

    @Autowired
    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public User createUser(User user) {
        ResponseEntity<User> response = restTemplate.postForEntity(BASE_URL, user, User.class);
        return response.getBody();
    }
    public User findUserByEmailAndPassword(User user) {
        try {
            ResponseEntity<User> response = restTemplate.postForEntity(BASE_URL + "/find", user, User.class);
            return response.getBody();
        } catch (ResourceAccessException e) {
            System.err.println("Erreur de connexion au serveur. Assurez-vous que le serveur est en cours d'exécution.");
            return null;
        } catch (Exception e) {
            System.err.println("Une erreur est survenue lors de la communication avec le serveur : " + e.getMessage());
            return null;
        }
    }
}
