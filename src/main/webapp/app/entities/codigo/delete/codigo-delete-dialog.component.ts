import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICodigo } from '../codigo.model';
import { CodigoService } from '../service/codigo.service';

@Component({
  templateUrl: './codigo-delete-dialog.component.html',
})
export class CodigoDeleteDialogComponent {
  codigo?: ICodigo;

  constructor(protected codigoService: CodigoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.codigoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
