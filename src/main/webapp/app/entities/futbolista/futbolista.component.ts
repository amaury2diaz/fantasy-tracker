import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFutbolista } from 'app/shared/model/futbolista.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { FutbolistaService } from './futbolista.service';
import { FutbolistaDeleteDialogComponent } from './futbolista-delete-dialog.component';

@Component({
  selector: 'jhi-futbolista',
  templateUrl: './futbolista.component.html',
})
export class FutbolistaComponent implements OnInit, OnDestroy {
  futbolistas: IFutbolista[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;

  constructor(
    protected futbolistaService: FutbolistaService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks,
    protected activatedRoute: ActivatedRoute
  ) {
    this.futbolistas = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.futbolistaService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe((res: HttpResponse<IFutbolista[]>) => this.paginateFutbolistas(res.body, res.headers));
      return;
    }

    this.futbolistaService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IFutbolista[]>) => this.paginateFutbolistas(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.futbolistas = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(query: string): void {
    this.futbolistas = [];
    this.links = {
      last: 0,
    };
    this.page = 0;
    if (query) {
      this.predicate = '_score';
      this.ascending = false;
    } else {
      this.predicate = 'id';
      this.ascending = true;
    }
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFutbolistas();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFutbolista): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFutbolistas(): void {
    this.eventSubscriber = this.eventManager.subscribe('futbolistaListModification', () => this.reset());
  }

  delete(futbolista: IFutbolista): void {
    const modalRef = this.modalService.open(FutbolistaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.futbolista = futbolista;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateFutbolistas(data: IFutbolista[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.futbolistas.push(data[i]);
      }
    }
  }
}
