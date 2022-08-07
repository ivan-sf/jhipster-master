import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoUsuario, TipoUsuario } from '../tipo-usuario.model';
import { TipoUsuarioService } from '../service/tipo-usuario.service';

@Injectable({ providedIn: 'root' })
export class TipoUsuarioRoutingResolveService implements Resolve<ITipoUsuario> {
  constructor(protected service: TipoUsuarioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITipoUsuario> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tipoUsuario: HttpResponse<TipoUsuario>) => {
          if (tipoUsuario.body) {
            return of(tipoUsuario.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TipoUsuario());
  }
}
