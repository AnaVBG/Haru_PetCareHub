package com.dam2.app.controller;

import com.dam2.app.dto.*;
import com.dam2.app.service.MascotaService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

	private final MascotaService mascotaService;

	public MascotaController(MascotaService mascotaService) {
		this.mascotaService = mascotaService;
	}

	@GetMapping("/dueno/{id}")
	public ResponseEntity<List<MascotaDTO>> getMascotasPorDueno(@PathVariable Long id) {
		return ResponseEntity.ok(mascotaService.obtenerMascotasPorDueno(id));
	}

	@GetMapping("/{id}")
	public ResponseEntity<MascotaDTO> getMascotaPorId(@PathVariable Long id) {
		return ResponseEntity.ok(mascotaService.obtenerPorId(id));
	}

	@GetMapping("/todas")
	public ResponseEntity<List<MascotaDTO>> buscarTodas(@RequestParam Long idUsuario,
			@RequestParam(required = false) String especie, @RequestParam(required = false) String buscar) {
		return ResponseEntity.ok(mascotaService.buscarTodas(idUsuario, especie, buscar));
	}

	@PostMapping("/inserta")
	public ResponseEntity<MascotaDTO> insertar(@RequestBody MascotaInsertarDTO dto) {
		return ResponseEntity.status(201).body(mascotaService.guardarDesdeDTO(dto));
	}

	@PostMapping(value = "/{id}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<MascotaDTO> subirFoto(@PathVariable Long id, @RequestParam("foto") MultipartFile foto) {
		return ResponseEntity.ok(mascotaService.subirFoto(id, foto));
	}

	@PostMapping("/vincular-clinica")
	public ResponseEntity<List<MascotaDTO>> vincularAClinica(@RequestBody VincularMascotaClinicaDTO dto) {
		return ResponseEntity.ok(mascotaService.vincularAClinica(dto));
	}

	@PostMapping("/crear-dueno-mascota")
	public ResponseEntity<MascotaDTO> crearDuenoConMascota(@RequestBody CrearDuenoConMascotaDTO dto) {
		return ResponseEntity.status(201).body(mascotaService.crearDuenoConMascota(dto));
	}
}