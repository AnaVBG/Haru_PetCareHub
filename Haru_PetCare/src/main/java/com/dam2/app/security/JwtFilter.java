package com.dam2.app.security;

import java.io.IOException;
import java.util.List;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Filtro que intercepta CADA petición HTTP antes de llegar al controlador.
 *
 * Flujo:
 * Petición Android → JwtFilter → ¿Token válido? → Controlador
 *                                               → 401 Unauthorized
 *
 * "OncePerRequestFilter" garantiza que se ejecuta una sola vez por petición.
 */
@Component
public class JwtFilter extends OncePerRequestFilter{

	private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
	
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Si no hay token o no empieza por "Bearer ", dejamos pasar
        // (las rutas públicas como /api/auth/** no lo necesitan)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // Quitamos "Bearer "

        if (jwtUtil.esValido(token)) {
            String email = jwtUtil.extraerEmail(token);
            String rol   = jwtUtil.extraerRol(token);

            // Registramos la identidad del usuario en el contexto de seguridad
            // Spring Security la usará para @PreAuthorize en los controladores
            var auth = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + rol))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

}
