import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFutbolista, Futbolista } from 'app/shared/model/futbolista.model';
import { FutbolistaService } from './futbolista.service';
import { FutbolistaComponent } from './futbolista.component';
import { FutbolistaDetailComponent } from './futbolista-detail.component';
import { FutbolistaUpdateComponent } from './futbolista-update.component';

@Injectable({ providedIn: 'root' })
export class FutbolistaResolve implements Resolve<IFutbolista> {
  constructor(private service: FutbolistaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFutbolista> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((futbolista: HttpResponse<Futbolista>) => {
          if (futbolista.body) {
            return of(futbolista.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Futbolista());
  }
}

export const futbolistaRoute: Routes = [
  {
    path: '',
    component: FutbolistaComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Futbolistas',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FutbolistaDetailComponent,
    resolve: {
      futbolista: FutbolistaResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Futbolistas',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FutbolistaUpdateComponent,
    resolve: {
      futbolista: FutbolistaResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Futbolistas',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FutbolistaUpdateComponent,
    resolve: {
      futbolista: FutbolistaResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Futbolistas',
    },
    canActivate: [UserRouteAccessService],
  },
];
