import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBodega } from '../bodega.model';
import { BodegaService } from '../service/bodega.service';

@Component({
  templateUrl: './bodega-delete-dialog.component.html',
})
export class BodegaDeleteDialogComponent {
  bodega?: IBodega;

  constructor(protected bodegaService: BodegaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bodegaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
