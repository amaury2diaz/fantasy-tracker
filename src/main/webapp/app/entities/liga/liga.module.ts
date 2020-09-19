import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FantasyTrackerSharedModule } from 'app/shared/shared.module';
import { LigaComponent } from './liga.component';
import { LigaDetailComponent } from './liga-detail.component';
import { LigaUpdateComponent } from './liga-update.component';
import { LigaDeleteDialogComponent } from './liga-delete-dialog.component';
import { ligaRoute } from './liga.route';

@NgModule({
  imports: [FantasyTrackerSharedModule, RouterModule.forChild(ligaRoute)],
  declarations: [LigaComponent, LigaDetailComponent, LigaUpdateComponent, LigaDeleteDialogComponent],
  entryComponents: [LigaDeleteDialogComponent],
})
export class FantasyTrackerLigaModule {}
