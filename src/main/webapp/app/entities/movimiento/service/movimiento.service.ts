import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMovimiento, getMovimientoIdentifier } from '../movimiento.model';

export type EntityResponseType = HttpResponse<IMovimiento>;
export type EntityArrayResponseType = HttpResponse<IMovimiento[]>;

@Injectable({ providedIn: 'root' })
export class MovimientoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/movimientos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(movimiento: IMovimiento): Observable<EntityResponseType> {
    return this.http.post<IMovimiento>(this.resourceUrl, movimiento, { observe: 'response' });
  }

  update(movimiento: IMovimiento): Observable<EntityResponseType> {
    return this.http.put<IMovimiento>(`${this.resourceUrl}/${getMovimientoIdentifier(movimiento) as number}`, movimiento, {
      observe: 'response',
    });
  }

  partialUpdate(movimiento: IMovimiento): Observable<EntityResponseType> {
    return this.http.patch<IMovimiento>(`${this.resourceUrl}/${getMovimientoIdentifier(movimiento) as number}`, movimiento, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMovimiento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMovimiento[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMovimientoToCollectionIfMissing(
    movimientoCollection: IMovimiento[],
    ...movimientosToCheck: (IMovimiento | null | undefined)[]
  ): IMovimiento[] {
    const movimientos: IMovimiento[] = movimientosToCheck.filter(isPresent);
    if (movimientos.length > 0) {
      const movimientoCollectionIdentifiers = movimientoCollection.map(movimientoItem => getMovimientoIdentifier(movimientoItem)!);
      const movimientosToAdd = movimientos.filter(movimientoItem => {
        const movimientoIdentifier = getMovimientoIdentifier(movimientoItem);
        if (movimientoIdentifier == null || movimientoCollectionIdentifiers.includes(movimientoIdentifier)) {
          return false;
        }
        movimientoCollectionIdentifiers.push(movimientoIdentifier);
        return true;
      });
      return [...movimientosToAdd, ...movimientoCollection];
    }
    return movimientoCollection;
  }
}
