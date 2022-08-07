import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICodigo, Codigo } from '../codigo.model';

import { CodigoService } from './codigo.service';

describe('Codigo Service', () => {
  let service: CodigoService;
  let httpMock: HttpTestingController;
  let elemDefault: ICodigo;
  let expectedResult: ICodigo | ICodigo[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CodigoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      codigo: 'AAAAAAA',
      detalle: 'AAAAAAA',
      fechaRegistro: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaRegistro: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Codigo', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaRegistro: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaRegistro: currentDate,
        },
        returnedFromService
      );

      service.create(new Codigo()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Codigo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          codigo: 'BBBBBB',
          detalle: 'BBBBBB',
          fechaRegistro: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaRegistro: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Codigo', () => {
      const patchObject = Object.assign(
        {
          fechaRegistro: currentDate.format(DATE_FORMAT),
        },
        new Codigo()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaRegistro: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Codigo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          codigo: 'BBBBBB',
          detalle: 'BBBBBB',
          fechaRegistro: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
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

    it('should delete a Codigo', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCodigoToCollectionIfMissing', () => {
      it('should add a Codigo to an empty array', () => {
        const codigo: ICodigo = { id: 123 };
        expectedResult = service.addCodigoToCollectionIfMissing([], codigo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(codigo);
      });

      it('should not add a Codigo to an array that contains it', () => {
        const codigo: ICodigo = { id: 123 };
        const codigoCollection: ICodigo[] = [
          {
            ...codigo,
          },
          { id: 456 },
        ];
        expectedResult = service.addCodigoToCollectionIfMissing(codigoCollection, codigo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Codigo to an array that doesn't contain it", () => {
        const codigo: ICodigo = { id: 123 };
        const codigoCollection: ICodigo[] = [{ id: 456 }];
        expectedResult = service.addCodigoToCollectionIfMissing(codigoCollection, codigo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(codigo);
      });

      it('should add only unique Codigo to an array', () => {
        const codigoArray: ICodigo[] = [{ id: 123 }, { id: 456 }, { id: 91049 }];
        const codigoCollection: ICodigo[] = [{ id: 123 }];
        expectedResult = service.addCodigoToCollectionIfMissing(codigoCollection, ...codigoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const codigo: ICodigo = { id: 123 };
        const codigo2: ICodigo = { id: 456 };
        expectedResult = service.addCodigoToCollectionIfMissing([], codigo, codigo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(codigo);
        expect(expectedResult).toContain(codigo2);
      });

      it('should accept null and undefined values', () => {
        const codigo: ICodigo = { id: 123 };
        expectedResult = service.addCodigoToCollectionIfMissing([], null, codigo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(codigo);
      });

      it('should return initial array if no Codigo is added', () => {
        const codigoCollection: ICodigo[] = [{ id: 123 }];
        expectedResult = service.addCodigoToCollectionIfMissing(codigoCollection, undefined, null);
        expect(expectedResult).toEqual(codigoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
