import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoUsuario } from '../tipo-usuario.model';
import { TipoUsuarioService } from '../service/tipo-usuario.service';

@Component({
  templateUrl: './tipo-usuario-delete-dialog.component.html',
})
export class TipoUsuarioDeleteDialogComponent {
  tipoUsuario?: ITipoUsuario;

  constructor(protected tipoUsuarioService: TipoUsuarioService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoUsuarioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
