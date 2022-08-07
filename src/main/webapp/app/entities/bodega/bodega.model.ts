import { IProducto } from 'app/entities/producto/producto.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { IInventario } from 'app/entities/inventario/inventario.model';

export interface IBodega {
  id?: number;
  nombre?: string | null;
  detalle?: string | null;
  ubicacion?: string | null;
  productos?: IProducto[] | null;
  usuarios?: IUsuario[] | null;
  inventario?: IInventario | null;
}

export class Bodega implements IBodega {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public detalle?: string | null,
    public ubicacion?: string | null,
    public productos?: IProducto[] | null,
    public usuarios?: IUsuario[] | null,
    public inventario?: IInventario | null
  ) {}
}

export function getBodegaIdentifier(bodega: IBodega): number | undefined {
  return bodega.id;
}
