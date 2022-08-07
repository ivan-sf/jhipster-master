import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BodegaComponent } from './list/bodega.component';
import { BodegaDetailComponent } from './detail/bodega-detail.component';
import { BodegaUpdateComponent } from './update/bodega-update.component';
import { BodegaDeleteDialogComponent } from './delete/bodega-delete-dialog.component';
import { BodegaRoutingModule } from './route/bodega-routing.module';

@NgModule({
  imports: [SharedModule, BodegaRoutingModule],
  declarations: [BodegaComponent, BodegaDetailComponent, BodegaUpdateComponent, BodegaDeleteDialogComponent],
  entryComponents: [BodegaDeleteDialogComponent],
})
export class BodegaModule {}
