import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IComprobanteContable, ComprobanteContable } from '../comprobante-contable.model';
import { ComprobanteContableService } from '../service/comprobante-contable.service';

import { ComprobanteContableRoutingResolveService } from './comprobante-contable-routing-resolve.service';

describe('ComprobanteContable routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ComprobanteContableRoutingResolveService;
  let service: ComprobanteContableService;
  let resultComprobanteContable: IComprobanteContable | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(ComprobanteContableRoutingResolveService);
    service = TestBed.inject(ComprobanteContableService);
    resultComprobanteContable = undefined;
  });

  describe('resolve', () => {
    it('should return IComprobanteContable returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultComprobanteContable = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultComprobanteContable).toEqual({ id: 123 });
    });

    it('should return new IComprobanteContable if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultComprobanteContable = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultComprobanteContable).toEqual(new ComprobanteContable());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ComprobanteContable })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultComprobanteContable = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultComprobanteContable).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
