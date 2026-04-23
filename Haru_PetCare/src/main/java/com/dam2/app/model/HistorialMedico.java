package com.dam2.app.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "historial_medico")
public class HistorialMedico {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "tipo_registro", nullable = false, length = 100)
	private String tipoRegistro;

	@Column(columnDefinition = "TEXT")
	private String descripcion;

	@Column(name = "fecha_registro")
	private LocalDateTime fechaRegistro;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "mascota_id", nullable = false)
	private Mascota mascota;

	public HistorialMedico() {
		super();
	}

	public HistorialMedico(Long id, String tipoRegistro, String descripcion, LocalDateTime fechaRegistro,
			Mascota mascota) {
		super();
		this.id = id;
		this.tipoRegistro = tipoRegistro;
		this.descripcion = descripcion;
		this.fechaRegistro = fechaRegistro;
		this.mascota = mascota;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Mascota getMascota() {
		return mascota;
	}

	public void setMascota(Mascota mascota) {
		this.mascota = mascota;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistorialMedico other = (HistorialMedico) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "HistorialMedico [id=" + id + ", tipoRegistro=" + tipoRegistro + ", fechaRegistro=" + fechaRegistro
				+ "]";
	}

}
