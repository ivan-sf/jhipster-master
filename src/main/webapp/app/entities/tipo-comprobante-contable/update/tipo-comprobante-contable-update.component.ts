import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITipoComprobanteContable, TipoComprobanteContable } from '../tipo-comprobante-contable.model';
import { TipoComprobanteContableService } from '../service/tipo-comprobante-contable.service';

@Component({
  selector: 'jhi-tipo-comprobante-contable-update',
  templateUrl: './tipo-comprobante-contable-update.component.html',
})
export class TipoComprobanteContableUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(
    protected tipoComprobanteContableService: TipoComprobanteContableService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoComprobanteContable }) => {
      this.updateForm(tipoComprobanteContable);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoComprobanteContable = this.createFromForm();
    if (tipoComprobanteContable.id !== undefined) {
      this.subscribeToSaveResponse(this.tipoComprobanteContableService.update(tipoComprobanteContable));
    } else {
      this.subscribeToSaveResponse(this.tipoComprobanteContableService.create(tipoComprobanteContable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoComprobanteContable>>): void {
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

  protected updateForm(tipoComprobanteContable: ITipoComprobanteContable): void {
    this.editForm.patchValue({
      id: tipoComprobanteContable.id,
      name: tipoComprobanteContable.name,
    });
  }

  protected createFromForm(): ITipoComprobanteContable {
    return {
      ...new TipoComprobanteContable(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
