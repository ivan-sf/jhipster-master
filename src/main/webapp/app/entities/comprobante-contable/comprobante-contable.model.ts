export interface IComprobanteContable {
  id?: number;
  name?: string | null;
}

export class ComprobanteContable implements IComprobanteContable {
  constructor(public id?: number, public name?: string | null) {}
}

export function getComprobanteContableIdentifier(comprobanteContable: IComprobanteContable): number | undefined {
  return comprobanteContable.id;
}
