package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Type;

/**
 * A Codigo.
 */
@Entity
@Table(name = "codigo")
public class Codigo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "codigo")
    private String codigo;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "detalle")
    private String detalle;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @ManyToMany(mappedBy = "codigos")
    @JsonIgnoreProperties(value = { "codigos", "precioIngresos", "precioSalidas", "bodega", "oficina" }, allowSetters = true)
    private Set<Producto> productos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Codigo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Codigo codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDetalle() {
        return this.detalle;
    }

    public Codigo detalle(String detalle) {
        this.setDetalle(detalle);
        return this;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public LocalDate getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Codigo fechaRegistro(LocalDate fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Set<Producto> getProductos() {
        return this.productos;
    }

    public void setProductos(Set<Producto> productos) {
        if (this.productos != null) {
            this.productos.forEach(i -> i.removeCodigo(this));
        }
        if (productos != null) {
            productos.forEach(i -> i.addCodigo(this));
        }
        this.productos = productos;
    }

    public Codigo productos(Set<Producto> productos) {
        this.setProductos(productos);
        return this;
    }

    public Codigo addProducto(Producto producto) {
        this.productos.add(producto);
        producto.getCodigos().add(this);
        return this;
    }

    public Codigo removeProducto(Producto producto) {
        this.productos.remove(producto);
        producto.getCodigos().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Codigo)) {
            return false;
        }
        return id != null && id.equals(((Codigo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Codigo{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", detalle='" + getDetalle() + "'" +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
