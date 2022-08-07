import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInventario, getInventarioIdentifier } from '../inventario.model';

export type EntityResponseType = HttpResponse<IInventario>;
export type EntityArrayResponseType = HttpResponse<IInventario[]>;

@Injectable({ providedIn: 'root' })
export class InventarioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inventarios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(inventario: IInventario): Observable<EntityResponseType> {
    return this.http.post<IInventario>(this.resourceUrl, inventario, { observe: 'response' });
  }

  update(inventario: IInventario): Observable<EntityResponseType> {
    return this.http.put<IInventario>(`${this.resourceUrl}/${getInventarioIdentifier(inventario) as number}`, inventario, {
      observe: 'response',
    });
  }

  partialUpdate(inventario: IInventario): Observable<EntityResponseType> {
    return this.http.patch<IInventario>(`${this.resourceUrl}/${getInventarioIdentifier(inventario) as number}`, inventario, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInventario>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInventario[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInventarioToCollectionIfMissing(
    inventarioCollection: IInventario[],
    ...inventariosToCheck: (IInventario | null | undefined)[]
  ): IInventario[] {
    const inventarios: IInventario[] = inventariosToCheck.filter(isPresent);
    if (inventarios.length > 0) {
      const inventarioCollectionIdentifiers = inventarioCollection.map(inventarioItem => getInventarioIdentifier(inventarioItem)!);
      const inventariosToAdd = inventarios.filter(inventarioItem => {
        const inventarioIdentifier = getInventarioIdentifier(inventarioItem);
        if (inventarioIdentifier == null || inventarioCollectionIdentifiers.includes(inventarioIdentifier)) {
          return false;
        }
        inventarioCollectionIdentifiers.push(inventarioIdentifier);
        return true;
      });
      return [...inventariosToAdd, ...inventarioCollection];
    }
    return inventarioCollection;
  }
}
