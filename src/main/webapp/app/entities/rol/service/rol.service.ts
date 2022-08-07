import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRol, getRolIdentifier } from '../rol.model';

export type EntityResponseType = HttpResponse<IRol>;
export type EntityArrayResponseType = HttpResponse<IRol[]>;

@Injectable({ providedIn: 'root' })
export class RolService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rols');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rol: IRol): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rol);
    return this.http
      .post<IRol>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rol: IRol): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rol);
    return this.http
      .put<IRol>(`${this.resourceUrl}/${getRolIdentifier(rol) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(rol: IRol): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rol);
    return this.http
      .patch<IRol>(`${this.resourceUrl}/${getRolIdentifier(rol) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRol>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRol[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRolToCollectionIfMissing(rolCollection: IRol[], ...rolsToCheck: (IRol | null | undefined)[]): IRol[] {
    const rols: IRol[] = rolsToCheck.filter(isPresent);
    if (rols.length > 0) {
      const rolCollectionIdentifiers = rolCollection.map(rolItem => getRolIdentifier(rolItem)!);
      const rolsToAdd = rols.filter(rolItem => {
        const rolIdentifier = getRolIdentifier(rolItem);
        if (rolIdentifier == null || rolCollectionIdentifiers.includes(rolIdentifier)) {
          return false;
        }
        rolCollectionIdentifiers.push(rolIdentifier);
        return true;
      });
      return [...rolsToAdd, ...rolCollection];
    }
    return rolCollection;
  }

  protected convertDateFromClient(rol: IRol): IRol {
    return Object.assign({}, rol, {
      fechaRegistro: rol.fechaRegistro?.isValid() ? rol.fechaRegistro.toJSON() : undefined,
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
      res.body.forEach((rol: IRol) => {
        rol.fechaRegistro = rol.fechaRegistro ? dayjs(rol.fechaRegistro) : undefined;
      });
    }
    return res;
  }
}
