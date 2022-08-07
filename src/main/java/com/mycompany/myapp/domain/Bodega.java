package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Type;

/**
 * A Bodega.
 */
@Entity
@Table(name = "bodega")
public class Bodega implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "detalle")
    private String detalle;

    @Column(name = "ubicacion")
    private String ubicacion;

    @OneToMany(mappedBy = "bodega")
    @JsonIgnoreProperties(value = { "codigos", "precioIngresos", "precioSalidas", "bodega", "oficina" }, allowSetters = true)
    private Set<Producto> productos = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_bodega__usuario",
        joinColumns = @JoinColumn(name = "bodega_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    @JsonIgnoreProperties(
        value = { "user", "rol", "infoLegals", "sucursals", "empresas", "empresaId", "bodegas", "oficinas" },
        allowSetters = true
    )
    private Set<Usuario> usuarios = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "bodegas", "sucursal" }, allowSetters = true)
    private Inventario inventario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bodega id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Bodega nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalle() {
        return this.detalle;
    }

    public Bodega detalle(String detalle) {
        this.setDetalle(detalle);
        return this;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getUbicacion() {
        return this.ubicacion;
    }

    public Bodega ubicacion(String ubicacion) {
        this.setUbicacion(ubicacion);
        return this;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Set<Producto> getProductos() {
        return this.productos;
    }

    public void setProductos(Set<Producto> productos) {
        if (this.productos != null) {
            this.productos.forEach(i -> i.setBodega(null));
        }
        if (productos != null) {
            productos.forEach(i -> i.setBodega(this));
        }
        this.productos = productos;
    }

    public Bodega productos(Set<Producto> productos) {
        this.setProductos(productos);
        return this;
    }

    public Bodega addProducto(Producto producto) {
        this.productos.add(producto);
        producto.setBodega(this);
        return this;
    }

    public Bodega removeProducto(Producto producto) {
        this.productos.remove(producto);
        producto.setBodega(null);
        return this;
    }

    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Bodega usuarios(Set<Usuario> usuarios) {
        this.setUsuarios(usuarios);
        return this;
    }

    public Bodega addUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        usuario.getBodegas().add(this);
        return this;
    }

    public Bodega removeUsuario(Usuario usuario) {
        this.usuarios.remove(usuario);
        usuario.getBodegas().remove(this);
        return this;
    }

    public Inventario getInventario() {
        return this.inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public Bodega inventario(Inventario inventario) {
        this.setInventario(inventario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bodega)) {
            return false;
        }
        return id != null && id.equals(((Bodega) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bodega{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", detalle='" + getDetalle() + "'" +
            ", ubicacion='" + getUbicacion() + "'" +
            "}";
    }
}
