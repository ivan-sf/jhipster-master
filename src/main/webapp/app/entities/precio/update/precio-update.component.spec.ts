import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PrecioService } from '../service/precio.service';
import { IPrecio, Precio } from '../precio.model';

import { PrecioUpdateComponent } from './precio-update.component';

describe('Precio Management Update Component', () => {
  let comp: PrecioUpdateComponent;
  let fixture: ComponentFixture<PrecioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let precioService: PrecioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PrecioUpdateComponent],
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
      .overrideTemplate(PrecioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrecioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    precioService = TestBed.inject(PrecioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const precio: IPrecio = { id: 456 };

      activatedRoute.data = of({ precio });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(precio));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Precio>>();
      const precio = { id: 123 };
      jest.spyOn(precioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ precio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: precio }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(precioService.update).toHaveBeenCalledWith(precio);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Precio>>();
      const precio = new Precio();
      jest.spyOn(precioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ precio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: precio }));
      saveSubject.complete();

      // THEN
      expect(precioService.create).toHaveBeenCalledWith(precio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Precio>>();
      const precio = { id: 123 };
      jest.spyOn(precioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ precio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(precioService.update).toHaveBeenCalledWith(precio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
