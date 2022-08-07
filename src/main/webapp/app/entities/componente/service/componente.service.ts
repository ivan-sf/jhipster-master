import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IComponente, getComponenteIdentifier } from '../componente.model';

export type EntityResponseType = HttpResponse<IComponente>;
export type EntityArrayResponseType = HttpResponse<IComponente[]>;

@Injectable({ providedIn: 'root' })
export class ComponenteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/componentes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(componente: IComponente): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(componente);
    return this.http
      .post<IComponente>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(componente: IComponente): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(componente);
    return this.http
      .put<IComponente>(`${this.resourceUrl}/${getComponenteIdentifier(componente) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(componente: IComponente): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(componente);
    return this.http
      .patch<IComponente>(`${this.resourceUrl}/${getComponenteIdentifier(componente) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IComponente>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IComponente[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addComponenteToCollectionIfMissing(
    componenteCollection: IComponente[],
    ...componentesToCheck: (IComponente | null | undefined)[]
  ): IComponente[] {
    const componentes: IComponente[] = componentesToCheck.filter(isPresent);
    if (componentes.length > 0) {
      const componenteCollectionIdentifiers = componenteCollection.map(componenteItem => getComponenteIdentifier(componenteItem)!);
      const componentesToAdd = componentes.filter(componenteItem => {
        const componenteIdentifier = getComponenteIdentifier(componenteItem);
        if (componenteIdentifier == null || componenteCollectionIdentifiers.includes(componenteIdentifier)) {
          return false;
        }
        componenteCollectionIdentifiers.push(componenteIdentifier);
        return true;
      });
      return [...componentesToAdd, ...componenteCollection];
    }
    return componenteCollection;
  }

  protected convertDateFromClient(componente: IComponente): IComponente {
    return Object.assign({}, componente, {
      fechaRegistro: componente.fechaRegistro?.isValid() ? componente.fechaRegistro.toJSON() : undefined,
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
      res.body.forEach((componente: IComponente) => {
        componente.fechaRegistro = componente.fechaRegistro ? dayjs(componente.fechaRegistro) : undefined;
      });
    }
    return res;
  }
}
