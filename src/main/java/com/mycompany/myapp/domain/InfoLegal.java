package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A InfoLegal.
 */
@Entity
@Table(name = "info_legal")
public class InfoLegal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nit")
    private String nit;

    @Column(name = "regimen")
    private String regimen;

    @Column(name = "prefijo_fe")
    private String prefijoFE;

    @Column(name = "prefijo_pos")
    private String prefijoPOS;

    @Column(name = "prefijo_nom")
    private String prefijoNOM;

    @Column(name = "resolucion_pos")
    private String resolucionPos;

    @Column(name = "prefijo_pos_inicial")
    private Integer prefijoPosInicial;

    @Column(name = "prefijo_pos_final")
    private Integer prefijoPosFinal;

    @Column(name = "resolucion_fac_elec")
    private String resolucionFacElec;

    @Column(name = "prefijo_fac_elec_inicial")
    private Integer prefijoFacElecInicial;

    @Column(name = "prefijo_fac_elec_final")
    private Integer prefijoFacElecFinal;

    @Column(name = "resolucion_nom_elec")
    private String resolucionNomElec;

    @Column(name = "prefijo_nom_elec_inicial")
    private Integer prefijoNomElecInicial;

    @Column(name = "prefijo_nom_elec_final")
    private Integer prefijoNomElecFinal;

    @Column(name = "estado")
    private Integer estado;

    @Column(name = "fecha_registro")
    private Instant fechaRegistro;

    @ManyToMany
    @JoinTable(
        name = "rel_info_legal__empresa_id",
        joinColumns = @JoinColumn(name = "info_legal_id"),
        inverseJoinColumns = @JoinColumn(name = "empresa_id_id")
    )
    @JsonIgnoreProperties(
        value = { "user", "componentes", "rols", "sucursals", "sucursalIds", "infoLegalIds", "usuarioIds" },
        allowSetters = true
    )
    private Set<Empresa> empresaIds = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_info_legal__sucursal",
        joinColumns = @JoinColumn(name = "info_legal_id"),
        inverseJoinColumns = @JoinColumn(name = "sucursal_id")
    )
    @JsonIgnoreProperties(value = { "oficinas", "inventarios", "empresa", "infoLegals", "usuarios", "empresaIds" }, allowSetters = true)
    private Set<Sucursal> sucursals = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "rol", "infoLegals", "sucursals", "empresaIds", "bodegas", "oficinas" }, allowSetters = true)
    private Usuario usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InfoLegal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNit() {
        return this.nit;
    }

    public InfoLegal nit(String nit) {
        this.setNit(nit);
        return this;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getRegimen() {
        return this.regimen;
    }

    public InfoLegal regimen(String regimen) {
        this.setRegimen(regimen);
        return this;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    public String getPrefijoFE() {
        return this.prefijoFE;
    }

    public InfoLegal prefijoFE(String prefijoFE) {
        this.setPrefijoFE(prefijoFE);
        return this;
    }

    public void setPrefijoFE(String prefijoFE) {
        this.prefijoFE = prefijoFE;
    }

    public String getPrefijoPOS() {
        return this.prefijoPOS;
    }

    public InfoLegal prefijoPOS(String prefijoPOS) {
        this.setPrefijoPOS(prefijoPOS);
        return this;
    }

    public void setPrefijoPOS(String prefijoPOS) {
        this.prefijoPOS = prefijoPOS;
    }

    public String getPrefijoNOM() {
        return this.prefijoNOM;
    }

    public InfoLegal prefijoNOM(String prefijoNOM) {
        this.setPrefijoNOM(prefijoNOM);
        return this;
    }

    public void setPrefijoNOM(String prefijoNOM) {
        this.prefijoNOM = prefijoNOM;
    }

    public String getResolucionPos() {
        return this.resolucionPos;
    }

    public InfoLegal resolucionPos(String resolucionPos) {
        this.setResolucionPos(resolucionPos);
        return this;
    }

    public void setResolucionPos(String resolucionPos) {
        this.resolucionPos = resolucionPos;
    }

    public Integer getPrefijoPosInicial() {
        return this.prefijoPosInicial;
    }

    public InfoLegal prefijoPosInicial(Integer prefijoPosInicial) {
        this.setPrefijoPosInicial(prefijoPosInicial);
        return this;
    }

    public void setPrefijoPosInicial(Integer prefijoPosInicial) {
        this.prefijoPosInicial = prefijoPosInicial;
    }

    public Integer getPrefijoPosFinal() {
        return this.prefijoPosFinal;
    }

    public InfoLegal prefijoPosFinal(Integer prefijoPosFinal) {
        this.setPrefijoPosFinal(prefijoPosFinal);
        return this;
    }

    public void setPrefijoPosFinal(Integer prefijoPosFinal) {
        this.prefijoPosFinal = prefijoPosFinal;
    }

    public String getResolucionFacElec() {
        return this.resolucionFacElec;
    }

    public InfoLegal resolucionFacElec(String resolucionFacElec) {
        this.setResolucionFacElec(resolucionFacElec);
        return this;
    }

    public void setResolucionFacElec(String resolucionFacElec) {
        this.resolucionFacElec = resolucionFacElec;
    }

    public Integer getPrefijoFacElecInicial() {
        return this.prefijoFacElecInicial;
    }

    public InfoLegal prefijoFacElecInicial(Integer prefijoFacElecInicial) {
        this.setPrefijoFacElecInicial(prefijoFacElecInicial);
        return this;
    }

    public void setPrefijoFacElecInicial(Integer prefijoFacElecInicial) {
        this.prefijoFacElecInicial = prefijoFacElecInicial;
    }

    public Integer getPrefijoFacElecFinal() {
        return this.prefijoFacElecFinal;
    }

    public InfoLegal prefijoFacElecFinal(Integer prefijoFacElecFinal) {
        this.setPrefijoFacElecFinal(prefijoFacElecFinal);
        return this;
    }

    public void setPrefijoFacElecFinal(Integer prefijoFacElecFinal) {
        this.prefijoFacElecFinal = prefijoFacElecFinal;
    }

    public String getResolucionNomElec() {
        return this.resolucionNomElec;
    }

    public InfoLegal resolucionNomElec(String resolucionNomElec) {
        this.setResolucionNomElec(resolucionNomElec);
        return this;
    }

    public void setResolucionNomElec(String resolucionNomElec) {
        this.resolucionNomElec = resolucionNomElec;
    }

    public Integer getPrefijoNomElecInicial() {
        return this.prefijoNomElecInicial;
    }

    public InfoLegal prefijoNomElecInicial(Integer prefijoNomElecInicial) {
        this.setPrefijoNomElecInicial(prefijoNomElecInicial);
        return this;
    }

    public void setPrefijoNomElecInicial(Integer prefijoNomElecInicial) {
        this.prefijoNomElecInicial = prefijoNomElecInicial;
    }

    public Integer getPrefijoNomElecFinal() {
        return this.prefijoNomElecFinal;
    }

    public InfoLegal prefijoNomElecFinal(Integer prefijoNomElecFinal) {
        this.setPrefijoNomElecFinal(prefijoNomElecFinal);
        return this;
    }

    public void setPrefijoNomElecFinal(Integer prefijoNomElecFinal) {
        this.prefijoNomElecFinal = prefijoNomElecFinal;
    }

    public Integer getEstado() {
        return this.estado;
    }

    public InfoLegal estado(Integer estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public InfoLegal fechaRegistro(Instant fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Set<Empresa> getEmpresaIds() {
        return this.empresaIds;
    }

    public void setEmpresaIds(Set<Empresa> empresas) {
        this.empresaIds = empresas;
    }

    public InfoLegal empresaIds(Set<Empresa> empresas) {
        this.setEmpresaIds(empresas);
        return this;
    }

    public InfoLegal addEmpresaId(Empresa empresa) {
        this.empresaIds.add(empresa);
        empresa.getInfoLegalIds().add(this);
        return this;
    }

    public InfoLegal removeEmpresaId(Empresa empresa) {
        this.empresaIds.remove(empresa);
        empresa.getInfoLegalIds().remove(this);
        return this;
    }

    public Set<Sucursal> getSucursals() {
        return this.sucursals;
    }

    public void setSucursals(Set<Sucursal> sucursals) {
        this.sucursals = sucursals;
    }

    public InfoLegal sucursals(Set<Sucursal> sucursals) {
        this.setSucursals(sucursals);
        return this;
    }

    public InfoLegal addSucursal(Sucursal sucursal) {
        this.sucursals.add(sucursal);
        sucursal.getInfoLegals().add(this);
        return this;
    }

    public InfoLegal removeSucursal(Sucursal sucursal) {
        this.sucursals.remove(sucursal);
        sucursal.getInfoLegals().remove(this);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public InfoLegal usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InfoLegal)) {
            return false;
        }
        return id != null && id.equals(((InfoLegal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfoLegal{" +
            "id=" + getId() +
            ", nit='" + getNit() + "'" +
            ", regimen='" + getRegimen() + "'" +
            ", prefijoFE='" + getPrefijoFE() + "'" +
            ", prefijoPOS='" + getPrefijoPOS() + "'" +
            ", prefijoNOM='" + getPrefijoNOM() + "'" +
            ", resolucionPos='" + getResolucionPos() + "'" +
            ", prefijoPosInicial=" + getPrefijoPosInicial() +
            ", prefijoPosFinal=" + getPrefijoPosFinal() +
            ", resolucionFacElec='" + getResolucionFacElec() + "'" +
            ", prefijoFacElecInicial=" + getPrefijoFacElecInicial() +
            ", prefijoFacElecFinal=" + getPrefijoFacElecFinal() +
            ", resolucionNomElec='" + getResolucionNomElec() + "'" +
            ", prefijoNomElecInicial=" + getPrefijoNomElecInicial() +
            ", prefijoNomElecFinal=" + getPrefijoNomElecFinal() +
            ", estado=" + getEstado() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
