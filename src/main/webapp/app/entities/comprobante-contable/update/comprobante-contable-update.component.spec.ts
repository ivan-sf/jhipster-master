import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ComprobanteContableService } from '../service/comprobante-contable.service';
import { IComprobanteContable, ComprobanteContable } from '../comprobante-contable.model';

import { ComprobanteContableUpdateComponent } from './comprobante-contable-update.component';

describe('ComprobanteContable Management Update Component', () => {
  let comp: ComprobanteContableUpdateComponent;
  let fixture: ComponentFixture<ComprobanteContableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let comprobanteContableService: ComprobanteContableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ComprobanteContableUpdateComponent],
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
      .overrideTemplate(ComprobanteContableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ComprobanteContableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    comprobanteContableService = TestBed.inject(ComprobanteContableService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const comprobanteContable: IComprobanteContable = { id: 456 };

      activatedRoute.data = of({ comprobanteContable });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(comprobanteContable));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ComprobanteContable>>();
      const comprobanteContable = { id: 123 };
      jest.spyOn(comprobanteContableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comprobanteContable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: comprobanteContable }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(comprobanteContableService.update).toHaveBeenCalledWith(comprobanteContable);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ComprobanteContable>>();
      const comprobanteContable = new ComprobanteContable();
      jest.spyOn(comprobanteContableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comprobanteContable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: comprobanteContable }));
      saveSubject.complete();

      // THEN
      expect(comprobanteContableService.create).toHaveBeenCalledWith(comprobanteContable);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ComprobanteContable>>();
      const comprobanteContable = { id: 123 };
      jest.spyOn(comprobanteContableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ comprobanteContable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(comprobanteContableService.update).toHaveBeenCalledWith(comprobanteContable);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
