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
 * A Futbolista.
 */
@Entity
@Table(name = "futbolista")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "futbolista")
public class Futbolista implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "futbolista")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Operacion> operaciones = new HashSet<>();

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

    public Futbolista nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Operacion> getOperaciones() {
        return operaciones;
    }

    public Futbolista operaciones(Set<Operacion> operacions) {
        this.operaciones = operacions;
        return this;
    }

    public Futbolista addOperaciones(Operacion operacion) {
        this.operaciones.add(operacion);
        operacion.setFutbolista(this);
        return this;
    }

    public Futbolista removeOperaciones(Operacion operacion) {
        this.operaciones.remove(operacion);
        operacion.setFutbolista(null);
        return this;
    }

    public void setOperaciones(Set<Operacion> operacions) {
        this.operaciones = operacions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Futbolista)) {
            return false;
        }
        return id != null && id.equals(((Futbolista) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Futbolista{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
