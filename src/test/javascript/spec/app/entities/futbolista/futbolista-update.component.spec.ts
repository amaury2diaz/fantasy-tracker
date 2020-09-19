import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FantasyTrackerTestModule } from '../../../test.module';
import { FutbolistaUpdateComponent } from 'app/entities/futbolista/futbolista-update.component';
import { FutbolistaService } from 'app/entities/futbolista/futbolista.service';
import { Futbolista } from 'app/shared/model/futbolista.model';

describe('Component Tests', () => {
  describe('Futbolista Management Update Component', () => {
    let comp: FutbolistaUpdateComponent;
    let fixture: ComponentFixture<FutbolistaUpdateComponent>;
    let service: FutbolistaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FantasyTrackerTestModule],
        declarations: [FutbolistaUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(FutbolistaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FutbolistaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FutbolistaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Futbolista(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Futbolista();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
