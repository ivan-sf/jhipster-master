import dayjs from 'dayjs/esm';
import { IEmpresa } from 'app/entities/empresa/empresa.model';

export interface IRol {
  id?: number;
  nombre?: string | null;
  descripcion?: string | null;
  estado?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  empresa?: IEmpresa | null;
}

export class Rol implements IRol {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public descripcion?: string | null,
    public estado?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public empresa?: IEmpresa | null
  ) {}
}

export function getRolIdentifier(rol: IRol): number | undefined {
  return rol.id;
}
