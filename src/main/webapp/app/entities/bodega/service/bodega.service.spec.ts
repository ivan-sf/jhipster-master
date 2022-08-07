import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBodega, Bodega } from '../bodega.model';

import { BodegaService } from './bodega.service';

describe('Bodega Service', () => {
  let service: BodegaService;
  let httpMock: HttpTestingController;
  let elemDefault: IBodega;
  let expectedResult: IBodega | IBodega[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BodegaService);
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

    it('should create a Bodega', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Bodega()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Bodega', () => {
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

    it('should partial update a Bodega', () => {
      const patchObject = Object.assign(
        {
          detalle: 'BBBBBB',
        },
        new Bodega()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Bodega', () => {
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

    it('should delete a Bodega', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBodegaToCollectionIfMissing', () => {
      it('should add a Bodega to an empty array', () => {
        const bodega: IBodega = { id: 123 };
        expectedResult = service.addBodegaToCollectionIfMissing([], bodega);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bodega);
      });

      it('should not add a Bodega to an array that contains it', () => {
        const bodega: IBodega = { id: 123 };
        const bodegaCollection: IBodega[] = [
          {
            ...bodega,
          },
          { id: 456 },
        ];
        expectedResult = service.addBodegaToCollectionIfMissing(bodegaCollection, bodega);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Bodega to an array that doesn't contain it", () => {
        const bodega: IBodega = { id: 123 };
        const bodegaCollection: IBodega[] = [{ id: 456 }];
        expectedResult = service.addBodegaToCollectionIfMissing(bodegaCollection, bodega);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bodega);
      });

      it('should add only unique Bodega to an array', () => {
        const bodegaArray: IBodega[] = [{ id: 123 }, { id: 456 }, { id: 76661 }];
        const bodegaCollection: IBodega[] = [{ id: 123 }];
        expectedResult = service.addBodegaToCollectionIfMissing(bodegaCollection, ...bodegaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bodega: IBodega = { id: 123 };
        const bodega2: IBodega = { id: 456 };
        expectedResult = service.addBodegaToCollectionIfMissing([], bodega, bodega2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bodega);
        expect(expectedResult).toContain(bodega2);
      });

      it('should accept null and undefined values', () => {
        const bodega: IBodega = { id: 123 };
        expectedResult = service.addBodegaToCollectionIfMissing([], null, bodega, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bodega);
      });

      it('should return initial array if no Bodega is added', () => {
        const bodegaCollection: IBodega[] = [{ id: 123 }];
        expectedResult = service.addBodegaToCollectionIfMissing(bodegaCollection, undefined, null);
        expect(expectedResult).toEqual(bodegaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
