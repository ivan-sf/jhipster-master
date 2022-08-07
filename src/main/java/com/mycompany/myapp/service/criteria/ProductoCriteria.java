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
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Producto} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ProductoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /productos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ProductoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter iva;

    private LocalDateFilter fechaRegistro;

    private LongFilter codigoId;

    private LongFilter precioIngresoId;

    private LongFilter precioSalidaId;

    private LongFilter bodegaId;

    private LongFilter oficinaId;

    private Boolean distinct;

    public ProductoCriteria() {}

    public ProductoCriteria(ProductoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.iva = other.iva == null ? null : other.iva.copy();
        this.fechaRegistro = other.fechaRegistro == null ? null : other.fechaRegistro.copy();
        this.codigoId = other.codigoId == null ? null : other.codigoId.copy();
        this.precioIngresoId = other.precioIngresoId == null ? null : other.precioIngresoId.copy();
        this.precioSalidaId = other.precioSalidaId == null ? null : other.precioSalidaId.copy();
        this.bodegaId = other.bodegaId == null ? null : other.bodegaId.copy();
        this.oficinaId = other.oficinaId == null ? null : other.oficinaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductoCriteria copy() {
        return new ProductoCriteria(this);
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

    public StringFilter getIva() {
        return iva;
    }

    public StringFilter iva() {
        if (iva == null) {
            iva = new StringFilter();
        }
        return iva;
    }

    public void setIva(StringFilter iva) {
        this.iva = iva;
    }

    public LocalDateFilter getFechaRegistro() {
        return fechaRegistro;
    }

    public LocalDateFilter fechaRegistro() {
        if (fechaRegistro == null) {
            fechaRegistro = new LocalDateFilter();
        }
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateFilter fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LongFilter getCodigoId() {
        return codigoId;
    }

    public LongFilter codigoId() {
        if (codigoId == null) {
            codigoId = new LongFilter();
        }
        return codigoId;
    }

    public void setCodigoId(LongFilter codigoId) {
        this.codigoId = codigoId;
    }

    public LongFilter getPrecioIngresoId() {
        return precioIngresoId;
    }

    public LongFilter precioIngresoId() {
        if (precioIngresoId == null) {
            precioIngresoId = new LongFilter();
        }
        return precioIngresoId;
    }

    public void setPrecioIngresoId(LongFilter precioIngresoId) {
        this.precioIngresoId = precioIngresoId;
    }

    public LongFilter getPrecioSalidaId() {
        return precioSalidaId;
    }

    public LongFilter precioSalidaId() {
        if (precioSalidaId == null) {
            precioSalidaId = new LongFilter();
        }
        return precioSalidaId;
    }

    public void setPrecioSalidaId(LongFilter precioSalidaId) {
        this.precioSalidaId = precioSalidaId;
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
        final ProductoCriteria that = (ProductoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(iva, that.iva) &&
            Objects.equals(fechaRegistro, that.fechaRegistro) &&
            Objects.equals(codigoId, that.codigoId) &&
            Objects.equals(precioIngresoId, that.precioIngresoId) &&
            Objects.equals(precioSalidaId, that.precioSalidaId) &&
            Objects.equals(bodegaId, that.bodegaId) &&
            Objects.equals(oficinaId, that.oficinaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, iva, fechaRegistro, codigoId, precioIngresoId, precioSalidaId, bodegaId, oficinaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (iva != null ? "iva=" + iva + ", " : "") +
            (fechaRegistro != null ? "fechaRegistro=" + fechaRegistro + ", " : "") +
            (codigoId != null ? "codigoId=" + codigoId + ", " : "") +
            (precioIngresoId != null ? "precioIngresoId=" + precioIngresoId + ", " : "") +
            (precioSalidaId != null ? "precioSalidaId=" + precioSalidaId + ", " : "") +
            (bodegaId != null ? "bodegaId=" + bodegaId + ", " : "") +
            (oficinaId != null ? "oficinaId=" + oficinaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
