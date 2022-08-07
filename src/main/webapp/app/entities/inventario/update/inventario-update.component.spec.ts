import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InventarioService } from '../service/inventario.service';
import { IInventario, Inventario } from '../inventario.model';
import { ISucursal } from 'app/entities/sucursal/sucursal.model';
import { SucursalService } from 'app/entities/sucursal/service/sucursal.service';

import { InventarioUpdateComponent } from './inventario-update.component';

describe('Inventario Management Update Component', () => {
  let comp: InventarioUpdateComponent;
  let fixture: ComponentFixture<InventarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inventarioService: InventarioService;
  let sucursalService: SucursalService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InventarioUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(InventarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InventarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inventarioService = TestBed.inject(InventarioService);
    sucursalService = TestBed.inject(SucursalService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Sucursal query and add missing value', () => {
      const inventario: IInventario = { id: 456 };
      const sucursal: ISucursal = { id: 50341 };
      inventario.sucursal = sucursal;

      const sucursalCollection: ISucursal[] = [{ id: 70056 }];
      jest.spyOn(sucursalService, 'query').mockReturnValue(of(new HttpResponse({ body: sucursalCollection })));
      const additionalSucursals = [sucursal];
      const expectedCollection: ISucursal[] = [...additionalSucursals, ...sucursalCollection];
      jest.spyOn(sucursalService, 'addSucursalToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventario });
      comp.ngOnInit();

      expect(sucursalService.query).toHaveBeenCalled();
      expect(sucursalService.addSucursalToCollectionIfMissing).toHaveBeenCalledWith(sucursalCollection, ...additionalSucursals);
      expect(comp.sucursalsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const inventario: IInventario = { id: 456 };
      const sucursal: ISucursal = { id: 48085 };
      inventario.sucursal = sucursal;

      activatedRoute.data = of({ inventario });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(inventario));
      expect(comp.sucursalsSharedCollection).toContain(sucursal);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Inventario>>();
      const inventario = { id: 123 };
      jest.spyOn(inventarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inventario }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(inventarioService.update).toHaveBeenCalledWith(inventario);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Inventario>>();
      const inventario = new Inventario();
      jest.spyOn(inventarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inventario }));
      saveSubject.complete();

      // THEN
      expect(inventarioService.create).toHaveBeenCalledWith(inventario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Inventario>>();
      const inventario = { id: 123 };
      jest.spyOn(inventarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inventarioService.update).toHaveBeenCalledWith(inventario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSucursalById', () => {
      it('Should return tracked Sucursal primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSucursalById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
