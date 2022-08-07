import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoComprobanteContable, TipoComprobanteContable } from '../tipo-comprobante-contable.model';
import { TipoComprobanteContableService } from '../service/tipo-comprobante-contable.service';

@Injectable({ providedIn: 'root' })
export class TipoComprobanteContableRoutingResolveService implements Resolve<ITipoComprobanteContable> {
  constructor(protected service: TipoComprobanteContableService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITipoComprobanteContable> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tipoComprobanteContable: HttpResponse<TipoComprobanteContable>) => {
          if (tipoComprobanteContable.body) {
            return of(tipoComprobanteContable.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TipoComprobanteContable());
  }
}
