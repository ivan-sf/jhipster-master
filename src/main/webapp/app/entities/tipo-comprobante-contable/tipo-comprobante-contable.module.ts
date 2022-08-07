import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TipoComprobanteContableComponent } from './list/tipo-comprobante-contable.component';
import { TipoComprobanteContableDetailComponent } from './detail/tipo-comprobante-contable-detail.component';
import { TipoComprobanteContableUpdateComponent } from './update/tipo-comprobante-contable-update.component';
import { TipoComprobanteContableDeleteDialogComponent } from './delete/tipo-comprobante-contable-delete-dialog.component';
import { TipoComprobanteContableRoutingModule } from './route/tipo-comprobante-contable-routing.module';

@NgModule({
  imports: [SharedModule, TipoComprobanteContableRoutingModule],
  declarations: [
    TipoComprobanteContableComponent,
    TipoComprobanteContableDetailComponent,
    TipoComprobanteContableUpdateComponent,
    TipoComprobanteContableDeleteDialogComponent,
  ],
  entryComponents: [TipoComprobanteContableDeleteDialogComponent],
})
export class TipoComprobanteContableModule {}
