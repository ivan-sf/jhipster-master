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
 * Criteria class for the {@link com.mycompany.myapp.domain.Empresa} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.EmpresaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /empresas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class EmpresaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter direccion;

    private StringFilter direccionGPS;

    private StringFilter email;

    private StringFilter celular;

    private StringFilter indicativo;

    private IntegerFilter estado;

    private InstantFilter fechaRegistro;

    private LongFilter componenteId;

    private LongFilter rolId;

    private LongFilter usuarioId;

    private LongFilter sucursalId;

    private LongFilter sucursalIdId;

    private LongFilter infoLegalIdId;

    private LongFilter usuarioIdId;

    private Boolean distinct;

    public EmpresaCriteria() {}

    public EmpresaCriteria(EmpresaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.direccion = other.direccion == null ? null : other.direccion.copy();
        this.direccionGPS = other.direccionGPS == null ? null : other.direccionGPS.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.celular = other.celular == null ? null : other.celular.copy();
        this.indicativo = other.indicativo == null ? null : other.indicativo.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.fechaRegistro = other.fechaRegistro == null ? null : other.fechaRegistro.copy();
        this.componenteId = other.componenteId == null ? null : other.componenteId.copy();
        this.rolId = other.rolId == null ? null : other.rolId.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.sucursalId = other.sucursalId == null ? null : other.sucursalId.copy();
        this.sucursalIdId = other.sucursalIdId == null ? null : other.sucursalIdId.copy();
        this.infoLegalIdId = other.infoLegalIdId == null ? null : other.infoLegalIdId.copy();
        this.usuarioIdId = other.usuarioIdId == null ? null : other.usuarioIdId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EmpresaCriteria copy() {
        return new EmpresaCriteria(this);
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

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
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

    public LongFilter getComponenteId() {
        return componenteId;
    }

    public LongFilter componenteId() {
        if (componenteId == null) {
            componenteId = new LongFilter();
        }
        return componenteId;
    }

    public void setComponenteId(LongFilter componenteId) {
        this.componenteId = componenteId;
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

    public LongFilter getSucursalIdId() {
        return sucursalIdId;
    }

    public LongFilter sucursalIdId() {
        if (sucursalIdId == null) {
            sucursalIdId = new LongFilter();
        }
        return sucursalIdId;
    }

    public void setSucursalIdId(LongFilter sucursalIdId) {
        this.sucursalIdId = sucursalIdId;
    }

    public LongFilter getInfoLegalIdId() {
        return infoLegalIdId;
    }

    public LongFilter infoLegalIdId() {
        if (infoLegalIdId == null) {
            infoLegalIdId = new LongFilter();
        }
        return infoLegalIdId;
    }

    public void setInfoLegalIdId(LongFilter infoLegalIdId) {
        this.infoLegalIdId = infoLegalIdId;
    }

    public LongFilter getUsuarioIdId() {
        return usuarioIdId;
    }

    public LongFilter usuarioIdId() {
        if (usuarioIdId == null) {
            usuarioIdId = new LongFilter();
        }
        return usuarioIdId;
    }

    public void setUsuarioIdId(LongFilter usuarioIdId) {
        this.usuarioIdId = usuarioIdId;
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
        final EmpresaCriteria that = (EmpresaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(direccion, that.direccion) &&
            Objects.equals(direccionGPS, that.direccionGPS) &&
            Objects.equals(email, that.email) &&
            Objects.equals(celular, that.celular) &&
            Objects.equals(indicativo, that.indicativo) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(fechaRegistro, that.fechaRegistro) &&
            Objects.equals(componenteId, that.componenteId) &&
            Objects.equals(rolId, that.rolId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(sucursalId, that.sucursalId) &&
            Objects.equals(sucursalIdId, that.sucursalIdId) &&
            Objects.equals(infoLegalIdId, that.infoLegalIdId) &&
            Objects.equals(usuarioIdId, that.usuarioIdId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombre,
            direccion,
            direccionGPS,
            email,
            celular,
            indicativo,
            estado,
            fechaRegistro,
            componenteId,
            rolId,
            usuarioId,
            sucursalId,
            sucursalIdId,
            infoLegalIdId,
            usuarioIdId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmpresaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (direccion != null ? "direccion=" + direccion + ", " : "") +
            (direccionGPS != null ? "direccionGPS=" + direccionGPS + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (celular != null ? "celular=" + celular + ", " : "") +
            (indicativo != null ? "indicativo=" + indicativo + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (fechaRegistro != null ? "fechaRegistro=" + fechaRegistro + ", " : "") +
            (componenteId != null ? "componenteId=" + componenteId + ", " : "") +
            (rolId != null ? "rolId=" + rolId + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (sucursalId != null ? "sucursalId=" + sucursalId + ", " : "") +
            (sucursalIdId != null ? "sucursalIdId=" + sucursalIdId + ", " : "") +
            (infoLegalIdId != null ? "infoLegalIdId=" + infoLegalIdId + ", " : "") +
            (usuarioIdId != null ? "usuarioIdId=" + usuarioIdId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
