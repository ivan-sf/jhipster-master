import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ComprobanteContableComponent } from './list/comprobante-contable.component';
import { ComprobanteContableDetailComponent } from './detail/comprobante-contable-detail.component';
import { ComprobanteContableUpdateComponent } from './update/comprobante-contable-update.component';
import { ComprobanteContableDeleteDialogComponent } from './delete/comprobante-contable-delete-dialog.component';
import { ComprobanteContableRoutingModule } from './route/comprobante-contable-routing.module';

@NgModule({
  imports: [SharedModule, ComprobanteContableRoutingModule],
  declarations: [
    ComprobanteContableComponent,
    ComprobanteContableDetailComponent,
    ComprobanteContableUpdateComponent,
    ComprobanteContableDeleteDialogComponent,
  ],
  entryComponents: [ComprobanteContableDeleteDialogComponent],
})
export class ComprobanteContableModule {}
