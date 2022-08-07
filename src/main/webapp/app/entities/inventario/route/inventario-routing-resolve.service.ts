import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInventario, Inventario } from '../inventario.model';
import { InventarioService } from '../service/inventario.service';

@Injectable({ providedIn: 'root' })
export class InventarioRoutingResolveService implements Resolve<IInventario> {
  constructor(protected service: InventarioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInventario> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((inventario: HttpResponse<Inventario>) => {
          if (inventario.body) {
            return of(inventario.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Inventario());
  }
}
