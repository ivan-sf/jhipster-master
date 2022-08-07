import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InfoLegalDetailComponent } from './info-legal-detail.component';

describe('InfoLegal Management Detail Component', () => {
  let comp: InfoLegalDetailComponent;
  let fixture: ComponentFixture<InfoLegalDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InfoLegalDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ infoLegal: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InfoLegalDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InfoLegalDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load infoLegal on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.infoLegal).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
