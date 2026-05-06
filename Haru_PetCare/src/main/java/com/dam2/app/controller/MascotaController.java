package com.dam2.app.controller;

import com.dam2.app.service.MascotaService;
import com.dam2.app.dto.MascotaDTO;
import com.dam2.app.dto.MascotaInsertarDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de mascotas.
 *
 * Correcciones respecto a la versión original:
 * 1. Inyección por constructor en vez de @Autowired — buena práctica
 *    que facilita los tests unitarios y deja las dependencias explícitas.
 * 2. El mapeo Entidad → DTO se hace en el Service, no aquí.
 *    El Controller solo recibe la petición, llama al Service y devuelve
 *    la respuesta. No tiene lógica propia.
 * 3. ResponseEntity<T> como tipo de retorno — nos da control total
 *    sobre el código HTTP devuelto (200, 201, 404...).
 */
@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    /**
     * GET /api/mascotas/dueno/{id}
     * Devuelve la lista de mascotas de un dueño concreto.
     * Android lo llama al entrar en MascotasFragment.
     */
    @GetMapping("/dueno/{id}")
    public ResponseEntity<List<MascotaDTO>> getMascotasPorDueno(@PathVariable Long id) {
        return ResponseEntity.ok(mascotaService.obtenerMascotasPorDueno(id));
    }

    /**
     * POST /api/mascotas/inserta
     * Crea una mascota nueva desde Android.
     * Recibe un MascotaInsertarDTO y devuelve el MascotaDTO creado.
     * ResponseEntity.status(201) indica "recurso creado" — más correcto
     * semánticamente que un 200 para una creación.
     */
    @PostMapping("/inserta")
    public ResponseEntity<MascotaDTO> insertar(@RequestBody MascotaInsertarDTO dto) {
        MascotaDTO creada = mascotaService.guardarDesdeDTO(dto);
        return ResponseEntity.status(201).body(creada);
    }
}