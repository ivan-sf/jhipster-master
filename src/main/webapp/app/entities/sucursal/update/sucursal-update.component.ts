import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISucursal, Sucursal } from '../sucursal.model';
import { SucursalService } from '../service/sucursal.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { WelcomeComponent } from 'app/welcome/welcome.component';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

@Component({
  selector: 'jhi-sucursal-update',
  templateUrl: './sucursal-update.component.html',
})
export class SucursalUpdateComponent implements OnInit {
  account: Account | null = null;
  isSaving = false;
  hidden = true;
  empresa: any;
  empresasSharedCollection: IEmpresa[] = [];
  auxS = WelcomeComponent.auxSucursal;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    nit: [],
    detalle: [],
    direccion: [],
    direccionGPS: [],
    logo: [],
    logoContentType: [],
    estado: [],
    fechaRegistro: [],
    empresa: [],
  });
  return: any | null;
  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected sucursalService: SucursalService,
    protected empresaService: EmpresaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private accountService: AccountService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (this.activatedRoute.snapshot.paramMap.get('return') !== '') {
      this.return = this.activatedRoute.snapshot.paramMap.get('return');
    }

    console.error(this.return);

    this.activatedRoute.data.subscribe(({ sucursal }) => {
      if (sucursal.id === undefined) {
        const today = dayjs().startOf('day');
        sucursal.fechaRegistro = today;
      }

      this.accountService.getAuthenticationState().subscribe(account => {
        this.account = account;
        this.checkEmpresa(sucursal);
      });
      // this.updateForm(sucursal);

      this.loadRelationshipsOptions();
    });
  }

  checkEmpresa(sucursal: any): void {
    this.empresaService
      .query({
        'userId.equals': this.account?.id,
      })
      .subscribe(success => {
        this.empresa = success.body![0];
        this.updateForm(sucursal);
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
    const sucursal = this.createFromForm();
    if (sucursal.id !== undefined) {
      this.subscribeToSaveResponse(this.sucursalService.update(sucursal));
    } else {
      this.subscribeToSaveResponse(this.sucursalService.create(sucursal));
    }
  }

  trackEmpresaById(index: number, item: IEmpresa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISucursal>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    if (this.return === 'welcome') {
      this.router.navigate(['empresa', this.empresa.id, 'edit', { return: 'sucursal' }]);
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

  protected updateForm(sucursal: ISucursal): void {
    this.editForm.patchValue({
      id: sucursal.id,
      nombre: sucursal.nombre === undefined ? this.empresa.nombre : sucursal.nombre,
      nit: sucursal.nit,
      detalle: sucursal.detalle,
      direccion: sucursal.direccion === undefined ? this.empresa.direccion : sucursal.direccion,
      direccionGPS: sucursal.direccionGPS,
      logo: sucursal.logo,
      logoContentType: sucursal.logoContentType,
      estado: sucursal.estado,
      fechaRegistro: sucursal.fechaRegistro ? sucursal.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      empresa: sucursal.empresa === undefined ? this.empresa : sucursal.empresa,
    });

    this.empresasSharedCollection = this.empresaService.addEmpresaToCollectionIfMissing(this.empresasSharedCollection, sucursal.empresa);
  }

  protected loadRelationshipsOptions(): void {
    this.empresaService
      .query()
      .pipe(map((res: HttpResponse<IEmpresa[]>) => res.body ?? []))
      .pipe(
        map((empresas: IEmpresa[]) => this.empresaService.addEmpresaToCollectionIfMissing(empresas, this.editForm.get('empresa')!.value))
      )
      .subscribe((empresas: IEmpresa[]) => (this.empresasSharedCollection = empresas));
  }

  protected createFromForm(): ISucursal {
    return {
      ...new Sucursal(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      nit: this.editForm.get(['nit'])!.value,
      detalle: this.editForm.get(['detalle'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      direccionGPS: this.editForm.get(['direccionGPS'])!.value,
      logoContentType: this.editForm.get(['logoContentType'])!.value,
      logo: this.editForm.get(['logo'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      empresa: this.editForm.get(['empresa'])!.value,
    };
  }
}
