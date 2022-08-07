package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Inventario.
 */
@Entity
@Table(name = "inventario")
public class Inventario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "inventario")
    @JsonIgnoreProperties(value = { "productos", "usuarios", "inventario" }, allowSetters = true)
    private Set<Bodega> bodegas = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "oficinas", "inventarios", "empresa", "infoLegals", "usuarios", "empresaIds" }, allowSetters = true)
    private Sucursal sucursal;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inventario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Inventario nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Bodega> getBodegas() {
        return this.bodegas;
    }

    public void setBodegas(Set<Bodega> bodegas) {
        if (this.bodegas != null) {
            this.bodegas.forEach(i -> i.setInventario(null));
        }
        if (bodegas != null) {
            bodegas.forEach(i -> i.setInventario(this));
        }
        this.bodegas = bodegas;
    }

    public Inventario bodegas(Set<Bodega> bodegas) {
        this.setBodegas(bodegas);
        return this;
    }

    public Inventario addBodega(Bodega bodega) {
        this.bodegas.add(bodega);
        bodega.setInventario(this);
        return this;
    }

    public Inventario removeBodega(Bodega bodega) {
        this.bodegas.remove(bodega);
        bodega.setInventario(null);
        return this;
    }

    public Sucursal getSucursal() {
        return this.sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Inventario sucursal(Sucursal sucursal) {
        this.setSucursal(sucursal);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inventario)) {
            return false;
        }
        return id != null && id.equals(((Inventario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inventario{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
