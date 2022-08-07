import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBodega, Bodega } from '../bodega.model';
import { BodegaService } from '../service/bodega.service';

@Injectable({ providedIn: 'root' })
export class BodegaRoutingResolveService implements Resolve<IBodega> {
  constructor(protected service: BodegaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBodega> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bodega: HttpResponse<Bodega>) => {
          if (bodega.body) {
            return of(bodega.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Bodega());
  }
}
