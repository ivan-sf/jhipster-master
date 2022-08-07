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
 * Criteria class for the {@link com.mycompany.myapp.domain.Usuario} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.UsuarioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /usuarios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class UsuarioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter primerNombre;

    private StringFilter segundoNombre;

    private StringFilter primerApellido;

    private StringFilter segundoApellido;

    private StringFilter tipoDocumento;

    private StringFilter documento;

    private StringFilter documentoDV;

    private InstantFilter edad;

    private StringFilter indicativo;

    private StringFilter celular;

    private StringFilter direccion;

    private StringFilter direccionGps;

    private InstantFilter fechaRegistro;

    private LongFilter userId;

    private LongFilter rolId;

    private LongFilter infoLegalId;

    private LongFilter sucursalId;

    private LongFilter empresaId;

    private LongFilter empresaIdId;

    private LongFilter bodegaId;

    private LongFilter oficinaId;

    private Boolean distinct;

    public UsuarioCriteria() {}

    public UsuarioCriteria(UsuarioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.primerNombre = other.primerNombre == null ? null : other.primerNombre.copy();
        this.segundoNombre = other.segundoNombre == null ? null : other.segundoNombre.copy();
        this.primerApellido = other.primerApellido == null ? null : other.primerApellido.copy();
        this.segundoApellido = other.segundoApellido == null ? null : other.segundoApellido.copy();
        this.tipoDocumento = other.tipoDocumento == null ? null : other.tipoDocumento.copy();
        this.documento = other.documento == null ? null : other.documento.copy();
        this.documentoDV = other.documentoDV == null ? null : other.documentoDV.copy();
        this.edad = other.edad == null ? null : other.edad.copy();
        this.indicativo = other.indicativo == null ? null : other.indicativo.copy();
        this.celular = other.celular == null ? null : other.celular.copy();
        this.direccion = other.direccion == null ? null : other.direccion.copy();
        this.direccionGps = other.direccionGps == null ? null : other.direccionGps.copy();
        this.fechaRegistro = other.fechaRegistro == null ? null : other.fechaRegistro.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.rolId = other.rolId == null ? null : other.rolId.copy();
        this.infoLegalId = other.infoLegalId == null ? null : other.infoLegalId.copy();
        this.sucursalId = other.sucursalId == null ? null : other.sucursalId.copy();
        this.empresaId = other.empresaId == null ? null : other.empresaId.copy();
        this.empresaIdId = other.empresaIdId == null ? null : other.empresaIdId.copy();
        this.bodegaId = other.bodegaId == null ? null : other.bodegaId.copy();
        this.oficinaId = other.oficinaId == null ? null : other.oficinaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UsuarioCriteria copy() {
        return new UsuarioCriteria(this);
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

    public StringFilter getPrimerNombre() {
        return primerNombre;
    }

    public StringFilter primerNombre() {
        if (primerNombre == null) {
            primerNombre = new StringFilter();
        }
        return primerNombre;
    }

    public void setPrimerNombre(StringFilter primerNombre) {
        this.primerNombre = primerNombre;
    }

    public StringFilter getSegundoNombre() {
        return segundoNombre;
    }

    public StringFilter segundoNombre() {
        if (segundoNombre == null) {
            segundoNombre = new StringFilter();
        }
        return segundoNombre;
    }

    public void setSegundoNombre(StringFilter segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public StringFilter getPrimerApellido() {
        return primerApellido;
    }

    public StringFilter primerApellido() {
        if (primerApellido == null) {
            primerApellido = new StringFilter();
        }
        return primerApellido;
    }

    public void setPrimerApellido(StringFilter primerApellido) {
        this.primerApellido = primerApellido;
    }

    public StringFilter getSegundoApellido() {
        return segundoApellido;
    }

    public StringFilter segundoApellido() {
        if (segundoApellido == null) {
            segundoApellido = new StringFilter();
        }
        return segundoApellido;
    }

    public void setSegundoApellido(StringFilter segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public StringFilter getTipoDocumento() {
        return tipoDocumento;
    }

    public StringFilter tipoDocumento() {
        if (tipoDocumento == null) {
            tipoDocumento = new StringFilter();
        }
        return tipoDocumento;
    }

    public void setTipoDocumento(StringFilter tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public StringFilter getDocumento() {
        return documento;
    }

    public StringFilter documento() {
        if (documento == null) {
            documento = new StringFilter();
        }
        return documento;
    }

    public void setDocumento(StringFilter documento) {
        this.documento = documento;
    }

    public StringFilter getDocumentoDV() {
        return documentoDV;
    }

    public StringFilter documentoDV() {
        if (documentoDV == null) {
            documentoDV = new StringFilter();
        }
        return documentoDV;
    }

    public void setDocumentoDV(StringFilter documentoDV) {
        this.documentoDV = documentoDV;
    }

    public InstantFilter getEdad() {
        return edad;
    }

    public InstantFilter edad() {
        if (edad == null) {
            edad = new InstantFilter();
        }
        return edad;
    }

    public void setEdad(InstantFilter edad) {
        this.edad = edad;
    }

    public StringFilter getIndicativo() {
        return indicativo;
    }

    public StringFilter indicativo() {
        if (indicativo == null) {
            indicativo = new StringFilter();
        }
        return indicativo;
    }

    public void setIndicativo(StringFilter indicativo) {
        this.indicativo = indicativo;
    }

    public StringFilter getCelular() {
        return celular;
    }

    public StringFilter celular() {
        if (celular == null) {
            celular = new StringFilter();
        }
        return celular;
    }

    public void setCelular(StringFilter celular) {
        this.celular = celular;
    }

    public StringFilter getDireccion() {
        return direccion;
    }

    public StringFilter direccion() {
        if (direccion == null) {
            direccion = new StringFilter();
        }
        return direccion;
    }

    public void setDireccion(StringFilter direccion) {
        this.direccion = direccion;
    }

    public StringFilter getDireccionGps() {
        return direccionGps;
    }

    public StringFilter direccionGps() {
        if (direccionGps == null) {
            direccionGps = new StringFilter();
        }
        return direccionGps;
    }

    public void setDireccionGps(StringFilter direccionGps) {
        this.direccionGps = direccionGps;
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

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getRolId() {
        return rolId;
    }

    public LongFilter rolId() {
        if (rolId == null) {
            rolId = new LongFilter();
        }
        return rolId;
    }

    public void setRolId(LongFilter rolId) {
        this.rolId = rolId;
    }

    public LongFilter getInfoLegalId() {
        return infoLegalId;
    }

    public LongFilter infoLegalId() {
        if (infoLegalId == null) {
            infoLegalId = new LongFilter();
        }
        return infoLegalId;
    }

    public void setInfoLegalId(LongFilter infoLegalId) {
        this.infoLegalId = infoLegalId;
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

    public LongFilter getEmpresaId() {
        return empresaId;
    }

    public LongFilter empresaId() {
        if (empresaId == null) {
            empresaId = new LongFilter();
        }
        return empresaId;
    }

    public void setEmpresaId(LongFilter empresaId) {
        this.empresaId = empresaId;
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

    public LongFilter getBodegaId() {
        return bodegaId;
    }

    public LongFilter bodegaId() {
        if (bodegaId == null) {
            bodegaId = new LongFilter();
        }
        return bodegaId;
    }

    public void setBodegaId(LongFilter bodegaId) {
        this.bodegaId = bodegaId;
    }

    public LongFilter getOficinaId() {
        return oficinaId;
    }

    public LongFilter oficinaId() {
        if (oficinaId == null) {
            oficinaId = new LongFilter();
        }
        return oficinaId;
    }

    public void setOficinaId(LongFilter oficinaId) {
        this.oficinaId = oficinaId;
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
        final UsuarioCriteria that = (UsuarioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(primerNombre, that.primerNombre) &&
            Objects.equals(segundoNombre, that.segundoNombre) &&
            Objects.equals(primerApellido, that.primerApellido) &&
            Objects.equals(segundoApellido, that.segundoApellido) &&
            Objects.equals(tipoDocumento, that.tipoDocumento) &&
            Objects.equals(documento, that.documento) &&
            Objects.equals(documentoDV, that.documentoDV) &&
            Objects.equals(edad, that.edad) &&
            Objects.equals(indicativo, that.indicativo) &&
            Objects.equals(celular, that.celular) &&
            Objects.equals(direccion, that.direccion) &&
            Objects.equals(direccionGps, that.direccionGps) &&
            Objects.equals(fechaRegistro, that.fechaRegistro) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(rolId, that.rolId) &&
            Objects.equals(infoLegalId, that.infoLegalId) &&
            Objects.equals(sucursalId, that.sucursalId) &&
            Objects.equals(empresaId, that.empresaId) &&
            Objects.equals(empresaIdId, that.empresaIdId) &&
            Objects.equals(bodegaId, that.bodegaId) &&
            Objects.equals(oficinaId, that.oficinaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            primerNombre,
            segundoNombre,
            primerApellido,
            segundoApellido,
            tipoDocumento,
            documento,
            documentoDV,
            edad,
            indicativo,
            celular,
            direccion,
            direccionGps,
            fechaRegistro,
            userId,
            rolId,
            infoLegalId,
            sucursalId,
            empresaId,
            empresaIdId,
            bodegaId,
            oficinaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (primerNombre != null ? "primerNombre=" + primerNombre + ", " : "") +
            (segundoNombre != null ? "segundoNombre=" + segundoNombre + ", " : "") +
            (primerApellido != null ? "primerApellido=" + primerApellido + ", " : "") +
            (segundoApellido != null ? "segundoApellido=" + segundoApellido + ", " : "") +
            (tipoDocumento != null ? "tipoDocumento=" + tipoDocumento + ", " : "") +
            (documento != null ? "documento=" + documento + ", " : "") +
            (documentoDV != null ? "documentoDV=" + documentoDV + ", " : "") +
            (edad != null ? "edad=" + edad + ", " : "") +
            (indicativo != null ? "indicativo=" + indicativo + ", " : "") +
            (celular != null ? "celular=" + celular + ", " : "") +
            (direccion != null ? "direccion=" + direccion + ", " : "") +
            (direccionGps != null ? "direccionGps=" + direccionGps + ", " : "") +
            (fechaRegistro != null ? "fechaRegistro=" + fechaRegistro + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (rolId != null ? "rolId=" + rolId + ", " : "") +
            (infoLegalId != null ? "infoLegalId=" + infoLegalId + ", " : "") +
            (sucursalId != null ? "sucursalId=" + sucursalId + ", " : "") +
            (empresaId != null ? "empresaId=" + empresaId + ", " : "") +
            (empresaIdId != null ? "empresaIdId=" + empresaIdId + ", " : "") +
            (bodegaId != null ? "bodegaId=" + bodegaId + ", " : "") +
            (oficinaId != null ? "oficinaId=" + oficinaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
