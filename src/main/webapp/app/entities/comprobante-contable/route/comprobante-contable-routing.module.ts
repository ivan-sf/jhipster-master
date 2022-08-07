import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ComprobanteContableComponent } from '../list/comprobante-contable.component';
import { ComprobanteContableDetailComponent } from '../detail/comprobante-contable-detail.component';
import { ComprobanteContableUpdateComponent } from '../update/comprobante-contable-update.component';
import { ComprobanteContableRoutingResolveService } from './comprobante-contable-routing-resolve.service';

const comprobanteContableRoute: Routes = [
  {
    path: '',
    component: ComprobanteContableComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ComprobanteContableDetailComponent,
    resolve: {
      comprobanteContable: ComprobanteContableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ComprobanteContableUpdateComponent,
    resolve: {
      comprobanteContable: ComprobanteContableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ComprobanteContableUpdateComponent,
    resolve: {
      comprobanteContable: ComprobanteContableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(comprobanteContableRoute)],
  exports: [RouterModule],
})
export class ComprobanteContableRoutingModule {}
