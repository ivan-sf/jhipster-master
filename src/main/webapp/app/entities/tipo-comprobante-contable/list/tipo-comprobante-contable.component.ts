import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoComprobanteContable } from '../tipo-comprobante-contable.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { TipoComprobanteContableService } from '../service/tipo-comprobante-contable.service';
import { TipoComprobanteContableDeleteDialogComponent } from '../delete/tipo-comprobante-contable-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-tipo-comprobante-contable',
  templateUrl: './tipo-comprobante-contable.component.html',
})
export class TipoComprobanteContableComponent implements OnInit {
  tipoComprobanteContables: ITipoComprobanteContable[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected tipoComprobanteContableService: TipoComprobanteContableService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.tipoComprobanteContables = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.tipoComprobanteContableService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<ITipoComprobanteContable[]>) => {
          this.isLoading = false;
          this.paginateTipoComprobanteContables(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.tipoComprobanteContables = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITipoComprobanteContable): number {
    return item.id!;
  }

  delete(tipoComprobanteContable: ITipoComprobanteContable): void {
    const modalRef = this.modalService.open(TipoComprobanteContableDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tipoComprobanteContable = tipoComprobanteContable;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTipoComprobanteContables(data: ITipoComprobanteContable[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.tipoComprobanteContables.push(d);
      }
    }
  }
}
