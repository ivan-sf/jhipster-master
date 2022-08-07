import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBodega, Bodega } from '../bodega.model';
import { BodegaService } from '../service/bodega.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IInventario } from 'app/entities/inventario/inventario.model';
import { InventarioService } from 'app/entities/inventario/service/inventario.service';

@Component({
  selector: 'jhi-bodega-update',
  templateUrl: './bodega-update.component.html',
})
export class BodegaUpdateComponent implements OnInit {
  isSaving = false;

  usuariosSharedCollection: IUsuario[] = [];
  inventariosSharedCollection: IInventario[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    detalle: [],
    ubicacion: [],
    usuarios: [],
    inventario: [],
  });
  return: any | null;
  inventario: any | null;

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected bodegaService: BodegaService,
    protected usuarioService: UsuarioService,
    protected inventarioService: InventarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.return = this.activatedRoute.snapshot.paramMap.get('return');
    this.inventario = this.activatedRoute.snapshot.paramMap.get('inventario');
    console.error('this.return', this.return);
    console.error('this.inventario', this.inventario);
    if (this.return === 'sucursal' && this.inventario !== null) {
      this.getInventario();
    }

    this.activatedRoute.data.subscribe(({ bodega }) => {
      this.updateForm(bodega);

      this.loadRelationshipsOptions();
    });
  }

  getInventario(): void {
    this.inventarioService
      .query({
        'id.equals': this.inventario,
      })
      .subscribe(success => {
        this.inventario = success.body![0];
        this.generateBodega();
      });
  }

  generateBodega(): void {
    const bodega = {
      id: undefined,
      nombre: 'Bodega Principal',
      detalle: undefined,
      ubicacion: undefined,
      usuarios: undefined,
      inventario: this.inventario,
    };
    this.updateForm(bodega);
    this.save();
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
    const bodega = this.createFromForm();
    if (bodega.id !== undefined) {
      this.subscribeToSaveResponse(this.bodegaService.update(bodega));
    } else {
      this.subscribeToSaveResponse(this.bodegaService.create(bodega));
    }
  }

  trackUsuarioById(index: number, item: IUsuario): number {
    return item.id!;
  }

  trackInventarioById(index: number, item: IInventario): number {
    return item.id!;
  }

  getSelectedUsuario(option: IUsuario, selectedVals?: IUsuario[]): IUsuario {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBodega>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    if (this.inventario !== null) {
      this.router.navigate(['/']);
    } else {
      this.previousState();
    }
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(bodega: IBodega): void {
    this.editForm.patchValue({
      id: bodega.id,
      nombre: bodega.nombre,
      detalle: bodega.detalle,
      ubicacion: bodega.ubicacion,
      usuarios: bodega.usuarios,
      inventario: bodega.inventario,
    });

    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(
      this.usuariosSharedCollection,
      ...(bodega.usuarios ?? [])
    );
    this.inventariosSharedCollection = this.inventarioService.addInventarioToCollectionIfMissing(
      this.inventariosSharedCollection,
      bodega.inventario
    );
  }

  protected loadRelationshipsOptions(): void {
    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) =>
          this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, ...(this.editForm.get('usuarios')!.value ?? []))
        )
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));

    this.inventarioService
      .query()
      .pipe(map((res: HttpResponse<IInventario[]>) => res.body ?? []))
      .pipe(
        map((inventarios: IInventario[]) =>
          this.inventarioService.addInventarioToCollectionIfMissing(inventarios, this.editForm.get('inventario')!.value)
        )
      )
      .subscribe((inventarios: IInventario[]) => (this.inventariosSharedCollection = inventarios));
  }

  protected createFromForm(): IBodega {
    return {
      ...new Bodega(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      detalle: this.editForm.get(['detalle'])!.value,
      ubicacion: this.editForm.get(['ubicacion'])!.value,
      usuarios: this.editForm.get(['usuarios'])!.value,
      inventario: this.editForm.get(['inventario'])!.value,
    };
  }
}
