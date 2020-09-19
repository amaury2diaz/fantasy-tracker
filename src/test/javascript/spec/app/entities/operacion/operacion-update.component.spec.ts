import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FantasyTrackerTestModule } from '../../../test.module';
import { OperacionUpdateComponent } from 'app/entities/operacion/operacion-update.component';
import { OperacionService } from 'app/entities/operacion/operacion.service';
import { Operacion } from 'app/shared/model/operacion.model';

describe('Component Tests', () => {
  describe('Operacion Management Update Component', () => {
    let comp: OperacionUpdateComponent;
    let fixture: ComponentFixture<OperacionUpdateComponent>;
    let service: OperacionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FantasyTrackerTestModule],
        declarations: [OperacionUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(OperacionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OperacionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(OperacionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Operacion(123);
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
        const entity = new Operacion();
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
