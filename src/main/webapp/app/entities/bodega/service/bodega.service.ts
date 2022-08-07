import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBodega, getBodegaIdentifier } from '../bodega.model';

export type EntityResponseType = HttpResponse<IBodega>;
export type EntityArrayResponseType = HttpResponse<IBodega[]>;

@Injectable({ providedIn: 'root' })
export class BodegaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bodegas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bodega: IBodega): Observable<EntityResponseType> {
    return this.http.post<IBodega>(this.resourceUrl, bodega, { observe: 'response' });
  }

  update(bodega: IBodega): Observable<EntityResponseType> {
    return this.http.put<IBodega>(`${this.resourceUrl}/${getBodegaIdentifier(bodega) as number}`, bodega, { observe: 'response' });
  }

  partialUpdate(bodega: IBodega): Observable<EntityResponseType> {
    return this.http.patch<IBodega>(`${this.resourceUrl}/${getBodegaIdentifier(bodega) as number}`, bodega, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBodega>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBodega[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBodegaToCollectionIfMissing(bodegaCollection: IBodega[], ...bodegasToCheck: (IBodega | null | undefined)[]): IBodega[] {
    const bodegas: IBodega[] = bodegasToCheck.filter(isPresent);
    if (bodegas.length > 0) {
      const bodegaCollectionIdentifiers = bodegaCollection.map(bodegaItem => getBodegaIdentifier(bodegaItem)!);
      const bodegasToAdd = bodegas.filter(bodegaItem => {
        const bodegaIdentifier = getBodegaIdentifier(bodegaItem);
        if (bodegaIdentifier == null || bodegaCollectionIdentifiers.includes(bodegaIdentifier)) {
          return false;
        }
        bodegaCollectionIdentifiers.push(bodegaIdentifier);
        return true;
      });
      return [...bodegasToAdd, ...bodegaCollection];
    }
    return bodegaCollection;
  }
}
