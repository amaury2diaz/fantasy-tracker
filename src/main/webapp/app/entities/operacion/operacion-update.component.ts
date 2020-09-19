import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable, of, Subject } from 'rxjs';

import { IOperacion, Operacion } from 'app/shared/model/operacion.model';
import { OperacionService } from './operacion.service';
import { IFutbolista } from 'app/shared/model/futbolista.model';
import { FutbolistaService } from 'app/entities/futbolista/futbolista.service';
import { IUsuario } from 'app/shared/model/usuario.model';
import { UsuarioService } from 'app/entities/usuario/usuario.service';
import { ILiga } from 'app/shared/model/liga.model';
import { LigaService } from 'app/entities/liga/liga.service';
import { catchError, debounceTime, distinctUntilChanged, map, switchMap, tap } from 'rxjs/operators';
import { ITEMS_PER_PAGE } from '../../shared/constants/pagination.constants';

type SelectableEntity = IFutbolista | IUsuario | ILiga;

@Component({
  selector: 'jhi-operacion-update',
  templateUrl: './operacion-update.component.html',
})
export class OperacionUpdateComponent implements OnInit {
  isSaving = false;
  ligas: ILiga[] = [];
  itemsPerPage: number = ITEMS_PER_PAGE;
  public deUsuarios: IUsuario[] = [];
  public deUsuariosLoading = false;
  public deUsuariosInput$ = new Subject<string>();
  public aUsuarios: IUsuario[] = [];
  public aUsuariosLoading = false;
  public aUsuariosInput$ = new Subject<string>();
  public futbolistas: IFutbolista[] = [];
  public futbolistasLoading = false;
  public futbolistasInput$ = new Subject<string>();

  editForm = this.fb.group({
    id: [],
    precio: [null, [Validators.required]],
    accion: [null, [Validators.required]],
    futbolista: [],
    deUsuario: [],
    aUsuario: [],
    liga: [],
  });

  constructor(
    protected operacionService: OperacionService,
    protected futbolistaService: FutbolistaService,
    protected usuarioService: UsuarioService,
    protected ligaService: LigaService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.loadDeUsuarios();
    this.loadAUsuarios();
    this.loadFutbolistas();
    this.activatedRoute.data.subscribe(({ operacion }) => {
      this.updateForm(operacion);
      this.ligaService.query().subscribe((res: HttpResponse<ILiga[]>) => (this.ligas = res.body || []));
    });
  }

  updateForm(operacion: IOperacion): void {
    this.editForm.patchValue({
      id: operacion.id,
      precio: operacion.precio,
      accion: operacion.accion,
      futbolista: operacion.futbolista,
      deUsuario: operacion.deUsuario,
      aUsuario: operacion.aUsuario,
      liga: operacion.liga,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const operacion = this.createFromForm();
    if (operacion.id !== undefined) {
      this.subscribeToSaveResponse(this.operacionService.update(operacion));
    } else {
      this.subscribeToSaveResponse(this.operacionService.create(operacion));
    }
  }

  public createNewFutbolista() {}

  private createFromForm(): IOperacion {
    return {
      ...new Operacion(),
      id: this.editForm.get(['id'])!.value,
      precio: this.editForm.get(['precio'])!.value,
      accion: this.editForm.get(['accion'])!.value,
      futbolista: this.editForm.get(['futbolista'])!.value,
      deUsuario: this.editForm.get(['deUsuario'])!.value,
      aUsuario: this.editForm.get(['aUsuario'])!.value,
      liga: this.editForm.get(['liga'])!.value,
    };
  }

  private loadDeUsuarios(): void {
    this.deUsuariosInput$
      .pipe(
        debounceTime(200),
        distinctUntilChanged(),
        tap(() => (this.deUsuariosLoading = true)),
        switchMap((term: string) => {
          console.warn(term);
          return this.usuarioService.search({ page: 0, query: term, size: this.itemsPerPage, sort: ['id,asc'] }).pipe(
            map((res: HttpResponse<IUsuario[]>) => (this.deUsuarios = res.body || [])),
            catchError(() => (this.deUsuarios = [])),
            tap(() => (this.deUsuariosLoading = false))
          );
        })
      )
      .subscribe();
  }

  private loadAUsuarios(): void {
    this.aUsuariosInput$
      .pipe(
        debounceTime(200),
        distinctUntilChanged(),
        tap(() => (this.aUsuariosLoading = true)),
        switchMap((term: string) => {
          return this.usuarioService.search({ page: 0, query: term, size: this.itemsPerPage, sort: ['id,asc'] }).pipe(
            map((res: HttpResponse<IUsuario[]>) => (this.aUsuarios = res.body || [])),
            catchError(() => (this.aUsuarios = [])),
            tap(() => (this.aUsuariosLoading = false))
          );
        })
      )
      .subscribe();
  }

  private loadFutbolistas(): void {
    this.futbolistasInput$
      .pipe(
        debounceTime(200),
        distinctUntilChanged(),
        tap(() => (this.futbolistasLoading = true)),
        switchMap((term: string) => {
          return this.futbolistaService.search({ page: 0, query: term, size: this.itemsPerPage, sort: ['id,asc'] }).pipe(
            map((res: HttpResponse<IFutbolista[]>) => (this.futbolistas = res.body || [])),
            catchError(() => (this.futbolistas = [])),
            tap(() => (this.futbolistasLoading = false))
          );
        })
      )
      .subscribe();
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOperacion>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
