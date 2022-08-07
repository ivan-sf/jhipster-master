import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InfoLegalComponent } from './list/info-legal.component';
import { InfoLegalDetailComponent } from './detail/info-legal-detail.component';
import { InfoLegalUpdateComponent } from './update/info-legal-update.component';
import { InfoLegalDeleteDialogComponent } from './delete/info-legal-delete-dialog.component';
import { InfoLegalRoutingModule } from './route/info-legal-routing.module';

@NgModule({
  imports: [SharedModule, InfoLegalRoutingModule],
  declarations: [InfoLegalComponent, InfoLegalDetailComponent, InfoLegalUpdateComponent, InfoLegalDeleteDialogComponent],
  entryComponents: [InfoLegalDeleteDialogComponent],
})
export class InfoLegalModule {}
