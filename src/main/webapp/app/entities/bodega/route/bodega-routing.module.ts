import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BodegaComponent } from '../list/bodega.component';
import { BodegaDetailComponent } from '../detail/bodega-detail.component';
import { BodegaUpdateComponent } from '../update/bodega-update.component';
import { BodegaRoutingResolveService } from './bodega-routing-resolve.service';

const bodegaRoute: Routes = [
  {
    path: '',
    component: BodegaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BodegaDetailComponent,
    resolve: {
      bodega: BodegaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BodegaUpdateComponent,
    resolve: {
      bodega: BodegaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BodegaUpdateComponent,
    resolve: {
      bodega: BodegaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bodegaRoute)],
  exports: [RouterModule],
})
export class BodegaRoutingModule {}
