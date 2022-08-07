package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.InfoLegal} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.InfoLegalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /info-legals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class InfoLegalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nit;

    private IntegerFilter regimen;

    private StringFilter resolucionPos;

    private IntegerFilter prefijoPosInicial;

    private IntegerFilter prefijoPosFinal;

    private StringFilter resolucionFacElec;

    private IntegerFilter prefijoFacElecFinal;

    private StringFilter resolucionNomElec;

    private IntegerFilter estado;

    private InstantFilter fechaRegistro;

    private LongFilter empresaIdId;

    private LongFilter sucursalId;

    private LongFilter usuarioId;

    private Boolean distinct;

    public InfoLegalCriteria() {}

    public InfoLegalCriteria(InfoLegalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nit = other.nit == null ? null : other.nit.copy();
        this.regimen = other.regimen == null ? null : other.regimen.copy();
        this.resolucionPos = other.resolucionPos == null ? null : other.resolucionPos.copy();
        this.prefijoPosInicial = other.prefijoPosInicial == null ? null : other.prefijoPosInicial.copy();
        this.prefijoPosFinal = other.prefijoPosFinal == null ? null : other.prefijoPosFinal.copy();
        this.resolucionFacElec = other.resolucionFacElec == null ? null : other.resolucionFacElec.copy();
        this.prefijoFacElecFinal = other.prefijoFacElecFinal == null ? null : other.prefijoFacElecFinal.copy();
        this.resolucionNomElec = other.resolucionNomElec == null ? null : other.resolucionNomElec.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.fechaRegistro = other.fechaRegistro == null ? null : other.fechaRegistro.copy();
        this.empresaIdId = other.empresaIdId == null ? null : other.empresaIdId.copy();
        this.sucursalId = other.sucursalId == null ? null : other.sucursalId.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public InfoLegalCriteria copy() {
        return new InfoLegalCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNit() {
        return nit;
    }

    public StringFilter nit() {
        if (nit == null) {
            nit = new StringFilter();
        }
        return nit;
    }

    public void setNit(StringFilter nit) {
        this.nit = nit;
    }

    public IntegerFilter getRegimen() {
        return regimen;
    }

    public IntegerFilter regimen() {
        if (regimen == null) {
            regimen = new IntegerFilter();
        }
        return regimen;
    }

    public void setRegimen(IntegerFilter regimen) {
        this.regimen = regimen;
    }

    public StringFilter getResolucionPos() {
        return resolucionPos;
    }

    public StringFilter resolucionPos() {
        if (resolucionPos == null) {
            resolucionPos = new StringFilter();
        }
        return resolucionPos;
    }

    public void setResolucionPos(StringFilter resolucionPos) {
        this.resolucionPos = resolucionPos;
    }

    public IntegerFilter getPrefijoPosInicial() {
        return prefijoPosInicial;
    }

    public IntegerFilter prefijoPosInicial() {
        if (prefijoPosInicial == null) {
            prefijoPosInicial = new IntegerFilter();
        }
        return prefijoPosInicial;
    }

    public void setPrefijoPosInicial(IntegerFilter prefijoPosInicial) {
        this.prefijoPosInicial = prefijoPosInicial;
    }

    public IntegerFilter getPrefijoPosFinal() {
        return prefijoPosFinal;
    }

    public IntegerFilter prefijoPosFinal() {
        if (prefijoPosFinal == null) {
            prefijoPosFinal = new IntegerFilter();
        }
        return prefijoPosFinal;
    }

    public void setPrefijoPosFinal(IntegerFilter prefijoPosFinal) {
        this.prefijoPosFinal = prefijoPosFinal;
    }

    public StringFilter getResolucionFacElec() {
        return resolucionFacElec;
    }

    public StringFilter resolucionFacElec() {
        if (resolucionFacElec == null) {
            resolucionFacElec = new StringFilter();
        }
        return resolucionFacElec;
    }

    public void setResolucionFacElec(StringFilter resolucionFacElec) {
        this.resolucionFacElec = resolucionFacElec;
    }

    public IntegerFilter getPrefijoFacElecFinal() {
        return prefijoFacElecFinal;
    }

    public IntegerFilter prefijoFacElecFinal() {
        if (prefijoFacElecFinal == null) {
            prefijoFacElecFinal = new IntegerFilter();
        }
        return prefijoFacElecFinal;
    }

    public void setPrefijoFacElecFinal(IntegerFilter prefijoFacElecFinal) {
        this.prefijoFacElecFinal = prefijoFacElecFinal;
    }

    public StringFilter getResolucionNomElec() {
        return resolucionNomElec;
    }

    public StringFilter resolucionNomElec() {
        if (resolucionNomElec == null) {
            resolucionNomElec = new StringFilter();
        }
        return resolucionNomElec;
    }

    public void setResolucionNomElec(StringFilter resolucionNomElec) {
        this.resolucionNomElec = resolucionNomElec;
    }

    public IntegerFilter getEstado() {
        return estado;
    }

    public IntegerFilter estado() {
        if (estado == null) {
            estado = new IntegerFilter();
        }
        return estado;
    }

    public void setEstado(IntegerFilter estado) {
        this.estado = estado;
    }

    public InstantFilter getFechaRegistro() {
        return fechaRegistro;
    }

    public InstantFilter fechaRegistro() {
        if (fechaRegistro == null) {
            fechaRegistro = new InstantFilter();
        }
        return fechaRegistro;
    }

    public void setFechaRegistro(InstantFilter fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LongFilter getEmpresaIdId() {
        return empresaIdId;
    }

    public LongFilter empresaIdId() {
        if (empresaIdId == null) {
            empresaIdId = new LongFilter();
        }
        return empresaIdId;
    }

    public void setEmpresaIdId(LongFilter empresaIdId) {
        this.empresaIdId = empresaIdId;
    }

    public LongFilter getSucursalId() {
        return sucursalId;
    }

    public LongFilter sucursalId() {
        if (sucursalId == null) {
            sucursalId = new LongFilter();
        }
        return sucursalId;
    }

    public void setSucursalId(LongFilter sucursalId) {
        this.sucursalId = sucursalId;
    }

    public LongFilter getUsuarioId() {
        return usuarioId;
    }

    public LongFilter usuarioId() {
        if (usuarioId == null) {
            usuarioId = new LongFilter();
        }
        return usuarioId;
    }

    public void setUsuarioId(LongFilter usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InfoLegalCriteria that = (InfoLegalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nit, that.nit) &&
            Objects.equals(regimen, that.regimen) &&
            Objects.equals(resolucionPos, that.resolucionPos) &&
            Objects.equals(prefijoPosInicial, that.prefijoPosInicial) &&
            Objects.equals(prefijoPosFinal, that.prefijoPosFinal) &&
            Objects.equals(resolucionFacElec, that.resolucionFacElec) &&
            Objects.equals(prefijoFacElecFinal, that.prefijoFacElecFinal) &&
            Objects.equals(resolucionNomElec, that.resolucionNomElec) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(fechaRegistro, that.fechaRegistro) &&
            Objects.equals(empresaIdId, that.empresaIdId) &&
            Objects.equals(sucursalId, that.sucursalId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nit,
            regimen,
            resolucionPos,
            prefijoPosInicial,
            prefijoPosFinal,
            resolucionFacElec,
            prefijoFacElecFinal,
            resolucionNomElec,
            estado,
            fechaRegistro,
            empresaIdId,
            sucursalId,
            usuarioId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfoLegalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nit != null ? "nit=" + nit + ", " : "") +
            (regimen != null ? "regimen=" + regimen + ", " : "") +
            (resolucionPos != null ? "resolucionPos=" + resolucionPos + ", " : "") +
            (prefijoPosInicial != null ? "prefijoPosInicial=" + prefijoPosInicial + ", " : "") +
            (prefijoPosFinal != null ? "prefijoPosFinal=" + prefijoPosFinal + ", " : "") +
            (resolucionFacElec != null ? "resolucionFacElec=" + resolucionFacElec + ", " : "") +
            (prefijoFacElecFinal != null ? "prefijoFacElecFinal=" + prefijoFacElecFinal + ", " : "") +
            (resolucionNomElec != null ? "resolucionNomElec=" + resolucionNomElec + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (fechaRegistro != null ? "fechaRegistro=" + fechaRegistro + ", " : "") +
            (empresaIdId != null ? "empresaIdId=" + empresaIdId + ", " : "") +
            (sucursalId != null ? "sucursalId=" + sucursalId + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
