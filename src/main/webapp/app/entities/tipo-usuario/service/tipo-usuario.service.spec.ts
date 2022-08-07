import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITipoUsuario, TipoUsuario } from '../tipo-usuario.model';

import { TipoUsuarioService } from './tipo-usuario.service';

describe('TipoUsuario Service', () => {
  let service: TipoUsuarioService;
  let httpMock: HttpTestingController;
  let elemDefault: ITipoUsuario;
  let expectedResult: ITipoUsuario | ITipoUsuario[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TipoUsuarioService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TipoUsuario', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TipoUsuario()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoUsuario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TipoUsuario', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new TipoUsuario()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoUsuario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TipoUsuario', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTipoUsuarioToCollectionIfMissing', () => {
      it('should add a TipoUsuario to an empty array', () => {
        const tipoUsuario: ITipoUsuario = { id: 123 };
        expectedResult = service.addTipoUsuarioToCollectionIfMissing([], tipoUsuario);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoUsuario);
      });

      it('should not add a TipoUsuario to an array that contains it', () => {
        const tipoUsuario: ITipoUsuario = { id: 123 };
        const tipoUsuarioCollection: ITipoUsuario[] = [
          {
            ...tipoUsuario,
          },
          { id: 456 },
        ];
        expectedResult = service.addTipoUsuarioToCollectionIfMissing(tipoUsuarioCollection, tipoUsuario);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoUsuario to an array that doesn't contain it", () => {
        const tipoUsuario: ITipoUsuario = { id: 123 };
        const tipoUsuarioCollection: ITipoUsuario[] = [{ id: 456 }];
        expectedResult = service.addTipoUsuarioToCollectionIfMissing(tipoUsuarioCollection, tipoUsuario);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoUsuario);
      });

      it('should add only unique TipoUsuario to an array', () => {
        const tipoUsuarioArray: ITipoUsuario[] = [{ id: 123 }, { id: 456 }, { id: 38003 }];
        const tipoUsuarioCollection: ITipoUsuario[] = [{ id: 123 }];
        expectedResult = service.addTipoUsuarioToCollectionIfMissing(tipoUsuarioCollection, ...tipoUsuarioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoUsuario: ITipoUsuario = { id: 123 };
        const tipoUsuario2: ITipoUsuario = { id: 456 };
        expectedResult = service.addTipoUsuarioToCollectionIfMissing([], tipoUsuario, tipoUsuario2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoUsuario);
        expect(expectedResult).toContain(tipoUsuario2);
      });

      it('should accept null and undefined values', () => {
        const tipoUsuario: ITipoUsuario = { id: 123 };
        expectedResult = service.addTipoUsuarioToCollectionIfMissing([], null, tipoUsuario, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoUsuario);
      });

      it('should return initial array if no TipoUsuario is added', () => {
        const tipoUsuarioCollection: ITipoUsuario[] = [{ id: 123 }];
        expectedResult = service.addTipoUsuarioToCollectionIfMissing(tipoUsuarioCollection, undefined, null);
        expect(expectedResult).toEqual(tipoUsuarioCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
