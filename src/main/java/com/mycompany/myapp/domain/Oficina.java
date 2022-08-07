package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Type;

/**
 * A Oficina.
 */
@Entity
@Table(name = "oficina")
public class Oficina implements Serializable {

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

    @OneToMany(mappedBy = "oficina")
    @JsonIgnoreProperties(value = { "codigos", "precioIngresos", "precioSalidas", "bodega", "oficina" }, allowSetters = true)
    private Set<Producto> productos = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_oficina__usuario",
        joinColumns = @JoinColumn(name = "oficina_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    @JsonIgnoreProperties(
        value = { "user", "rol", "infoLegals", "sucursals", "empresas", "empresaId", "bodegas", "oficinas" },
        allowSetters = true
    )
    private Set<Usuario> usuarios = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "oficinas", "inventarios", "empresa", "infoLegals", "usuarios", "empresaIds" }, allowSetters = true)
    private Sucursal sucursal;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Oficina id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Oficina nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalle() {
        return this.detalle;
    }

    public Oficina detalle(String detalle) {
        this.setDetalle(detalle);
        return this;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getUbicacion() {
        return this.ubicacion;
    }

    public Oficina ubicacion(String ubicacion) {
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
            this.productos.forEach(i -> i.setOficina(null));
        }
        if (productos != null) {
            productos.forEach(i -> i.setOficina(this));
        }
        this.productos = productos;
    }

    public Oficina productos(Set<Producto> productos) {
        this.setProductos(productos);
        return this;
    }

    public Oficina addProducto(Producto producto) {
        this.productos.add(producto);
        producto.setOficina(this);
        return this;
    }

    public Oficina removeProducto(Producto producto) {
        this.productos.remove(producto);
        producto.setOficina(null);
        return this;
    }

    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Oficina usuarios(Set<Usuario> usuarios) {
        this.setUsuarios(usuarios);
        return this;
    }

    public Oficina addUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        usuario.getOficinas().add(this);
        return this;
    }

    public Oficina removeUsuario(Usuario usuario) {
        this.usuarios.remove(usuario);
        usuario.getOficinas().remove(this);
        return this;
    }

    public Sucursal getSucursal() {
        return this.sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Oficina sucursal(Sucursal sucursal) {
        this.setSucursal(sucursal);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Oficina)) {
            return false;
        }
        return id != null && id.equals(((Oficina) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Oficina{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", detalle='" + getDetalle() + "'" +
            ", ubicacion='" + getUbicacion() + "'" +
            "}";
    }
}
