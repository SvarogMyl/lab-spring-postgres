package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Map;

@RestController
public class LoginController {

    private final RestClient restClient = RestClient.create();

    @Value("${auth.service.url:http://lab-auth-service:8082}")
    private String authServiceUrl;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            // El frontend envía "username"; auth-service espera "email"
            Map<String, String> authRequest = new java.util.HashMap<>(credentials);
            if (!authRequest.containsKey("email") && authRequest.containsKey("username")) {
                authRequest.put("email", authRequest.remove("username"));
            }
            Map<?, ?> response = restClient.post()
                    .uri(authServiceUrl + "/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(authRequest)
                    .retrieve()
                    .body(Map.class);
            return ResponseEntity.ok(response);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", "Credenciales inválidas"));
        }
    }
}
