import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TipoComprobanteContableComponent } from '../list/tipo-comprobante-contable.component';
import { TipoComprobanteContableDetailComponent } from '../detail/tipo-comprobante-contable-detail.component';
import { TipoComprobanteContableUpdateComponent } from '../update/tipo-comprobante-contable-update.component';
import { TipoComprobanteContableRoutingResolveService } from './tipo-comprobante-contable-routing-resolve.service';

const tipoComprobanteContableRoute: Routes = [
  {
    path: '',
    component: TipoComprobanteContableComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoComprobanteContableDetailComponent,
    resolve: {
      tipoComprobanteContable: TipoComprobanteContableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoComprobanteContableUpdateComponent,
    resolve: {
      tipoComprobanteContable: TipoComprobanteContableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoComprobanteContableUpdateComponent,
    resolve: {
      tipoComprobanteContable: TipoComprobanteContableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tipoComprobanteContableRoute)],
  exports: [RouterModule],
})
export class TipoComprobanteContableRoutingModule {}
