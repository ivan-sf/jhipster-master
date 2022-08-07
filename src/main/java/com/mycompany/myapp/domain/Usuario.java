package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "primer_nombre")
    private String primerNombre;

    @Column(name = "segundo_nombre")
    private String segundoNombre;

    @Column(name = "primer_apellido")
    private String primerApellido;

    @Column(name = "segundo_apellido")
    private String segundoApellido;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "documento")
    private String documento;

    @Column(name = "documento_dv")
    private String documentoDV;

    @Column(name = "edad")
    private Instant edad;

    @Column(name = "indicativo")
    private String indicativo;

    @Column(name = "celular")
    private String celular;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "direccion_gps")
    private String direccionGps;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "foto_content_type")
    private String fotoContentType;

    @Column(name = "fecha_registro")
    private Instant fechaRegistro;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @JsonIgnoreProperties(value = { "empresa" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Rol rol;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnoreProperties(value = { "empresaIds", "sucursals", "usuario" }, allowSetters = true)
    private Set<InfoLegal> infoLegals = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_usuario__sucursal",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "sucursal_id")
    )
    @JsonIgnoreProperties(value = { "oficinas", "inventarios", "empresa", "infoLegals", "usuarios", "empresaIds" }, allowSetters = true)
    private Set<Sucursal> sucursals = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_usuario__empresa",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "empresa_id")
    )
    @JsonIgnoreProperties(
        value = { "componentes", "rols", "usuarios", "sucursals", "sucursalIds", "infoLegalIds", "usuarioIds" },
        allowSetters = true
    )
    private Set<Empresa> empresas = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "componentes", "rols", "usuarios", "sucursals", "sucursalIds", "infoLegalIds", "usuarioIds" },
        allowSetters = true
    )
    private Empresa empresaId;

    @ManyToMany(mappedBy = "usuarios")
    @JsonIgnoreProperties(value = { "productos", "usuarios", "inventario" }, allowSetters = true)
    private Set<Bodega> bodegas = new HashSet<>();

    @ManyToMany(mappedBy = "usuarios")
    @JsonIgnoreProperties(value = { "productos", "usuarios", "sucursal" }, allowSetters = true)
    private Set<Oficina> oficinas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Usuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrimerNombre() {
        return this.primerNombre;
    }

    public Usuario primerNombre(String primerNombre) {
        this.setPrimerNombre(primerNombre);
        return this;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return this.segundoNombre;
    }

    public Usuario segundoNombre(String segundoNombre) {
        this.setSegundoNombre(segundoNombre);
        return this;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return this.primerApellido;
    }

    public Usuario primerApellido(String primerApellido) {
        this.setPrimerApellido(primerApellido);
        return this;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return this.segundoApellido;
    }

    public Usuario segundoApellido(String segundoApellido) {
        this.setSegundoApellido(segundoApellido);
        return this;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getTipoDocumento() {
        return this.tipoDocumento;
    }

    public Usuario tipoDocumento(String tipoDocumento) {
        this.setTipoDocumento(tipoDocumento);
        return this;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return this.documento;
    }

    public Usuario documento(String documento) {
        this.setDocumento(documento);
        return this;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDocumentoDV() {
        return this.documentoDV;
    }

    public Usuario documentoDV(String documentoDV) {
        this.setDocumentoDV(documentoDV);
        return this;
    }

    public void setDocumentoDV(String documentoDV) {
        this.documentoDV = documentoDV;
    }

    public Instant getEdad() {
        return this.edad;
    }

    public Usuario edad(Instant edad) {
        this.setEdad(edad);
        return this;
    }

    public void setEdad(Instant edad) {
        this.edad = edad;
    }

    public String getIndicativo() {
        return this.indicativo;
    }

    public Usuario indicativo(String indicativo) {
        this.setIndicativo(indicativo);
        return this;
    }

    public void setIndicativo(String indicativo) {
        this.indicativo = indicativo;
    }

    public String getCelular() {
        return this.celular;
    }

    public Usuario celular(String celular) {
        this.setCelular(celular);
        return this;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Usuario direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccionGps() {
        return this.direccionGps;
    }

    public Usuario direccionGps(String direccionGps) {
        this.setDireccionGps(direccionGps);
        return this;
    }

    public void setDireccionGps(String direccionGps) {
        this.direccionGps = direccionGps;
    }

    public byte[] getFoto() {
        return this.foto;
    }

    public Usuario foto(byte[] foto) {
        this.setFoto(foto);
        return this;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return this.fotoContentType;
    }

    public Usuario fotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
        return this;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Usuario fechaRegistro(Instant fechaRegistro) {
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

    public Usuario user(User user) {
        this.setUser(user);
        return this;
    }

    public Rol getRol() {
        return this.rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Usuario rol(Rol rol) {
        this.setRol(rol);
        return this;
    }

    public Set<InfoLegal> getInfoLegals() {
        return this.infoLegals;
    }

    public void setInfoLegals(Set<InfoLegal> infoLegals) {
        if (this.infoLegals != null) {
            this.infoLegals.forEach(i -> i.setUsuario(null));
        }
        if (infoLegals != null) {
            infoLegals.forEach(i -> i.setUsuario(this));
        }
        this.infoLegals = infoLegals;
    }

    public Usuario infoLegals(Set<InfoLegal> infoLegals) {
        this.setInfoLegals(infoLegals);
        return this;
    }

    public Usuario addInfoLegal(InfoLegal infoLegal) {
        this.infoLegals.add(infoLegal);
        infoLegal.setUsuario(this);
        return this;
    }

    public Usuario removeInfoLegal(InfoLegal infoLegal) {
        this.infoLegals.remove(infoLegal);
        infoLegal.setUsuario(null);
        return this;
    }

    public Set<Sucursal> getSucursals() {
        return this.sucursals;
    }

    public void setSucursals(Set<Sucursal> sucursals) {
        this.sucursals = sucursals;
    }

    public Usuario sucursals(Set<Sucursal> sucursals) {
        this.setSucursals(sucursals);
        return this;
    }

    public Usuario addSucursal(Sucursal sucursal) {
        this.sucursals.add(sucursal);
        sucursal.getUsuarios().add(this);
        return this;
    }

    public Usuario removeSucursal(Sucursal sucursal) {
        this.sucursals.remove(sucursal);
        sucursal.getUsuarios().remove(this);
        return this;
    }

    public Set<Empresa> getEmpresas() {
        return this.empresas;
    }

    public void setEmpresas(Set<Empresa> empresas) {
        this.empresas = empresas;
    }

    public Usuario empresas(Set<Empresa> empresas) {
        this.setEmpresas(empresas);
        return this;
    }

    public Usuario addEmpresa(Empresa empresa) {
        this.empresas.add(empresa);
        empresa.getUsuarioIds().add(this);
        return this;
    }

    public Usuario removeEmpresa(Empresa empresa) {
        this.empresas.remove(empresa);
        empresa.getUsuarioIds().remove(this);
        return this;
    }

    public Empresa getEmpresaId() {
        return this.empresaId;
    }

    public void setEmpresaId(Empresa empresa) {
        this.empresaId = empresa;
    }

    public Usuario empresaId(Empresa empresa) {
        this.setEmpresaId(empresa);
        return this;
    }

    public Set<Bodega> getBodegas() {
        return this.bodegas;
    }

    public void setBodegas(Set<Bodega> bodegas) {
        if (this.bodegas != null) {
            this.bodegas.forEach(i -> i.removeUsuario(this));
        }
        if (bodegas != null) {
            bodegas.forEach(i -> i.addUsuario(this));
        }
        this.bodegas = bodegas;
    }

    public Usuario bodegas(Set<Bodega> bodegas) {
        this.setBodegas(bodegas);
        return this;
    }

    public Usuario addBodega(Bodega bodega) {
        this.bodegas.add(bodega);
        bodega.getUsuarios().add(this);
        return this;
    }

    public Usuario removeBodega(Bodega bodega) {
        this.bodegas.remove(bodega);
        bodega.getUsuarios().remove(this);
        return this;
    }

    public Set<Oficina> getOficinas() {
        return this.oficinas;
    }

    public void setOficinas(Set<Oficina> oficinas) {
        if (this.oficinas != null) {
            this.oficinas.forEach(i -> i.removeUsuario(this));
        }
        if (oficinas != null) {
            oficinas.forEach(i -> i.addUsuario(this));
        }
        this.oficinas = oficinas;
    }

    public Usuario oficinas(Set<Oficina> oficinas) {
        this.setOficinas(oficinas);
        return this;
    }

    public Usuario addOficina(Oficina oficina) {
        this.oficinas.add(oficina);
        oficina.getUsuarios().add(this);
        return this;
    }

    public Usuario removeOficina(Oficina oficina) {
        this.oficinas.remove(oficina);
        oficina.getUsuarios().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        return id != null && id.equals(((Usuario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + getId() +
            ", primerNombre='" + getPrimerNombre() + "'" +
            ", segundoNombre='" + getSegundoNombre() + "'" +
            ", primerApellido='" + getPrimerApellido() + "'" +
            ", segundoApellido='" + getSegundoApellido() + "'" +
            ", tipoDocumento='" + getTipoDocumento() + "'" +
            ", documento='" + getDocumento() + "'" +
            ", documentoDV='" + getDocumentoDV() + "'" +
            ", edad='" + getEdad() + "'" +
            ", indicativo='" + getIndicativo() + "'" +
            ", celular='" + getCelular() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", direccionGps='" + getDireccionGps() + "'" +
            ", foto='" + getFoto() + "'" +
            ", fotoContentType='" + getFotoContentType() + "'" +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
