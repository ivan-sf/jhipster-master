package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Type;

/**
 * A Producto.
 */
@Entity
@Table(name = "producto")
public class Producto implements Serializable {

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

    @Column(name = "iva")
    private String iva;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "foto_content_type")
    private String fotoContentType;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @ManyToMany
    @JoinTable(
        name = "rel_producto__codigo",
        joinColumns = @JoinColumn(name = "producto_id"),
        inverseJoinColumns = @JoinColumn(name = "codigo_id")
    )
    @JsonIgnoreProperties(value = { "productos" }, allowSetters = true)
    private Set<Codigo> codigos = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_producto__precio_ingreso",
        joinColumns = @JoinColumn(name = "producto_id"),
        inverseJoinColumns = @JoinColumn(name = "precio_ingreso_id")
    )
    @JsonIgnoreProperties(value = { "productoIngresos", "productoSalidas" }, allowSetters = true)
    private Set<Precio> precioIngresos = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_producto__precio_salida",
        joinColumns = @JoinColumn(name = "producto_id"),
        inverseJoinColumns = @JoinColumn(name = "precio_salida_id")
    )
    @JsonIgnoreProperties(value = { "productoIngresos", "productoSalidas" }, allowSetters = true)
    private Set<Precio> precioSalidas = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "productos", "usuarios", "inventario" }, allowSetters = true)
    private Bodega bodega;

    @ManyToOne
    @JsonIgnoreProperties(value = { "productos", "usuarios", "sucursal" }, allowSetters = true)
    private Oficina oficina;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Producto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Producto nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalle() {
        return this.detalle;
    }

    public Producto detalle(String detalle) {
        this.setDetalle(detalle);
        return this;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getIva() {
        return this.iva;
    }

    public Producto iva(String iva) {
        this.setIva(iva);
        return this;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public byte[] getFoto() {
        return this.foto;
    }

    public Producto foto(byte[] foto) {
        this.setFoto(foto);
        return this;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return this.fotoContentType;
    }

    public Producto fotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
        return this;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    public LocalDate getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Producto fechaRegistro(LocalDate fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Set<Codigo> getCodigos() {
        return this.codigos;
    }

    public void setCodigos(Set<Codigo> codigos) {
        this.codigos = codigos;
    }

    public Producto codigos(Set<Codigo> codigos) {
        this.setCodigos(codigos);
        return this;
    }

    public Producto addCodigo(Codigo codigo) {
        this.codigos.add(codigo);
        codigo.getProductos().add(this);
        return this;
    }

    public Producto removeCodigo(Codigo codigo) {
        this.codigos.remove(codigo);
        codigo.getProductos().remove(this);
        return this;
    }

    public Set<Precio> getPrecioIngresos() {
        return this.precioIngresos;
    }

    public void setPrecioIngresos(Set<Precio> precios) {
        this.precioIngresos = precios;
    }

    public Producto precioIngresos(Set<Precio> precios) {
        this.setPrecioIngresos(precios);
        return this;
    }

    public Producto addPrecioIngreso(Precio precio) {
        this.precioIngresos.add(precio);
        precio.getProductoIngresos().add(this);
        return this;
    }

    public Producto removePrecioIngreso(Precio precio) {
        this.precioIngresos.remove(precio);
        precio.getProductoIngresos().remove(this);
        return this;
    }

    public Set<Precio> getPrecioSalidas() {
        return this.precioSalidas;
    }

    public void setPrecioSalidas(Set<Precio> precios) {
        this.precioSalidas = precios;
    }

    public Producto precioSalidas(Set<Precio> precios) {
        this.setPrecioSalidas(precios);
        return this;
    }

    public Producto addPrecioSalida(Precio precio) {
        this.precioSalidas.add(precio);
        precio.getProductoSalidas().add(this);
        return this;
    }

    public Producto removePrecioSalida(Precio precio) {
        this.precioSalidas.remove(precio);
        precio.getProductoSalidas().remove(this);
        return this;
    }

    public Bodega getBodega() {
        return this.bodega;
    }

    public void setBodega(Bodega bodega) {
        this.bodega = bodega;
    }

    public Producto bodega(Bodega bodega) {
        this.setBodega(bodega);
        return this;
    }

    public Oficina getOficina() {
        return this.oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public Producto oficina(Oficina oficina) {
        this.setOficina(oficina);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Producto)) {
            return false;
        }
        return id != null && id.equals(((Producto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Producto{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", detalle='" + getDetalle() + "'" +
            ", iva='" + getIva() + "'" +
            ", foto='" + getFoto() + "'" +
            ", fotoContentType='" + getFotoContentType() + "'" +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
