// service/PdfService.java
package com.dam2.app.service;

import com.dam2.app.model.HistorialMedico;
import com.dam2.app.model.Mascota;
import com.dam2.app.repo.HistorialMedicoRepository;
import com.dam2.app.repo.MascotaRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Genera el historial médico completo en formato PDF.
 *
 * Requisito funcional de tu memoria: sección 4.1
 * "El sistema debe permitir generar y descargar un documento PDF
 *  con el historial médico completo del animal."
 *
 * Usamos iText 7 (librería Java estándar para generación de PDF).
 * El controlador devuelve el PDF como array de bytes con el
 * Content-Type "application/pdf" para que Android pueda descargarlo.
 *
 */
@Service
public class PdfService {

    private final MascotaRepository         mascotaRepo;
    private final HistorialMedicoRepository  historialRepo;

    public PdfService(MascotaRepository mascotaRepo,
                      HistorialMedicoRepository historialRepo) {
        this.mascotaRepo   = mascotaRepo;
        this.historialRepo = historialRepo;
    }

    @Transactional(readOnly = true)
    public byte[] generarHistorialPdf(Long idMascota) {
        Mascota mascota = mascotaRepo.findById(idMascota)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        List<HistorialMedico> registros =
                historialRepo.findByMascota_IdOrderByFechaRegistroDesc(idMascota);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter   writer   = new PdfWriter(baos);
            PdfDocument pdfDoc   = new PdfDocument(writer);
            Document    document = new Document(pdfDoc);

            // ── Cabecera ──────────────────────────────────────────────
            document.add(new Paragraph("HARU PETCARE — Historial Médico")
                    .setBold().setFontSize(18));
            document.add(new Paragraph(
                    "Mascota: " + mascota.getNombre()
                  + " | Especie: " + mascota.getEspecie()
                  + " | Raza: "    + mascota.getRaza()));
            document.add(new Paragraph(" "));

            // ── Tabla de registros ────────────────────────────────────
            Table tabla = new Table(3); // 3 columnas
            tabla.addCell("Fecha");
            tabla.addCell("Tipo");
            tabla.addCell("Descripción");

            for (HistorialMedico h : registros) {
                tabla.addCell(h.getFechaRegistro().toString());
                tabla.addCell(h.getTipoRegistro());
                tabla.addCell(h.getDescripcion() != null ? h.getDescripcion() : "—");
            }

            document.add(tabla);
            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF: " + e.getMessage());
        }
    }
}