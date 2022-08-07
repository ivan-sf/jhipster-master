package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Type;

/**
 * A Precio.
 */
@Entity
@Table(name = "precio")
public class Precio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "valor")
    private Integer valor;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "detalle")
    private String detalle;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @ManyToMany(mappedBy = "precioIngresos")
    @JsonIgnoreProperties(value = { "codigos", "precioIngresos", "precioSalidas", "bodega", "oficina" }, allowSetters = true)
    private Set<Producto> productoIngresos = new HashSet<>();

    @ManyToMany(mappedBy = "precioSalidas")
    @JsonIgnoreProperties(value = { "codigos", "precioIngresos", "precioSalidas", "bodega", "oficina" }, allowSetters = true)
    private Set<Producto> productoSalidas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Precio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getValor() {
        return this.valor;
    }

    public Precio valor(Integer valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public String getDetalle() {
        return this.detalle;
    }

    public Precio detalle(String detalle) {
        this.setDetalle(detalle);
        return this;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public LocalDate getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Precio fechaRegistro(LocalDate fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Set<Producto> getProductoIngresos() {
        return this.productoIngresos;
    }

    public void setProductoIngresos(Set<Producto> productos) {
        if (this.productoIngresos != null) {
            this.productoIngresos.forEach(i -> i.removePrecioIngreso(this));
        }
        if (productos != null) {
            productos.forEach(i -> i.addPrecioIngreso(this));
        }
        this.productoIngresos = productos;
    }

    public Precio productoIngresos(Set<Producto> productos) {
        this.setProductoIngresos(productos);
        return this;
    }

    public Precio addProductoIngreso(Producto producto) {
        this.productoIngresos.add(producto);
        producto.getPrecioIngresos().add(this);
        return this;
    }

    public Precio removeProductoIngreso(Producto producto) {
        this.productoIngresos.remove(producto);
        producto.getPrecioIngresos().remove(this);
        return this;
    }

    public Set<Producto> getProductoSalidas() {
        return this.productoSalidas;
    }

    public void setProductoSalidas(Set<Producto> productos) {
        if (this.productoSalidas != null) {
            this.productoSalidas.forEach(i -> i.removePrecioSalida(this));
        }
        if (productos != null) {
            productos.forEach(i -> i.addPrecioSalida(this));
        }
        this.productoSalidas = productos;
    }

    public Precio productoSalidas(Set<Producto> productos) {
        this.setProductoSalidas(productos);
        return this;
    }

    public Precio addProductoSalida(Producto producto) {
        this.productoSalidas.add(producto);
        producto.getPrecioSalidas().add(this);
        return this;
    }

    public Precio removeProductoSalida(Producto producto) {
        this.productoSalidas.remove(producto);
        producto.getPrecioSalidas().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Precio)) {
            return false;
        }
        return id != null && id.equals(((Precio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Precio{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", detalle='" + getDetalle() + "'" +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
