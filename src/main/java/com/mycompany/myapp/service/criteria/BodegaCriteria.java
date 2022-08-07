package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Bodega} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.BodegaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bodegas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class BodegaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter ubicacion;

    private LongFilter productoId;

    private LongFilter usuarioId;

    private LongFilter inventarioId;

    private Boolean distinct;

    public BodegaCriteria() {}

    public BodegaCriteria(BodegaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.ubicacion = other.ubicacion == null ? null : other.ubicacion.copy();
        this.productoId = other.productoId == null ? null : other.productoId.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.inventarioId = other.inventarioId == null ? null : other.inventarioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BodegaCriteria copy() {
        return new BodegaCriteria(this);
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

    public StringFilter getUbicacion() {
        return ubicacion;
    }

    public StringFilter ubicacion() {
        if (ubicacion == null) {
            ubicacion = new StringFilter();
        }
        return ubicacion;
    }

    public void setUbicacion(StringFilter ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LongFilter getProductoId() {
        return productoId;
    }

    public LongFilter productoId() {
        if (productoId == null) {
            productoId = new LongFilter();
        }
        return productoId;
    }

    public void setProductoId(LongFilter productoId) {
        this.productoId = productoId;
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
        final BodegaCriteria that = (BodegaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(ubicacion, that.ubicacion) &&
            Objects.equals(productoId, that.productoId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(inventarioId, that.inventarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, ubicacion, productoId, usuarioId, inventarioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BodegaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (ubicacion != null ? "ubicacion=" + ubicacion + ", " : "") +
            (productoId != null ? "productoId=" + productoId + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (inventarioId != null ? "inventarioId=" + inventarioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
