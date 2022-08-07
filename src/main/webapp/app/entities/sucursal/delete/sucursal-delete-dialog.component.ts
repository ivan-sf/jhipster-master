import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISucursal } from '../sucursal.model';
import { SucursalService } from '../service/sucursal.service';

@Component({
  templateUrl: './sucursal-delete-dialog.component.html',
})
export class SucursalDeleteDialogComponent {
  sucursal?: ISucursal;

  constructor(protected sucursalService: SucursalService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sucursalService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
