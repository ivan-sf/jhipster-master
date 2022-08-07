import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss'],
})
export class WelcomeComponent implements OnInit {
  account: Account | null = null;
  idUser: any;
  usuario: any = '';
  constructor(private accountService: AccountService, private router: Router, private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      this.idUser = this.account?.id;
    });

    this.checkUser();
    setTimeout(() => {
      console.error('this.usuario', this.usuario);
    }, 2000);
  }

  checkUser(): void {
    this.usuarioService
      .query({
        'userId.equals': this.idUser,
      })
      .subscribe(success => {
        this.usuario = success.body![0];
      });
  }
}
