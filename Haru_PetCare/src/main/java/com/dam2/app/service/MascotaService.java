package com.dam2.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dam2.app.model.Mascota;
import com.dam2.app.model.Usuario;
import com.dam2.app.repo.MascotaRepository;
import com.dam2.app.repo.UsuarioRepository;
import com.dama2.app.dto.MascotaDTO;
import com.dama2.app.dto.MascotaInsertarDTO;


@Service
public class MascotaService {

	private final MascotaRepository mascotaRepo;
    private final UsuarioRepository usuarioRepo;

    // Inyección por constructor (Buenas prácticas)
    public MascotaService(MascotaRepository mascotaRepo, UsuarioRepository usuarioRepo) {
        super();
        this.mascotaRepo = mascotaRepo;
		this.usuarioRepo = usuarioRepo;
    }
    
    // @Transactional(readOnly = true) porque solo hacemos SELECT, no modificamos datos
//    @Transactional(readOnly = true)
//    public List<MascotaDTO> obtenerMascotasPorDueno(Long idDueno) {
//        
//        // 1. Buscamos las mascotas usando el repositorio
//        List<Mascota> mascotas = mascotaRepo.findByDueno_Id(idDueno);
//        
//        // 2. Mapeamos de Entidad a DTO para enviar solo lo necesario a Android
//        return mascotas.stream()
//                .map(m -> new MascotaDTO(
//                        m.getId(), 
//                        m.getNombre(), 
//                        m.getEspecie(), 
//                        m.getRaza(), 
//                        m.getFechaNacimiento(),
//                        m.getdi
//                ))
//                .toList();
//    }
    
    // Método para guardar/actualizar (NO lleva readOnly=true porque hace un INSERT/UPDATE)
    @Transactional
    public Mascota guardarMascota(Mascota nuevaMascota) {
        return mascotaRepo.save(nuevaMascota);
    }
    

    public List<Mascota> obtenerMascotasEntidadPorDueno(Long idDueno) {
        return mascotaRepo.findByDueno_Id(idDueno);
    }

    @Transactional
    public Mascota guardarDesdeDTO(MascotaInsertarDTO dto) {
        // Buscamos al dueño en la base de datos usando el ID que nos manda el móvil
        Usuario dueno = usuarioRepo.findById(dto.duenoId())
                .orElseThrow(() -> new RuntimeException("Dueño no encontrado"));
                
        // Creamos la mascota nueva
        Mascota nuevaMascota = new Mascota();
        nuevaMascota.setNombre(dto.nombre());
        nuevaMascota.setEspecie(dto.especie());
        nuevaMascota.setRaza(dto.raza());
        nuevaMascota.setFechaNacimiento(dto.fechaNacimiento());
        nuevaMascota.setDueno(dueno); // ¡Aquí enlazamos la mascota con su dueño!
        
        return mascotaRepo.save(nuevaMascota);
    }
	
}
