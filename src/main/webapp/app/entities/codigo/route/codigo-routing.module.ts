import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CodigoComponent } from '../list/codigo.component';
import { CodigoDetailComponent } from '../detail/codigo-detail.component';
import { CodigoUpdateComponent } from '../update/codigo-update.component';
import { CodigoRoutingResolveService } from './codigo-routing-resolve.service';

const codigoRoute: Routes = [
  {
    path: '',
    component: CodigoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CodigoDetailComponent,
    resolve: {
      codigo: CodigoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CodigoUpdateComponent,
    resolve: {
      codigo: CodigoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CodigoUpdateComponent,
    resolve: {
      codigo: CodigoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(codigoRoute)],
  exports: [RouterModule],
})
export class CodigoRoutingModule {}
