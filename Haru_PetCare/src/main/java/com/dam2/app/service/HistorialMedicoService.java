// service/HistorialMedicoService.java
package com.dam2.app.service;

import com.dam2.app.dto.HistorialInsertarDTO;
import com.dam2.app.dto.HistorialMedicoDTO;
import com.dam2.app.model.HistorialMedico;
import com.dam2.app.model.Mascota;
import com.dam2.app.repo.HistorialMedicoRepository;
import com.dam2.app.repo.MascotaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Gestiona el historial clínico de las mascotas.
 * También es usado por el PdfService para generar el PDF exportable.
 */
@Service
public class HistorialMedicoService {

    private final HistorialMedicoRepository historialRepo;
    private final MascotaRepository         mascotaRepo;

    public HistorialMedicoService(HistorialMedicoRepository historialRepo,
                                  MascotaRepository mascotaRepo) {
        this.historialRepo = historialRepo;
        this.mascotaRepo   = mascotaRepo;
    }

    @Transactional(readOnly = true)
    public List<HistorialMedicoDTO> obtenerHistorialDeMascota(Long idMascota) {
        return historialRepo
                .findByMascota_IdOrderByFechaRegistroDesc(idMascota)
                .stream()
                .map(h -> new HistorialMedicoDTO(
                        h.getId(),
                        h.getTipoRegistro(),
                        h.getDescripcion(),
                        h.getFechaRegistro(),
                        h.getMascota().getId()
                ))
                .toList();
    }

    @Transactional
    public HistorialMedicoDTO guardar(HistorialInsertarDTO dto) {
        Mascota mascota = mascotaRepo.findById(dto.idMascota())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        HistorialMedico registro = new HistorialMedico();
        registro.setTipoRegistro(dto.tipoRegistro());
        registro.setDescripcion(dto.descripcion());
        registro.setFechaRegistro(LocalDateTime.now());
        registro.setMascota(mascota);

        HistorialMedico guardado = historialRepo.save(registro);

        return new HistorialMedicoDTO(
                guardado.getId(),
                guardado.getTipoRegistro(),
                guardado.getDescripcion(),
                guardado.getFechaRegistro(),
                mascota.getId()
        );
    }
}