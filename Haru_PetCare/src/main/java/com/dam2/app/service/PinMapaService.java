// service/PinMapaService.java
package com.dam2.app.service;

import com.dam2.app.dto.PinInsertarDTO;
import com.dam2.app.dto.PinMapaDTO;
import com.dam2.app.model.PinMapa;
import com.dam2.app.model.TipoPin;
import com.dam2.app.model.Usuario;
import com.dam2.app.repo.PinMapaRepository;
import com.dam2.app.repo.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Gestiona los pines colaborativos del mapa comunitario.
 *
 * Operaciones:
 * - Obtener todos los pines activos (GET /api/pines)
 * - Crear un pin en la ubicación del usuario (POST /api/pines)
 * - Borrar un pin propio (DELETE /api/pines/{id})
 */
@Service
public class PinMapaService {

    private final PinMapaRepository pinRepo;
    private final UsuarioRepository usuarioRepo;

    public PinMapaService(PinMapaRepository pinRepo, UsuarioRepository usuarioRepo) {
        this.pinRepo     = pinRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Transactional(readOnly = true)
    public List<PinMapaDTO> obtenerTodos() {
        return pinRepo.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PinMapaDTO> obtenerPorTipo(TipoPin tipo) {
        return pinRepo.findByTipo(tipo)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public PinMapaDTO crear(PinInsertarDTO dto) {
        Usuario usuario = usuarioRepo.findById(dto.idUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        PinMapa pin = new PinMapa();
        pin.setTipo(TipoPin.valueOf(dto.tipo()));
        pin.setLatitud(dto.latitud());
        pin.setLongitud(dto.longitud());
        pin.setDescripcion(dto.descripcion());
        pin.setFechaCreacion(LocalDateTime.now());
        pin.setUsuario(usuario);

        PinMapa guardado = pinRepo.save(pin);
        return toDTO(guardado);
    }

    @Transactional
    public void borrar(Long idPin) {
        if (!pinRepo.existsById(idPin)) {
            throw new RuntimeException("Pin no encontrado");
        }
        pinRepo.deleteById(idPin);
    }

    // ── Mapeo privado entidad → DTO ───────────────────────────────────────
    private PinMapaDTO toDTO(PinMapa p) {
        return new PinMapaDTO(
                p.getId(),
                p.getTipo().name(),
                p.getLatitud(),
                p.getLongitud(),
                p.getDescripcion(),
                p.getFechaCreacion(),
                p.getUsuario().getId(),
                p.getUsuario().getNombre()
        );
    }
}