import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IComprobanteContable, ComprobanteContable } from '../comprobante-contable.model';
import { ComprobanteContableService } from '../service/comprobante-contable.service';

@Component({
  selector: 'jhi-comprobante-contable-update',
  templateUrl: './comprobante-contable-update.component.html',
})
export class ComprobanteContableUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(
    protected comprobanteContableService: ComprobanteContableService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comprobanteContable }) => {
      this.updateForm(comprobanteContable);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const comprobanteContable = this.createFromForm();
    if (comprobanteContable.id !== undefined) {
      this.subscribeToSaveResponse(this.comprobanteContableService.update(comprobanteContable));
    } else {
      this.subscribeToSaveResponse(this.comprobanteContableService.create(comprobanteContable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComprobanteContable>>): void {
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

  protected updateForm(comprobanteContable: IComprobanteContable): void {
    this.editForm.patchValue({
      id: comprobanteContable.id,
      name: comprobanteContable.name,
    });
  }

  protected createFromForm(): IComprobanteContable {
    return {
      ...new ComprobanteContable(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
