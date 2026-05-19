package com.dam2.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Convierte las RuntimeException de los Services en respuestas HTTP
 * con código y cuerpo JSON legibles.
 *
 * Sin esto, Spring Boot convierte las excepciones en 403/500 con
 * cuerpo vacío, lo que hace imposible diagnosticar el error desde Android.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        String mensaje = ex.getMessage() != null ? ex.getMessage() : "Error interno del servidor";

        if (mensaje.contains("ya está registrado")) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)               // 409
                    .body(Map.of("error", mensaje));
        }
        if (mensaje.contains("no encontrado") || mensaje.contains("no encontrada")) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)              // 404
                    .body(Map.of("error", mensaje));
        }
        if (mensaje.contains("Contraseña incorrecta")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)           // 401
                    .body(Map.of("error", mensaje));
        }
        if (mensaje.contains("Rol no válido")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)            // 400
                    .body(Map.of("error", mensaje));
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)      // 500
                .body(Map.of("error", mensaje));
    }
}