import { IProducto } from 'app/entities/producto/producto.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { ISucursal } from 'app/entities/sucursal/sucursal.model';

export interface IOficina {
  id?: number;
  nombre?: string | null;
  detalle?: string | null;
  ubicacion?: string | null;
  productos?: IProducto[] | null;
  usuarios?: IUsuario[] | null;
  sucursal?: ISucursal | null;
}

export class Oficina implements IOficina {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public detalle?: string | null,
    public ubicacion?: string | null,
    public productos?: IProducto[] | null,
    public usuarios?: IUsuario[] | null,
    public sucursal?: ISucursal | null
  ) {}
}

export function getOficinaIdentifier(oficina: IOficina): number | undefined {
  return oficina.id;
}
