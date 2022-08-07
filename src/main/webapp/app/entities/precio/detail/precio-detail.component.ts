import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrecio } from '../precio.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-precio-detail',
  templateUrl: './precio-detail.component.html',
})
export class PrecioDetailComponent implements OnInit {
  precio: IPrecio | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ precio }) => {
      this.precio = precio;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
