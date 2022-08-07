export interface IMovimiento {
  id?: number;
  name?: string | null;
}

export class Movimiento implements IMovimiento {
  constructor(public id?: number, public name?: string | null) {}
}

export function getMovimientoIdentifier(movimiento: IMovimiento): number | undefined {
  return movimiento.id;
}
