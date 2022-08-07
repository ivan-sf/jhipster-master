import dayjs from 'dayjs/esm';
import { IProducto } from 'app/entities/producto/producto.model';

export interface ICodigo {
  id?: number;
  codigo?: string | null;
  detalle?: string | null;
  fechaRegistro?: dayjs.Dayjs | null;
  productos?: IProducto[] | null;
}

export class Codigo implements ICodigo {
  constructor(
    public id?: number,
    public codigo?: string | null,
    public detalle?: string | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public productos?: IProducto[] | null
  ) {}
}

export function getCodigoIdentifier(codigo: ICodigo): number | undefined {
  return codigo.id;
}
