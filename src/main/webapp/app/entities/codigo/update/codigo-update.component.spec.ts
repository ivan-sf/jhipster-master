import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CodigoService } from '../service/codigo.service';
import { ICodigo, Codigo } from '../codigo.model';

import { CodigoUpdateComponent } from './codigo-update.component';

describe('Codigo Management Update Component', () => {
  let comp: CodigoUpdateComponent;
  let fixture: ComponentFixture<CodigoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let codigoService: CodigoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CodigoUpdateComponent],
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
      .overrideTemplate(CodigoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CodigoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    codigoService = TestBed.inject(CodigoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const codigo: ICodigo = { id: 456 };

      activatedRoute.data = of({ codigo });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(codigo));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Codigo>>();
      const codigo = { id: 123 };
      jest.spyOn(codigoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codigo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: codigo }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(codigoService.update).toHaveBeenCalledWith(codigo);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Codigo>>();
      const codigo = new Codigo();
      jest.spyOn(codigoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codigo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: codigo }));
      saveSubject.complete();

      // THEN
      expect(codigoService.create).toHaveBeenCalledWith(codigo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Codigo>>();
      const codigo = { id: 123 };
      jest.spyOn(codigoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ codigo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(codigoService.update).toHaveBeenCalledWith(codigo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
