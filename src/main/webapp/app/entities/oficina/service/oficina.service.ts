import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOficina, getOficinaIdentifier } from '../oficina.model';

export type EntityResponseType = HttpResponse<IOficina>;
export type EntityArrayResponseType = HttpResponse<IOficina[]>;

@Injectable({ providedIn: 'root' })
export class OficinaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/oficinas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(oficina: IOficina): Observable<EntityResponseType> {
    return this.http.post<IOficina>(this.resourceUrl, oficina, { observe: 'response' });
  }

  update(oficina: IOficina): Observable<EntityResponseType> {
    return this.http.put<IOficina>(`${this.resourceUrl}/${getOficinaIdentifier(oficina) as number}`, oficina, { observe: 'response' });
  }

  partialUpdate(oficina: IOficina): Observable<EntityResponseType> {
    return this.http.patch<IOficina>(`${this.resourceUrl}/${getOficinaIdentifier(oficina) as number}`, oficina, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOficina>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOficina[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOficinaToCollectionIfMissing(oficinaCollection: IOficina[], ...oficinasToCheck: (IOficina | null | undefined)[]): IOficina[] {
    const oficinas: IOficina[] = oficinasToCheck.filter(isPresent);
    if (oficinas.length > 0) {
      const oficinaCollectionIdentifiers = oficinaCollection.map(oficinaItem => getOficinaIdentifier(oficinaItem)!);
      const oficinasToAdd = oficinas.filter(oficinaItem => {
        const oficinaIdentifier = getOficinaIdentifier(oficinaItem);
        if (oficinaIdentifier == null || oficinaCollectionIdentifiers.includes(oficinaIdentifier)) {
          return false;
        }
        oficinaCollectionIdentifiers.push(oficinaIdentifier);
        return true;
      });
      return [...oficinasToAdd, ...oficinaCollection];
    }
    return oficinaCollection;
  }
}
