import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IInfoLegal, InfoLegal } from '../info-legal.model';
import { InfoLegalService } from '../service/info-legal.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { ISucursal } from 'app/entities/sucursal/sucursal.model';
import { SucursalService } from 'app/entities/sucursal/service/sucursal.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-info-legal-update',
  templateUrl: './info-legal-update.component.html',
})
export class InfoLegalUpdateComponent implements OnInit {
  isSaving = false;

  empresasSharedCollection: IEmpresa[] = [];
  sucursalsSharedCollection: ISucursal[] = [];
  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    nit: [],
    regimen: [],
    resolucionPos: [],
    prefijoPosInicial: [],
    prefijoPosFinal: [],
    resolucionFacElec: [],
    prefijoFacElecFinal: [],
    resolucionNomElec: [],
    estado: [],
    fechaRegistro: [],
    empresaIds: [],
    sucursals: [],
    usuario: [],
  });

  constructor(
    protected infoLegalService: InfoLegalService,
    protected empresaService: EmpresaService,
    protected sucursalService: SucursalService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infoLegal }) => {
      if (infoLegal.id === undefined) {
        const today = dayjs().startOf('day');
        infoLegal.fechaRegistro = today;
      }

      this.updateForm(infoLegal);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const infoLegal = this.createFromForm();
    if (infoLegal.id !== undefined) {
      this.subscribeToSaveResponse(this.infoLegalService.update(infoLegal));
    } else {
      this.subscribeToSaveResponse(this.infoLegalService.create(infoLegal));
    }
  }

  trackEmpresaById(index: number, item: IEmpresa): number {
    return item.id!;
  }

  trackSucursalById(index: number, item: ISucursal): number {
    return item.id!;
  }

  trackUsuarioById(index: number, item: IUsuario): number {
    return item.id!;
  }

  getSelectedEmpresa(option: IEmpresa, selectedVals?: IEmpresa[]): IEmpresa {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedSucursal(option: ISucursal, selectedVals?: ISucursal[]): ISucursal {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInfoLegal>>): void {
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

  protected updateForm(infoLegal: IInfoLegal): void {
    this.editForm.patchValue({
      id: infoLegal.id,
      nit: infoLegal.nit,
      regimen: infoLegal.regimen,
      resolucionPos: infoLegal.resolucionPos,
      prefijoPosInicial: infoLegal.prefijoPosInicial,
      prefijoPosFinal: infoLegal.prefijoPosFinal,
      resolucionFacElec: infoLegal.resolucionFacElec,
      prefijoFacElecFinal: infoLegal.prefijoFacElecFinal,
      resolucionNomElec: infoLegal.resolucionNomElec,
      estado: infoLegal.estado,
      fechaRegistro: infoLegal.fechaRegistro ? infoLegal.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      empresaIds: infoLegal.empresaIds,
      sucursals: infoLegal.sucursals,
      usuario: infoLegal.usuario,
    });

    this.empresasSharedCollection = this.empresaService.addEmpresaToCollectionIfMissing(
      this.empresasSharedCollection,
      ...(infoLegal.empresaIds ?? [])
    );
    this.sucursalsSharedCollection = this.sucursalService.addSucursalToCollectionIfMissing(
      this.sucursalsSharedCollection,
      ...(infoLegal.sucursals ?? [])
    );
    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(this.usuariosSharedCollection, infoLegal.usuario);
  }

  protected loadRelationshipsOptions(): void {
    this.empresaService
      .query()
      .pipe(map((res: HttpResponse<IEmpresa[]>) => res.body ?? []))
      .pipe(
        map((empresas: IEmpresa[]) =>
          this.empresaService.addEmpresaToCollectionIfMissing(empresas, ...(this.editForm.get('empresaIds')!.value ?? []))
        )
      )
      .subscribe((empresas: IEmpresa[]) => (this.empresasSharedCollection = empresas));

    this.sucursalService
      .query()
      .pipe(map((res: HttpResponse<ISucursal[]>) => res.body ?? []))
      .pipe(
        map((sucursals: ISucursal[]) =>
          this.sucursalService.addSucursalToCollectionIfMissing(sucursals, ...(this.editForm.get('sucursals')!.value ?? []))
        )
      )
      .subscribe((sucursals: ISucursal[]) => (this.sucursalsSharedCollection = sucursals));

    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('usuario')!.value))
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }

  protected createFromForm(): IInfoLegal {
    return {
      ...new InfoLegal(),
      id: this.editForm.get(['id'])!.value,
      nit: this.editForm.get(['nit'])!.value,
      regimen: this.editForm.get(['regimen'])!.value,
      resolucionPos: this.editForm.get(['resolucionPos'])!.value,
      prefijoPosInicial: this.editForm.get(['prefijoPosInicial'])!.value,
      prefijoPosFinal: this.editForm.get(['prefijoPosFinal'])!.value,
      resolucionFacElec: this.editForm.get(['resolucionFacElec'])!.value,
      prefijoFacElecFinal: this.editForm.get(['prefijoFacElecFinal'])!.value,
      resolucionNomElec: this.editForm.get(['resolucionNomElec'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      empresaIds: this.editForm.get(['empresaIds'])!.value,
      sucursals: this.editForm.get(['sucursals'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
    };
  }
}
