import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITipoUsuario } from '../tipo-usuario.model';

@Component({
  selector: 'jhi-tipo-usuario-detail',
  templateUrl: './tipo-usuario-detail.component.html',
})
export class TipoUsuarioDetailComponent implements OnInit {
  tipoUsuario: ITipoUsuario | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoUsuario }) => {
      this.tipoUsuario = tipoUsuario;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
