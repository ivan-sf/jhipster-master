import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ComprobanteContableDetailComponent } from './comprobante-contable-detail.component';

describe('ComprobanteContable Management Detail Component', () => {
  let comp: ComprobanteContableDetailComponent;
  let fixture: ComponentFixture<ComprobanteContableDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ComprobanteContableDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ comprobanteContable: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ComprobanteContableDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ComprobanteContableDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load comprobanteContable on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.comprobanteContable).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
