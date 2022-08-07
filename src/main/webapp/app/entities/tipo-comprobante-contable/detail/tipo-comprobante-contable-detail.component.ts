import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITipoComprobanteContable } from '../tipo-comprobante-contable.model';

@Component({
  selector: 'jhi-tipo-comprobante-contable-detail',
  templateUrl: './tipo-comprobante-contable-detail.component.html',
})
export class TipoComprobanteContableDetailComponent implements OnInit {
  tipoComprobanteContable: ITipoComprobanteContable | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoComprobanteContable }) => {
      this.tipoComprobanteContable = tipoComprobanteContable;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
