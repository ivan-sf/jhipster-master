import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TipoUsuarioService } from '../service/tipo-usuario.service';
import { ITipoUsuario, TipoUsuario } from '../tipo-usuario.model';

import { TipoUsuarioUpdateComponent } from './tipo-usuario-update.component';

describe('TipoUsuario Management Update Component', () => {
  let comp: TipoUsuarioUpdateComponent;
  let fixture: ComponentFixture<TipoUsuarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoUsuarioService: TipoUsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TipoUsuarioUpdateComponent],
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
      .overrideTemplate(TipoUsuarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoUsuarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoUsuarioService = TestBed.inject(TipoUsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tipoUsuario: ITipoUsuario = { id: 456 };

      activatedRoute.data = of({ tipoUsuario });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tipoUsuario));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoUsuario>>();
      const tipoUsuario = { id: 123 };
      jest.spyOn(tipoUsuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoUsuario }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoUsuarioService.update).toHaveBeenCalledWith(tipoUsuario);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoUsuario>>();
      const tipoUsuario = new TipoUsuario();
      jest.spyOn(tipoUsuarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoUsuario }));
      saveSubject.complete();

      // THEN
      expect(tipoUsuarioService.create).toHaveBeenCalledWith(tipoUsuario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoUsuario>>();
      const tipoUsuario = { id: 123 };
      jest.spyOn(tipoUsuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoUsuarioService.update).toHaveBeenCalledWith(tipoUsuario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
