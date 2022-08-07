import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TipoComprobanteContableService } from '../service/tipo-comprobante-contable.service';
import { ITipoComprobanteContable, TipoComprobanteContable } from '../tipo-comprobante-contable.model';

import { TipoComprobanteContableUpdateComponent } from './tipo-comprobante-contable-update.component';

describe('TipoComprobanteContable Management Update Component', () => {
  let comp: TipoComprobanteContableUpdateComponent;
  let fixture: ComponentFixture<TipoComprobanteContableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoComprobanteContableService: TipoComprobanteContableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TipoComprobanteContableUpdateComponent],
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
      .overrideTemplate(TipoComprobanteContableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoComprobanteContableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoComprobanteContableService = TestBed.inject(TipoComprobanteContableService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tipoComprobanteContable: ITipoComprobanteContable = { id: 456 };

      activatedRoute.data = of({ tipoComprobanteContable });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tipoComprobanteContable));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoComprobanteContable>>();
      const tipoComprobanteContable = { id: 123 };
      jest.spyOn(tipoComprobanteContableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoComprobanteContable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoComprobanteContable }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoComprobanteContableService.update).toHaveBeenCalledWith(tipoComprobanteContable);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoComprobanteContable>>();
      const tipoComprobanteContable = new TipoComprobanteContable();
      jest.spyOn(tipoComprobanteContableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoComprobanteContable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoComprobanteContable }));
      saveSubject.complete();

      // THEN
      expect(tipoComprobanteContableService.create).toHaveBeenCalledWith(tipoComprobanteContable);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoComprobanteContable>>();
      const tipoComprobanteContable = { id: 123 };
      jest.spyOn(tipoComprobanteContableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoComprobanteContable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoComprobanteContableService.update).toHaveBeenCalledWith(tipoComprobanteContable);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
