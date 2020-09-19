package com.fantasy.tracker2.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import com.fantasy.tracker2.domain.enumeration.AccionEnum;

/**
 * A Operacion.
 */
@Entity
@Table(name = "operacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "operacion")
public class Operacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "precio", nullable = false)
    private Long precio;


    @Column(name = "fecha")
    private Instant fecha;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "accion", nullable = false)
    private AccionEnum accion;

    @ManyToOne
    @JsonIgnoreProperties(value = "operaciones", allowSetters = true)
    private Futbolista futbolista;

    @ManyToOne
    @JsonIgnoreProperties(value = "deOperaciones", allowSetters = true)
    private Usuario deUsuario;

    @ManyToOne
    @JsonIgnoreProperties(value = "aOperaciones", allowSetters = true)
    private Usuario aUsuario;

    @ManyToOne
    @JsonIgnoreProperties(value = "operaciones", allowSetters = true)
    private Liga liga;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrecio() {
        return precio;
    }

    public Operacion precio(Long precio) {
        this.precio = precio;
        return this;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }

    public Instant getFecha() {
        return fecha;
    }

    public Operacion fecha(Instant fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public AccionEnum getAccion() {
        return accion;
    }

    public Operacion accion(AccionEnum accion) {
        this.accion = accion;
        return this;
    }

    public void setAccion(AccionEnum accion) {
        this.accion = accion;
    }

    public Futbolista getFutbolista() {
        return futbolista;
    }

    public Operacion futbolista(Futbolista futbolista) {
        this.futbolista = futbolista;
        return this;
    }

    public void setFutbolista(Futbolista futbolista) {
        this.futbolista = futbolista;
    }

    public Usuario getDeUsuario() {
        return deUsuario;
    }

    public Operacion deUsuario(Usuario usuario) {
        this.deUsuario = usuario;
        return this;
    }

    public void setDeUsuario(Usuario usuario) {
        this.deUsuario = usuario;
    }

    public Usuario getaUsuario() {
        return aUsuario;
    }

    public Operacion aUsuario(Usuario usuario) {
        this.aUsuario = usuario;
        return this;
    }

    public void setAUsuario(Usuario usuario) {
        this.aUsuario = usuario;
    }

    public Liga getLiga() {
        return liga;
    }

    public Operacion liga(Liga liga) {
        this.liga = liga;
        return this;
    }

    public void setLiga(Liga liga) {
        this.liga = liga;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Operacion)) {
            return false;
        }
        return id != null && id.equals(((Operacion) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Operacion{" +
            "id=" + getId() +
            ", precio=" + getPrecio() +
            ", fecha='" + getFecha() + "'" +
            ", accion='" + getAccion() + "'" +
            "}";
    }
}
