import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IComprobanteContable } from '../comprobante-contable.model';
import { ComprobanteContableService } from '../service/comprobante-contable.service';

@Component({
  templateUrl: './comprobante-contable-delete-dialog.component.html',
})
export class ComprobanteContableDeleteDialogComponent {
  comprobanteContable?: IComprobanteContable;

  constructor(protected comprobanteContableService: ComprobanteContableService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.comprobanteContableService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
