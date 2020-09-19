package com.fantasy.tracker2.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "deUsuario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Operacion> deOperaciones = new HashSet<>();

    @OneToMany(mappedBy = "aUsuario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Operacion> aOperaciones = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Usuario nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Operacion> getDeOperaciones() {
        return deOperaciones;
    }

    public Usuario deOperaciones(Set<Operacion> operacions) {
        this.deOperaciones = operacions;
        return this;
    }

    public Usuario addDeOperaciones(Operacion operacion) {
        this.deOperaciones.add(operacion);
        operacion.setDeUsuario(this);
        return this;
    }

    public Usuario removeDeOperaciones(Operacion operacion) {
        this.deOperaciones.remove(operacion);
        operacion.setDeUsuario(null);
        return this;
    }

    public void setDeOperaciones(Set<Operacion> operacions) {
        this.deOperaciones = operacions;
    }

    public Set<Operacion> getAOperaciones() {
        return aOperaciones;
    }

    public Usuario aOperaciones(Set<Operacion> operacions) {
        this.aOperaciones = operacions;
        return this;
    }

    public Usuario addAOperaciones(Operacion operacion) {
        this.aOperaciones.add(operacion);
        operacion.setAUsuario(this);
        return this;
    }

    public Usuario removeAOperaciones(Operacion operacion) {
        this.aOperaciones.remove(operacion);
        operacion.setAUsuario(null);
        return this;
    }

    public void setAOperaciones(Set<Operacion> operacions) {
        this.aOperaciones = operacions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        return id != null && id.equals(((Usuario) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
