import { IBodega } from 'app/entities/bodega/bodega.model';
import { ISucursal } from 'app/entities/sucursal/sucursal.model';

export interface IInventario {
  id?: number;
  nombre?: string | null;
  bodegas?: IBodega[] | null;
  sucursal?: ISucursal | null;
}

export class Inventario implements IInventario {
  constructor(public id?: number, public nombre?: string | null, public bodegas?: IBodega[] | null, public sucursal?: ISucursal | null) {}
}

export function getInventarioIdentifier(inventario: IInventario): number | undefined {
  return inventario.id;
}
