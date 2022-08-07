import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrecio, Precio } from '../precio.model';
import { PrecioService } from '../service/precio.service';

@Injectable({ providedIn: 'root' })
export class PrecioRoutingResolveService implements Resolve<IPrecio> {
  constructor(protected service: PrecioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrecio> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((precio: HttpResponse<Precio>) => {
          if (precio.body) {
            return of(precio.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Precio());
  }
}
