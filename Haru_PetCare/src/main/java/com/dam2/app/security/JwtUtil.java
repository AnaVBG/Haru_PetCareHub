package com.dam2.app.security;

import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * Utilidad para generar y validar tokens JWT.
 *
 * ¿Por qué JWT y no sesiones?
 * La app Android no tiene cookies. Cada petición HTTP desde el móvil
 * incluirá el token en el header "Authorization: Bearer <token>".
 * El servidor lo valida sin consultar la base de datos en cada petición.
 */
@Component
public class JwtUtil {
	
	// Clave secreta de al menos 256 bits (requerida por HS256)
    // En producción: leer de variable de entorno, nunca hardcodeada
    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();

    private static final long EXPIRATION_MS = 1000 * 60 * 60 * 24; // 24 horas

    /**
     * Genera un token JWT para un usuario autenticado.
     * El "subject" es el email — identificador único del usuario.
     */
    public String generarToken(String email, String rol) {
        return Jwts.builder()
                .subject(email)
                .claim("rol", rol)             // rol antes de la autenticación
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(secretKey)            // Ahora detecta el algoritmo automáticamente de la llave
                .compact();
    }

    public String extraerEmail(String token) {
        return parsearClaims(token).getSubject();
    }

    public String extraerRol(String token) {
        return (String) parsearClaims(token).get("rol");
    }

    public boolean esValido(String token) {
        try {
            parsearClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parsearClaims(String token) {
        return Jwts.parser()                 // 1. Antes parserBuilder()
                .verifyWith(secretKey)       // 2. Antes setSigningKey()
                .build()
                .parseSignedClaims(token)    // 3. Antes parseClaimsJws()
                .getPayload();               // 4. Antes getBody()
    }
	
}
