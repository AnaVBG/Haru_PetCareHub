package com.dam2.app.service;

import com.dam2.app.dto.AlertaInsertarDTO;
import com.dam2.app.dto.AlertaPerdidaDTO;
import com.dam2.app.model.*;
import com.dam2.app.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertaPerdidaService {

    private final AlertaPerdidaRepository alertaRepo;
    private final MascotaRepository mascotaRepo;
    private final UsuarioRepository usuarioRepo;
    private final FcmService fcmService;

    public AlertaPerdidaService(AlertaPerdidaRepository alertaRepo, MascotaRepository mascotaRepo,
            UsuarioRepository usuarioRepo, FcmService fcmService) {
        this.alertaRepo = alertaRepo;
        this.mascotaRepo = mascotaRepo;
        this.usuarioRepo = usuarioRepo;
        this.fcmService = fcmService;
    }

    @Transactional(readOnly = true)
    public List<AlertaPerdidaDTO> obtenerAlertasActivas() {
        return alertaRepo.findByActivaTrue().stream().map(this::toDTO).toList();
    }

    @Transactional
    public AlertaPerdidaDTO crearAlerta(AlertaInsertarDTO dto) {
        Mascota mascota = mascotaRepo.findById(dto.idMascota())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        Usuario usuario = usuarioRepo.findById(dto.idUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        AlertaPerdida alerta = new AlertaPerdida();
        alerta.setUltimaUbicacionLat(dto.ultimaUbicacionLat());
        alerta.setUltimaUbicacionLng(dto.ultimaUbicacionLng());
        alerta.setMensajeAdicional(dto.mensajeAdicional());
        alerta.setActiva(true);
        alerta.setFechaAlerta(LocalDateTime.now());
        alerta.setMascota(mascota);
        alerta.setUsuario(usuario);

        alertaRepo.save(alerta);

        notificarVecinosCercanos(alerta, mascota, usuario);

        return toDTO(alerta);
    }

    @Transactional
    public void resolverAlerta(Long idAlerta) {
        AlertaPerdida alerta = alertaRepo.findById(idAlerta)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada"));
        alerta.setActiva(false);
    }

    private double calcularDistanciaKm(double lat1, double lng1, double lat2, double lng2) {
        final double RADIO_TIERRA_KM = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIO_TIERRA_KM * c;
    }

    private void notificarVecinosCercanos(AlertaPerdida alerta, Mascota mascota, Usuario dueno) {
        List<Usuario> candidatos = usuarioRepo.findByTokenFcmIsNotNull();

        String titulo = "Mascota perdida cerca de ti";
        String mensaje = mascota.getNombre() + " (" + mascota.getEspecie() + ") se ha perdido. Contacta con "
                + dueno.getNombre() + " — " + dueno.getTelefono();

        candidatos.stream()
                .filter(u -> !u.getId().equals(dueno.getId()))
                .filter(u -> {
                    if (u.getUltimaLat() == null || u.getUltimaLng() == null) return false;
                    double distancia = calcularDistanciaKm(
                        alerta.getUltimaUbicacionLat(), alerta.getUltimaUbicacionLng(),
                        u.getUltimaLat(), u.getUltimaLng()
                    );
                    return distancia <= 5.0;
                })
                .forEach(u -> fcmService.enviarNotificacion(u.getTokenFcm(), titulo, mensaje));
    }

    private AlertaPerdidaDTO toDTO(AlertaPerdida a) {
        return new AlertaPerdidaDTO(a.getId(), a.getUltimaUbicacionLat(), a.getUltimaUbicacionLng(),
                a.getMensajeAdicional(), a.getActiva(), a.getFechaAlerta(), a.getMascota().getNombre(),
                a.getMascota().getFotoUrl(), a.getUsuario().getNombre(), a.getUsuario().getTelefono(),
                a.getUsuario().getId());
    }
}