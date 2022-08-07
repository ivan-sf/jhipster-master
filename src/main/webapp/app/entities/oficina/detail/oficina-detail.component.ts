import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOficina } from '../oficina.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-oficina-detail',
  templateUrl: './oficina-detail.component.html',
})
export class OficinaDetailComponent implements OnInit {
  oficina: IOficina | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ oficina }) => {
      this.oficina = oficina;
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
