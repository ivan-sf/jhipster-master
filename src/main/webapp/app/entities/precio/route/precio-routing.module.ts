import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrecioComponent } from '../list/precio.component';
import { PrecioDetailComponent } from '../detail/precio-detail.component';
import { PrecioUpdateComponent } from '../update/precio-update.component';
import { PrecioRoutingResolveService } from './precio-routing-resolve.service';

const precioRoute: Routes = [
  {
    path: '',
    component: PrecioComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrecioDetailComponent,
    resolve: {
      precio: PrecioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrecioUpdateComponent,
    resolve: {
      precio: PrecioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrecioUpdateComponent,
    resolve: {
      precio: PrecioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(precioRoute)],
  exports: [RouterModule],
})
export class PrecioRoutingModule {}
