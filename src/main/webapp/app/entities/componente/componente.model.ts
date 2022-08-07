import dayjs from 'dayjs/esm';
import { IEmpresa } from 'app/entities/empresa/empresa.model';

export interface IComponente {
  id?: number;
  nombre?: string | null;
  descripcion?: string | null;
  estado?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  empresa?: IEmpresa | null;
}

export class Componente implements IComponente {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public descripcion?: string | null,
    public estado?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public empresa?: IEmpresa | null
  ) {}
}

export function getComponenteIdentifier(componente: IComponente): number | undefined {
  return componente.id;
}
