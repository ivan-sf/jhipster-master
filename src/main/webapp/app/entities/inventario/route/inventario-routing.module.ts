import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InventarioComponent } from '../list/inventario.component';
import { InventarioDetailComponent } from '../detail/inventario-detail.component';
import { InventarioUpdateComponent } from '../update/inventario-update.component';
import { InventarioRoutingResolveService } from './inventario-routing-resolve.service';

const inventarioRoute: Routes = [
  {
    path: '',
    component: InventarioComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InventarioDetailComponent,
    resolve: {
      inventario: InventarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InventarioUpdateComponent,
    resolve: {
      inventario: InventarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InventarioUpdateComponent,
    resolve: {
      inventario: InventarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(inventarioRoute)],
  exports: [RouterModule],
})
export class InventarioRoutingModule {}
