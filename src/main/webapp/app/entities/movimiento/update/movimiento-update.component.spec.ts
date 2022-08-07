import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MovimientoService } from '../service/movimiento.service';
import { IMovimiento, Movimiento } from '../movimiento.model';

import { MovimientoUpdateComponent } from './movimiento-update.component';

describe('Movimiento Management Update Component', () => {
  let comp: MovimientoUpdateComponent;
  let fixture: ComponentFixture<MovimientoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let movimientoService: MovimientoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MovimientoUpdateComponent],
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
      .overrideTemplate(MovimientoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MovimientoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    movimientoService = TestBed.inject(MovimientoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const movimiento: IMovimiento = { id: 456 };

      activatedRoute.data = of({ movimiento });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(movimiento));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Movimiento>>();
      const movimiento = { id: 123 };
      jest.spyOn(movimientoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ movimiento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: movimiento }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(movimientoService.update).toHaveBeenCalledWith(movimiento);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Movimiento>>();
      const movimiento = new Movimiento();
      jest.spyOn(movimientoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ movimiento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: movimiento }));
      saveSubject.complete();

      // THEN
      expect(movimientoService.create).toHaveBeenCalledWith(movimiento);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Movimiento>>();
      const movimiento = { id: 123 };
      jest.spyOn(movimientoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ movimiento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(movimientoService.update).toHaveBeenCalledWith(movimiento);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
