import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IFutbolista } from 'app/shared/model/futbolista.model';

type EntityResponseType = HttpResponse<IFutbolista>;
type EntityArrayResponseType = HttpResponse<IFutbolista[]>;

@Injectable({ providedIn: 'root' })
export class FutbolistaService {
  public resourceUrl = SERVER_API_URL + 'api/futbolistas';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/futbolistas';

  constructor(protected http: HttpClient) {}

  create(futbolista: IFutbolista): Observable<EntityResponseType> {
    return this.http.post<IFutbolista>(this.resourceUrl, futbolista, { observe: 'response' });
  }

  update(futbolista: IFutbolista): Observable<EntityResponseType> {
    return this.http.put<IFutbolista>(this.resourceUrl, futbolista, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFutbolista>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFutbolista[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFutbolista[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
