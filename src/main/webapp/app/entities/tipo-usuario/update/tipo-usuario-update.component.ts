import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITipoUsuario, TipoUsuario } from '../tipo-usuario.model';
import { TipoUsuarioService } from '../service/tipo-usuario.service';

@Component({
  selector: 'jhi-tipo-usuario-update',
  templateUrl: './tipo-usuario-update.component.html',
})
export class TipoUsuarioUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(protected tipoUsuarioService: TipoUsuarioService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoUsuario }) => {
      this.updateForm(tipoUsuario);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoUsuario = this.createFromForm();
    if (tipoUsuario.id !== undefined) {
      this.subscribeToSaveResponse(this.tipoUsuarioService.update(tipoUsuario));
    } else {
      this.subscribeToSaveResponse(this.tipoUsuarioService.create(tipoUsuario));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoUsuario>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(tipoUsuario: ITipoUsuario): void {
    this.editForm.patchValue({
      id: tipoUsuario.id,
      name: tipoUsuario.name,
    });
  }

  protected createFromForm(): ITipoUsuario {
    return {
      ...new TipoUsuario(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
