import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITipoUsuario, TipoUsuario } from '../tipo-usuario.model';
import { TipoUsuarioService } from '../service/tipo-usuario.service';

import { TipoUsuarioRoutingResolveService } from './tipo-usuario-routing-resolve.service';

describe('TipoUsuario routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TipoUsuarioRoutingResolveService;
  let service: TipoUsuarioService;
  let resultTipoUsuario: ITipoUsuario | undefined;

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
    routingResolveService = TestBed.inject(TipoUsuarioRoutingResolveService);
    service = TestBed.inject(TipoUsuarioService);
    resultTipoUsuario = undefined;
  });

  describe('resolve', () => {
    it('should return ITipoUsuario returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoUsuario = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoUsuario).toEqual({ id: 123 });
    });

    it('should return new ITipoUsuario if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoUsuario = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTipoUsuario).toEqual(new TipoUsuario());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TipoUsuario })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoUsuario = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoUsuario).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
