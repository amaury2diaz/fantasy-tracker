import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOperacion } from 'app/shared/model/operacion.model';
import { OperacionService } from './operacion.service';

@Component({
  templateUrl: './operacion-delete-dialog.component.html',
})
export class OperacionDeleteDialogComponent {
  operacion?: IOperacion;

  constructor(protected operacionService: OperacionService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.operacionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('operacionListModification');
      this.activeModal.close();
    });
  }
}
