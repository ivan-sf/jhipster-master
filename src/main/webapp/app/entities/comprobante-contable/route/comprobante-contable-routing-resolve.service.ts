import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IComprobanteContable, ComprobanteContable } from '../comprobante-contable.model';
import { ComprobanteContableService } from '../service/comprobante-contable.service';

@Injectable({ providedIn: 'root' })
export class ComprobanteContableRoutingResolveService implements Resolve<IComprobanteContable> {
  constructor(protected service: ComprobanteContableService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IComprobanteContable> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((comprobanteContable: HttpResponse<ComprobanteContable>) => {
          if (comprobanteContable.body) {
            return of(comprobanteContable.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ComprobanteContable());
  }
}
