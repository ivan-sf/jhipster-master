import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISucursal, Sucursal } from '../sucursal.model';

import { SucursalService } from './sucursal.service';

describe('Sucursal Service', () => {
  let service: SucursalService;
  let httpMock: HttpTestingController;
  let elemDefault: ISucursal;
  let expectedResult: ISucursal | ISucursal[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SucursalService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      nit: 'AAAAAAA',
      detalle: 'AAAAAAA',
      direccion: 'AAAAAAA',
      direccionGPS: 'AAAAAAA',
      logoContentType: 'image/png',
      logo: 'AAAAAAA',
      estado: 0,
      fechaRegistro: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Sucursal', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaRegistro: currentDate,
        },
        returnedFromService
      );

      service.create(new Sucursal()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Sucursal', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          nit: 'BBBBBB',
          detalle: 'BBBBBB',
          direccion: 'BBBBBB',
          direccionGPS: 'BBBBBB',
          logo: 'BBBBBB',
          estado: 1,
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
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

    it('should partial update a Sucursal', () => {
      const patchObject = Object.assign(
        {
          detalle: 'BBBBBB',
          direccionGPS: 'BBBBBB',
          estado: 1,
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
        },
        new Sucursal()
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

    it('should return a list of Sucursal', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          nit: 'BBBBBB',
          detalle: 'BBBBBB',
          direccion: 'BBBBBB',
          direccionGPS: 'BBBBBB',
          logo: 'BBBBBB',
          estado: 1,
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
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

    it('should delete a Sucursal', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSucursalToCollectionIfMissing', () => {
      it('should add a Sucursal to an empty array', () => {
        const sucursal: ISucursal = { id: 123 };
        expectedResult = service.addSucursalToCollectionIfMissing([], sucursal);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sucursal);
      });

      it('should not add a Sucursal to an array that contains it', () => {
        const sucursal: ISucursal = { id: 123 };
        const sucursalCollection: ISucursal[] = [
          {
            ...sucursal,
          },
          { id: 456 },
        ];
        expectedResult = service.addSucursalToCollectionIfMissing(sucursalCollection, sucursal);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Sucursal to an array that doesn't contain it", () => {
        const sucursal: ISucursal = { id: 123 };
        const sucursalCollection: ISucursal[] = [{ id: 456 }];
        expectedResult = service.addSucursalToCollectionIfMissing(sucursalCollection, sucursal);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sucursal);
      });

      it('should add only unique Sucursal to an array', () => {
        const sucursalArray: ISucursal[] = [{ id: 123 }, { id: 456 }, { id: 64713 }];
        const sucursalCollection: ISucursal[] = [{ id: 123 }];
        expectedResult = service.addSucursalToCollectionIfMissing(sucursalCollection, ...sucursalArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sucursal: ISucursal = { id: 123 };
        const sucursal2: ISucursal = { id: 456 };
        expectedResult = service.addSucursalToCollectionIfMissing([], sucursal, sucursal2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sucursal);
        expect(expectedResult).toContain(sucursal2);
      });

      it('should accept null and undefined values', () => {
        const sucursal: ISucursal = { id: 123 };
        expectedResult = service.addSucursalToCollectionIfMissing([], null, sucursal, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sucursal);
      });

      it('should return initial array if no Sucursal is added', () => {
        const sucursalCollection: ISucursal[] = [{ id: 123 }];
        expectedResult = service.addSucursalToCollectionIfMissing(sucursalCollection, undefined, null);
        expect(expectedResult).toEqual(sucursalCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
