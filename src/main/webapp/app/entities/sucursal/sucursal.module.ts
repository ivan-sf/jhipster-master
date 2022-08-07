import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SucursalComponent } from './list/sucursal.component';
import { SucursalDetailComponent } from './detail/sucursal-detail.component';
import { SucursalUpdateComponent } from './update/sucursal-update.component';
import { SucursalDeleteDialogComponent } from './delete/sucursal-delete-dialog.component';
import { SucursalRoutingModule } from './route/sucursal-routing.module';

@NgModule({
  imports: [SharedModule, SucursalRoutingModule],
  declarations: [SucursalComponent, SucursalDetailComponent, SucursalUpdateComponent, SucursalDeleteDialogComponent],
  entryComponents: [SucursalDeleteDialogComponent],
})
export class SucursalModule {}
