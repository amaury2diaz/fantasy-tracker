import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILiga, Liga } from 'app/shared/model/liga.model';
import { LigaService } from './liga.service';

@Component({
  selector: 'jhi-liga-update',
  templateUrl: './liga-update.component.html',
})
export class LigaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    liga: [null, [Validators.required]],
  });

  constructor(protected ligaService: LigaService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ liga }) => {
      this.updateForm(liga);
    });
  }

  updateForm(liga: ILiga): void {
    this.editForm.patchValue({
      id: liga.id,
      liga: liga.liga,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const liga = this.createFromForm();
    if (liga.id !== undefined) {
      this.subscribeToSaveResponse(this.ligaService.update(liga));
    } else {
      this.subscribeToSaveResponse(this.ligaService.create(liga));
    }
  }

  private createFromForm(): ILiga {
    return {
      ...new Liga(),
      id: this.editForm.get(['id'])!.value,
      liga: this.editForm.get(['liga'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILiga>>): void {
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
