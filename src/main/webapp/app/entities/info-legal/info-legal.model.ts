import dayjs from 'dayjs/esm';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { ISucursal } from 'app/entities/sucursal/sucursal.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IInfoLegal {
  id?: number;
  nit?: string | null;
  regimen?: number | null;
  prefijo?: number | null;
  resolucionPos?: string | null;
  prefijoPosInicial?: number | null;
  prefijoPosFinal?: number | null;
  resolucionFacElec?: string | null;
  prefijoFacElecInicial?: number | null;
  prefijoFacElecFinal?: number | null;
  resolucionNomElec?: string | null;
  estado?: number | null;
  fechaRegistro?: dayjs.Dayjs | null;
  empresaIds?: IEmpresa[] | null;
  sucursals?: ISucursal[] | null;
  usuario?: IUsuario | null;
}

export class InfoLegal implements IInfoLegal {
  constructor(
    public id?: number,
    public nit?: string | null,
    public regimen?: number | null,
    public prefijo?: number | null,
    public resolucionPos?: string | null,
    public prefijoPosInicial?: number | null,
    public prefijoPosFinal?: number | null,
    public resolucionFacElec?: string | null,
    public prefijoFacElecInicial?: number | null,
    public prefijoFacElecFinal?: number | null,
    public resolucionNomElec?: string | null,
    public estado?: number | null,
    public fechaRegistro?: dayjs.Dayjs | null,
    public empresaIds?: IEmpresa[] | null,
    public sucursals?: ISucursal[] | null,
    public usuario?: IUsuario | null
  ) {}
}

export function getInfoLegalIdentifier(infoLegal: IInfoLegal): number | undefined {
  return infoLegal.id;
}
