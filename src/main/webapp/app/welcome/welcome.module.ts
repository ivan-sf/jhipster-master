import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WelcomeComponent } from './welcome.component';
import { SharedModule } from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';
import { WELCOME_ROUTE } from './welcome.route';

@NgModule({
  imports: [CommonModule, SharedModule, RouterModule.forChild([WELCOME_ROUTE])],
  declarations: [WelcomeComponent],
})
export class WelcomeModule {}
