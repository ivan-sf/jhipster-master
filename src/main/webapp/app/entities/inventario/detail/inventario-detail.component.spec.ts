import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InventarioDetailComponent } from './inventario-detail.component';

describe('Inventario Management Detail Component', () => {
  let comp: InventarioDetailComponent;
  let fixture: ComponentFixture<InventarioDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InventarioDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ inventario: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InventarioDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InventarioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load inventario on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.inventario).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
