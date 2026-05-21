package com.dam2.app.service;

import com.dam2.app.dto.*;
import com.dam2.app.model.*;
import com.dam2.app.repo.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MascotaService {

    private final MascotaRepository mascotaRepo;
    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder   passwordEncoder;

    public MascotaService(MascotaRepository mascotaRepo,
                          UsuarioRepository usuarioRepo,
                          PasswordEncoder passwordEncoder) {
        this.mascotaRepo     = mascotaRepo;
        this.usuarioRepo     = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<MascotaDTO> obtenerMascotasPorDueno(Long idDueno) {
        return mascotaRepo.findByDueno_Id(idDueno)
                .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<MascotaDTO> buscarTodas(Long idUsuario, String especie, String buscar) {
        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Mascota> resultado;

        if (RolUsuario.VETERINARIO.equals(usuario.getRol())
                && usuario.getClinica() != null) {
            resultado = mascotaRepo.buscarPorClinica(
                    usuario.getClinica().getId(), especie, buscar);
        } else if (RolUsuario.CLINICA.equals(usuario.getRol())) {
            resultado = mascotaRepo.buscarPorClinica(
                    usuario.getId(), especie, buscar);
        } else {
            resultado = mascotaRepo.buscarTodas(especie, buscar);
        }

        return resultado.stream().map(this::toDTO).toList();
    }

    @Transactional
    public List<MascotaDTO> vincularAClinica(VincularMascotaClinicaDTO dto) {
        Usuario clinica = usuarioRepo.findById(dto.idClinica())
                .orElseThrow(() -> new RuntimeException("Clínica no encontrada"));

        if (!RolUsuario.CLINICA.equals(clinica.getRol())) {
            throw new RuntimeException("El usuario indicado no es una clínica");
        }

        List<Mascota> mascotas;

        if (dto.idMascota() != null) {
            Mascota m = mascotaRepo.findById(dto.idMascota())
                    .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
            m.setClinica(clinica);
            mascotas = List.of(mascotaRepo.save(m));
        } else {
            mascotas = mascotaRepo.findByDueno_Id(dto.idDueno());
            if (mascotas.isEmpty()) {
                throw new RuntimeException("El dueño no tiene mascotas registradas");
            }
            mascotas.forEach(m -> m.setClinica(clinica));
            mascotas = mascotaRepo.saveAll(mascotas);
        }

        return mascotas.stream().map(this::toDTO).toList();
    }

    @Transactional
    public MascotaDTO crearDuenoConMascota(CrearDuenoConMascotaDTO dto) {
        if (usuarioRepo.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario clinica = usuarioRepo.findById(dto.idClinica())
                .orElseThrow(() -> new RuntimeException("Clínica no encontrada"));

        Usuario dueno = new Usuario();
        dueno.setNombre(dto.nombre());
        dueno.setEmail(dto.email());
        dueno.setPassword(passwordEncoder.encode(dto.password()));
        dueno.setRol(RolUsuario.DUENO);
        dueno.setTelefono(dto.telefono());
        dueno.setFechaRegistro(LocalDateTime.now());
        usuarioRepo.save(dueno);

        Mascota mascota = new Mascota();
        mascota.setNombre(dto.nombreMascota());
        mascota.setEspecie(dto.especie());
        mascota.setRaza(dto.raza());
        mascota.setFechaNacimiento(dto.fechaNacimiento());
        mascota.setDueno(dueno);
        mascota.setClinica(clinica);

        return toDTO(mascotaRepo.save(mascota));
    }

    @Transactional
    public MascotaDTO guardarDesdeDTO(MascotaInsertarDTO dto) {
        Usuario dueno = usuarioRepo.findById(dto.duenoId())
                .orElseThrow(() -> new RuntimeException("Dueño no encontrado"));

        Mascota nuevaMascota = new Mascota();
        nuevaMascota.setNombre(dto.nombre());
        nuevaMascota.setEspecie(dto.especie());
        nuevaMascota.setRaza(dto.raza());
        nuevaMascota.setFechaNacimiento(dto.fechaNacimiento());
        nuevaMascota.setDueno(dueno);
        nuevaMascota.setFotoUrl(dto.fotoUrl());   // ← línea añadida

        return toDTO(mascotaRepo.save(nuevaMascota));
    }

    @Transactional
    public MascotaDTO actualizarFotoUrl(Long id, String fotoUrl) {
        Mascota mascota = mascotaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con id: " + id));
        String cleanUrl = fotoUrl.replaceAll("^\"|\"$", "");
        mascota.setFotoUrl(cleanUrl);
        return toDTO(mascotaRepo.save(mascota));
    }

    @Transactional
    public MascotaDTO subirFoto(Long id, MultipartFile foto) {
        Mascota mascota = mascotaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con id: " + id));
        try {
            Path uploadsDir = Paths.get(System.getProperty("user.home"), "haru_uploads");
            if (!Files.exists(uploadsDir)) Files.createDirectories(uploadsDir);

            String original  = foto.getOriginalFilename();
            String extension = (original != null && original.contains("."))
                    ? original.substring(original.lastIndexOf('.')) : ".jpg";
            String nombreFichero = "mascota_" + id + "_" + System.currentTimeMillis() + extension;
            Files.copy(foto.getInputStream(), uploadsDir.resolve(nombreFichero),
                    StandardCopyOption.REPLACE_EXISTING);

            mascota.setFotoUrl("/uploads/" + nombreFichero);
            return toDTO(mascotaRepo.save(mascota));
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la foto: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public MascotaDTO obtenerPorId(Long id) {
        return toDTO(mascotaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada")));
    }

    private MascotaDTO toDTO(Mascota m) {
        return new MascotaDTO(
                m.getId(),
                m.getNombre(),
                m.getEspecie(),
                m.getRaza(),
                m.getFechaNacimiento(),
                m.getFotoUrl(),
                m.getDueno().getId(),
                m.getDueno().getNombre(),
                m.getClinica() != null ? m.getClinica().getId() : null
        );
    }
}