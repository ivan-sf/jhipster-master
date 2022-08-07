import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInfoLegal } from '../info-legal.model';

@Component({
  selector: 'jhi-info-legal-detail',
  templateUrl: './info-legal-detail.component.html',
})
export class InfoLegalDetailComponent implements OnInit {
  infoLegal: IInfoLegal | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infoLegal }) => {
      this.infoLegal = infoLegal;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
