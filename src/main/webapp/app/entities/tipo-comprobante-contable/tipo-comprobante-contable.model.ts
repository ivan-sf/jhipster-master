export interface ITipoComprobanteContable {
  id?: number;
  name?: string | null;
}

export class TipoComprobanteContable implements ITipoComprobanteContable {
  constructor(public id?: number, public name?: string | null) {}
}

export function getTipoComprobanteContableIdentifier(tipoComprobanteContable: ITipoComprobanteContable): number | undefined {
  return tipoComprobanteContable.id;
}
