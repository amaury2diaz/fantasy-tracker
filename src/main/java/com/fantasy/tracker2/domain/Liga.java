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
 * A Liga.
 */
@Entity
@Table(name = "liga")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "liga")
public class Liga implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "liga", nullable = false)
    private String liga;

    @OneToMany(mappedBy = "liga")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Operacion> operaciones = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLiga() {
        return liga;
    }

    public Liga liga(String liga) {
        this.liga = liga;
        return this;
    }

    public void setLiga(String liga) {
        this.liga = liga;
    }

    public Set<Operacion> getOperaciones() {
        return operaciones;
    }

    public Liga operaciones(Set<Operacion> operacions) {
        this.operaciones = operacions;
        return this;
    }

    public Liga addOperaciones(Operacion operacion) {
        this.operaciones.add(operacion);
        operacion.setLiga(this);
        return this;
    }

    public Liga removeOperaciones(Operacion operacion) {
        this.operaciones.remove(operacion);
        operacion.setLiga(null);
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
        if (!(o instanceof Liga)) {
            return false;
        }
        return id != null && id.equals(((Liga) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Liga{" +
            "id=" + getId() +
            ", liga='" + getLiga() + "'" +
            "}";
    }
}
