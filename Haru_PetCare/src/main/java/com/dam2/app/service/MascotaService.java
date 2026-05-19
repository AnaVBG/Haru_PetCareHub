// service/MascotaService.java — versión completa y limpia
package com.dam2.app.service;

import com.dam2.app.dto.MascotaDTO;
import com.dam2.app.dto.MascotaInsertarDTO;
import com.dam2.app.model.Mascota;
import com.dam2.app.model.Usuario;
import com.dam2.app.repo.MascotaRepository;
import com.dam2.app.repo.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Gestiona toda la lógica de negocio relacionada con las mascotas.
 *
 * Versión limpia respecto al original:
 * 1. Eliminado obtenerMascotasEntidadPorDueno() — exponía entidades JPA
 *    fuera del Service, lo que viola Clean Architecture. El Controller
 *    ya no lo necesita porque recibe DTOs directamente.
 * 2. Eliminado guardarMascota(Mascota) — recibía una entidad desde fuera,
 *    lo que significa que alguien externo construía la entidad JPA.
 *    Toda construcción de entidades es responsabilidad del Service.
 * 3. guardarDesdeDTO devuelve MascotaDTO en vez de Mascota — nunca
 *    exponemos entidades JPA fuera de la capa de servicio.
 */
@Service
public class MascotaService {

    private final MascotaRepository mascotaRepo;
    private final UsuarioRepository usuarioRepo;

    public MascotaService(MascotaRepository mascotaRepo, UsuarioRepository usuarioRepo) {
        this.mascotaRepo = mascotaRepo;
        this.usuarioRepo = usuarioRepo;
    }

    /**
     * Devuelve todas las mascotas de un dueño como DTOs.
     * Lo llama GET /api/mascotas/dueno/{id}
     *
     * @Transactional(readOnly = true): le indica a JPA que esta operación
     * solo hace SELECT. JPA puede optimizarla: no abre transacción de escritura,
     * no hace dirty checking (comparar entidades para detectar cambios),
     * lo que mejora el rendimiento especialmente con listas grandes.
     */
    @Transactional(readOnly = true)
    public List<MascotaDTO> obtenerMascotasPorDueno(Long idDueno) {
        List<Mascota> mascotas = mascotaRepo.findByDueno_Id(idDueno);

        return mascotas.stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Crea una mascota nueva a partir de los datos que envía Android.
     * Lo llama POST /api/mascotas/inserta
     *
     * Sin readOnly porque hace un INSERT en base de datos.
     */
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
        // fotoUrl se gestiona en un endpoint separado de subida de imagen

        Mascota guardada = mascotaRepo.save(nuevaMascota);
        return toDTO(guardada);
    }

    /**
     * Método privado de mapeo Mascota → MascotaDTO.
     * Centralizado aquí para no repetir el mismo bloque en cada método.
     * El símbolo 'this::toDTO' en los streams hace referencia a este método.
     */
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
    
    @Transactional(readOnly = true)
    public MascotaDTO obtenerPorId(Long id) {
        Mascota m = mascotaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        return toDTO(m);
    }
    
}