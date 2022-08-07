import { Component, Input, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEmpresa, Empresa } from '../empresa.model';
import { EmpresaService } from '../service/empresa.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ISucursal } from 'app/entities/sucursal/sucursal.model';
import { SucursalService } from 'app/entities/sucursal/service/sucursal.service';
import { WelcomeComponent } from 'app/welcome/welcome.component';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-empresa-update',
  templateUrl: './empresa-update.component.html',
  styleUrls: ['./empresa-update.component.scss'],
})
export class EmpresaUpdateComponent implements OnInit {
  account: Account | null = null;
  @Input() aaa: any = '';
  isSaving = false;
  hidden = true;
  usersSharedCollection: IUser[] = [];
  sucursalsSharedCollection: ISucursal[] = [];
  sucursales: any;
  empresaId: any;
  return: any = false;
  editForm = this.fb.group({
    id: [],
    nombre: [],
    direccion: [],
    direccionGPS: [],
    email: [],
    celular: [],
    indicativo: [],
    estado: [],
    fechaRegistro: [],
    user: [],
    sucursalIds: [],
  });

  constructor(
    protected empresaService: EmpresaService,
    protected userService: UserService,
    protected sucursalService: SucursalService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private accountService: AccountService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.return = this.activatedRoute.snapshot.paramMap.get('return');
    this.activatedRoute.data.subscribe(({ empresa }) => {
      this.empresaId = empresa.id;
      if (empresa.id === undefined) {
        const today = dayjs().startOf('day');
        empresa.fechaRegistro = today;
      }

      this.accountService.getAuthenticationState().subscribe(account => {
        this.account = account;
        if (this.empresaId !== undefined) {
          this.getSucursales(empresa);
        } else {
          this.updateForm(empresa);
        }
      });

      this.loadRelationshipsOptions();
    });
  }

  getSucursales(empresa: any): void {
    this.sucursalService
      .query({
        'empresaId.equals': this.empresaId,
      })
      .subscribe(success => {
        this.sucursales = success.body;
        this.updateForm(empresa);
        if (this.return !== null) {
          this.save();
        }
      });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empresa = this.createFromForm();
    if (empresa.id !== undefined) {
      this.subscribeToSaveResponse(this.empresaService.update(empresa));
    } else {
      this.subscribeToSaveResponse(this.empresaService.create(empresa));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackSucursalById(index: number, item: ISucursal): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpresa>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      success => {
        this.onSaveSuccess(success);
      },
      error => {
        this.onSaveError();
      }
    );
  }

  protected onSaveSuccess(empresa: any): void {
    if (this.return === 'welcome') {
      this.router.navigate(['rol', 'new', { return: 'welcome' }]);
    } else if (this.return === 'sucursal') {
      this.router.navigate(['inventario', 'new', { return: 'sucursal' }]);
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

  protected updateForm(empresa: IEmpresa): void {
    this.editForm.patchValue({
      id: empresa.id,
      nombre: empresa.nombre === undefined ? this.account?.lastName : empresa.nombre,
      direccion: empresa.direccion,
      direccionGPS: empresa.direccionGPS,
      email: empresa.email === undefined ? this.account?.email : empresa.email,
      celular: empresa.celular,
      indicativo: empresa.indicativo,
      estado: empresa.estado,
      fechaRegistro: empresa.fechaRegistro ? empresa.fechaRegistro.format(DATE_TIME_FORMAT) : null,
      user: empresa.user === undefined ? this.account : empresa.user,
      sucursalIds: empresa.sucursalIds,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, empresa.user);
    this.sucursalsSharedCollection = this.sucursalService.addSucursalToCollectionIfMissing(
      this.sucursalsSharedCollection,
      ...(empresa.sucursalIds ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.sucursalService
      .query()
      .pipe(map((res: HttpResponse<ISucursal[]>) => res.body ?? []))
      .pipe(
        map((sucursals: ISucursal[]) =>
          this.sucursalService.addSucursalToCollectionIfMissing(sucursals, ...(this.editForm.get('sucursalIds')!.value ?? []))
        )
      )
      .subscribe((sucursals: ISucursal[]) => (this.sucursalsSharedCollection = sucursals));
  }

  protected createFromForm(): IEmpresa {
    return {
      ...new Empresa(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      direccionGPS: this.editForm.get(['direccionGPS'])!.value,
      email: this.editForm.get(['email'])!.value,
      celular: this.editForm.get(['celular'])!.value,
      indicativo: this.editForm.get(['indicativo'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      fechaRegistro: this.editForm.get(['fechaRegistro'])!.value
        ? dayjs(this.editForm.get(['fechaRegistro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value,
      sucursalIds: [],
      // sucursalIds: this.editForm.get(['sucursalIds'])!.value,
      // sucursalIds: this.sucursales,
    };
  }
}
