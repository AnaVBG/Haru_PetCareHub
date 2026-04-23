package com.dam2.app.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pines_mapa")
public class PinMapa {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoPin tipo;

	@Column(nullable = false)
	private Double latitud;

	@Column(nullable = false)
	private Double longitud;

	@Column(length = 255)
	private String descripcion;

	@Column(name = "fecha_creacion")
	private LocalDateTime fechaCreacion;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	public PinMapa() {
		super();
	}

	public PinMapa(Long id, TipoPin tipo, Double latitud, Double longitud, String descripcion,
			LocalDateTime fechaCreacion, Usuario usuario) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.latitud = latitud;
		this.longitud = longitud;
		this.descripcion = descripcion;
		this.fechaCreacion = fechaCreacion;
		this.usuario = usuario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoPin getTipo() {
		return tipo;
	}

	public void setTipo(TipoPin tipo) {
		this.tipo = tipo;
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
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
		PinMapa other = (PinMapa) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "PinMapa [id=" + id + ", tipo=" + tipo + ", latitud=" + latitud + ", longitud=" + longitud + "]";
	}

}
