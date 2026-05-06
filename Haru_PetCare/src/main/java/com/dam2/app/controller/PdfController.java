package com.dam2.app.controller;

import com.dam2.app.service.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Devuelve el PDF como array de bytes con el Content-Type correcto.
 * Android usa DownloadManager para guardar el archivo en el dispositivo.
 */
@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/historial/{idMascota}")
    public ResponseEntity<byte[]> descargarHistorial(@PathVariable Long idMascota) {
        byte[] pdf = pdfService.generarHistorialPdf(idMascota);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(
                "attachment", "historial_mascota_" + idMascota + ".pdf");

        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}