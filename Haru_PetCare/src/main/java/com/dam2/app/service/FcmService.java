// service/FcmService.java
package com.dam2.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

/**
 * Puente entre Spring Boot y Firebase Cloud Messaging.
 *
 * Flujo completo de una notificación de emergencia:
 * Android (botón) → POST /api/alertas → AlertaPerdidaService
 *   → FcmService → Firebase → Dispositivos vecinos
 *
 * Usamos la API HTTP v1 de FCM (la Legacy API fue deprecada en 2024).
 * En producción: autenticar con Google Service Account (OAuth2).
 * Para el TFG: se puede usar la clave del servidor FCM directamente.
 */
@Service
public class FcmService {

    // En producción: inyectar desde application.properties
    private static final String FCM_URL =
        "https://fcm.googleapis.com/fcm/send";

    private final WebClient webClient;

    public FcmService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(FCM_URL).build();
    }

    /**
     * Envía una notificación push a un dispositivo Android concreto.
     *
     * @param tokenFcm Token del dispositivo destino (guardado en la tabla usuarios)
     * @param titulo   Título de la notificación (aparece en negrita)
     * @param cuerpo   Texto de la notificación
     */
    public void enviarNotificacion(String tokenFcm, String titulo, String cuerpo) {
        Map<String, Object> payload = Map.of(
            "to", tokenFcm,
            "notification", Map.of(
                "title", titulo,
                "body",  cuerpo
            )
        );

        // Petición asíncrona: no bloqueamos el hilo principal mientras Firebase responde
        webClient.post()
                .header("Authorization", "key=TU_SERVER_KEY_DE_FIREBASE")
                .header("Content-Type", "application/json")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(); // fire-and-forget
    }
}