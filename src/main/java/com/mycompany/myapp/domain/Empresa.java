package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Empresa.
 */
@Entity
@Table(name = "empresa")
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "direccion_gps")
    private String direccionGPS;

    @Column(name = "email")
    private String email;

    @Column(name = "celular")
    private String celular;

    @Column(name = "indicativo")
    private String indicativo;

    @Column(name = "estado")
    private Integer estado;

    @Column(name = "fecha_registro")
    private Instant fechaRegistro;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "empresa")
    @JsonIgnoreProperties(value = { "empresa" }, allowSetters = true)
    private Set<Componente> componentes = new HashSet<>();

    @OneToMany(mappedBy = "empresa")
    @JsonIgnoreProperties(value = { "empresa" }, allowSetters = true)
    private Set<Rol> rols = new HashSet<>();

    @OneToMany(mappedBy = "empresa")
    @JsonIgnoreProperties(value = { "oficinas", "inventarios", "empresa", "infoLegals", "usuarios", "empresaIds" }, allowSetters = true)
    private Set<Sucursal> sucursals = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_empresa__sucursal_id",
        joinColumns = @JoinColumn(name = "empresa_id"),
        inverseJoinColumns = @JoinColumn(name = "sucursal_id_id")
    )
    @JsonIgnoreProperties(value = { "oficinas", "inventarios", "empresa", "infoLegals", "usuarios", "empresaIds" }, allowSetters = true)
    private Set<Sucursal> sucursalIds = new HashSet<>();

    @ManyToMany(mappedBy = "empresaIds")
    @JsonIgnoreProperties(value = { "empresaIds", "sucursals", "usuario" }, allowSetters = true)
    private Set<InfoLegal> infoLegalIds = new HashSet<>();

    @ManyToMany(mappedBy = "empresaIds")
    @JsonIgnoreProperties(value = { "user", "rol", "infoLegals", "sucursals", "empresaIds", "bodegas", "oficinas" }, allowSetters = true)
    private Set<Usuario> usuarioIds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Empresa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Empresa nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Empresa direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccionGPS() {
        return this.direccionGPS;
    }

    public Empresa direccionGPS(String direccionGPS) {
        this.setDireccionGPS(direccionGPS);
        return this;
    }

    public void setDireccionGPS(String direccionGPS) {
        this.direccionGPS = direccionGPS;
    }

    public String getEmail() {
        return this.email;
    }

    public Empresa email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return this.celular;
    }

    public Empresa celular(String celular) {
        this.setCelular(celular);
        return this;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getIndicativo() {
        return this.indicativo;
    }

    public Empresa indicativo(String indicativo) {
        this.setIndicativo(indicativo);
        return this;
    }

    public void setIndicativo(String indicativo) {
        this.indicativo = indicativo;
    }

    public Integer getEstado() {
        return this.estado;
    }

    public Empresa estado(Integer estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Empresa fechaRegistro(Instant fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Empresa user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Componente> getComponentes() {
        return this.componentes;
    }

    public void setComponentes(Set<Componente> componentes) {
        if (this.componentes != null) {
            this.componentes.forEach(i -> i.setEmpresa(null));
        }
        if (componentes != null) {
            componentes.forEach(i -> i.setEmpresa(this));
        }
        this.componentes = componentes;
    }

    public Empresa componentes(Set<Componente> componentes) {
        this.setComponentes(componentes);
        return this;
    }

    public Empresa addComponente(Componente componente) {
        this.componentes.add(componente);
        componente.setEmpresa(this);
        return this;
    }

    public Empresa removeComponente(Componente componente) {
        this.componentes.remove(componente);
        componente.setEmpresa(null);
        return this;
    }

    public Set<Rol> getRols() {
        return this.rols;
    }

    public void setRols(Set<Rol> rols) {
        if (this.rols != null) {
            this.rols.forEach(i -> i.setEmpresa(null));
        }
        if (rols != null) {
            rols.forEach(i -> i.setEmpresa(this));
        }
        this.rols = rols;
    }

    public Empresa rols(Set<Rol> rols) {
        this.setRols(rols);
        return this;
    }

    public Empresa addRol(Rol rol) {
        this.rols.add(rol);
        rol.setEmpresa(this);
        return this;
    }

    public Empresa removeRol(Rol rol) {
        this.rols.remove(rol);
        rol.setEmpresa(null);
        return this;
    }

    public Set<Sucursal> getSucursals() {
        return this.sucursals;
    }

    public void setSucursals(Set<Sucursal> sucursals) {
        if (this.sucursals != null) {
            this.sucursals.forEach(i -> i.setEmpresa(null));
        }
        if (sucursals != null) {
            sucursals.forEach(i -> i.setEmpresa(this));
        }
        this.sucursals = sucursals;
    }

    public Empresa sucursals(Set<Sucursal> sucursals) {
        this.setSucursals(sucursals);
        return this;
    }

    public Empresa addSucursal(Sucursal sucursal) {
        this.sucursals.add(sucursal);
        sucursal.setEmpresa(this);
        return this;
    }

    public Empresa removeSucursal(Sucursal sucursal) {
        this.sucursals.remove(sucursal);
        sucursal.setEmpresa(null);
        return this;
    }

    public Set<Sucursal> getSucursalIds() {
        return this.sucursalIds;
    }

    public void setSucursalIds(Set<Sucursal> sucursals) {
        this.sucursalIds = sucursals;
    }

    public Empresa sucursalIds(Set<Sucursal> sucursals) {
        this.setSucursalIds(sucursals);
        return this;
    }

    public Empresa addSucursalId(Sucursal sucursal) {
        this.sucursalIds.add(sucursal);
        sucursal.getEmpresaIds().add(this);
        return this;
    }

    public Empresa removeSucursalId(Sucursal sucursal) {
        this.sucursalIds.remove(sucursal);
        sucursal.getEmpresaIds().remove(this);
        return this;
    }

    public Set<InfoLegal> getInfoLegalIds() {
        return this.infoLegalIds;
    }

    public void setInfoLegalIds(Set<InfoLegal> infoLegals) {
        if (this.infoLegalIds != null) {
            this.infoLegalIds.forEach(i -> i.removeEmpresaId(this));
        }
        if (infoLegals != null) {
            infoLegals.forEach(i -> i.addEmpresaId(this));
        }
        this.infoLegalIds = infoLegals;
    }

    public Empresa infoLegalIds(Set<InfoLegal> infoLegals) {
        this.setInfoLegalIds(infoLegals);
        return this;
    }

    public Empresa addInfoLegalId(InfoLegal infoLegal) {
        this.infoLegalIds.add(infoLegal);
        infoLegal.getEmpresaIds().add(this);
        return this;
    }

    public Empresa removeInfoLegalId(InfoLegal infoLegal) {
        this.infoLegalIds.remove(infoLegal);
        infoLegal.getEmpresaIds().remove(this);
        return this;
    }

    public Set<Usuario> getUsuarioIds() {
        return this.usuarioIds;
    }

    public void setUsuarioIds(Set<Usuario> usuarios) {
        if (this.usuarioIds != null) {
            this.usuarioIds.forEach(i -> i.removeEmpresaId(this));
        }
        if (usuarios != null) {
            usuarios.forEach(i -> i.addEmpresaId(this));
        }
        this.usuarioIds = usuarios;
    }

    public Empresa usuarioIds(Set<Usuario> usuarios) {
        this.setUsuarioIds(usuarios);
        return this;
    }

    public Empresa addUsuarioId(Usuario usuario) {
        this.usuarioIds.add(usuario);
        usuario.getEmpresaIds().add(this);
        return this;
    }

    public Empresa removeUsuarioId(Usuario usuario) {
        this.usuarioIds.remove(usuario);
        usuario.getEmpresaIds().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Empresa)) {
            return false;
        }
        return id != null && id.equals(((Empresa) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Empresa{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", direccionGPS='" + getDireccionGPS() + "'" +
            ", email='" + getEmail() + "'" +
            ", celular='" + getCelular() + "'" +
            ", indicativo='" + getIndicativo() + "'" +
            ", estado=" + getEstado() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
