import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoUsuario, getTipoUsuarioIdentifier } from '../tipo-usuario.model';

export type EntityResponseType = HttpResponse<ITipoUsuario>;
export type EntityArrayResponseType = HttpResponse<ITipoUsuario[]>;

@Injectable({ providedIn: 'root' })
export class TipoUsuarioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-usuarios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tipoUsuario: ITipoUsuario): Observable<EntityResponseType> {
    return this.http.post<ITipoUsuario>(this.resourceUrl, tipoUsuario, { observe: 'response' });
  }

  update(tipoUsuario: ITipoUsuario): Observable<EntityResponseType> {
    return this.http.put<ITipoUsuario>(`${this.resourceUrl}/${getTipoUsuarioIdentifier(tipoUsuario) as number}`, tipoUsuario, {
      observe: 'response',
    });
  }

  partialUpdate(tipoUsuario: ITipoUsuario): Observable<EntityResponseType> {
    return this.http.patch<ITipoUsuario>(`${this.resourceUrl}/${getTipoUsuarioIdentifier(tipoUsuario) as number}`, tipoUsuario, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoUsuario>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoUsuario[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTipoUsuarioToCollectionIfMissing(
    tipoUsuarioCollection: ITipoUsuario[],
    ...tipoUsuariosToCheck: (ITipoUsuario | null | undefined)[]
  ): ITipoUsuario[] {
    const tipoUsuarios: ITipoUsuario[] = tipoUsuariosToCheck.filter(isPresent);
    if (tipoUsuarios.length > 0) {
      const tipoUsuarioCollectionIdentifiers = tipoUsuarioCollection.map(tipoUsuarioItem => getTipoUsuarioIdentifier(tipoUsuarioItem)!);
      const tipoUsuariosToAdd = tipoUsuarios.filter(tipoUsuarioItem => {
        const tipoUsuarioIdentifier = getTipoUsuarioIdentifier(tipoUsuarioItem);
        if (tipoUsuarioIdentifier == null || tipoUsuarioCollectionIdentifiers.includes(tipoUsuarioIdentifier)) {
          return false;
        }
        tipoUsuarioCollectionIdentifiers.push(tipoUsuarioIdentifier);
        return true;
      });
      return [...tipoUsuariosToAdd, ...tipoUsuarioCollection];
    }
    return tipoUsuarioCollection;
  }
}
