import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrecio } from '../precio.model';
import { PrecioService } from '../service/precio.service';

@Component({
  templateUrl: './precio-delete-dialog.component.html',
})
export class PrecioDeleteDialogComponent {
  precio?: IPrecio;

  constructor(protected precioService: PrecioService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.precioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
