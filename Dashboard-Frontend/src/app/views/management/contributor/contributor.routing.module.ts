import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ContributorComponent } from './contributor.component';

const routes: Routes = [
  {
    path: '',
    component: ContributorComponent,
    data: {
      title: 'Import'
    },
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ContributorRoutingModule { }
