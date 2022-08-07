import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IInfoLegal, InfoLegal } from '../info-legal.model';
import { InfoLegalService } from '../service/info-legal.service';

import { InfoLegalRoutingResolveService } from './info-legal-routing-resolve.service';

describe('InfoLegal routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: InfoLegalRoutingResolveService;
  let service: InfoLegalService;
  let resultInfoLegal: IInfoLegal | undefined;

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
    routingResolveService = TestBed.inject(InfoLegalRoutingResolveService);
    service = TestBed.inject(InfoLegalService);
    resultInfoLegal = undefined;
  });

  describe('resolve', () => {
    it('should return IInfoLegal returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInfoLegal = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInfoLegal).toEqual({ id: 123 });
    });

    it('should return new IInfoLegal if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInfoLegal = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInfoLegal).toEqual(new InfoLegal());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as InfoLegal })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInfoLegal = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInfoLegal).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
