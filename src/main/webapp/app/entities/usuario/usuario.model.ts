import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IRol } from 'app/entities/rol/rol.model';
import { IInfoLegal } from 'app/entities/info-legal/info-legal.model';
import { ISucursal } from 'app/entities/sucursal/sucursal.model';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { IBodega } from 'app/entities/bodega/bodega.model';
import { IOficina } from 'app/entities/oficina/oficina.model';

export interface IUsuario {
  id?: number;
  primerNombre?: string | null;
  segundoNombre?: string | null;
  primerApellido?: string | null;
  segundoApellido?: string | null;
  tipoDocumento?: string | null;
  documento?: string | null;
  documentoDV?: string | null;
  edad?: dayjs.Dayjs | null;
  indicativo?: string | null;
  celular?: string | null;
  direccion?: string | null;
  direccionGps?: string | null;
  fotoContentType?: string | null;
  foto?: string | null;
  fechaRegistro?: dayjs.Dayjs | null;
  user?: IUser | null;
  rol?: IRol | null;
  infoLegals?: IInfoLegal[] | null;
  sucursals?: ISucursal[] | null;
  empresaIds?: IEmpresa[] | null;
  bodegas?: IBodega[] | null;
  oficinas?: IOficina[] | null;
}

export class Usuario implements IUsuario {
  constructor(
    public id?: number,
    public primerNombre?: string | null,
    public segundoNombre?: string | null,
    public primerApellido?: string | null,
    public segundoApellido?: string | null,
    public tipoDocumento?: string | null,
    public documento?: string | null,
    public documentoDV?: string | null,
    public edad?: dayjs.Dayjs | null,
    public indicativo?: string | null,
    public celular?: string | null,
    public direccion?: string | null,
    public direccionGps?: string | null,
    public fotoContentType?: string | null,
    public foto?: string | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public user?: IUser | null,
    public rol?: IRol | null,
    public infoLegals?: IInfoLegal[] | null,
    public sucursals?: ISucursal[] | null,
    public empresaIds?: IEmpresa[] | null,
    public bodegas?: IBodega[] | null,
    public oficinas?: IOficina[] | null
  ) {}
}

export function getUsuarioIdentifier(usuario: IUsuario): number | undefined {
  return usuario.id;
}
