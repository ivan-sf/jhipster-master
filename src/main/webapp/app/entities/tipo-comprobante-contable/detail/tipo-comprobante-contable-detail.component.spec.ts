import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TipoComprobanteContableDetailComponent } from './tipo-comprobante-contable-detail.component';

describe('TipoComprobanteContable Management Detail Component', () => {
  let comp: TipoComprobanteContableDetailComponent;
  let fixture: ComponentFixture<TipoComprobanteContableDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TipoComprobanteContableDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tipoComprobanteContable: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TipoComprobanteContableDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TipoComprobanteContableDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tipoComprobanteContable on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tipoComprobanteContable).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
