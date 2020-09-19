import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFutbolista } from 'app/shared/model/futbolista.model';
import { FutbolistaService } from './futbolista.service';

@Component({
  templateUrl: './futbolista-delete-dialog.component.html',
})
export class FutbolistaDeleteDialogComponent {
  futbolista?: IFutbolista;

  constructor(
    protected futbolistaService: FutbolistaService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.futbolistaService.delete(id).subscribe(() => {
      this.eventManager.broadcast('futbolistaListModification');
      this.activeModal.close();
    });
  }
}
