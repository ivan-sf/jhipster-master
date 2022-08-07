import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IComprobanteContable } from '../comprobante-contable.model';

@Component({
  selector: 'jhi-comprobante-contable-detail',
  templateUrl: './comprobante-contable-detail.component.html',
})
export class ComprobanteContableDetailComponent implements OnInit {
  comprobanteContable: IComprobanteContable | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comprobanteContable }) => {
      this.comprobanteContable = comprobanteContable;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
