import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ConfigSalaryLeaderComponent } from './config-salary-leader.component';

const routes: Routes = [
    {
        path: '',
        component: ConfigSalaryLeaderComponent,
        data: {
            title: 'Cấu hình lương cho cụm trưởng'
        },
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ConfigSalaryLeaderRoutingModule { }
