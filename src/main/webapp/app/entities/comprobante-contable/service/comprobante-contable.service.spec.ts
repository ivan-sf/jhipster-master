import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IComprobanteContable, ComprobanteContable } from '../comprobante-contable.model';

import { ComprobanteContableService } from './comprobante-contable.service';

describe('ComprobanteContable Service', () => {
  let service: ComprobanteContableService;
  let httpMock: HttpTestingController;
  let elemDefault: IComprobanteContable;
  let expectedResult: IComprobanteContable | IComprobanteContable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ComprobanteContableService);
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

    it('should create a ComprobanteContable', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ComprobanteContable()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ComprobanteContable', () => {
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

    it('should partial update a ComprobanteContable', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new ComprobanteContable()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ComprobanteContable', () => {
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

    it('should delete a ComprobanteContable', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addComprobanteContableToCollectionIfMissing', () => {
      it('should add a ComprobanteContable to an empty array', () => {
        const comprobanteContable: IComprobanteContable = { id: 123 };
        expectedResult = service.addComprobanteContableToCollectionIfMissing([], comprobanteContable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(comprobanteContable);
      });

      it('should not add a ComprobanteContable to an array that contains it', () => {
        const comprobanteContable: IComprobanteContable = { id: 123 };
        const comprobanteContableCollection: IComprobanteContable[] = [
          {
            ...comprobanteContable,
          },
          { id: 456 },
        ];
        expectedResult = service.addComprobanteContableToCollectionIfMissing(comprobanteContableCollection, comprobanteContable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ComprobanteContable to an array that doesn't contain it", () => {
        const comprobanteContable: IComprobanteContable = { id: 123 };
        const comprobanteContableCollection: IComprobanteContable[] = [{ id: 456 }];
        expectedResult = service.addComprobanteContableToCollectionIfMissing(comprobanteContableCollection, comprobanteContable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(comprobanteContable);
      });

      it('should add only unique ComprobanteContable to an array', () => {
        const comprobanteContableArray: IComprobanteContable[] = [{ id: 123 }, { id: 456 }, { id: 2722 }];
        const comprobanteContableCollection: IComprobanteContable[] = [{ id: 123 }];
        expectedResult = service.addComprobanteContableToCollectionIfMissing(comprobanteContableCollection, ...comprobanteContableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const comprobanteContable: IComprobanteContable = { id: 123 };
        const comprobanteContable2: IComprobanteContable = { id: 456 };
        expectedResult = service.addComprobanteContableToCollectionIfMissing([], comprobanteContable, comprobanteContable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(comprobanteContable);
        expect(expectedResult).toContain(comprobanteContable2);
      });

      it('should accept null and undefined values', () => {
        const comprobanteContable: IComprobanteContable = { id: 123 };
        expectedResult = service.addComprobanteContableToCollectionIfMissing([], null, comprobanteContable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(comprobanteContable);
      });

      it('should return initial array if no ComprobanteContable is added', () => {
        const comprobanteContableCollection: IComprobanteContable[] = [{ id: 123 }];
        expectedResult = service.addComprobanteContableToCollectionIfMissing(comprobanteContableCollection, undefined, null);
        expect(expectedResult).toEqual(comprobanteContableCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
