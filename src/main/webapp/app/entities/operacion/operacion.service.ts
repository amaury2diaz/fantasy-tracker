import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IOperacion } from 'app/shared/model/operacion.model';

type EntityResponseType = HttpResponse<IOperacion>;
type EntityArrayResponseType = HttpResponse<IOperacion[]>;

@Injectable({ providedIn: 'root' })
export class OperacionService {
  public resourceUrl = SERVER_API_URL + 'api/operacions';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/operacions';

  constructor(protected http: HttpClient) {}

  create(operacion: IOperacion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(operacion);
    return this.http
      .post<IOperacion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(operacion: IOperacion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(operacion);
    return this.http
      .put<IOperacion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOperacion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOperacion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOperacion[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(operacion: IOperacion): IOperacion {
    const copy: IOperacion = Object.assign({}, operacion, {
      fecha: operacion.fecha && operacion.fecha.isValid() ? operacion.fecha.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fecha = res.body.fecha ? moment(res.body.fecha) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((operacion: IOperacion) => {
        operacion.fecha = operacion.fecha ? moment(operacion.fecha) : undefined;
      });
    }
    return res;
  }
}
