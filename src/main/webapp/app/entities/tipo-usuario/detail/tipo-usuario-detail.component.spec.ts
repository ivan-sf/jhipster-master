import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TipoUsuarioDetailComponent } from './tipo-usuario-detail.component';

describe('TipoUsuario Management Detail Component', () => {
  let comp: TipoUsuarioDetailComponent;
  let fixture: ComponentFixture<TipoUsuarioDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TipoUsuarioDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tipoUsuario: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TipoUsuarioDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TipoUsuarioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tipoUsuario on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tipoUsuario).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
