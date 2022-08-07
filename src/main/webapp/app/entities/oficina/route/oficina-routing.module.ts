import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OficinaComponent } from '../list/oficina.component';
import { OficinaDetailComponent } from '../detail/oficina-detail.component';
import { OficinaUpdateComponent } from '../update/oficina-update.component';
import { OficinaRoutingResolveService } from './oficina-routing-resolve.service';

const oficinaRoute: Routes = [
  {
    path: '',
    component: OficinaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OficinaDetailComponent,
    resolve: {
      oficina: OficinaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OficinaUpdateComponent,
    resolve: {
      oficina: OficinaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OficinaUpdateComponent,
    resolve: {
      oficina: OficinaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(oficinaRoute)],
  exports: [RouterModule],
})
export class OficinaRoutingModule {}
