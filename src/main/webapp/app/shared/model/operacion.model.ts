import { Moment } from 'moment';
import { IFutbolista } from 'app/shared/model/futbolista.model';
import { IUsuario } from 'app/shared/model/usuario.model';
import { ILiga } from 'app/shared/model/liga.model';
import { AccionEnum } from 'app/shared/model/enumerations/accion-enum.model';

export interface IOperacion {
  id?: number;
  precio?: number;
  fecha?: Moment;
  accion?: AccionEnum;
  futbolista?: IFutbolista;
  deUsuario?: IUsuario;
  aUsuario?: IUsuario;
  liga?: ILiga;
}

export class Operacion implements IOperacion {
  constructor(
    public id?: number,
    public precio?: number,
    public fecha?: Moment,
    public accion?: AccionEnum,
    public futbolista?: IFutbolista,
    public deUsuario?: IUsuario,
    public aUsuario?: IUsuario,
    public liga?: ILiga
  ) {}
}
