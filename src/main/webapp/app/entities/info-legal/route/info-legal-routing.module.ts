import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InfoLegalComponent } from '../list/info-legal.component';
import { InfoLegalDetailComponent } from '../detail/info-legal-detail.component';
import { InfoLegalUpdateComponent } from '../update/info-legal-update.component';
import { InfoLegalRoutingResolveService } from './info-legal-routing-resolve.service';

const infoLegalRoute: Routes = [
  {
    path: '',
    component: InfoLegalComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InfoLegalDetailComponent,
    resolve: {
      infoLegal: InfoLegalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InfoLegalUpdateComponent,
    resolve: {
      infoLegal: InfoLegalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InfoLegalUpdateComponent,
    resolve: {
      infoLegal: InfoLegalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(infoLegalRoute)],
  exports: [RouterModule],
})
export class InfoLegalRoutingModule {}
