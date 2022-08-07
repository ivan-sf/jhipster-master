import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITipoComprobanteContable, TipoComprobanteContable } from '../tipo-comprobante-contable.model';
import { TipoComprobanteContableService } from '../service/tipo-comprobante-contable.service';

import { TipoComprobanteContableRoutingResolveService } from './tipo-comprobante-contable-routing-resolve.service';

describe('TipoComprobanteContable routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TipoComprobanteContableRoutingResolveService;
  let service: TipoComprobanteContableService;
  let resultTipoComprobanteContable: ITipoComprobanteContable | undefined;

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
    routingResolveService = TestBed.inject(TipoComprobanteContableRoutingResolveService);
    service = TestBed.inject(TipoComprobanteContableService);
    resultTipoComprobanteContable = undefined;
  });

  describe('resolve', () => {
    it('should return ITipoComprobanteContable returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoComprobanteContable = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoComprobanteContable).toEqual({ id: 123 });
    });

    it('should return new ITipoComprobanteContable if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoComprobanteContable = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTipoComprobanteContable).toEqual(new TipoComprobanteContable());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TipoComprobanteContable })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoComprobanteContable = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoComprobanteContable).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
