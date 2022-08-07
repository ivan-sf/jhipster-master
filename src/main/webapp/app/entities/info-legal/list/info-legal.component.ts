import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInfoLegal } from '../info-legal.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { InfoLegalService } from '../service/info-legal.service';
import { InfoLegalDeleteDialogComponent } from '../delete/info-legal-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-info-legal',
  templateUrl: './info-legal.component.html',
})
export class InfoLegalComponent implements OnInit {
  infoLegals: IInfoLegal[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected infoLegalService: InfoLegalService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.infoLegals = [];
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

    this.infoLegalService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IInfoLegal[]>) => {
          this.isLoading = false;
          this.paginateInfoLegals(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.infoLegals = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IInfoLegal): number {
    return item.id!;
  }

  delete(infoLegal: IInfoLegal): void {
    const modalRef = this.modalService.open(InfoLegalDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.infoLegal = infoLegal;
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

  protected paginateInfoLegals(data: IInfoLegal[] | null, headers: HttpHeaders): void {
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
        this.infoLegals.push(d);
      }
    }
  }
}
