import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TipoUsuarioComponent } from './list/tipo-usuario.component';
import { TipoUsuarioDetailComponent } from './detail/tipo-usuario-detail.component';
import { TipoUsuarioUpdateComponent } from './update/tipo-usuario-update.component';
import { TipoUsuarioDeleteDialogComponent } from './delete/tipo-usuario-delete-dialog.component';
import { TipoUsuarioRoutingModule } from './route/tipo-usuario-routing.module';

@NgModule({
  imports: [SharedModule, TipoUsuarioRoutingModule],
  declarations: [TipoUsuarioComponent, TipoUsuarioDetailComponent, TipoUsuarioUpdateComponent, TipoUsuarioDeleteDialogComponent],
  entryComponents: [TipoUsuarioDeleteDialogComponent],
})
export class TipoUsuarioModule {}
