import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoComprobanteContable, getTipoComprobanteContableIdentifier } from '../tipo-comprobante-contable.model';

export type EntityResponseType = HttpResponse<ITipoComprobanteContable>;
export type EntityArrayResponseType = HttpResponse<ITipoComprobanteContable[]>;

@Injectable({ providedIn: 'root' })
export class TipoComprobanteContableService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-comprobante-contables');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tipoComprobanteContable: ITipoComprobanteContable): Observable<EntityResponseType> {
    return this.http.post<ITipoComprobanteContable>(this.resourceUrl, tipoComprobanteContable, { observe: 'response' });
  }

  update(tipoComprobanteContable: ITipoComprobanteContable): Observable<EntityResponseType> {
    return this.http.put<ITipoComprobanteContable>(
      `${this.resourceUrl}/${getTipoComprobanteContableIdentifier(tipoComprobanteContable) as number}`,
      tipoComprobanteContable,
      { observe: 'response' }
    );
  }

  partialUpdate(tipoComprobanteContable: ITipoComprobanteContable): Observable<EntityResponseType> {
    return this.http.patch<ITipoComprobanteContable>(
      `${this.resourceUrl}/${getTipoComprobanteContableIdentifier(tipoComprobanteContable) as number}`,
      tipoComprobanteContable,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoComprobanteContable>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoComprobanteContable[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTipoComprobanteContableToCollectionIfMissing(
    tipoComprobanteContableCollection: ITipoComprobanteContable[],
    ...tipoComprobanteContablesToCheck: (ITipoComprobanteContable | null | undefined)[]
  ): ITipoComprobanteContable[] {
    const tipoComprobanteContables: ITipoComprobanteContable[] = tipoComprobanteContablesToCheck.filter(isPresent);
    if (tipoComprobanteContables.length > 0) {
      const tipoComprobanteContableCollectionIdentifiers = tipoComprobanteContableCollection.map(
        tipoComprobanteContableItem => getTipoComprobanteContableIdentifier(tipoComprobanteContableItem)!
      );
      const tipoComprobanteContablesToAdd = tipoComprobanteContables.filter(tipoComprobanteContableItem => {
        const tipoComprobanteContableIdentifier = getTipoComprobanteContableIdentifier(tipoComprobanteContableItem);
        if (
          tipoComprobanteContableIdentifier == null ||
          tipoComprobanteContableCollectionIdentifiers.includes(tipoComprobanteContableIdentifier)
        ) {
          return false;
        }
        tipoComprobanteContableCollectionIdentifiers.push(tipoComprobanteContableIdentifier);
        return true;
      });
      return [...tipoComprobanteContablesToAdd, ...tipoComprobanteContableCollection];
    }
    return tipoComprobanteContableCollection;
  }
}
