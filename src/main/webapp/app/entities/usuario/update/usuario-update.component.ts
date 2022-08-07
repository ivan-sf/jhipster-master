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
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-usuario-update',
  templateUrl: './usuario-update.component.html',
})
export class UsuarioUpdateComponent implements OnInit {
  isSaving = false;
  hidden = true;
  account: Account | null = null;
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
  user: any | null;
  empresaId: any | null;
  empresa: any;
  usuario: any;
  sucursal: any;
  rol: any;

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected usuarioService: UsuarioService,
    protected userService: UserService,
    protected rolService: RolService,
    protected sucursalService: SucursalService,
    protected empresaService: EmpresaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.return = this.activatedRoute.snapshot.paramMap.get('return');
    this.empresaId = this.activatedRoute.snapshot.paramMap.get('empresa');

    if (this.return === 'welcome') {
      this.auxR = true;
      this.accountService.getAuthenticationState().subscribe(account => {
        this.account = account;
        this.getEmpresa();
        // this.getRol();
      });
    }

    this.activatedRoute.data.subscribe(({ usuario }) => {
      this.usuario = usuario;
      if (usuario.id === undefined) {
        const today = dayjs().startOf('day');
        usuario.edad = today;
        usuario.fechaRegistro = today;
      }
      if (this.return === null) {
        this.updateForm(this.usuario);
      }
      this.loadRelationshipsOptions();
    });
    WelcomeComponent.auxRepresentante = false;
  }

  getEmpresa(): void {
    this.empresaService
      .query({
        'id.equals': this.empresaId,
      })
      .subscribe(success => {
        this.empresa = success.body;
        this.getSucursal();
      });
  }

  getSucursal(): void {
    this.sucursalService
      .query({
        'empresaId.equals': this.empresaId,
      })
      .subscribe(success => {
        this.sucursal = success.body;
        this.getRol();
      });
  }

  getRol(): void {
    this.rolService
      .query({
        'empresaId.equals': this.empresaId,
        'nombre.equals': 'SUPER_ADMIN',
      })
      .subscribe(success => {
        this.rol = success.body![0];
        this.updateForm(this.usuario);
        // this.getRol();
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
    let newUser;
    let newEmpresa;
    let newSucursal;
    let newRol;
    this.account !== null && this.return === 'welcome' ? (newUser = this.account) : (newUser = usuario.user);
    this.empresa !== undefined && this.return === 'welcome' ? (newEmpresa = this.empresa) : (newEmpresa = usuario.empresaIds);
    this.sucursal !== undefined && this.return === 'welcome' ? (newSucursal = this.sucursal) : (newSucursal = usuario.sucursals);
    this.rol !== undefined && this.return === 'welcome' ? (newRol = this.rol) : (newRol = usuario.rol);

    console.error('this.rol', this.rol);
    console.error('newroil', newRol);
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
      user: newUser,
      rol: newRol,
      sucursals: newSucursal,
      empresaIds: newEmpresa,
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
