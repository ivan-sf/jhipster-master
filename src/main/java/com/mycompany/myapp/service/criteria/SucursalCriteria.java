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
 * Criteria class for the {@link com.mycompany.myapp.domain.Sucursal} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.SucursalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sucursals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class SucursalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter nit;

    private StringFilter direccion;

    private StringFilter direccionGPS;

    private IntegerFilter estado;

    private InstantFilter fechaRegistro;

    private LongFilter oficinaId;

    private LongFilter inventarioId;

    private LongFilter empresaId;

    private LongFilter infoLegalId;

    private LongFilter usuarioId;

    private LongFilter empresaIdId;

    private Boolean distinct;

    public SucursalCriteria() {}

    public SucursalCriteria(SucursalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.nit = other.nit == null ? null : other.nit.copy();
        this.direccion = other.direccion == null ? null : other.direccion.copy();
        this.direccionGPS = other.direccionGPS == null ? null : other.direccionGPS.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.fechaRegistro = other.fechaRegistro == null ? null : other.fechaRegistro.copy();
        this.oficinaId = other.oficinaId == null ? null : other.oficinaId.copy();
        this.inventarioId = other.inventarioId == null ? null : other.inventarioId.copy();
        this.empresaId = other.empresaId == null ? null : other.empresaId.copy();
        this.infoLegalId = other.infoLegalId == null ? null : other.infoLegalId.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.empresaIdId = other.empresaIdId == null ? null : other.empresaIdId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SucursalCriteria copy() {
        return new SucursalCriteria(this);
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

    public StringFilter getNombre() {
        return nombre;
    }

    public StringFilter nombre() {
        if (nombre == null) {
            nombre = new StringFilter();
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
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

    public StringFilter getDireccionGPS() {
        return direccionGPS;
    }

    public StringFilter direccionGPS() {
        if (direccionGPS == null) {
            direccionGPS = new StringFilter();
        }
        return direccionGPS;
    }

    public void setDireccionGPS(StringFilter direccionGPS) {
        this.direccionGPS = direccionGPS;
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

    public LongFilter getInventarioId() {
        return inventarioId;
    }

    public LongFilter inventarioId() {
        if (inventarioId == null) {
            inventarioId = new LongFilter();
        }
        return inventarioId;
    }

    public void setInventarioId(LongFilter inventarioId) {
        this.inventarioId = inventarioId;
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
        final SucursalCriteria that = (SucursalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(nit, that.nit) &&
            Objects.equals(direccion, that.direccion) &&
            Objects.equals(direccionGPS, that.direccionGPS) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(fechaRegistro, that.fechaRegistro) &&
            Objects.equals(oficinaId, that.oficinaId) &&
            Objects.equals(inventarioId, that.inventarioId) &&
            Objects.equals(empresaId, that.empresaId) &&
            Objects.equals(infoLegalId, that.infoLegalId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(empresaIdId, that.empresaIdId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombre,
            nit,
            direccion,
            direccionGPS,
            estado,
            fechaRegistro,
            oficinaId,
            inventarioId,
            empresaId,
            infoLegalId,
            usuarioId,
            empresaIdId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SucursalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (nit != null ? "nit=" + nit + ", " : "") +
            (direccion != null ? "direccion=" + direccion + ", " : "") +
            (direccionGPS != null ? "direccionGPS=" + direccionGPS + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (fechaRegistro != null ? "fechaRegistro=" + fechaRegistro + ", " : "") +
            (oficinaId != null ? "oficinaId=" + oficinaId + ", " : "") +
            (inventarioId != null ? "inventarioId=" + inventarioId + ", " : "") +
            (empresaId != null ? "empresaId=" + empresaId + ", " : "") +
            (infoLegalId != null ? "infoLegalId=" + infoLegalId + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (empresaIdId != null ? "empresaIdId=" + empresaIdId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
