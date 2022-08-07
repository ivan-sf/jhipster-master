import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrecioComponent } from './list/precio.component';
import { PrecioDetailComponent } from './detail/precio-detail.component';
import { PrecioUpdateComponent } from './update/precio-update.component';
import { PrecioDeleteDialogComponent } from './delete/precio-delete-dialog.component';
import { PrecioRoutingModule } from './route/precio-routing.module';

@NgModule({
  imports: [SharedModule, PrecioRoutingModule],
  declarations: [PrecioComponent, PrecioDetailComponent, PrecioUpdateComponent, PrecioDeleteDialogComponent],
  entryComponents: [PrecioDeleteDialogComponent],
})
export class PrecioModule {}
