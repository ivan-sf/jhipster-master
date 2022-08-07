import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OficinaComponent } from './list/oficina.component';
import { OficinaDetailComponent } from './detail/oficina-detail.component';
import { OficinaUpdateComponent } from './update/oficina-update.component';
import { OficinaDeleteDialogComponent } from './delete/oficina-delete-dialog.component';
import { OficinaRoutingModule } from './route/oficina-routing.module';

@NgModule({
  imports: [SharedModule, OficinaRoutingModule],
  declarations: [OficinaComponent, OficinaDetailComponent, OficinaUpdateComponent, OficinaDeleteDialogComponent],
  entryComponents: [OficinaDeleteDialogComponent],
})
export class OficinaModule {}
