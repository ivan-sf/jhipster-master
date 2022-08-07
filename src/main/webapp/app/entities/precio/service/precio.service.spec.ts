import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPrecio, Precio } from '../precio.model';

import { PrecioService } from './precio.service';

describe('Precio Service', () => {
  let service: PrecioService;
  let httpMock: HttpTestingController;
  let elemDefault: IPrecio;
  let expectedResult: IPrecio | IPrecio[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PrecioService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      valor: 0,
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

    it('should create a Precio', () => {
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

      service.create(new Precio()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Precio', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          valor: 1,
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

    it('should partial update a Precio', () => {
      const patchObject = Object.assign(
        {
          valor: 1,
        },
        new Precio()
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

    it('should return a list of Precio', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          valor: 1,
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

    it('should delete a Precio', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPrecioToCollectionIfMissing', () => {
      it('should add a Precio to an empty array', () => {
        const precio: IPrecio = { id: 123 };
        expectedResult = service.addPrecioToCollectionIfMissing([], precio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(precio);
      });

      it('should not add a Precio to an array that contains it', () => {
        const precio: IPrecio = { id: 123 };
        const precioCollection: IPrecio[] = [
          {
            ...precio,
          },
          { id: 456 },
        ];
        expectedResult = service.addPrecioToCollectionIfMissing(precioCollection, precio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Precio to an array that doesn't contain it", () => {
        const precio: IPrecio = { id: 123 };
        const precioCollection: IPrecio[] = [{ id: 456 }];
        expectedResult = service.addPrecioToCollectionIfMissing(precioCollection, precio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(precio);
      });

      it('should add only unique Precio to an array', () => {
        const precioArray: IPrecio[] = [{ id: 123 }, { id: 456 }, { id: 9198 }];
        const precioCollection: IPrecio[] = [{ id: 123 }];
        expectedResult = service.addPrecioToCollectionIfMissing(precioCollection, ...precioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const precio: IPrecio = { id: 123 };
        const precio2: IPrecio = { id: 456 };
        expectedResult = service.addPrecioToCollectionIfMissing([], precio, precio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(precio);
        expect(expectedResult).toContain(precio2);
      });

      it('should accept null and undefined values', () => {
        const precio: IPrecio = { id: 123 };
        expectedResult = service.addPrecioToCollectionIfMissing([], null, precio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(precio);
      });

      it('should return initial array if no Precio is added', () => {
        const precioCollection: IPrecio[] = [{ id: 123 }];
        expectedResult = service.addPrecioToCollectionIfMissing(precioCollection, undefined, null);
        expect(expectedResult).toEqual(precioCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
