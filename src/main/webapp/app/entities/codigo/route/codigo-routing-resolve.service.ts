import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICodigo, Codigo } from '../codigo.model';
import { CodigoService } from '../service/codigo.service';

@Injectable({ providedIn: 'root' })
export class CodigoRoutingResolveService implements Resolve<ICodigo> {
  constructor(protected service: CodigoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICodigo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((codigo: HttpResponse<Codigo>) => {
          if (codigo.body) {
            return of(codigo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Codigo());
  }
}
