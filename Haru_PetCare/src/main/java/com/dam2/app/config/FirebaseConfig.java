package com.dam2.app.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        if (FirebaseApp.getApps().isEmpty()) {
            String credentialsJson = System.getenv("FIREBASE_CREDENTIALS_JSON");
            if (credentialsJson == null || credentialsJson.isBlank()) {
                throw new IllegalStateException(
                    "La variable de entorno FIREBASE_CREDENTIALS_JSON no está configurada");
            }
            InputStream serviceAccount =
                new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8));
            try {
                FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
                FirebaseApp.initializeApp(options);
            } catch (Exception e) {
                throw new IllegalStateException("Error al inicializar Firebase: " + e.getMessage(), e);
            }
        }
    }
}