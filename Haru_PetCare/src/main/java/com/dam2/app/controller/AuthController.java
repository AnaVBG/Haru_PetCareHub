package com.dam2.app.controller;

import com.dam2.app.dto.LoginRequestDTO;
import com.dam2.app.dto.LoginResponseDTO;
import com.dam2.app.dto.UsuarioRegistroDTO;
import com.dam2.app.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints públicos — no requieren JWT.
 * Son los únicos que SecurityConfig.permitAll().
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    public ResponseEntity<LoginResponseDTO> registrar(@RequestBody UsuarioRegistroDTO dto) {
        return ResponseEntity.ok(authService.registrar(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    /** Android llama a esto al iniciar para registrar su token FCM */
    @PutMapping("/fcm/{idUsuario}")
    public ResponseEntity<Void> actualizarFcm(@PathVariable Long idUsuario,
                                               @RequestBody String tokenFcm) {
        authService.actualizarTokenFcm(idUsuario, tokenFcm);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/logout/{idUsuario}")
    public ResponseEntity<Void> cerrarSesion(@PathVariable Long idUsuario) {
        authService.cerrarSesion(idUsuario);
        return ResponseEntity.ok().build();
    }
}