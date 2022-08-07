import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOficina } from '../oficina.model';
import { OficinaService } from '../service/oficina.service';

@Component({
  templateUrl: './oficina-delete-dialog.component.html',
})
export class OficinaDeleteDialogComponent {
  oficina?: IOficina;

  constructor(protected oficinaService: OficinaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.oficinaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
