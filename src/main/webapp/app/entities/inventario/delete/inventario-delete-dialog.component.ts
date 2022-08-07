import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInventario } from '../inventario.model';
import { InventarioService } from '../service/inventario.service';

@Component({
  templateUrl: './inventario-delete-dialog.component.html',
})
export class InventarioDeleteDialogComponent {
  inventario?: IInventario;

  constructor(protected inventarioService: InventarioService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inventarioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
