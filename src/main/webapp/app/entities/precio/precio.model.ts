import dayjs from 'dayjs/esm';
import { IProducto } from 'app/entities/producto/producto.model';

export interface IPrecio {
  id?: number;
  valor?: number | null;
  detalle?: string | null;
  fechaRegistro?: dayjs.Dayjs | null;
  productoIngresos?: IProducto[] | null;
  productoSalidas?: IProducto[] | null;
}

export class Precio implements IPrecio {
  constructor(
    public id?: number,
    public valor?: number | null,
    public detalle?: string | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public productoIngresos?: IProducto[] | null,
    public productoSalidas?: IProducto[] | null
  ) {}
}

export function getPrecioIdentifier(precio: IPrecio): number | undefined {
  return precio.id;
}
