import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOficina, Oficina } from '../oficina.model';

import { OficinaService } from './oficina.service';

describe('Oficina Service', () => {
  let service: OficinaService;
  let httpMock: HttpTestingController;
  let elemDefault: IOficina;
  let expectedResult: IOficina | IOficina[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OficinaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      detalle: 'AAAAAAA',
      ubicacion: 'AAAAAAA',
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

    it('should create a Oficina', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Oficina()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Oficina', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          detalle: 'BBBBBB',
          ubicacion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Oficina', () => {
      const patchObject = Object.assign(
        {
          detalle: 'BBBBBB',
        },
        new Oficina()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Oficina', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          detalle: 'BBBBBB',
          ubicacion: 'BBBBBB',
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

    it('should delete a Oficina', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOficinaToCollectionIfMissing', () => {
      it('should add a Oficina to an empty array', () => {
        const oficina: IOficina = { id: 123 };
        expectedResult = service.addOficinaToCollectionIfMissing([], oficina);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(oficina);
      });

      it('should not add a Oficina to an array that contains it', () => {
        const oficina: IOficina = { id: 123 };
        const oficinaCollection: IOficina[] = [
          {
            ...oficina,
          },
          { id: 456 },
        ];
        expectedResult = service.addOficinaToCollectionIfMissing(oficinaCollection, oficina);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Oficina to an array that doesn't contain it", () => {
        const oficina: IOficina = { id: 123 };
        const oficinaCollection: IOficina[] = [{ id: 456 }];
        expectedResult = service.addOficinaToCollectionIfMissing(oficinaCollection, oficina);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(oficina);
      });

      it('should add only unique Oficina to an array', () => {
        const oficinaArray: IOficina[] = [{ id: 123 }, { id: 456 }, { id: 79911 }];
        const oficinaCollection: IOficina[] = [{ id: 123 }];
        expectedResult = service.addOficinaToCollectionIfMissing(oficinaCollection, ...oficinaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const oficina: IOficina = { id: 123 };
        const oficina2: IOficina = { id: 456 };
        expectedResult = service.addOficinaToCollectionIfMissing([], oficina, oficina2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(oficina);
        expect(expectedResult).toContain(oficina2);
      });

      it('should accept null and undefined values', () => {
        const oficina: IOficina = { id: 123 };
        expectedResult = service.addOficinaToCollectionIfMissing([], null, oficina, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(oficina);
      });

      it('should return initial array if no Oficina is added', () => {
        const oficinaCollection: IOficina[] = [{ id: 123 }];
        expectedResult = service.addOficinaToCollectionIfMissing(oficinaCollection, undefined, null);
        expect(expectedResult).toEqual(oficinaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
