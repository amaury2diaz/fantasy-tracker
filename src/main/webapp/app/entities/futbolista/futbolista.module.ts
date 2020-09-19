import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FantasyTrackerSharedModule } from 'app/shared/shared.module';
import { FutbolistaComponent } from './futbolista.component';
import { FutbolistaDetailComponent } from './futbolista-detail.component';
import { FutbolistaUpdateComponent } from './futbolista-update.component';
import { FutbolistaDeleteDialogComponent } from './futbolista-delete-dialog.component';
import { futbolistaRoute } from './futbolista.route';

@NgModule({
  imports: [FantasyTrackerSharedModule, RouterModule.forChild(futbolistaRoute)],
  declarations: [FutbolistaComponent, FutbolistaDetailComponent, FutbolistaUpdateComponent, FutbolistaDeleteDialogComponent],
  entryComponents: [FutbolistaDeleteDialogComponent],
})
export class FantasyTrackerFutbolistaModule {}
