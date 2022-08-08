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
  styleUrls: ['./info-legal-update.component.scss'],
})
export class InfoLegalUpdateComponent implements OnInit {
  isSaving = false;
  hidden = true;
  posHidden = true;
  feHidden = true;
  nomHidden = true;

  empresasSharedCollection: IEmpresa[] = [];
  sucursalsSharedCollection: ISucursal[] = [];
  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    nit: [],
    regimen: [],
    prefijoFE: [],
    prefijoPOS: [],
    prefijoNOM: [],
    resolucionPos: [],
    prefijoPosInicial: [],
    prefijoPosFinal: [],
    resolucionFacElec: [],
    prefijoFacElecInicial: [],
    prefijoFacElecFinal: [],
    resolucionNomElec: [],
    prefijoNomElecInicial: [],
    prefijoNomElecFinal: [],
    estado: [],
    fechaRegistro: [],
    empresaIds: [],
    sucursals: [],
    usuario: [],
  });

  return: any | null;
  usuarioId: any | null;
  empresaId: any | null;
  sucursalId: any | null;
  infoLegal: any;

  constructor(
    protected infoLegalService: InfoLegalService,
    protected empresaService: EmpresaService,
    protected sucursalService: SucursalService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.return = this.activatedRoute.snapshot.paramMap.get('return');
    this.usuarioId = this.activatedRoute.snapshot.paramMap.get('usuario');
    this.empresaId = this.activatedRoute.snapshot.paramMap.get('empresa');
    this.sucursalId = this.activatedRoute.snapshot.paramMap.get('sucursal');

    if (this.return === 'welcome') {
      this.getEmpresa();
    }

    this.activatedRoute.data.subscribe(({ infoLegal }) => {
      this.infoLegal = infoLegal;
      if (infoLegal.id === undefined) {
        const today = dayjs().startOf('day');
        infoLegal.fechaRegistro = today;
      }
      if (this.return === null) {
        this.updateForm(this.infoLegal);
      }
      this.loadRelationshipsOptions();
    });
  }

  getEmpresa(): void {
    this.empresaService
      .query({
        'id.equals': this.empresaId,
      })
      .subscribe(success => {
        this.empresaId = success.body;
        this.getSucursal();
      });
  }

  getSucursal(): void {
    this.sucursalService
      .query({
        'id.equals': this.sucursalId,
      })
      .subscribe(success => {
        this.sucursalId = success.body;
        this.getUsuario();
        // this.getSucursal()
      });
  }

  getUsuario(): void {
    this.usuarioService
      .query({
        'id.equals': this.usuarioId,
      })
      .subscribe(success => {
        this.usuarioId = success.body![0];
        this.updateForm(this.infoLegal);
        // this.getSucursal()
      });
  }

  change(type: any): void {
    console.error('type', type);
    type === 'pos' ? (this.posHidden = false) : null;
    type === 'fe' ? (this.feHidden = false) : null;
    type === 'nom' ? (this.nomHidden = false) : null;
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
    let auxEmpresa;
    let auxSucursal;
    let auxUsuario;
    this.empresaId !== null ? (auxEmpresa = this.empresaId) : (auxEmpresa = infoLegal.empresaIds);
    this.sucursalId !== null ? (auxSucursal = this.sucursalId) : (auxSucursal = infoLegal.sucursals);
    this.usuarioId !== null ? (auxUsuario = this.usuarioId) : (auxUsuario = infoLegal.usuario);
    this.editForm.patchValue({
      id: infoLegal.id,
      nit: infoLegal.nit,
      regimen: infoLegal.regimen,
      prefijoFE: infoLegal.prefijoFE,
      prefijoPOS: infoLegal.prefijoPOS,
      prefijoNOM: infoLegal.prefijoNOM,
      resolucionPos: infoLegal.resolucionPos,
      prefijoPosInicial: infoLegal.prefijoPosInicial,
      prefijoPosFinal: infoLegal.prefijoPosFinal,
      resolucionFacElec: infoLegal.resolucionFacElec,
      prefijoFacElecInicial: infoLegal.prefijoFacElecInicial,
      prefijoFacElecFinal: infoLegal.prefijoFacElecFinal,
      resolucionNomElec: infoLegal.resolucionNomElec,
      prefijoNomElecInicial: infoLegal.prefijoNomElecInicial,
      prefijoNomElecFinal: infoLegal.prefijoNomElecFinal,
      estado: infoLegal.estado,
      fechaRegistro: infoLegal.fechaRegistro ? infoLegal.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      empresaIds: auxEmpresa,
      sucursals: auxSucursal,
      usuario: auxUsuario,
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
      prefijoFE: this.editForm.get(['prefijoFE'])!.value,
      prefijoPOS: this.editForm.get(['prefijoPOS'])!.value,
      prefijoNOM: this.editForm.get(['prefijoNOM'])!.value,
      resolucionPos: this.editForm.get(['resolucionPos'])!.value,
      prefijoPosInicial: this.editForm.get(['prefijoPosInicial'])!.value,
      prefijoPosFinal: this.editForm.get(['prefijoPosFinal'])!.value,
      resolucionFacElec: this.editForm.get(['resolucionFacElec'])!.value,
      prefijoFacElecInicial: this.editForm.get(['prefijoFacElecInicial'])!.value,
      prefijoFacElecFinal: this.editForm.get(['prefijoFacElecFinal'])!.value,
      resolucionNomElec: this.editForm.get(['resolucionNomElec'])!.value,
      prefijoNomElecInicial: this.editForm.get(['prefijoNomElecInicial'])!.value,
      prefijoNomElecFinal: this.editForm.get(['prefijoNomElecFinal'])!.value,
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
