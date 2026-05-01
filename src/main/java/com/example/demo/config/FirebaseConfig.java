package com.example.demo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${FIREBASE_CONFIG_PATH:}")
    private String configPath;

    @PostConstruct
    public void initialize() {
        try {
            if (configPath == null || configPath.isEmpty()) {
                System.out.println("⚠️ FIREBASE_CONFIG_PATH no configurada. Saltando inicialización de Firebase.");
                return;
            }

            FileInputStream serviceAccount = new FileInputStream(configPath);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("🔥 Firebase ha sido inicializado correctamente.");
            }
        } catch (IOException e) {
            System.err.println("❌ Error al inicializar Firebase: " + e.getMessage());
        }
    }
}
