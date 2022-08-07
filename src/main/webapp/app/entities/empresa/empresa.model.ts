import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IComponente } from 'app/entities/componente/componente.model';
import { IRol } from 'app/entities/rol/rol.model';
import { ISucursal } from 'app/entities/sucursal/sucursal.model';
import { IInfoLegal } from 'app/entities/info-legal/info-legal.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IEmpresa {
  id?: number;
  nombre?: string | null;
  direccion?: string | null;
  direccionGPS?: string | null;
  email?: string | null;
  celular?: string | null;
  indicativo?: string | null;
  estado?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  user?: IUser | null;
  componentes?: IComponente[] | null;
  rols?: IRol[] | null;
  sucursals?: ISucursal[] | null;
  sucursalIds?: ISucursal[] | null;
  infoLegalIds?: IInfoLegal[] | null;
  usuarioIds?: IUsuario[] | null;
}

export class Empresa implements IEmpresa {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public direccion?: string | null,
    public direccionGPS?: string | null,
    public email?: string | null,
    public celular?: string | null,
    public indicativo?: string | null,
    public estado?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public user?: IUser | null,
    public componentes?: IComponente[] | null,
    public rols?: IRol[] | null,
    public sucursals?: ISucursal[] | null,
    public sucursalIds?: ISucursal[] | null,
    public infoLegalIds?: IInfoLegal[] | null,
    public usuarioIds?: IUsuario[] | null
  ) {}
}

export function getEmpresaIdentifier(empresa: IEmpresa): number | undefined {
  return empresa.id;
}
