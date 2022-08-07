import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductoService } from '../service/producto.service';
import { IProducto, Producto } from '../producto.model';
import { ICodigo } from 'app/entities/codigo/codigo.model';
import { CodigoService } from 'app/entities/codigo/service/codigo.service';
import { IPrecio } from 'app/entities/precio/precio.model';
import { PrecioService } from 'app/entities/precio/service/precio.service';
import { IBodega } from 'app/entities/bodega/bodega.model';
import { BodegaService } from 'app/entities/bodega/service/bodega.service';
import { IOficina } from 'app/entities/oficina/oficina.model';
import { OficinaService } from 'app/entities/oficina/service/oficina.service';

import { ProductoUpdateComponent } from './producto-update.component';

describe('Producto Management Update Component', () => {
  let comp: ProductoUpdateComponent;
  let fixture: ComponentFixture<ProductoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productoService: ProductoService;
  let codigoService: CodigoService;
  let precioService: PrecioService;
  let bodegaService: BodegaService;
  let oficinaService: OficinaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductoUpdateComponent],
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
      .overrideTemplate(ProductoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productoService = TestBed.inject(ProductoService);
    codigoService = TestBed.inject(CodigoService);
    precioService = TestBed.inject(PrecioService);
    bodegaService = TestBed.inject(BodegaService);
    oficinaService = TestBed.inject(OficinaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Codigo query and add missing value', () => {
      const producto: IProducto = { id: 456 };
      const codigos: ICodigo[] = [{ id: 69481 }];
      producto.codigos = codigos;

      const codigoCollection: ICodigo[] = [{ id: 28188 }];
      jest.spyOn(codigoService, 'query').mockReturnValue(of(new HttpResponse({ body: codigoCollection })));
      const additionalCodigos = [...codigos];
      const expectedCollection: ICodigo[] = [...additionalCodigos, ...codigoCollection];
      jest.spyOn(codigoService, 'addCodigoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      expect(codigoService.query).toHaveBeenCalled();
      expect(codigoService.addCodigoToCollectionIfMissing).toHaveBeenCalledWith(codigoCollection, ...additionalCodigos);
      expect(comp.codigosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Precio query and add missing value', () => {
      const producto: IProducto = { id: 456 };
      const precioIngresos: IPrecio[] = [{ id: 13869 }];
      producto.precioIngresos = precioIngresos;
      const precioSalidas: IPrecio[] = [{ id: 23258 }];
      producto.precioSalidas = precioSalidas;

      const precioCollection: IPrecio[] = [{ id: 29053 }];
      jest.spyOn(precioService, 'query').mockReturnValue(of(new HttpResponse({ body: precioCollection })));
      const additionalPrecios = [...precioIngresos, ...precioSalidas];
      const expectedCollection: IPrecio[] = [...additionalPrecios, ...precioCollection];
      jest.spyOn(precioService, 'addPrecioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      expect(precioService.query).toHaveBeenCalled();
      expect(precioService.addPrecioToCollectionIfMissing).toHaveBeenCalledWith(precioCollection, ...additionalPrecios);
      expect(comp.preciosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Bodega query and add missing value', () => {
      const producto: IProducto = { id: 456 };
      const bodega: IBodega = { id: 17310 };
      producto.bodega = bodega;

      const bodegaCollection: IBodega[] = [{ id: 447 }];
      jest.spyOn(bodegaService, 'query').mockReturnValue(of(new HttpResponse({ body: bodegaCollection })));
      const additionalBodegas = [bodega];
      const expectedCollection: IBodega[] = [...additionalBodegas, ...bodegaCollection];
      jest.spyOn(bodegaService, 'addBodegaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      expect(bodegaService.query).toHaveBeenCalled();
      expect(bodegaService.addBodegaToCollectionIfMissing).toHaveBeenCalledWith(bodegaCollection, ...additionalBodegas);
      expect(comp.bodegasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Oficina query and add missing value', () => {
      const producto: IProducto = { id: 456 };
      const oficina: IOficina = { id: 32827 };
      producto.oficina = oficina;

      const oficinaCollection: IOficina[] = [{ id: 69982 }];
      jest.spyOn(oficinaService, 'query').mockReturnValue(of(new HttpResponse({ body: oficinaCollection })));
      const additionalOficinas = [oficina];
      const expectedCollection: IOficina[] = [...additionalOficinas, ...oficinaCollection];
      jest.spyOn(oficinaService, 'addOficinaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      expect(oficinaService.query).toHaveBeenCalled();
      expect(oficinaService.addOficinaToCollectionIfMissing).toHaveBeenCalledWith(oficinaCollection, ...additionalOficinas);
      expect(comp.oficinasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const producto: IProducto = { id: 456 };
      const codigos: ICodigo = { id: 11637 };
      producto.codigos = [codigos];
      const precioIngresos: IPrecio = { id: 56860 };
      producto.precioIngresos = [precioIngresos];
      const precioSalidas: IPrecio = { id: 98028 };
      producto.precioSalidas = [precioSalidas];
      const bodega: IBodega = { id: 79164 };
      producto.bodega = bodega;
      const oficina: IOficina = { id: 81516 };
      producto.oficina = oficina;

      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(producto));
      expect(comp.codigosSharedCollection).toContain(codigos);
      expect(comp.preciosSharedCollection).toContain(precioIngresos);
      expect(comp.preciosSharedCollection).toContain(precioSalidas);
      expect(comp.bodegasSharedCollection).toContain(bodega);
      expect(comp.oficinasSharedCollection).toContain(oficina);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Producto>>();
      const producto = { id: 123 };
      jest.spyOn(productoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: producto }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(productoService.update).toHaveBeenCalledWith(producto);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Producto>>();
      const producto = new Producto();
      jest.spyOn(productoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: producto }));
      saveSubject.complete();

      // THEN
      expect(productoService.create).toHaveBeenCalledWith(producto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Producto>>();
      const producto = { id: 123 };
      jest.spyOn(productoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productoService.update).toHaveBeenCalledWith(producto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCodigoById', () => {
      it('Should return tracked Codigo primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCodigoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPrecioById', () => {
      it('Should return tracked Precio primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPrecioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackBodegaById', () => {
      it('Should return tracked Bodega primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBodegaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackOficinaById', () => {
      it('Should return tracked Oficina primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOficinaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedCodigo', () => {
      it('Should return option if no Codigo is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedCodigo(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Codigo for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedCodigo(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Codigo is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedCodigo(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedPrecio', () => {
      it('Should return option if no Precio is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPrecio(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Precio for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPrecio(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Precio is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPrecio(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
