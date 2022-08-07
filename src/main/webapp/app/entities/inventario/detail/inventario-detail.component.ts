import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInventario } from '../inventario.model';

@Component({
  selector: 'jhi-inventario-detail',
  templateUrl: './inventario-detail.component.html',
})
export class InventarioDetailComponent implements OnInit {
  inventario: IInventario | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventario }) => {
      this.inventario = inventario;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
