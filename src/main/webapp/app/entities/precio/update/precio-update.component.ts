import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPrecio, Precio } from '../precio.model';
import { PrecioService } from '../service/precio.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-precio-update',
  templateUrl: './precio-update.component.html',
})
export class PrecioUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    valor: [],
    detalle: [],
    fechaRegistro: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected precioService: PrecioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ precio }) => {
      this.updateForm(precio);
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
    const precio = this.createFromForm();
    if (precio.id !== undefined) {
      this.subscribeToSaveResponse(this.precioService.update(precio));
    } else {
      this.subscribeToSaveResponse(this.precioService.create(precio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrecio>>): void {
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

  protected updateForm(precio: IPrecio): void {
    this.editForm.patchValue({
      id: precio.id,
      valor: precio.valor,
      detalle: precio.detalle,
      fechaRegistro: precio.fechaRegistro,
    });
  }

  protected createFromForm(): IPrecio {
    return {
      ...new Precio(),
      id: this.editForm.get(['id'])!.value,
      valor: this.editForm.get(['valor'])!.value,
      detalle: this.editForm.get(['detalle'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value,
    };
  }
}
