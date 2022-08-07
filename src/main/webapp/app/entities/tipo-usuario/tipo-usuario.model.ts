export interface ITipoUsuario {
  id?: number;
  name?: string | null;
}

export class TipoUsuario implements ITipoUsuario {
  constructor(public id?: number, public name?: string | null) {}
}

export function getTipoUsuarioIdentifier(tipoUsuario: ITipoUsuario): number | undefined {
  return tipoUsuario.id;
}
