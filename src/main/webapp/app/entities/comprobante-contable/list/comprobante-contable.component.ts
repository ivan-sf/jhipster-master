import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IComprobanteContable } from '../comprobante-contable.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ComprobanteContableService } from '../service/comprobante-contable.service';
import { ComprobanteContableDeleteDialogComponent } from '../delete/comprobante-contable-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-comprobante-contable',
  templateUrl: './comprobante-contable.component.html',
})
export class ComprobanteContableComponent implements OnInit {
  comprobanteContables: IComprobanteContable[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected comprobanteContableService: ComprobanteContableService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.comprobanteContables = [];
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

    this.comprobanteContableService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IComprobanteContable[]>) => {
          this.isLoading = false;
          this.paginateComprobanteContables(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.comprobanteContables = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IComprobanteContable): number {
    return item.id!;
  }

  delete(comprobanteContable: IComprobanteContable): void {
    const modalRef = this.modalService.open(ComprobanteContableDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.comprobanteContable = comprobanteContable;
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

  protected paginateComprobanteContables(data: IComprobanteContable[] | null, headers: HttpHeaders): void {
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
        this.comprobanteContables.push(d);
      }
    }
  }
}
