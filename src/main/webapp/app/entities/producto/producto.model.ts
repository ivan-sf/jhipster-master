import dayjs from 'dayjs/esm';
import { ICodigo } from 'app/entities/codigo/codigo.model';
import { IPrecio } from 'app/entities/precio/precio.model';
import { IBodega } from 'app/entities/bodega/bodega.model';
import { IOficina } from 'app/entities/oficina/oficina.model';

export interface IProducto {
  id?: number;
  nombre?: string | null;
  detalle?: string | null;
  iva?: string | null;
  fotoContentType?: string | null;
  foto?: string | null;
  fechaRegistro?: dayjs.Dayjs | null;
  codigos?: ICodigo[] | null;
  precioIngresos?: IPrecio[] | null;
  precioSalidas?: IPrecio[] | null;
  bodega?: IBodega | null;
  oficina?: IOficina | null;
}

export class Producto implements IProducto {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public detalle?: string | null,
    public iva?: string | null,
    public fotoContentType?: string | null,
    public foto?: string | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public codigos?: ICodigo[] | null,
    public precioIngresos?: IPrecio[] | null,
    public precioSalidas?: IPrecio[] | null,
    public bodega?: IBodega | null,
    public oficina?: IOficina | null
  ) {}
}

export function getProductoIdentifier(producto: IProducto): number | undefined {
  return producto.id;
}
