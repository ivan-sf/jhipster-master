import dayjs from 'dayjs/esm';
import { IOficina } from 'app/entities/oficina/oficina.model';
import { IInventario } from 'app/entities/inventario/inventario.model';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { IInfoLegal } from 'app/entities/info-legal/info-legal.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface ISucursal {
  id?: number;
  nombre?: string | null;
  nit?: string | null;
  detalle?: string | null;
  direccion?: string | null;
  direccionGPS?: string | null;
  logoContentType?: string | null;
  logo?: string | null;
  estado?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  oficinas?: IOficina[] | null;
  inventarios?: IInventario[] | null;
  empresa?: IEmpresa | null;
  infoLegals?: IInfoLegal[] | null;
  usuarios?: IUsuario[] | null;
  empresaIds?: IEmpresa[] | null;
}

export class Sucursal implements ISucursal {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public nit?: string | null,
    public detalle?: string | null,
    public direccion?: string | null,
    public direccionGPS?: string | null,
    public logoContentType?: string | null,
    public logo?: string | null,
    public estado?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public oficinas?: IOficina[] | null,
    public inventarios?: IInventario[] | null,
    public empresa?: IEmpresa | null,
    public infoLegals?: IInfoLegal[] | null,
    public usuarios?: IUsuario[] | null,
    public empresaIds?: IEmpresa[] | null
  ) {}
}

export function getSucursalIdentifier(sucursal: ISucursal): number | undefined {
  return sucursal.id;
}
