import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInfoLegal, InfoLegal } from '../info-legal.model';

import { InfoLegalService } from './info-legal.service';

describe('InfoLegal Service', () => {
  let service: InfoLegalService;
  let httpMock: HttpTestingController;
  let elemDefault: IInfoLegal;
  let expectedResult: IInfoLegal | IInfoLegal[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InfoLegalService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nit: 'AAAAAAA',
      regimen: 0,
      prefijo: 0,
      resolucionPos: 'AAAAAAA',
      prefijoPosInicial: 0,
      prefijoPosFinal: 0,
      resolucionFacElec: 'AAAAAAA',
      prefijoFacElecInicial: 0,
      prefijoFacElecFinal: 0,
      resolucionNomElec: 'AAAAAAA',
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

    it('should create a InfoLegal', () => {
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

      service.create(new InfoLegal()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InfoLegal', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nit: 'BBBBBB',
          regimen: 1,
          prefijo: 1,
          resolucionPos: 'BBBBBB',
          prefijoPosInicial: 1,
          prefijoPosFinal: 1,
          resolucionFacElec: 'BBBBBB',
          prefijoFacElecInicial: 1,
          prefijoFacElecFinal: 1,
          resolucionNomElec: 'BBBBBB',
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

    it('should partial update a InfoLegal', () => {
      const patchObject = Object.assign(
        {
          resolucionPos: 'BBBBBB',
          prefijoPosInicial: 1,
          prefijoFacElecInicial: 1,
          prefijoFacElecFinal: 1,
          fechaRegistro: currentDate.format(DATE_TIME_FORMAT),
        },
        new InfoLegal()
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

    it('should return a list of InfoLegal', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nit: 'BBBBBB',
          regimen: 1,
          prefijo: 1,
          resolucionPos: 'BBBBBB',
          prefijoPosInicial: 1,
          prefijoPosFinal: 1,
          resolucionFacElec: 'BBBBBB',
          prefijoFacElecInicial: 1,
          prefijoFacElecFinal: 1,
          resolucionNomElec: 'BBBBBB',
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

    it('should delete a InfoLegal', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInfoLegalToCollectionIfMissing', () => {
      it('should add a InfoLegal to an empty array', () => {
        const infoLegal: IInfoLegal = { id: 123 };
        expectedResult = service.addInfoLegalToCollectionIfMissing([], infoLegal);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(infoLegal);
      });

      it('should not add a InfoLegal to an array that contains it', () => {
        const infoLegal: IInfoLegal = { id: 123 };
        const infoLegalCollection: IInfoLegal[] = [
          {
            ...infoLegal,
          },
          { id: 456 },
        ];
        expectedResult = service.addInfoLegalToCollectionIfMissing(infoLegalCollection, infoLegal);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InfoLegal to an array that doesn't contain it", () => {
        const infoLegal: IInfoLegal = { id: 123 };
        const infoLegalCollection: IInfoLegal[] = [{ id: 456 }];
        expectedResult = service.addInfoLegalToCollectionIfMissing(infoLegalCollection, infoLegal);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(infoLegal);
      });

      it('should add only unique InfoLegal to an array', () => {
        const infoLegalArray: IInfoLegal[] = [{ id: 123 }, { id: 456 }, { id: 73189 }];
        const infoLegalCollection: IInfoLegal[] = [{ id: 123 }];
        expectedResult = service.addInfoLegalToCollectionIfMissing(infoLegalCollection, ...infoLegalArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const infoLegal: IInfoLegal = { id: 123 };
        const infoLegal2: IInfoLegal = { id: 456 };
        expectedResult = service.addInfoLegalToCollectionIfMissing([], infoLegal, infoLegal2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(infoLegal);
        expect(expectedResult).toContain(infoLegal2);
      });

      it('should accept null and undefined values', () => {
        const infoLegal: IInfoLegal = { id: 123 };
        expectedResult = service.addInfoLegalToCollectionIfMissing([], null, infoLegal, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(infoLegal);
      });

      it('should return initial array if no InfoLegal is added', () => {
        const infoLegalCollection: IInfoLegal[] = [{ id: 123 }];
        expectedResult = service.addInfoLegalToCollectionIfMissing(infoLegalCollection, undefined, null);
        expect(expectedResult).toEqual(infoLegalCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
