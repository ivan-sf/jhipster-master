import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'empresa',
        data: { pageTitle: 'Empresas' },
        loadChildren: () => import('./empresa/empresa.module').then(m => m.EmpresaModule),
      },
      {
        path: 'info-legal',
        data: { pageTitle: 'InfoLegals' },
        loadChildren: () => import('./info-legal/info-legal.module').then(m => m.InfoLegalModule),
      },
      {
        path: 'sucursal',
        data: { pageTitle: 'Sucursals' },
        loadChildren: () => import('./sucursal/sucursal.module').then(m => m.SucursalModule),
      },
      {
        path: 'producto',
        data: { pageTitle: 'Productos' },
        loadChildren: () => import('./producto/producto.module').then(m => m.ProductoModule),
      },
      {
        path: 'codigo',
        data: { pageTitle: 'Codigos' },
        loadChildren: () => import('./codigo/codigo.module').then(m => m.CodigoModule),
      },
      {
        path: 'precio',
        data: { pageTitle: 'Precios' },
        loadChildren: () => import('./precio/precio.module').then(m => m.PrecioModule),
      },
      {
        path: 'bodega',
        data: { pageTitle: 'Bodegas' },
        loadChildren: () => import('./bodega/bodega.module').then(m => m.BodegaModule),
      },
      {
        path: 'oficina',
        data: { pageTitle: 'Oficinas' },
        loadChildren: () => import('./oficina/oficina.module').then(m => m.OficinaModule),
      },
      {
        path: 'inventario',
        data: { pageTitle: 'Inventarios' },
        loadChildren: () => import('./inventario/inventario.module').then(m => m.InventarioModule),
      },
      {
        path: 'movimiento',
        data: { pageTitle: 'Movimientos' },
        loadChildren: () => import('./movimiento/movimiento.module').then(m => m.MovimientoModule),
      },
      {
        path: 'comprobante-contable',
        data: { pageTitle: 'ComprobanteContables' },
        loadChildren: () => import('./comprobante-contable/comprobante-contable.module').then(m => m.ComprobanteContableModule),
      },
      {
        path: 'tipo-comprobante-contable',
        data: { pageTitle: 'TipoComprobanteContables' },
        loadChildren: () =>
          import('./tipo-comprobante-contable/tipo-comprobante-contable.module').then(m => m.TipoComprobanteContableModule),
      },
      {
        path: 'usuario',
        data: { pageTitle: 'Usuarios' },
        loadChildren: () => import('./usuario/usuario.module').then(m => m.UsuarioModule),
      },
      {
        path: 'componente',
        data: { pageTitle: 'Componentes' },
        loadChildren: () => import('./componente/componente.module').then(m => m.ComponenteModule),
      },
      {
        path: 'rol',
        data: { pageTitle: 'Rols' },
        loadChildren: () => import('./rol/rol.module').then(m => m.RolModule),
      },
      {
        path: 'tipo-usuario',
        data: { pageTitle: 'TipoUsuarios' },
        loadChildren: () => import('./tipo-usuario/tipo-usuario.module').then(m => m.TipoUsuarioModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
