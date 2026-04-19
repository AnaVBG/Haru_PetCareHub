package com.dam2.app.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {
	
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 100)
	private String nombre;
	
	@Column(nullable = false, length = 150, unique = true)
	private String email;
	
	@Column(nullable = false, length = 255)
	private String password;
	
	@Enumerated(EnumType.STRING)
    @Column(nullable = false)
	private RolUsuario rol;
	
	@Column(length = 20)
	private String telefono;
	
	@Column(name = "token_fcm", length = 255)
	private String tokenFcm;
	
	@Column(name = "fecha_registro", nullable = false)
	private LocalDateTime fechaRegistro;
	
	@OneToMany(mappedBy = "dueno", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Mascota> mascotas = new HashSet<>();
	
	@OneToMany(mappedBy = "veterinario", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Cita> citasComoVeterinario = new HashSet<>();
	
	@OneToMany(mappedBy = "dueno", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Cita> citasComoUsuario = new HashSet<>();
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public RolUsuario getRol() {
		return rol;
	}
	public void setRol(RolUsuario rol) {
		this.rol = rol;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getTokenFcm() {
		return tokenFcm;
	}
	public void setTokenFcm(String tokenFcm) {
		this.tokenFcm = tokenFcm;
	}
	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public Set<Mascota> getMascotas() {
		return mascotas;
	}
	public void setMascotas(Set<Mascota> mascotas) {
		this.mascotas = mascotas;
	}
	public Set<Cita> getCitasComoVeterinario() {
		return citasComoVeterinario;
	}
	public void setCitasComoVeterinario(Set<Cita> citasComoVeterinario) {
		this.citasComoVeterinario = citasComoVeterinario;
	}
	public Set<Cita> getCitasComoUsuario() {
		return citasComoUsuario;
	}
	public void setCitasComoUsuario(Set<Cita> citasComoUsuario) {
		this.citasComoUsuario = citasComoUsuario;
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
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}
	
	@Override
    public String toString() {
        return "Usuario [id=" + id + ", nombre=" + nombre + ", email=" + email + ", rol=" + rol + "]";
    }

}
