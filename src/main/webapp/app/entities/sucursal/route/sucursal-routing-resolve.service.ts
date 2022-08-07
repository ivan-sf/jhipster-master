import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISucursal, Sucursal } from '../sucursal.model';
import { SucursalService } from '../service/sucursal.service';

@Injectable({ providedIn: 'root' })
export class SucursalRoutingResolveService implements Resolve<ISucursal> {
  constructor(protected service: SucursalService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISucursal> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sucursal: HttpResponse<Sucursal>) => {
          if (sucursal.body) {
            return of(sucursal.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Sucursal());
  }
}
