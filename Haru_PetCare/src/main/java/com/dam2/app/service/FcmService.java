package com.dam2.app.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Puente entre Spring Boot y Firebase Cloud Messaging.
 *
 * Versión corregida: usa RestTemplate en vez de WebClient.
 * WebClient pertenece a Spring WebFlux (programación reactiva),
 * que no está en el pom.xml del proyecto.
 * RestTemplate es el cliente HTTP clásico de Spring Web,
 * que sí tenemos disponible sin dependencias extra.
 *
 * Para el TFG es más que suficiente — la diferencia es que
 * WebClient es no bloqueante y RestTemplate es bloqueante,
 * pero para enviar notificaciones push ocasionales no hay
 * ninguna diferencia práctica.
 */
@Service
public class FcmService {

    private static final String FCM_URL =
            "https://fcm.googleapis.com/fcm/send";

    // RestTemplate: cliente HTTP síncrono incluido en Spring Web
    // No necesita dependencias extra ni configuración adicional
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Envía una notificación push a un dispositivo Android concreto.
     *
     * @param tokenFcm Token del dispositivo destino
     * @param titulo   Título de la notificación
     * @param cuerpo   Texto de la notificación
     */
    public void enviarNotificacion(String tokenFcm, String titulo, String cuerpo) {
        // Cabeceras HTTP: tipo de contenido y clave del servidor Firebase
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Sustituye YOUR_SERVER_KEY por la Server Key de tu proyecto Firebase
        // Firebase Console → Configuración del proyecto → Cloud Messaging → Server Key
        headers.set("Authorization", "key=YOUR_SERVER_KEY");

        // Cuerpo de la petición — estructura que espera la API Legacy de FCM
        Map<String, Object> notification = new HashMap<>();
        notification.put("title", titulo);
        notification.put("body",  cuerpo);

        Map<String, Object> payload = new HashMap<>();
        payload.put("to",           tokenFcm);
        payload.put("notification", notification);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            // postForObject hace el POST y devuelve la respuesta como String
            // Si falla (token inválido, sin conexión...) lanzará una excepción
            // que capturamos para no interrumpir el flujo de la alerta
            restTemplate.postForObject(FCM_URL, request, String.class);
        } catch (Exception e) {
            // Logueamos el error pero NO lo propagamos — si FCM falla,
            // la alerta se guarda igualmente en la base de datos.
            // El fallo de notificación no debe hacer fallar la petición HTTP
            // que hizo el dueño desde el botón de emergencia.
            System.err.println("Error enviando notificación FCM: " + e.getMessage());
        }
    }
}