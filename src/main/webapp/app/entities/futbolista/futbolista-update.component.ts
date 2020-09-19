import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFutbolista, Futbolista } from 'app/shared/model/futbolista.model';
import { FutbolistaService } from './futbolista.service';

@Component({
  selector: 'jhi-futbolista-update',
  templateUrl: './futbolista-update.component.html',
})
export class FutbolistaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
  });

  constructor(protected futbolistaService: FutbolistaService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ futbolista }) => {
      this.updateForm(futbolista);
    });
  }

  updateForm(futbolista: IFutbolista): void {
    this.editForm.patchValue({
      id: futbolista.id,
      nombre: futbolista.nombre,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const futbolista = this.createFromForm();
    if (futbolista.id !== undefined) {
      this.subscribeToSaveResponse(this.futbolistaService.update(futbolista));
    } else {
      this.subscribeToSaveResponse(this.futbolistaService.create(futbolista));
    }
  }

  private createFromForm(): IFutbolista {
    return {
      ...new Futbolista(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFutbolista>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
