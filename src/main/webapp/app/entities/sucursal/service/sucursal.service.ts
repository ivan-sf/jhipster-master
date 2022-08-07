import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISucursal, getSucursalIdentifier } from '../sucursal.model';

export type EntityResponseType = HttpResponse<ISucursal>;
export type EntityArrayResponseType = HttpResponse<ISucursal[]>;

@Injectable({ providedIn: 'root' })
export class SucursalService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sucursals');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sucursal: ISucursal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sucursal);
    return this.http
      .post<ISucursal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(sucursal: ISucursal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sucursal);
    return this.http
      .put<ISucursal>(`${this.resourceUrl}/${getSucursalIdentifier(sucursal) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(sucursal: ISucursal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sucursal);
    return this.http
      .patch<ISucursal>(`${this.resourceUrl}/${getSucursalIdentifier(sucursal) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISucursal>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISucursal[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSucursalToCollectionIfMissing(sucursalCollection: ISucursal[], ...sucursalsToCheck: (ISucursal | null | undefined)[]): ISucursal[] {
    const sucursals: ISucursal[] = sucursalsToCheck.filter(isPresent);
    if (sucursals.length > 0) {
      const sucursalCollectionIdentifiers = sucursalCollection.map(sucursalItem => getSucursalIdentifier(sucursalItem)!);
      const sucursalsToAdd = sucursals.filter(sucursalItem => {
        const sucursalIdentifier = getSucursalIdentifier(sucursalItem);
        if (sucursalIdentifier == null || sucursalCollectionIdentifiers.includes(sucursalIdentifier)) {
          return false;
        }
        sucursalCollectionIdentifiers.push(sucursalIdentifier);
        return true;
      });
      return [...sucursalsToAdd, ...sucursalCollection];
    }
    return sucursalCollection;
  }

  protected convertDateFromClient(sucursal: ISucursal): ISucursal {
    return Object.assign({}, sucursal, {
      fechaRegistro: sucursal.fechaRegistro?.isValid() ? sucursal.fechaRegistro.toJSON() : undefined,
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
      res.body.forEach((sucursal: ISucursal) => {
        sucursal.fechaRegistro = sucursal.fechaRegistro ? dayjs(sucursal.fechaRegistro) : undefined;
      });
    }
    return res;
  }
}
