import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFutbolista } from 'app/shared/model/futbolista.model';

@Component({
  selector: 'jhi-futbolista-detail',
  templateUrl: './futbolista-detail.component.html',
})
export class FutbolistaDetailComponent implements OnInit {
  futbolista: IFutbolista | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ futbolista }) => (this.futbolista = futbolista));
  }

  previousState(): void {
    window.history.back();
  }
}
