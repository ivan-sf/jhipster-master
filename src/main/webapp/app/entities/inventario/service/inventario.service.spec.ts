import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInventario, Inventario } from '../inventario.model';

import { InventarioService } from './inventario.service';

describe('Inventario Service', () => {
  let service: InventarioService;
  let httpMock: HttpTestingController;
  let elemDefault: IInventario;
  let expectedResult: IInventario | IInventario[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InventarioService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
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

    it('should create a Inventario', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Inventario()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Inventario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Inventario', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
        },
        new Inventario()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Inventario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
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

    it('should delete a Inventario', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInventarioToCollectionIfMissing', () => {
      it('should add a Inventario to an empty array', () => {
        const inventario: IInventario = { id: 123 };
        expectedResult = service.addInventarioToCollectionIfMissing([], inventario);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inventario);
      });

      it('should not add a Inventario to an array that contains it', () => {
        const inventario: IInventario = { id: 123 };
        const inventarioCollection: IInventario[] = [
          {
            ...inventario,
          },
          { id: 456 },
        ];
        expectedResult = service.addInventarioToCollectionIfMissing(inventarioCollection, inventario);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Inventario to an array that doesn't contain it", () => {
        const inventario: IInventario = { id: 123 };
        const inventarioCollection: IInventario[] = [{ id: 456 }];
        expectedResult = service.addInventarioToCollectionIfMissing(inventarioCollection, inventario);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inventario);
      });

      it('should add only unique Inventario to an array', () => {
        const inventarioArray: IInventario[] = [{ id: 123 }, { id: 456 }, { id: 62687 }];
        const inventarioCollection: IInventario[] = [{ id: 123 }];
        expectedResult = service.addInventarioToCollectionIfMissing(inventarioCollection, ...inventarioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inventario: IInventario = { id: 123 };
        const inventario2: IInventario = { id: 456 };
        expectedResult = service.addInventarioToCollectionIfMissing([], inventario, inventario2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inventario);
        expect(expectedResult).toContain(inventario2);
      });

      it('should accept null and undefined values', () => {
        const inventario: IInventario = { id: 123 };
        expectedResult = service.addInventarioToCollectionIfMissing([], null, inventario, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inventario);
      });

      it('should return initial array if no Inventario is added', () => {
        const inventarioCollection: IInventario[] = [{ id: 123 }];
        expectedResult = service.addInventarioToCollectionIfMissing(inventarioCollection, undefined, null);
        expect(expectedResult).toEqual(inventarioCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
