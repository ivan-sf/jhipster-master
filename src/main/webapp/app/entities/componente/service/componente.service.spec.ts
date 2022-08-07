import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IComponente, Componente } from '../componente.model';

import { ComponenteService } from './componente.service';

describe('Componente Service', () => {
  let service: ComponenteService;
  let httpMock: HttpTestingController;
  let elemDefault: IComponente;
  let expectedResult: IComponente | IComponente[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ComponenteService);
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

    it('should create a Componente', () => {
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

      service.create(new Componente()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Componente', () => {
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

    it('should partial update a Componente', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
        },
        new Componente()
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

    it('should return a list of Componente', () => {
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

    it('should delete a Componente', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addComponenteToCollectionIfMissing', () => {
      it('should add a Componente to an empty array', () => {
        const componente: IComponente = { id: 123 };
        expectedResult = service.addComponenteToCollectionIfMissing([], componente);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(componente);
      });

      it('should not add a Componente to an array that contains it', () => {
        const componente: IComponente = { id: 123 };
        const componenteCollection: IComponente[] = [
          {
            ...componente,
          },
          { id: 456 },
        ];
        expectedResult = service.addComponenteToCollectionIfMissing(componenteCollection, componente);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Componente to an array that doesn't contain it", () => {
        const componente: IComponente = { id: 123 };
        const componenteCollection: IComponente[] = [{ id: 456 }];
        expectedResult = service.addComponenteToCollectionIfMissing(componenteCollection, componente);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(componente);
      });

      it('should add only unique Componente to an array', () => {
        const componenteArray: IComponente[] = [{ id: 123 }, { id: 456 }, { id: 20855 }];
        const componenteCollection: IComponente[] = [{ id: 123 }];
        expectedResult = service.addComponenteToCollectionIfMissing(componenteCollection, ...componenteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const componente: IComponente = { id: 123 };
        const componente2: IComponente = { id: 456 };
        expectedResult = service.addComponenteToCollectionIfMissing([], componente, componente2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(componente);
        expect(expectedResult).toContain(componente2);
      });

      it('should accept null and undefined values', () => {
        const componente: IComponente = { id: 123 };
        expectedResult = service.addComponenteToCollectionIfMissing([], null, componente, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(componente);
      });

      it('should return initial array if no Componente is added', () => {
        const componenteCollection: IComponente[] = [{ id: 123 }];
        expectedResult = service.addComponenteToCollectionIfMissing(componenteCollection, undefined, null);
        expect(expectedResult).toEqual(componenteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
