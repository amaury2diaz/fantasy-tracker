import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IOperacion, Operacion } from 'app/shared/model/operacion.model';
import { OperacionService } from './operacion.service';
import { OperacionComponent } from './operacion.component';
import { OperacionDetailComponent } from './operacion-detail.component';
import { OperacionUpdateComponent } from './operacion-update.component';

@Injectable({ providedIn: 'root' })
export class OperacionResolve implements Resolve<IOperacion> {
  constructor(private service: OperacionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOperacion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((operacion: HttpResponse<Operacion>) => {
          if (operacion.body) {
            return of(operacion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Operacion());
  }
}

export const operacionRoute: Routes = [
  {
    path: '',
    component: OperacionComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Operacions',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OperacionDetailComponent,
    resolve: {
      operacion: OperacionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Operacions',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OperacionUpdateComponent,
    resolve: {
      operacion: OperacionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Operacions',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OperacionUpdateComponent,
    resolve: {
      operacion: OperacionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Operacions',
    },
    canActivate: [UserRouteAccessService],
  },
];
