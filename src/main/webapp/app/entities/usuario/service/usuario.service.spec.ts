import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUsuario, Usuario } from '../usuario.model';

import { UsuarioService } from './usuario.service';

describe('Usuario Service', () => {
  let service: UsuarioService;
  let httpMock: HttpTestingController;
  let elemDefault: IUsuario;
  let expectedResult: IUsuario | IUsuario[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UsuarioService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      primerNombre: 'AAAAAAA',
      segundoNombre: 'AAAAAAA',
      primerApellido: 'AAAAAAA',
      segundoApellido: 'AAAAAAA',
      tipoDocumento: 'AAAAAAA',
      documento: 'AAAAAAA',
      documentoDV: 'AAAAAAA',
      edad: currentDate,
      indicativo: 'AAAAAAA',
      celular: 'AAAAAAA',
      direccion: 'AAAAAAA',
      direccionGps: 'AAAAAAA',
      fotoContentType: 'image/png',
      foto: 'AAAAAAA',
      fechaRegistro: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          edad: currentDate.format(DATE_TIME_FORMAT),
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Usuario', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          edad: currentDate.format(DATE_TIME_FORMAT),
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          edad: currentDate,
          fechaRegistro: currentDate,
        },
        returnedFromService
      );

      service.create(new Usuario()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Usuario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          primerNombre: 'BBBBBB',
          segundoNombre: 'BBBBBB',
          primerApellido: 'BBBBBB',
          segundoApellido: 'BBBBBB',
          tipoDocumento: 'BBBBBB',
          documento: 'BBBBBB',
          documentoDV: 'BBBBBB',
          edad: currentDate.format(DATE_TIME_FORMAT),
          indicativo: 'BBBBBB',
          celular: 'BBBBBB',
          direccion: 'BBBBBB',
          direccionGps: 'BBBBBB',
          foto: 'BBBBBB',
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          edad: currentDate,
          fechaRegistro: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Usuario', () => {
      const patchObject = Object.assign(
        {
          segundoNombre: 'BBBBBB',
          tipoDocumento: 'BBBBBB',
          documentoDV: 'BBBBBB',
          edad: currentDate.format(DATE_TIME_FORMAT),
          indicativo: 'BBBBBB',
          celular: 'BBBBBB',
          direccion: 'BBBBBB',
          direccionGps: 'BBBBBB',
          foto: 'BBBBBB',
        },
        new Usuario()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          edad: currentDate,
          fechaRegistro: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Usuario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          primerNombre: 'BBBBBB',
          segundoNombre: 'BBBBBB',
          primerApellido: 'BBBBBB',
          segundoApellido: 'BBBBBB',
          tipoDocumento: 'BBBBBB',
          documento: 'BBBBBB',
          documentoDV: 'BBBBBB',
          edad: currentDate.format(DATE_TIME_FORMAT),
          indicativo: 'BBBBBB',
          celular: 'BBBBBB',
          direccion: 'BBBBBB',
          direccionGps: 'BBBBBB',
          foto: 'BBBBBB',
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          edad: currentDate,
          fechaRegistro: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Usuario', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUsuarioToCollectionIfMissing', () => {
      it('should add a Usuario to an empty array', () => {
        const usuario: IUsuario = { id: 123 };
        expectedResult = service.addUsuarioToCollectionIfMissing([], usuario);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(usuario);
      });

      it('should not add a Usuario to an array that contains it', () => {
        const usuario: IUsuario = { id: 123 };
        const usuarioCollection: IUsuario[] = [
          {
            ...usuario,
          },
          { id: 456 },
        ];
        expectedResult = service.addUsuarioToCollectionIfMissing(usuarioCollection, usuario);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Usuario to an array that doesn't contain it", () => {
        const usuario: IUsuario = { id: 123 };
        const usuarioCollection: IUsuario[] = [{ id: 456 }];
        expectedResult = service.addUsuarioToCollectionIfMissing(usuarioCollection, usuario);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(usuario);
      });

      it('should add only unique Usuario to an array', () => {
        const usuarioArray: IUsuario[] = [{ id: 123 }, { id: 456 }, { id: 8647 }];
        const usuarioCollection: IUsuario[] = [{ id: 123 }];
        expectedResult = service.addUsuarioToCollectionIfMissing(usuarioCollection, ...usuarioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const usuario: IUsuario = { id: 123 };
        const usuario2: IUsuario = { id: 456 };
        expectedResult = service.addUsuarioToCollectionIfMissing([], usuario, usuario2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(usuario);
        expect(expectedResult).toContain(usuario2);
      });

      it('should accept null and undefined values', () => {
        const usuario: IUsuario = { id: 123 };
        expectedResult = service.addUsuarioToCollectionIfMissing([], null, usuario, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(usuario);
      });

      it('should return initial array if no Usuario is added', () => {
        const usuarioCollection: IUsuario[] = [{ id: 123 }];
        expectedResult = service.addUsuarioToCollectionIfMissing(usuarioCollection, undefined, null);
        expect(expectedResult).toEqual(usuarioCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
