import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICodigo, Codigo } from '../codigo.model';
import { CodigoService } from '../service/codigo.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-codigo-update',
  templateUrl: './codigo-update.component.html',
})
export class CodigoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    codigo: [],
    detalle: [],
    fechaRegistro: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected codigoService: CodigoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ codigo }) => {
      this.updateForm(codigo);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('delivApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const codigo = this.createFromForm();
    if (codigo.id !== undefined) {
      this.subscribeToSaveResponse(this.codigoService.update(codigo));
    } else {
      this.subscribeToSaveResponse(this.codigoService.create(codigo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICodigo>>): void {
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

  protected updateForm(codigo: ICodigo): void {
    this.editForm.patchValue({
      id: codigo.id,
      codigo: codigo.codigo,
      detalle: codigo.detalle,
      fechaRegistro: codigo.fechaRegistro,
    });
  }

  protected createFromForm(): ICodigo {
    return {
      ...new Codigo(),
      id: this.editForm.get(['id'])!.value,
      codigo: this.editForm.get(['codigo'])!.value,
      detalle: this.editForm.get(['detalle'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value,
    };
  }
}
