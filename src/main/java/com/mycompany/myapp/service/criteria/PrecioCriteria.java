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
 * Criteria class for the {@link com.mycompany.myapp.domain.Precio} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PrecioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /precios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class PrecioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter valor;

    private LocalDateFilter fechaRegistro;

    private LongFilter productoIngresoId;

    private LongFilter productoSalidaId;

    private Boolean distinct;

    public PrecioCriteria() {}

    public PrecioCriteria(PrecioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.valor = other.valor == null ? null : other.valor.copy();
        this.fechaRegistro = other.fechaRegistro == null ? null : other.fechaRegistro.copy();
        this.productoIngresoId = other.productoIngresoId == null ? null : other.productoIngresoId.copy();
        this.productoSalidaId = other.productoSalidaId == null ? null : other.productoSalidaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PrecioCriteria copy() {
        return new PrecioCriteria(this);
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

    public IntegerFilter getValor() {
        return valor;
    }

    public IntegerFilter valor() {
        if (valor == null) {
            valor = new IntegerFilter();
        }
        return valor;
    }

    public void setValor(IntegerFilter valor) {
        this.valor = valor;
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

    public LongFilter getProductoIngresoId() {
        return productoIngresoId;
    }

    public LongFilter productoIngresoId() {
        if (productoIngresoId == null) {
            productoIngresoId = new LongFilter();
        }
        return productoIngresoId;
    }

    public void setProductoIngresoId(LongFilter productoIngresoId) {
        this.productoIngresoId = productoIngresoId;
    }

    public LongFilter getProductoSalidaId() {
        return productoSalidaId;
    }

    public LongFilter productoSalidaId() {
        if (productoSalidaId == null) {
            productoSalidaId = new LongFilter();
        }
        return productoSalidaId;
    }

    public void setProductoSalidaId(LongFilter productoSalidaId) {
        this.productoSalidaId = productoSalidaId;
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
        final PrecioCriteria that = (PrecioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(fechaRegistro, that.fechaRegistro) &&
            Objects.equals(productoIngresoId, that.productoIngresoId) &&
            Objects.equals(productoSalidaId, that.productoSalidaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, valor, fechaRegistro, productoIngresoId, productoSalidaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrecioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (valor != null ? "valor=" + valor + ", " : "") +
            (fechaRegistro != null ? "fechaRegistro=" + fechaRegistro + ", " : "") +
            (productoIngresoId != null ? "productoIngresoId=" + productoIngresoId + ", " : "") +
            (productoSalidaId != null ? "productoSalidaId=" + productoSalidaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
