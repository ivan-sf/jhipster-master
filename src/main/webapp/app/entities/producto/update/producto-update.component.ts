import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProducto, Producto } from '../producto.model';
import { ProductoService } from '../service/producto.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICodigo } from 'app/entities/codigo/codigo.model';
import { CodigoService } from 'app/entities/codigo/service/codigo.service';
import { IPrecio } from 'app/entities/precio/precio.model';
import { PrecioService } from 'app/entities/precio/service/precio.service';
import { IBodega } from 'app/entities/bodega/bodega.model';
import { BodegaService } from 'app/entities/bodega/service/bodega.service';
import { IOficina } from 'app/entities/oficina/oficina.model';
import { OficinaService } from 'app/entities/oficina/service/oficina.service';

@Component({
  selector: 'jhi-producto-update',
  templateUrl: './producto-update.component.html',
})
export class ProductoUpdateComponent implements OnInit {
  isSaving = false;

  codigosSharedCollection: ICodigo[] = [];
  preciosSharedCollection: IPrecio[] = [];
  bodegasSharedCollection: IBodega[] = [];
  oficinasSharedCollection: IOficina[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    detalle: [],
    iva: [],
    foto: [],
    fotoContentType: [],
    fechaRegistro: [],
    codigos: [],
    precioIngresos: [],
    precioSalidas: [],
    bodega: [],
    oficina: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected productoService: ProductoService,
    protected codigoService: CodigoService,
    protected precioService: PrecioService,
    protected bodegaService: BodegaService,
    protected oficinaService: OficinaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ producto }) => {
      this.updateForm(producto);

      this.loadRelationshipsOptions();
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
    const producto = this.createFromForm();
    if (producto.id !== undefined) {
      this.subscribeToSaveResponse(this.productoService.update(producto));
    } else {
      this.subscribeToSaveResponse(this.productoService.create(producto));
    }
  }

  trackCodigoById(index: number, item: ICodigo): number {
    return item.id!;
  }

  trackPrecioById(index: number, item: IPrecio): number {
    return item.id!;
  }

  trackBodegaById(index: number, item: IBodega): number {
    return item.id!;
  }

  trackOficinaById(index: number, item: IOficina): number {
    return item.id!;
  }

  getSelectedCodigo(option: ICodigo, selectedVals?: ICodigo[]): ICodigo {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedPrecio(option: IPrecio, selectedVals?: IPrecio[]): IPrecio {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProducto>>): void {
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

  protected updateForm(producto: IProducto): void {
    this.editForm.patchValue({
      id: producto.id,
      nombre: producto.nombre,
      detalle: producto.detalle,
      iva: producto.iva,
      foto: producto.foto,
      fotoContentType: producto.fotoContentType,
      fechaRegistro: producto.fechaRegistro,
      codigos: producto.codigos,
      precioIngresos: producto.precioIngresos,
      precioSalidas: producto.precioSalidas,
      bodega: producto.bodega,
      oficina: producto.oficina,
    });

    this.codigosSharedCollection = this.codigoService.addCodigoToCollectionIfMissing(
      this.codigosSharedCollection,
      ...(producto.codigos ?? [])
    );
    this.preciosSharedCollection = this.precioService.addPrecioToCollectionIfMissing(
      this.preciosSharedCollection,
      ...(producto.precioIngresos ?? []),
      ...(producto.precioSalidas ?? [])
    );
    this.bodegasSharedCollection = this.bodegaService.addBodegaToCollectionIfMissing(this.bodegasSharedCollection, producto.bodega);
    this.oficinasSharedCollection = this.oficinaService.addOficinaToCollectionIfMissing(this.oficinasSharedCollection, producto.oficina);
  }

  protected loadRelationshipsOptions(): void {
    this.codigoService
      .query()
      .pipe(map((res: HttpResponse<ICodigo[]>) => res.body ?? []))
      .pipe(
        map((codigos: ICodigo[]) =>
          this.codigoService.addCodigoToCollectionIfMissing(codigos, ...(this.editForm.get('codigos')!.value ?? []))
        )
      )
      .subscribe((codigos: ICodigo[]) => (this.codigosSharedCollection = codigos));

    this.precioService
      .query()
      .pipe(map((res: HttpResponse<IPrecio[]>) => res.body ?? []))
      .pipe(
        map((precios: IPrecio[]) =>
          this.precioService.addPrecioToCollectionIfMissing(
            precios,
            ...(this.editForm.get('precioIngresos')!.value ?? []),
            ...(this.editForm.get('precioSalidas')!.value ?? [])
          )
        )
      )
      .subscribe((precios: IPrecio[]) => (this.preciosSharedCollection = precios));

    this.bodegaService
      .query()
      .pipe(map((res: HttpResponse<IBodega[]>) => res.body ?? []))
      .pipe(map((bodegas: IBodega[]) => this.bodegaService.addBodegaToCollectionIfMissing(bodegas, this.editForm.get('bodega')!.value)))
      .subscribe((bodegas: IBodega[]) => (this.bodegasSharedCollection = bodegas));

    this.oficinaService
      .query()
      .pipe(map((res: HttpResponse<IOficina[]>) => res.body ?? []))
      .pipe(
        map((oficinas: IOficina[]) => this.oficinaService.addOficinaToCollectionIfMissing(oficinas, this.editForm.get('oficina')!.value))
      )
      .subscribe((oficinas: IOficina[]) => (this.oficinasSharedCollection = oficinas));
  }

  protected createFromForm(): IProducto {
    return {
      ...new Producto(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      detalle: this.editForm.get(['detalle'])!.value,
      iva: this.editForm.get(['iva'])!.value,
      fotoContentType: this.editForm.get(['fotoContentType'])!.value,
      foto: this.editForm.get(['foto'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value,
      codigos: this.editForm.get(['codigos'])!.value,
      precioIngresos: this.editForm.get(['precioIngresos'])!.value,
      precioSalidas: this.editForm.get(['precioSalidas'])!.value,
      bodega: this.editForm.get(['bodega'])!.value,
      oficina: this.editForm.get(['oficina'])!.value,
    };
  }
}
