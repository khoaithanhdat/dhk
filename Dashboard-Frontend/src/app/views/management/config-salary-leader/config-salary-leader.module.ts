import {NgModule} from '@angular/core';
import {CommonModule, DatePipe} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { ConfigSalaryLeaderComponent } from './config-salary-leader.component';
import { ConfigSalaryLeaderRoutingModule } from './config-salary-leader-routing.module';
import { TranslateModule } from '@ngx-translate/core';
import { MatDatepickerModule, MatFormFieldModule, MatInputModule } from '@angular/material';
import { NgxPaginationModule } from 'ngx-pagination';
import { ConfigSalaryLeaderService } from '../../../services/management/config-salary-leader.service';
import { TooltipModule } from 'ngx-bootstrap';

@NgModule({
  declarations: [ConfigSalaryLeaderComponent],
  imports: [
    CommonModule,
    FormsModule,
    ConfigSalaryLeaderRoutingModule,
    TranslateModule,
    ReactiveFormsModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    NgxPaginationModule,
    TooltipModule
  ],
  providers: [
    ConfigSalaryLeaderService,
    DatePipe,
  ]
})
export class ConfigSalaryLeaderModule {
}
