import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BodegaService } from '../service/bodega.service';
import { IBodega, Bodega } from '../bodega.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IInventario } from 'app/entities/inventario/inventario.model';
import { InventarioService } from 'app/entities/inventario/service/inventario.service';

import { BodegaUpdateComponent } from './bodega-update.component';

describe('Bodega Management Update Component', () => {
  let comp: BodegaUpdateComponent;
  let fixture: ComponentFixture<BodegaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bodegaService: BodegaService;
  let usuarioService: UsuarioService;
  let inventarioService: InventarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BodegaUpdateComponent],
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
      .overrideTemplate(BodegaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BodegaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bodegaService = TestBed.inject(BodegaService);
    usuarioService = TestBed.inject(UsuarioService);
    inventarioService = TestBed.inject(InventarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Usuario query and add missing value', () => {
      const bodega: IBodega = { id: 456 };
      const usuarios: IUsuario[] = [{ id: 21119 }];
      bodega.usuarios = usuarios;

      const usuarioCollection: IUsuario[] = [{ id: 65456 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [...usuarios];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bodega });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Inventario query and add missing value', () => {
      const bodega: IBodega = { id: 456 };
      const inventario: IInventario = { id: 58470 };
      bodega.inventario = inventario;

      const inventarioCollection: IInventario[] = [{ id: 61217 }];
      jest.spyOn(inventarioService, 'query').mockReturnValue(of(new HttpResponse({ body: inventarioCollection })));
      const additionalInventarios = [inventario];
      const expectedCollection: IInventario[] = [...additionalInventarios, ...inventarioCollection];
      jest.spyOn(inventarioService, 'addInventarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bodega });
      comp.ngOnInit();

      expect(inventarioService.query).toHaveBeenCalled();
      expect(inventarioService.addInventarioToCollectionIfMissing).toHaveBeenCalledWith(inventarioCollection, ...additionalInventarios);
      expect(comp.inventariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bodega: IBodega = { id: 456 };
      const usuarios: IUsuario = { id: 65930 };
      bodega.usuarios = [usuarios];
      const inventario: IInventario = { id: 9720 };
      bodega.inventario = inventario;

      activatedRoute.data = of({ bodega });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(bodega));
      expect(comp.usuariosSharedCollection).toContain(usuarios);
      expect(comp.inventariosSharedCollection).toContain(inventario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bodega>>();
      const bodega = { id: 123 };
      jest.spyOn(bodegaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bodega });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bodega }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(bodegaService.update).toHaveBeenCalledWith(bodega);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bodega>>();
      const bodega = new Bodega();
      jest.spyOn(bodegaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bodega });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bodega }));
      saveSubject.complete();

      // THEN
      expect(bodegaService.create).toHaveBeenCalledWith(bodega);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bodega>>();
      const bodega = { id: 123 };
      jest.spyOn(bodegaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bodega });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bodegaService.update).toHaveBeenCalledWith(bodega);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUsuarioById', () => {
      it('Should return tracked Usuario primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUsuarioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackInventarioById', () => {
      it('Should return tracked Inventario primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInventarioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedUsuario', () => {
      it('Should return option if no Usuario is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedUsuario(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Usuario for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedUsuario(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Usuario is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedUsuario(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
