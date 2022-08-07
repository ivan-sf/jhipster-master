import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InventarioComponent } from './list/inventario.component';
import { InventarioDetailComponent } from './detail/inventario-detail.component';
import { InventarioUpdateComponent } from './update/inventario-update.component';
import { InventarioDeleteDialogComponent } from './delete/inventario-delete-dialog.component';
import { InventarioRoutingModule } from './route/inventario-routing.module';

@NgModule({
  imports: [SharedModule, InventarioRoutingModule],
  declarations: [InventarioComponent, InventarioDetailComponent, InventarioUpdateComponent, InventarioDeleteDialogComponent],
  entryComponents: [InventarioDeleteDialogComponent],
})
export class InventarioModule {}
