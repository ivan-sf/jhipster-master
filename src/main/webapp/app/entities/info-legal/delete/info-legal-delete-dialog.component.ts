import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInfoLegal } from '../info-legal.model';
import { InfoLegalService } from '../service/info-legal.service';

@Component({
  templateUrl: './info-legal-delete-dialog.component.html',
})
export class InfoLegalDeleteDialogComponent {
  infoLegal?: IInfoLegal;

  constructor(protected infoLegalService: InfoLegalService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.infoLegalService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
