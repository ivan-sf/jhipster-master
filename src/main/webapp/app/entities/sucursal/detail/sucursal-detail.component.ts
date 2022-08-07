import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISucursal } from '../sucursal.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-sucursal-detail',
  templateUrl: './sucursal-detail.component.html',
})
export class SucursalDetailComponent implements OnInit {
  sucursal: ISucursal | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sucursal }) => {
      this.sucursal = sucursal;
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
