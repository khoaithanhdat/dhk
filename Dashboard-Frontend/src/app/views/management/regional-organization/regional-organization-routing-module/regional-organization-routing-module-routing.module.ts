import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RegionalOrganizationComponentComponent } from '../regional-organization-component/regional-organization-component.component';


const routes: Routes = [
  {
    path: '',
    component: RegionalOrganizationComponentComponent,
    data: {
      title: 'Cấu hình chức năng'
    },
  }
];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RegionalOrganizationRoutingModuleRoutingModule { }
