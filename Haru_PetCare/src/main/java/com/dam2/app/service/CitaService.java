package com.dam2.app.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dam2.app.model.Cita;
import com.dam2.app.model.EstadoCita;
import com.dam2.app.repo.CitaRepository;
import com.dama2.app.dto.CitaDTO;

@Service
public class CitaService {

	private final CitaRepository citaRepo;

    public CitaService(CitaRepository citaRepo) {
        super();
        this.citaRepo = citaRepo;
    }
    
    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerAgendaVeterinario(Long idVeterinario) {
        
        List<Cita> citas = citaRepo.findByVeterinario_IdAndEstado(idVeterinario, EstadoCita.PENDIENTE);
        
        return citas.stream()
                .map(c -> new CitaDTO(
                        c.getId(), 
                        c.getFechaCita(), 
                        c.getMotivo(), 
                        c.getMascota().getNombre(), 
                        c.getVeterinario().getNombre()
                ))
                .toList();
    }
	
}
