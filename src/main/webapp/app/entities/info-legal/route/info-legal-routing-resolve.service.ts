import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInfoLegal, InfoLegal } from '../info-legal.model';
import { InfoLegalService } from '../service/info-legal.service';

@Injectable({ providedIn: 'root' })
export class InfoLegalRoutingResolveService implements Resolve<IInfoLegal> {
  constructor(protected service: InfoLegalService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInfoLegal> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((infoLegal: HttpResponse<InfoLegal>) => {
          if (infoLegal.body) {
            return of(infoLegal.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InfoLegal());
  }
}
