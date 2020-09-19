import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FantasyTrackerTestModule } from '../../../test.module';
import { FutbolistaDetailComponent } from 'app/entities/futbolista/futbolista-detail.component';
import { Futbolista } from 'app/shared/model/futbolista.model';

describe('Component Tests', () => {
  describe('Futbolista Management Detail Component', () => {
    let comp: FutbolistaDetailComponent;
    let fixture: ComponentFixture<FutbolistaDetailComponent>;
    const route = ({ data: of({ futbolista: new Futbolista(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FantasyTrackerTestModule],
        declarations: [FutbolistaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(FutbolistaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FutbolistaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load futbolista on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.futbolista).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
