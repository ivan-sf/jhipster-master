import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IUsuario, Usuario } from '../usuario.model';
import { UsuarioService } from '../service/usuario.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IRol } from 'app/entities/rol/rol.model';
import { RolService } from 'app/entities/rol/service/rol.service';
import { ISucursal } from 'app/entities/sucursal/sucursal.model';
import { SucursalService } from 'app/entities/sucursal/service/sucursal.service';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { WelcomeComponent } from 'app/welcome/welcome.component';

@Component({
  selector: 'jhi-usuario-update',
  templateUrl: './usuario-update.component.html',
})
export class UsuarioUpdateComponent implements OnInit {
  isSaving = false;
  auxR = WelcomeComponent.auxRepresentante;
  usersSharedCollection: IUser[] = [];
  rolsCollection: IRol[] = [];
  sucursalsSharedCollection: ISucursal[] = [];
  empresasSharedCollection: IEmpresa[] = [];

  editForm = this.fb.group({
    id: [],
    primerNombre: [],
    segundoNombre: [],
    primerApellido: [],
    segundoApellido: [],
    tipoDocumento: [],
    documento: [],
    documentoDV: [],
    edad: [],
    indicativo: [],
    celular: [],
    direccion: [],
    direccionGps: [],
    foto: [],
    fotoContentType: [],
    fechaRegistro: [],
    user: [],
    rol: [],
    sucursals: [],
    empresaIds: [],
  });
  return: any | null;

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected usuarioService: UsuarioService,
    protected userService: UserService,
    protected rolService: RolService,
    protected sucursalService: SucursalService,
    protected empresaService: EmpresaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.return = this.activatedRoute.snapshot.paramMap.get('return');

    this.activatedRoute.data.subscribe(({ usuario }) => {
      if (usuario.id === undefined) {
        const today = dayjs().startOf('day');
        usuario.edad = today;
        usuario.fechaRegistro = today;
      }

      this.updateForm(usuario);

      this.loadRelationshipsOptions();
    });
    WelcomeComponent.auxRepresentante = false;
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
    const usuario = this.createFromForm();
    if (usuario.id !== undefined) {
      this.subscribeToSaveResponse(this.usuarioService.update(usuario));
    } else {
      this.subscribeToSaveResponse(this.usuarioService.create(usuario));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackRolById(index: number, item: IRol): number {
    return item.id!;
  }

  trackSucursalById(index: number, item: ISucursal): number {
    return item.id!;
  }

  trackEmpresaById(index: number, item: IEmpresa): number {
    return item.id!;
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsuario>>): void {
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

  protected updateForm(usuario: IUsuario): void {
    this.editForm.patchValue({
      id: usuario.id,
      primerNombre: usuario.primerNombre,
      segundoNombre: usuario.segundoNombre,
      primerApellido: usuario.primerApellido,
      segundoApellido: usuario.segundoApellido,
      tipoDocumento: usuario.tipoDocumento,
      documento: usuario.documento,
      documentoDV: usuario.documentoDV,
      edad: usuario.edad ? usuario.edad.format(DATE_TIME_FORMAT) : null,
      indicativo: usuario.indicativo,
      celular: usuario.celular,
      direccion: usuario.direccion,
      direccionGps: usuario.direccionGps,
      foto: usuario.foto,
      fotoContentType: usuario.fotoContentType,
      fechaRegistro: usuario.fechaRegistro ? usuario.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      user: usuario.user,
      rol: usuario.rol,
      sucursals: usuario.sucursals,
      empresaIds: usuario.empresaIds,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, usuario.user);
    this.rolsCollection = this.rolService.addRolToCollectionIfMissing(this.rolsCollection, usuario.rol);
    this.sucursalsSharedCollection = this.sucursalService.addSucursalToCollectionIfMissing(
      this.sucursalsSharedCollection,
      ...(usuario.sucursals ?? [])
    );
    this.empresasSharedCollection = this.empresaService.addEmpresaToCollectionIfMissing(
      this.empresasSharedCollection,
      ...(usuario.empresaIds ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.rolService
      .query({ 'usuarioId.specified': 'false' })
      .pipe(map((res: HttpResponse<IRol[]>) => res.body ?? []))
      .pipe(map((rols: IRol[]) => this.rolService.addRolToCollectionIfMissing(rols, this.editForm.get('rol')!.value)))
      .subscribe((rols: IRol[]) => (this.rolsCollection = rols));

    this.sucursalService
      .query()
      .pipe(map((res: HttpResponse<ISucursal[]>) => res.body ?? []))
      .pipe(
        map((sucursals: ISucursal[]) =>
          this.sucursalService.addSucursalToCollectionIfMissing(sucursals, ...(this.editForm.get('sucursals')!.value ?? []))
        )
      )
      .subscribe((sucursals: ISucursal[]) => (this.sucursalsSharedCollection = sucursals));

    this.empresaService
      .query()
      .pipe(map((res: HttpResponse<IEmpresa[]>) => res.body ?? []))
      .pipe(
        map((empresas: IEmpresa[]) =>
          this.empresaService.addEmpresaToCollectionIfMissing(empresas, ...(this.editForm.get('empresaIds')!.value ?? []))
        )
      )
      .subscribe((empresas: IEmpresa[]) => (this.empresasSharedCollection = empresas));
  }

  protected createFromForm(): IUsuario {
    return {
      ...new Usuario(),
      id: this.editForm.get(['id'])!.value,
      primerNombre: this.editForm.get(['primerNombre'])!.value,
      segundoNombre: this.editForm.get(['segundoNombre'])!.value,
      primerApellido: this.editForm.get(['primerApellido'])!.value,
      segundoApellido: this.editForm.get(['segundoApellido'])!.value,
      tipoDocumento: this.editForm.get(['tipoDocumento'])!.value,
      documento: this.editForm.get(['documento'])!.value,
      documentoDV: this.editForm.get(['documentoDV'])!.value,
      edad: this.editForm.get(['edad'])!.value ? dayjs(this.editForm.get(['edad'])!.value, DATE_TIME_FORMAT) : undefined,
      indicativo: this.editForm.get(['indicativo'])!.value,
      celular: this.editForm.get(['celular'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      direccionGps: this.editForm.get(['direccionGps'])!.value,
      fotoContentType: this.editForm.get(['fotoContentType'])!.value,
      foto: this.editForm.get(['foto'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value,
      rol: this.editForm.get(['rol'])!.value,
      sucursals: this.editForm.get(['sucursals'])!.value,
      empresaIds: this.editForm.get(['empresaIds'])!.value,
    };
  }
}
