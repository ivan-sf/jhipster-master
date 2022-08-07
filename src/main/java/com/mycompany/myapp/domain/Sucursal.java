package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Type;

/**
 * A Sucursal.
 */
@Entity
@Table(name = "sucursal")
public class Sucursal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "nit")
    private String nit;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "detalle")
    private String detalle;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "direccion_gps")
    private String direccionGPS;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @Column(name = "estado")
    private Integer estado;

    @Column(name = "fecha_registro")
    private Instant fechaRegistro;

    @OneToMany(mappedBy = "sucursal")
    @JsonIgnoreProperties(value = { "productos", "usuarios", "sucursal" }, allowSetters = true)
    private Set<Oficina> oficinas = new HashSet<>();

    @OneToMany(mappedBy = "sucursal")
    @JsonIgnoreProperties(value = { "bodegas", "sucursal" }, allowSetters = true)
    private Set<Inventario> inventarios = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "componentes", "rols", "usuarios", "sucursals", "sucursalIds", "infoLegalIds", "usuarioIds" },
        allowSetters = true
    )
    private Empresa empresa;

    @ManyToMany(mappedBy = "sucursals")
    @JsonIgnoreProperties(value = { "empresaIds", "sucursals", "usuario" }, allowSetters = true)
    private Set<InfoLegal> infoLegals = new HashSet<>();

    @ManyToMany(mappedBy = "sucursals")
    @JsonIgnoreProperties(
        value = { "user", "rol", "infoLegals", "sucursals", "empresas", "empresaId", "bodegas", "oficinas" },
        allowSetters = true
    )
    private Set<Usuario> usuarios = new HashSet<>();

    @ManyToMany(mappedBy = "sucursalIds")
    @JsonIgnoreProperties(
        value = { "componentes", "rols", "usuarios", "sucursals", "sucursalIds", "infoLegalIds", "usuarioIds" },
        allowSetters = true
    )
    private Set<Empresa> empresaIds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sucursal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Sucursal nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return this.nit;
    }

    public Sucursal nit(String nit) {
        this.setNit(nit);
        return this;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDetalle() {
        return this.detalle;
    }

    public Sucursal detalle(String detalle) {
        this.setDetalle(detalle);
        return this;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Sucursal direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccionGPS() {
        return this.direccionGPS;
    }

    public Sucursal direccionGPS(String direccionGPS) {
        this.setDireccionGPS(direccionGPS);
        return this;
    }

    public void setDireccionGPS(String direccionGPS) {
        this.direccionGPS = direccionGPS;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public Sucursal logo(byte[] logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public Sucursal logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public Integer getEstado() {
        return this.estado;
    }

    public Sucursal estado(Integer estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Sucursal fechaRegistro(Instant fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Set<Oficina> getOficinas() {
        return this.oficinas;
    }

    public void setOficinas(Set<Oficina> oficinas) {
        if (this.oficinas != null) {
            this.oficinas.forEach(i -> i.setSucursal(null));
        }
        if (oficinas != null) {
            oficinas.forEach(i -> i.setSucursal(this));
        }
        this.oficinas = oficinas;
    }

    public Sucursal oficinas(Set<Oficina> oficinas) {
        this.setOficinas(oficinas);
        return this;
    }

    public Sucursal addOficina(Oficina oficina) {
        this.oficinas.add(oficina);
        oficina.setSucursal(this);
        return this;
    }

    public Sucursal removeOficina(Oficina oficina) {
        this.oficinas.remove(oficina);
        oficina.setSucursal(null);
        return this;
    }

    public Set<Inventario> getInventarios() {
        return this.inventarios;
    }

    public void setInventarios(Set<Inventario> inventarios) {
        if (this.inventarios != null) {
            this.inventarios.forEach(i -> i.setSucursal(null));
        }
        if (inventarios != null) {
            inventarios.forEach(i -> i.setSucursal(this));
        }
        this.inventarios = inventarios;
    }

    public Sucursal inventarios(Set<Inventario> inventarios) {
        this.setInventarios(inventarios);
        return this;
    }

    public Sucursal addInventario(Inventario inventario) {
        this.inventarios.add(inventario);
        inventario.setSucursal(this);
        return this;
    }

    public Sucursal removeInventario(Inventario inventario) {
        this.inventarios.remove(inventario);
        inventario.setSucursal(null);
        return this;
    }

    public Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Sucursal empresa(Empresa empresa) {
        this.setEmpresa(empresa);
        return this;
    }

    public Set<InfoLegal> getInfoLegals() {
        return this.infoLegals;
    }

    public void setInfoLegals(Set<InfoLegal> infoLegals) {
        if (this.infoLegals != null) {
            this.infoLegals.forEach(i -> i.removeSucursal(this));
        }
        if (infoLegals != null) {
            infoLegals.forEach(i -> i.addSucursal(this));
        }
        this.infoLegals = infoLegals;
    }

    public Sucursal infoLegals(Set<InfoLegal> infoLegals) {
        this.setInfoLegals(infoLegals);
        return this;
    }

    public Sucursal addInfoLegal(InfoLegal infoLegal) {
        this.infoLegals.add(infoLegal);
        infoLegal.getSucursals().add(this);
        return this;
    }

    public Sucursal removeInfoLegal(InfoLegal infoLegal) {
        this.infoLegals.remove(infoLegal);
        infoLegal.getSucursals().remove(this);
        return this;
    }

    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        if (this.usuarios != null) {
            this.usuarios.forEach(i -> i.removeSucursal(this));
        }
        if (usuarios != null) {
            usuarios.forEach(i -> i.addSucursal(this));
        }
        this.usuarios = usuarios;
    }

    public Sucursal usuarios(Set<Usuario> usuarios) {
        this.setUsuarios(usuarios);
        return this;
    }

    public Sucursal addUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        usuario.getSucursals().add(this);
        return this;
    }

    public Sucursal removeUsuario(Usuario usuario) {
        this.usuarios.remove(usuario);
        usuario.getSucursals().remove(this);
        return this;
    }

    public Set<Empresa> getEmpresaIds() {
        return this.empresaIds;
    }

    public void setEmpresaIds(Set<Empresa> empresas) {
        if (this.empresaIds != null) {
            this.empresaIds.forEach(i -> i.removeSucursalId(this));
        }
        if (empresas != null) {
            empresas.forEach(i -> i.addSucursalId(this));
        }
        this.empresaIds = empresas;
    }

    public Sucursal empresaIds(Set<Empresa> empresas) {
        this.setEmpresaIds(empresas);
        return this;
    }

    public Sucursal addEmpresaId(Empresa empresa) {
        this.empresaIds.add(empresa);
        empresa.getSucursalIds().add(this);
        return this;
    }

    public Sucursal removeEmpresaId(Empresa empresa) {
        this.empresaIds.remove(empresa);
        empresa.getSucursalIds().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sucursal)) {
            return false;
        }
        return id != null && id.equals(((Sucursal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sucursal{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", nit='" + getNit() + "'" +
            ", detalle='" + getDetalle() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", direccionGPS='" + getDireccionGPS() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            ", estado=" + getEstado() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
