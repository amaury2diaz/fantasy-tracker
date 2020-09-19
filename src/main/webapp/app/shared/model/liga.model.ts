import { IOperacion } from 'app/shared/model/operacion.model';

export interface ILiga {
  id?: number;
  liga?: string;
  operaciones?: IOperacion[];
}

export class Liga implements ILiga {
  constructor(public id?: number, public liga?: string, public operaciones?: IOperacion[]) {}
}
