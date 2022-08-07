import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPrecio, getPrecioIdentifier } from '../precio.model';

export type EntityResponseType = HttpResponse<IPrecio>;
export type EntityArrayResponseType = HttpResponse<IPrecio[]>;

@Injectable({ providedIn: 'root' })
export class PrecioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/precios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(precio: IPrecio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(precio);
    return this.http
      .post<IPrecio>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(precio: IPrecio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(precio);
    return this.http
      .put<IPrecio>(`${this.resourceUrl}/${getPrecioIdentifier(precio) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(precio: IPrecio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(precio);
    return this.http
      .patch<IPrecio>(`${this.resourceUrl}/${getPrecioIdentifier(precio) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPrecio>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrecio[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPrecioToCollectionIfMissing(precioCollection: IPrecio[], ...preciosToCheck: (IPrecio | null | undefined)[]): IPrecio[] {
    const precios: IPrecio[] = preciosToCheck.filter(isPresent);
    if (precios.length > 0) {
      const precioCollectionIdentifiers = precioCollection.map(precioItem => getPrecioIdentifier(precioItem)!);
      const preciosToAdd = precios.filter(precioItem => {
        const precioIdentifier = getPrecioIdentifier(precioItem);
        if (precioIdentifier == null || precioCollectionIdentifiers.includes(precioIdentifier)) {
          return false;
        }
        precioCollectionIdentifiers.push(precioIdentifier);
        return true;
      });
      return [...preciosToAdd, ...precioCollection];
    }
    return precioCollection;
  }

  protected convertDateFromClient(precio: IPrecio): IPrecio {
    return Object.assign({}, precio, {
      fechaRegistro: precio.fechaRegistro?.isValid() ? precio.fechaRegistro.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaRegistro = res.body.fechaRegistro ? dayjs(res.body.fechaRegistro) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((precio: IPrecio) => {
        precio.fechaRegistro = precio.fechaRegistro ? dayjs(precio.fechaRegistro) : undefined;
      });
    }
    return res;
  }
}
