import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IComprobanteContable, getComprobanteContableIdentifier } from '../comprobante-contable.model';

export type EntityResponseType = HttpResponse<IComprobanteContable>;
export type EntityArrayResponseType = HttpResponse<IComprobanteContable[]>;

@Injectable({ providedIn: 'root' })
export class ComprobanteContableService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/comprobante-contables');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(comprobanteContable: IComprobanteContable): Observable<EntityResponseType> {
    return this.http.post<IComprobanteContable>(this.resourceUrl, comprobanteContable, { observe: 'response' });
  }

  update(comprobanteContable: IComprobanteContable): Observable<EntityResponseType> {
    return this.http.put<IComprobanteContable>(
      `${this.resourceUrl}/${getComprobanteContableIdentifier(comprobanteContable) as number}`,
      comprobanteContable,
      { observe: 'response' }
    );
  }

  partialUpdate(comprobanteContable: IComprobanteContable): Observable<EntityResponseType> {
    return this.http.patch<IComprobanteContable>(
      `${this.resourceUrl}/${getComprobanteContableIdentifier(comprobanteContable) as number}`,
      comprobanteContable,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IComprobanteContable>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IComprobanteContable[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addComprobanteContableToCollectionIfMissing(
    comprobanteContableCollection: IComprobanteContable[],
    ...comprobanteContablesToCheck: (IComprobanteContable | null | undefined)[]
  ): IComprobanteContable[] {
    const comprobanteContables: IComprobanteContable[] = comprobanteContablesToCheck.filter(isPresent);
    if (comprobanteContables.length > 0) {
      const comprobanteContableCollectionIdentifiers = comprobanteContableCollection.map(
        comprobanteContableItem => getComprobanteContableIdentifier(comprobanteContableItem)!
      );
      const comprobanteContablesToAdd = comprobanteContables.filter(comprobanteContableItem => {
        const comprobanteContableIdentifier = getComprobanteContableIdentifier(comprobanteContableItem);
        if (comprobanteContableIdentifier == null || comprobanteContableCollectionIdentifiers.includes(comprobanteContableIdentifier)) {
          return false;
        }
        comprobanteContableCollectionIdentifiers.push(comprobanteContableIdentifier);
        return true;
      });
      return [...comprobanteContablesToAdd, ...comprobanteContableCollection];
    }
    return comprobanteContableCollection;
  }
}
