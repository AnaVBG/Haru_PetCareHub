package com.dam2.app.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FcmService {

    public void enviarNotificacion(String tokenFcm, String titulo, String cuerpo) {
        try {
            Message message = Message.builder()
                .setToken(tokenFcm)
                .setNotification(Notification.builder()
                    .setTitle(titulo)
                    .setBody(cuerpo)
                    .build())
                .build();
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            System.err.println("Error FCM: " + e.getMessage());
        }
    }
}