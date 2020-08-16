import { CommonModule, DatePipe } from '@angular/common';
import { WarningConfigModule } from './../warning-config/warning-config.module';
import {CUSTOM_ELEMENTS_SCHEMA, NgModule, NO_ERRORS_SCHEMA} from '@angular/core';
import { MatSelectModule } from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ButtonsModule, ModalModule, TooltipConfig, TooltipModule} from 'ngx-bootstrap';
import {NgxPaginationModule} from 'ngx-pagination';
import {MatTableModule} from '@angular/material/table';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {ToastrModule} from 'ngx-toastr';
import {TreeviewModule} from 'ngx-treeview';
import {TranslateModule} from '@ngx-translate/core';
import {MatDialogModule} from '@angular/material';
import { MatCheckboxModule, MatIconModule } from '@angular/material';

import { ContributorComponent } from './contributor.component';
import { ContributorRoutingModule } from "./contributor.routing.module";
import { AddContributorComponent } from './add-contributor/add-contributor.component';
import { ContributorService } from '../../../services/management/contributor.service';

@NgModule({
  declarations: [ContributorComponent, AddContributorComponent],
  imports: [
    CommonModule,
    ContributorRoutingModule,
    MatFormFieldModule, MatInputModule,
    MatSelectModule,
    FormsModule,
    ModalModule.forRoot(),
    ButtonsModule.forRoot(),
    ReactiveFormsModule,
    NgxPaginationModule,
    MatTableModule,
    MatPaginatorModule,
    MatDatepickerModule,
    ToastrModule.forRoot(),
    TooltipModule,
    TreeviewModule.forRoot(),
    TranslateModule,
    WarningConfigModule,
    MatDialogModule,MatCheckboxModule
  ],
  providers: [
    ContributorService,
    DatePipe,
  ],
  entryComponents: [
    AddContributorComponent
  ]
})


export class ContributorModule { }
