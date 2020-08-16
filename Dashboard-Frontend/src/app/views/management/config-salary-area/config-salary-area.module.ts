import { CommonModule ,DatePipe,DecimalPipe} from '@angular/common';

import { ConfigSalaryAreaRoutingModule } from './config-salary-area-routing.module';
import { ConfigSalaryAreaComponent } from './config-salary-area/config-salary-area.component';

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
import { ConfigSalaryAreaService } from '../../../services/management/config-salary-area.service';
import { MatCheckbox, MatCheckboxModule } from '@angular/material';

@NgModule({
  declarations: [ConfigSalaryAreaComponent],
  imports: [
    CommonModule,
    ConfigSalaryAreaRoutingModule,
    MatFormFieldModule, MatInputModule,
    MatSelectModule,
    FormsModule,
    CommonModule,
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
    MatCheckboxModule
  ],
  providers: [
    ConfigSalaryAreaService,
    DatePipe,
    DecimalPipe,
  ],
})
export class ConfigSalaryAreaModule { }
