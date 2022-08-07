import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInventario, Inventario } from '../inventario.model';
import { InventarioService } from '../service/inventario.service';
import { ISucursal } from 'app/entities/sucursal/sucursal.model';
import { SucursalService } from 'app/entities/sucursal/service/sucursal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { RolUpdateComponent } from 'app/entities/rol/update/rol-update.component';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';

@Component({
  selector: 'jhi-inventario-update',
  templateUrl: './inventario-update.component.html',
})
export class InventarioUpdateComponent implements OnInit {
  account: Account | null = null;
  isSaving = false;

  sucursalsSharedCollection: ISucursal[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    sucursal: [],
  });
  return: any | null;

  constructor(
    protected inventarioService: InventarioService,
    protected sucursalService: SucursalService,
    protected activatedRoute: ActivatedRoute,
    private accountService: AccountService,
    private empresaService: EmpresaService,
    private router: Router,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.return = this.activatedRoute.snapshot.paramMap.get('return');
    if (this.return === 'sucursal') {
      this.accountService.getAuthenticationState().subscribe(account => {
        this.account = account;
        this.getEmpresa();
      });
    }

    this.activatedRoute.data.subscribe(({ inventario }) => {
      this.updateForm(inventario);

      this.loadRelationshipsOptions();
    });
  }

  getEmpresa(): void {
    this.empresaService
      .query({
        'userId.equals': this.account?.id,
      })
      .subscribe(success => {
        const empresa = success.body![0];
        this.getSucursal(empresa);
      });
  }

  getSucursal(empresa: any) {
    this.sucursalService
      .query({
        'empresaId.equals': empresa.id,
      })
      .subscribe(success => {
        const sucursal = success.body![0];
        this.generateInventario(sucursal);
      });
  }

  generateInventario(sucursal: any): void {
    const inventario = { id: undefined, nombre: 'Inventario principal', sucursal };
    this.updateForm(inventario);
    this.save();
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inventario = this.createFromForm();
    if (inventario.id !== undefined) {
      this.subscribeToSaveResponse(this.inventarioService.update(inventario));
    } else {
      this.subscribeToSaveResponse(this.inventarioService.create(inventario));
    }
  }

  trackSucursalById(index: number, item: ISucursal): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInventario>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      success => this.onSaveSuccess(success),
      error => this.onSaveError()
    );
  }

  protected onSaveSuccess(inventario: any): void {
    if (this.return === 'sucursal') {
      this.router.navigate(['bodega', 'new', { return: 'sucursal', inventario: inventario.body.id }]);
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

  protected updateForm(inventario: IInventario): void {
    this.editForm.patchValue({
      id: inventario.id,
      nombre: inventario.nombre,
      sucursal: inventario.sucursal,
    });

    this.sucursalsSharedCollection = this.sucursalService.addSucursalToCollectionIfMissing(
      this.sucursalsSharedCollection,
      inventario.sucursal
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sucursalService
      .query()
      .pipe(map((res: HttpResponse<ISucursal[]>) => res.body ?? []))
      .pipe(
        map((sucursals: ISucursal[]) =>
          this.sucursalService.addSucursalToCollectionIfMissing(sucursals, this.editForm.get('sucursal')!.value)
        )
      )
      .subscribe((sucursals: ISucursal[]) => (this.sucursalsSharedCollection = sucursals));
  }

  protected createFromForm(): IInventario {
    return {
      ...new Inventario(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      sucursal: this.editForm.get(['sucursal'])!.value,
    };
  }
}
