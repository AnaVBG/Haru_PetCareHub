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
@Table(name = "alertas_perdida")
public class AlertaPerdida {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ultima_ubicacion_lat", nullable = false)
	private Double ultimaUbicacionLat;

	@Column(name = "ultima_ubicacion_lng", nullable = false)
	private Double ultimaUbicacionLng;

	@Column(name = "mensaje_adicional", columnDefinition = "TEXT")
	private String mensajeAdicional;

	@Column(nullable = false)
	private Boolean activa = true;

	@Column(name = "fecha_alerta")
	private LocalDateTime fechaAlerta;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "mascota_id", nullable = false)
	private Mascota mascota;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	public AlertaPerdida() {
		super();
	}

	public AlertaPerdida(Long id, Double ultimaUbicacionLat, Double ultimaUbicacionLng, String mensajeAdicional,
			Boolean activa, LocalDateTime fechaAlerta, Mascota mascota, Usuario usuario) {
		super();
		this.id = id;
		this.ultimaUbicacionLat = ultimaUbicacionLat;
		this.ultimaUbicacionLng = ultimaUbicacionLng;
		this.mensajeAdicional = mensajeAdicional;
		this.activa = activa;
		this.fechaAlerta = fechaAlerta;
		this.mascota = mascota;
		this.usuario = usuario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getUltimaUbicacionLat() {
		return ultimaUbicacionLat;
	}

	public void setUltimaUbicacionLat(Double ultimaUbicacionLat) {
		this.ultimaUbicacionLat = ultimaUbicacionLat;
	}

	public Double getUltimaUbicacionLng() {
		return ultimaUbicacionLng;
	}

	public void setUltimaUbicacionLng(Double ultimaUbicacionLng) {
		this.ultimaUbicacionLng = ultimaUbicacionLng;
	}

	public String getMensajeAdicional() {
		return mensajeAdicional;
	}

	public void setMensajeAdicional(String mensajeAdicional) {
		this.mensajeAdicional = mensajeAdicional;
	}

	public Boolean getActiva() {
		return activa;
	}

	public void setActiva(Boolean activa) {
		this.activa = activa;
	}

	public LocalDateTime getFechaAlerta() {
		return fechaAlerta;
	}

	public void setFechaAlerta(LocalDateTime fechaAlerta) {
		this.fechaAlerta = fechaAlerta;
	}

	public Mascota getMascota() {
		return mascota;
	}

	public void setMascota(Mascota mascota) {
		this.mascota = mascota;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
		AlertaPerdida other = (AlertaPerdida) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "AlertaPerdida [id=" + id + ", activa=" + activa + ", fechaAlerta=" + fechaAlerta + "]";
	}

}
