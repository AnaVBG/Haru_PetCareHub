package com.dam2.app.service;

import com.dam2.app.dto.MascotaDTO;
import com.dam2.app.dto.MascotaInsertarDTO;
import com.dam2.app.model.Mascota;
import com.dam2.app.model.Usuario;
import com.dam2.app.repo.MascotaRepository;
import com.dam2.app.repo.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class MascotaService {

    private final MascotaRepository mascotaRepo;
    private final UsuarioRepository usuarioRepo;

    public MascotaService(MascotaRepository mascotaRepo, UsuarioRepository usuarioRepo) {
        this.mascotaRepo = mascotaRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Transactional(readOnly = true)
    public List<MascotaDTO> obtenerMascotasPorDueno(Long idDueno) {
        return mascotaRepo.findByDueno_Id(idDueno)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public MascotaDTO guardarDesdeDTO(MascotaInsertarDTO dto) {
        Usuario dueno = usuarioRepo.findById(dto.duenoId())
                .orElseThrow(() -> new RuntimeException("Dueño no encontrado con id: " + dto.duenoId()));

        Mascota nuevaMascota = new Mascota();
        nuevaMascota.setNombre(dto.nombre());
        nuevaMascota.setEspecie(dto.especie());
        nuevaMascota.setRaza(dto.raza());
        nuevaMascota.setFechaNacimiento(dto.fechaNacimiento());
        nuevaMascota.setDueno(dueno);

        return toDTO(mascotaRepo.save(nuevaMascota));
    }

    @Transactional
    public MascotaDTO subirFoto(Long id, MultipartFile foto) {
        Mascota mascota = mascotaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con id: " + id));

        try {
            Path uploadsDir = Paths.get(System.getProperty("user.home"), "haru_uploads");
            if (!Files.exists(uploadsDir)) {
                Files.createDirectories(uploadsDir);
            }

            String original = foto.getOriginalFilename();
            String extension = (original != null && original.contains("."))
                    ? original.substring(original.lastIndexOf('.'))
                    : ".jpg";
            String nombreFichero = "mascota_" + id + "_" + System.currentTimeMillis() + extension;
            Path destino = uploadsDir.resolve(nombreFichero);
            Files.copy(foto.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            mascota.setFotoUrl("/uploads/" + nombreFichero);
            return toDTO(mascotaRepo.save(mascota));

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la foto: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public MascotaDTO obtenerPorId(Long id) {
        Mascota m = mascotaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        return toDTO(m);
    }

    private MascotaDTO toDTO(Mascota m) {
        return new MascotaDTO(
                m.getId(),
                m.getNombre(),
                m.getEspecie(),
                m.getRaza(),
                m.getFechaNacimiento(),
                m.getFotoUrl(),
                m.getDueno().getId()
        );
    }
}