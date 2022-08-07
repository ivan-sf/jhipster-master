import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInfoLegal, getInfoLegalIdentifier } from '../info-legal.model';

export type EntityResponseType = HttpResponse<IInfoLegal>;
export type EntityArrayResponseType = HttpResponse<IInfoLegal[]>;

@Injectable({ providedIn: 'root' })
export class InfoLegalService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/info-legals');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(infoLegal: IInfoLegal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(infoLegal);
    return this.http
      .post<IInfoLegal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(infoLegal: IInfoLegal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(infoLegal);
    return this.http
      .put<IInfoLegal>(`${this.resourceUrl}/${getInfoLegalIdentifier(infoLegal) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(infoLegal: IInfoLegal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(infoLegal);
    return this.http
      .patch<IInfoLegal>(`${this.resourceUrl}/${getInfoLegalIdentifier(infoLegal) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInfoLegal>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInfoLegal[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInfoLegalToCollectionIfMissing(
    infoLegalCollection: IInfoLegal[],
    ...infoLegalsToCheck: (IInfoLegal | null | undefined)[]
  ): IInfoLegal[] {
    const infoLegals: IInfoLegal[] = infoLegalsToCheck.filter(isPresent);
    if (infoLegals.length > 0) {
      const infoLegalCollectionIdentifiers = infoLegalCollection.map(infoLegalItem => getInfoLegalIdentifier(infoLegalItem)!);
      const infoLegalsToAdd = infoLegals.filter(infoLegalItem => {
        const infoLegalIdentifier = getInfoLegalIdentifier(infoLegalItem);
        if (infoLegalIdentifier == null || infoLegalCollectionIdentifiers.includes(infoLegalIdentifier)) {
          return false;
        }
        infoLegalCollectionIdentifiers.push(infoLegalIdentifier);
        return true;
      });
      return [...infoLegalsToAdd, ...infoLegalCollection];
    }
    return infoLegalCollection;
  }

  protected convertDateFromClient(infoLegal: IInfoLegal): IInfoLegal {
    return Object.assign({}, infoLegal, {
      fechaRegistro: infoLegal.fechaRegistro?.isValid() ? infoLegal.fechaRegistro.toJSON() : undefined,
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
      res.body.forEach((infoLegal: IInfoLegal) => {
        infoLegal.fechaRegistro = infoLegal.fechaRegistro ? dayjs(infoLegal.fechaRegistro) : undefined;
      });
    }
    return res;
  }
}
