import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InfoLegalService } from '../service/info-legal.service';
import { IInfoLegal, InfoLegal } from '../info-legal.model';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { ISucursal } from 'app/entities/sucursal/sucursal.model';
import { SucursalService } from 'app/entities/sucursal/service/sucursal.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

import { InfoLegalUpdateComponent } from './info-legal-update.component';

describe('InfoLegal Management Update Component', () => {
  let comp: InfoLegalUpdateComponent;
  let fixture: ComponentFixture<InfoLegalUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let infoLegalService: InfoLegalService;
  let empresaService: EmpresaService;
  let sucursalService: SucursalService;
  let usuarioService: UsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InfoLegalUpdateComponent],
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
      .overrideTemplate(InfoLegalUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InfoLegalUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    infoLegalService = TestBed.inject(InfoLegalService);
    empresaService = TestBed.inject(EmpresaService);
    sucursalService = TestBed.inject(SucursalService);
    usuarioService = TestBed.inject(UsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Empresa query and add missing value', () => {
      const infoLegal: IInfoLegal = { id: 456 };
      const empresaIds: IEmpresa[] = [{ id: 55347 }];
      infoLegal.empresaIds = empresaIds;

      const empresaCollection: IEmpresa[] = [{ id: 52355 }];
      jest.spyOn(empresaService, 'query').mockReturnValue(of(new HttpResponse({ body: empresaCollection })));
      const additionalEmpresas = [...empresaIds];
      const expectedCollection: IEmpresa[] = [...additionalEmpresas, ...empresaCollection];
      jest.spyOn(empresaService, 'addEmpresaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ infoLegal });
      comp.ngOnInit();

      expect(empresaService.query).toHaveBeenCalled();
      expect(empresaService.addEmpresaToCollectionIfMissing).toHaveBeenCalledWith(empresaCollection, ...additionalEmpresas);
      expect(comp.empresasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Sucursal query and add missing value', () => {
      const infoLegal: IInfoLegal = { id: 456 };
      const sucursals: ISucursal[] = [{ id: 62759 }];
      infoLegal.sucursals = sucursals;

      const sucursalCollection: ISucursal[] = [{ id: 41482 }];
      jest.spyOn(sucursalService, 'query').mockReturnValue(of(new HttpResponse({ body: sucursalCollection })));
      const additionalSucursals = [...sucursals];
      const expectedCollection: ISucursal[] = [...additionalSucursals, ...sucursalCollection];
      jest.spyOn(sucursalService, 'addSucursalToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ infoLegal });
      comp.ngOnInit();

      expect(sucursalService.query).toHaveBeenCalled();
      expect(sucursalService.addSucursalToCollectionIfMissing).toHaveBeenCalledWith(sucursalCollection, ...additionalSucursals);
      expect(comp.sucursalsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Usuario query and add missing value', () => {
      const infoLegal: IInfoLegal = { id: 456 };
      const usuario: IUsuario = { id: 97918 };
      infoLegal.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 52485 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ infoLegal });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const infoLegal: IInfoLegal = { id: 456 };
      const empresaIds: IEmpresa = { id: 71790 };
      infoLegal.empresaIds = [empresaIds];
      const sucursals: ISucursal = { id: 81833 };
      infoLegal.sucursals = [sucursals];
      const usuario: IUsuario = { id: 61462 };
      infoLegal.usuario = usuario;

      activatedRoute.data = of({ infoLegal });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(infoLegal));
      expect(comp.empresasSharedCollection).toContain(empresaIds);
      expect(comp.sucursalsSharedCollection).toContain(sucursals);
      expect(comp.usuariosSharedCollection).toContain(usuario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InfoLegal>>();
      const infoLegal = { id: 123 };
      jest.spyOn(infoLegalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infoLegal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: infoLegal }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(infoLegalService.update).toHaveBeenCalledWith(infoLegal);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InfoLegal>>();
      const infoLegal = new InfoLegal();
      jest.spyOn(infoLegalService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infoLegal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: infoLegal }));
      saveSubject.complete();

      // THEN
      expect(infoLegalService.create).toHaveBeenCalledWith(infoLegal);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InfoLegal>>();
      const infoLegal = { id: 123 };
      jest.spyOn(infoLegalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infoLegal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(infoLegalService.update).toHaveBeenCalledWith(infoLegal);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEmpresaById', () => {
      it('Should return tracked Empresa primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEmpresaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSucursalById', () => {
      it('Should return tracked Sucursal primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSucursalById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUsuarioById', () => {
      it('Should return tracked Usuario primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUsuarioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedEmpresa', () => {
      it('Should return option if no Empresa is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedEmpresa(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Empresa for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedEmpresa(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Empresa is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedEmpresa(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedSucursal', () => {
      it('Should return option if no Sucursal is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedSucursal(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Sucursal for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedSucursal(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Sucursal is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedSucursal(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
