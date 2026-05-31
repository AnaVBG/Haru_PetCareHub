package com.dam2.app.service;

import com.dam2.app.dto.CitaDTO;
import com.dam2.app.dto.CitaInsertarDTO;
import com.dam2.app.model.*;
import com.dam2.app.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CitaService {

    private final CitaRepository    citaRepo;
    private final MascotaRepository mascotaRepo;
    private final UsuarioRepository usuarioRepo;

    public CitaService(CitaRepository citaRepo,
                       MascotaRepository mascotaRepo,
                       UsuarioRepository usuarioRepo) {
        this.citaRepo    = citaRepo;
        this.mascotaRepo = mascotaRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerAgendaVeterinario(Long idVeterinario) {
        return citaRepo.findByVeterinario_IdAndEstado(idVeterinario, EstadoCita.PENDIENTE)
                .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerCitasDeDueno(Long idDueno) {
        return citaRepo.findByDueno_Id(idDueno)
                .stream().map(this::toDTO).toList();
    }

    /**
     * Todas las citas (cualquier estado) de todos los veterinarios
     * que pertenecen a la clínica indicada.
     */
    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerCitasDeClinica(Long idClinica) {
        return citaRepo.findByVeterinario_Clinica_Id(idClinica)
                .stream().map(this::toDTO).toList();
    }

    @Transactional
    public CitaDTO crearCita(CitaInsertarDTO dto) {
        Mascota mascota = mascotaRepo.findById(dto.idMascota())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        Usuario veterinario = usuarioRepo.findById(dto.idVeterinario())
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));
        Usuario dueno = usuarioRepo.findById(dto.idDueno())
                .orElseThrow(() -> new RuntimeException("Dueño no encontrado"));

        Cita cita = new Cita();
        cita.setFechaCita(dto.fechaCita());
        cita.setMotivo(dto.motivo());
        cita.setEstado(EstadoCita.PENDIENTE);
        cita.setMascota(mascota);
        cita.setVeterinario(veterinario);
        cita.setDueno(dueno);

        return toDTO(citaRepo.save(cita));
    }

    @Transactional
    public CitaDTO cambiarEstado(Long idCita, String nuevoEstado) {
        Cita cita = citaRepo.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        cita.setEstado(EstadoCita.valueOf(nuevoEstado));
        return toDTO(citaRepo.save(cita));
    }

    private CitaDTO toDTO(Cita c) {
        return new CitaDTO(
                c.getId(),
                c.getFechaCita(),
                c.getMotivo(),
                c.getEstado().name(),
                c.getMascota().getId(),
                c.getMascota().getNombre(),
                c.getVeterinario().getNombre(),
                c.getDueno().getId()
        );
    }
}