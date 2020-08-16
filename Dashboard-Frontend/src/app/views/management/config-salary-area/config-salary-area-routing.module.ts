import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ConfigSalaryAreaComponent } from './config-salary-area/config-salary-area.component';

const routes: Routes = [
  {
    path : '',
    component: ConfigSalaryAreaComponent,
    data: {
      title: 'Cấu hình lương theo vùng'
    },
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ConfigSalaryAreaRoutingModule { }
