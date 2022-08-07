import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TipoUsuarioComponent } from '../list/tipo-usuario.component';
import { TipoUsuarioDetailComponent } from '../detail/tipo-usuario-detail.component';
import { TipoUsuarioUpdateComponent } from '../update/tipo-usuario-update.component';
import { TipoUsuarioRoutingResolveService } from './tipo-usuario-routing-resolve.service';

const tipoUsuarioRoute: Routes = [
  {
    path: '',
    component: TipoUsuarioComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoUsuarioDetailComponent,
    resolve: {
      tipoUsuario: TipoUsuarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoUsuarioUpdateComponent,
    resolve: {
      tipoUsuario: TipoUsuarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoUsuarioUpdateComponent,
    resolve: {
      tipoUsuario: TipoUsuarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tipoUsuarioRoute)],
  exports: [RouterModule],
})
export class TipoUsuarioRoutingModule {}
