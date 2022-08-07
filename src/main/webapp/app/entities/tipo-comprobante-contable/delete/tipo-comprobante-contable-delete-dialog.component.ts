import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoComprobanteContable } from '../tipo-comprobante-contable.model';
import { TipoComprobanteContableService } from '../service/tipo-comprobante-contable.service';

@Component({
  templateUrl: './tipo-comprobante-contable-delete-dialog.component.html',
})
export class TipoComprobanteContableDeleteDialogComponent {
  tipoComprobanteContable?: ITipoComprobanteContable;

  constructor(protected tipoComprobanteContableService: TipoComprobanteContableService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoComprobanteContableService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
