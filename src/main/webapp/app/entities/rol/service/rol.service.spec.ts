import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRol, Rol } from '../rol.model';

import { RolService } from './rol.service';

describe('Rol Service', () => {
  let service: RolService;
  let httpMock: HttpTestingController;
  let elemDefault: IRol;
  let expectedResult: IRol | IRol[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RolService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      descripcion: 'AAAAAAA',
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

    it('should create a Rol', () => {
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

      service.create(new Rol()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Rol', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
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

    it('should partial update a Rol', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          estado: 1,
        },
        new Rol()
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

    it('should return a list of Rol', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          descripcion: 'BBBBBB',
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

    it('should delete a Rol', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRolToCollectionIfMissing', () => {
      it('should add a Rol to an empty array', () => {
        const rol: IRol = { id: 123 };
        expectedResult = service.addRolToCollectionIfMissing([], rol);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rol);
      });

      it('should not add a Rol to an array that contains it', () => {
        const rol: IRol = { id: 123 };
        const rolCollection: IRol[] = [
          {
            ...rol,
          },
          { id: 456 },
        ];
        expectedResult = service.addRolToCollectionIfMissing(rolCollection, rol);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Rol to an array that doesn't contain it", () => {
        const rol: IRol = { id: 123 };
        const rolCollection: IRol[] = [{ id: 456 }];
        expectedResult = service.addRolToCollectionIfMissing(rolCollection, rol);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rol);
      });

      it('should add only unique Rol to an array', () => {
        const rolArray: IRol[] = [{ id: 123 }, { id: 456 }, { id: 95772 }];
        const rolCollection: IRol[] = [{ id: 123 }];
        expectedResult = service.addRolToCollectionIfMissing(rolCollection, ...rolArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rol: IRol = { id: 123 };
        const rol2: IRol = { id: 456 };
        expectedResult = service.addRolToCollectionIfMissing([], rol, rol2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rol);
        expect(expectedResult).toContain(rol2);
      });

      it('should accept null and undefined values', () => {
        const rol: IRol = { id: 123 };
        expectedResult = service.addRolToCollectionIfMissing([], null, rol, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rol);
      });

      it('should return initial array if no Rol is added', () => {
        const rolCollection: IRol[] = [{ id: 123 }];
        expectedResult = service.addRolToCollectionIfMissing(rolCollection, undefined, null);
        expect(expectedResult).toEqual(rolCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
