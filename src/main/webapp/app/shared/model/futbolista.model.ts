import { IOperacion } from 'app/shared/model/operacion.model';

export interface IFutbolista {
  id?: number;
  nombre?: string;
  operaciones?: IOperacion[];
}

export class Futbolista implements IFutbolista {
  constructor(public id?: number, public nombre?: string, public operaciones?: IOperacion[]) {}
}
