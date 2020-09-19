import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'futbolista',
        loadChildren: () => import('./futbolista/futbolista.module').then(m => m.FantasyTrackerFutbolistaModule),
      },
      {
        path: 'liga',
        loadChildren: () => import('./liga/liga.module').then(m => m.FantasyTrackerLigaModule),
      },
      {
        path: 'usuario',
        loadChildren: () => import('./usuario/usuario.module').then(m => m.FantasyTrackerUsuarioModule),
      },
      {
        path: 'operacion',
        loadChildren: () => import('./operacion/operacion.module').then(m => m.FantasyTrackerOperacionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class FantasyTrackerEntityModule {}
