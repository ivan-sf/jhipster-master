import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { InfoLegalService } from 'app/entities/info-legal/service/info-legal.service';
import { SucursalService } from 'app/entities/sucursal/service/sucursal.service';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss'],
})
export class WelcomeComponent implements OnInit {
  account: Account | null = null;
  idUser: any;

  //SUCURSAL
  usuario: any = undefined;
  empresa: any = undefined;
  info: any = undefined;
  sucursal: any = undefined;
  paso = 0;
  public static auxRepresentante: boolean = false;
  public static auxSucursal: boolean = false;
  constructor(
    private accountService: AccountService,
    private empresaService: EmpresaService,
    private router: Router,
    private usuarioService: UsuarioService,
    private sucursalService: SucursalService,
    private infoLegalService: InfoLegalService
  ) {}

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      this.idUser = this.account?.id;
      this.checkEmpresa();
    });
  }

  checkEmpresa(): void {
    this.empresaService
      .query({
        'userId.equals': this.idUser,
      })
      .subscribe(success => {
        this.empresa = success.body![0];
        this.empresa === undefined ? (this.paso = 1) : null;
        this.empresa !== undefined ? this.checkSucursal() : null;
      });
  }

  checkSucursal(): void {
    this.sucursalService
      .query({
        'empresaId.equals': this.empresa.id,
      })
      .subscribe(success => {
        this.sucursal = success.body![0];
        this.sucursal === undefined ? (this.paso = 2) : null;
        this.sucursal !== undefined ? this.checkUser() : null;
        // (this.empresa !== undefined && this.sucursal !== undefined && this.usuario === undefined) ? this.paso = 3 : null;
      });
  }

  checkUser(): void {
    this.usuarioService
      .query({
        'userId.equals': this.idUser,
      })
      .subscribe(success => {
        this.usuario = success.body![0];
        this.usuario === undefined ? (this.paso = 3) : null;
        this.usuario !== undefined ? this.checkInfo() : null;
      });
  }

  checkInfo(): void {
    this.infoLegalService
      .query({
        'usuarioId.equals': this.usuario.id,
      })
      .subscribe(success => {
        this.info = success.body![0];
        this.info === undefined ? (this.paso = 4) : null;
        console.error('this.info', success.body);
      });
  }

  createEmpresa(): void {
    this.router.navigate(['empresa', 'new', { return: 'welcome' }]);
  }

  createSucursal(): void {
    WelcomeComponent.auxSucursal = true;
    this.router.navigate(['sucursal', 'new', { return: 'welcome' }]);
  }

  createUsuario(): void {
    this.router.navigate(['usuario', 'new', { return: 'welcome', empresa: this.empresa.id }]);
  }

  createInfo(): void {
    this.router.navigate([
      'info-legal',
      'new',
      { return: 'welcome', empresa: this.empresa.id, usuario: this.usuario.id, sucursal: this.sucursal.id },
    ]);
  }
}
