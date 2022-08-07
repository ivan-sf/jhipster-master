import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICodigo, getCodigoIdentifier } from '../codigo.model';

export type EntityResponseType = HttpResponse<ICodigo>;
export type EntityArrayResponseType = HttpResponse<ICodigo[]>;

@Injectable({ providedIn: 'root' })
export class CodigoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/codigos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(codigo: ICodigo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(codigo);
    return this.http
      .post<ICodigo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(codigo: ICodigo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(codigo);
    return this.http
      .put<ICodigo>(`${this.resourceUrl}/${getCodigoIdentifier(codigo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(codigo: ICodigo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(codigo);
    return this.http
      .patch<ICodigo>(`${this.resourceUrl}/${getCodigoIdentifier(codigo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICodigo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICodigo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCodigoToCollectionIfMissing(codigoCollection: ICodigo[], ...codigosToCheck: (ICodigo | null | undefined)[]): ICodigo[] {
    const codigos: ICodigo[] = codigosToCheck.filter(isPresent);
    if (codigos.length > 0) {
      const codigoCollectionIdentifiers = codigoCollection.map(codigoItem => getCodigoIdentifier(codigoItem)!);
      const codigosToAdd = codigos.filter(codigoItem => {
        const codigoIdentifier = getCodigoIdentifier(codigoItem);
        if (codigoIdentifier == null || codigoCollectionIdentifiers.includes(codigoIdentifier)) {
          return false;
        }
        codigoCollectionIdentifiers.push(codigoIdentifier);
        return true;
      });
      return [...codigosToAdd, ...codigoCollection];
    }
    return codigoCollection;
  }

  protected convertDateFromClient(codigo: ICodigo): ICodigo {
    return Object.assign({}, codigo, {
      fechaRegistro: codigo.fechaRegistro?.isValid() ? codigo.fechaRegistro.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((codigo: ICodigo) => {
        codigo.fechaRegistro = codigo.fechaRegistro ? dayjs(codigo.fechaRegistro) : undefined;
      });
    }
    return res;
  }
}
