package com.dam2.app.controlller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dam2.app.dto.MascotaDTO;
import com.dam2.app.dto.MascotaInsertarDTO;
import com.dam2.app.model.Mascota;
import com.dam2.app.service.MascotaService;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {
	@Autowired
    private MascotaService service;
    
    // Método para crear una nueva mascota desde el móvil
    @PostMapping("/inserta")
    public Mascota insert(@RequestBody MascotaInsertarDTO dto) {
        // Le pasamos el DTO al servicio para que él busque al dueño y guarde la mascota
        return service.guardarDesdeDTO(dto);
    }
    
    // Método para que el móvil pida la lista de mascotas de un dueño en concreto
    @GetMapping("/dueno/{id}")
    public List<MascotaDTO> getMascotasPorDueno(@PathVariable Long id) {
        
        // 1. Pedimos al servicio las entidades completas
        List<Mascota> mascotas = service.obtenerMascotasEntidadPorDueno(id);
        
        // 2. Mapeamos a Record DTO para evitar recursividad (bucle infinito de JSON)
        List<MascotaDTO> dtos = mascotas.stream()
            .map(m -> new MascotaDTO(
                    m.getId(), 
                    m.getNombre(), 
                    m.getEspecie(), 
                    m.getRaza(), 
                    m.getFechaNacimiento()))
            .toList();
            
        return dtos;
    }
}
