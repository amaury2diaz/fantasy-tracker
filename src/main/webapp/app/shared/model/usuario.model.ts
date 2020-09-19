import { IOperacion } from 'app/shared/model/operacion.model';

export interface IUsuario {
  id?: number;
  nombre?: string;
  deOperaciones?: IOperacion[];
  aOperaciones?: IOperacion[];
}

export class Usuario implements IUsuario {
  constructor(public id?: number, public nombre?: string, public deOperaciones?: IOperacion[], public aOperaciones?: IOperacion[]) {}
}
