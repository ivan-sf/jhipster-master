import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CodigoComponent } from './list/codigo.component';
import { CodigoDetailComponent } from './detail/codigo-detail.component';
import { CodigoUpdateComponent } from './update/codigo-update.component';
import { CodigoDeleteDialogComponent } from './delete/codigo-delete-dialog.component';
import { CodigoRoutingModule } from './route/codigo-routing.module';

@NgModule({
  imports: [SharedModule, CodigoRoutingModule],
  declarations: [CodigoComponent, CodigoDetailComponent, CodigoUpdateComponent, CodigoDeleteDialogComponent],
  entryComponents: [CodigoDeleteDialogComponent],
})
export class CodigoModule {}
