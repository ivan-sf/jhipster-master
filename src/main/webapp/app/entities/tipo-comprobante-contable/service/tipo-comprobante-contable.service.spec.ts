import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITipoComprobanteContable, TipoComprobanteContable } from '../tipo-comprobante-contable.model';

import { TipoComprobanteContableService } from './tipo-comprobante-contable.service';

describe('TipoComprobanteContable Service', () => {
  let service: TipoComprobanteContableService;
  let httpMock: HttpTestingController;
  let elemDefault: ITipoComprobanteContable;
  let expectedResult: ITipoComprobanteContable | ITipoComprobanteContable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TipoComprobanteContableService);
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

    it('should create a TipoComprobanteContable', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TipoComprobanteContable()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoComprobanteContable', () => {
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

    it('should partial update a TipoComprobanteContable', () => {
      const patchObject = Object.assign({}, new TipoComprobanteContable());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoComprobanteContable', () => {
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

    it('should delete a TipoComprobanteContable', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTipoComprobanteContableToCollectionIfMissing', () => {
      it('should add a TipoComprobanteContable to an empty array', () => {
        const tipoComprobanteContable: ITipoComprobanteContable = { id: 123 };
        expectedResult = service.addTipoComprobanteContableToCollectionIfMissing([], tipoComprobanteContable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoComprobanteContable);
      });

      it('should not add a TipoComprobanteContable to an array that contains it', () => {
        const tipoComprobanteContable: ITipoComprobanteContable = { id: 123 };
        const tipoComprobanteContableCollection: ITipoComprobanteContable[] = [
          {
            ...tipoComprobanteContable,
          },
          { id: 456 },
        ];
        expectedResult = service.addTipoComprobanteContableToCollectionIfMissing(
          tipoComprobanteContableCollection,
          tipoComprobanteContable
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoComprobanteContable to an array that doesn't contain it", () => {
        const tipoComprobanteContable: ITipoComprobanteContable = { id: 123 };
        const tipoComprobanteContableCollection: ITipoComprobanteContable[] = [{ id: 456 }];
        expectedResult = service.addTipoComprobanteContableToCollectionIfMissing(
          tipoComprobanteContableCollection,
          tipoComprobanteContable
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoComprobanteContable);
      });

      it('should add only unique TipoComprobanteContable to an array', () => {
        const tipoComprobanteContableArray: ITipoComprobanteContable[] = [{ id: 123 }, { id: 456 }, { id: 41819 }];
        const tipoComprobanteContableCollection: ITipoComprobanteContable[] = [{ id: 123 }];
        expectedResult = service.addTipoComprobanteContableToCollectionIfMissing(
          tipoComprobanteContableCollection,
          ...tipoComprobanteContableArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoComprobanteContable: ITipoComprobanteContable = { id: 123 };
        const tipoComprobanteContable2: ITipoComprobanteContable = { id: 456 };
        expectedResult = service.addTipoComprobanteContableToCollectionIfMissing([], tipoComprobanteContable, tipoComprobanteContable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoComprobanteContable);
        expect(expectedResult).toContain(tipoComprobanteContable2);
      });

      it('should accept null and undefined values', () => {
        const tipoComprobanteContable: ITipoComprobanteContable = { id: 123 };
        expectedResult = service.addTipoComprobanteContableToCollectionIfMissing([], null, tipoComprobanteContable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoComprobanteContable);
      });

      it('should return initial array if no TipoComprobanteContable is added', () => {
        const tipoComprobanteContableCollection: ITipoComprobanteContable[] = [{ id: 123 }];
        expectedResult = service.addTipoComprobanteContableToCollectionIfMissing(tipoComprobanteContableCollection, undefined, null);
        expect(expectedResult).toEqual(tipoComprobanteContableCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
