import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SucursalComponent } from '../list/sucursal.component';
import { SucursalDetailComponent } from '../detail/sucursal-detail.component';
import { SucursalUpdateComponent } from '../update/sucursal-update.component';
import { SucursalRoutingResolveService } from './sucursal-routing-resolve.service';

const sucursalRoute: Routes = [
  {
    path: '',
    component: SucursalComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SucursalDetailComponent,
    resolve: {
      sucursal: SucursalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SucursalUpdateComponent,
    resolve: {
      sucursal: SucursalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SucursalUpdateComponent,
    resolve: {
      sucursal: SucursalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sucursalRoute)],
  exports: [RouterModule],
})
export class SucursalRoutingModule {}
