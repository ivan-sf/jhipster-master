import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOficina, Oficina } from '../oficina.model';
import { OficinaService } from '../service/oficina.service';

@Injectable({ providedIn: 'root' })
export class OficinaRoutingResolveService implements Resolve<IOficina> {
  constructor(protected service: OficinaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOficina> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((oficina: HttpResponse<Oficina>) => {
          if (oficina.body) {
            return of(oficina.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Oficina());
  }
}
