import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RegionalOrganizationComponentComponent} from './regional-organization-component/regional-organization-component.component';
import {RegionalOrganizationRoutingModuleModule} from './regional-organization-routing-module/regional-organization-routing-module.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {TreeviewModule} from 'ngx-treeview';
import {ConfigTreeviewModule} from '../../../config-treeview/config-treeview.module';
import {ConfigTreeComponent} from './config-tree-select/config-tree-select.component';
import {AreaManageRegionalOrganiztionComponent} from './manage-regional-organiztion/area/manage-regional-organiztion.component';
import {PartnerManageRegionalOrganiztionComponent} from './manage-regional-organiztion/partner/manage-regional-organiztion.component';
import {LeadManageRegionalOrganiztionComponent} from './manage-regional-organiztion/lead/manage-regional-organiztion.component';
import {StaffManageRegionalOrganiztionComponent} from './manage-regional-organiztion/staff/manage-regional-organiztion.component';
import {AddRegionalOrganiztionComponent} from './add-regional-organiztion/add-regional-organiztion.component';
import {TranslateModule} from '@ngx-translate/core';
import {NgxPaginationModule} from 'ngx-pagination';
import {MatDatepickerModule, MatDialogModule, MatInputModule} from '@angular/material';
import {NgMultiSelectDropDownModule} from 'ng-multiselect-dropdown';
import {EditComponent} from './manage-regional-organiztion/area/edit/edit.component';
import {AddComponent} from './manage-regional-organiztion/area/add/add.component';
import {ChangeComponent} from './manage-regional-organiztion/area/change/change.component';
import {DialogMessageComponent} from './manage-regional-organiztion/dialog-message/dialog-message.component';
import {ModalModule} from 'ngx-bootstrap';


@NgModule({
  declarations: [
    RegionalOrganizationComponentComponent,
    ConfigTreeComponent,
    AreaManageRegionalOrganiztionComponent,
    PartnerManageRegionalOrganiztionComponent,
    LeadManageRegionalOrganiztionComponent,
    StaffManageRegionalOrganiztionComponent,
    AddRegionalOrganiztionComponent,
    EditComponent,
    AddComponent,
    ChangeComponent,
    DialogMessageComponent],
  imports: [
    CommonModule,
    FormsModule,
    ModalModule.forRoot(),
    TreeviewModule.forRoot(),
    ConfigTreeviewModule,
    RegionalOrganizationRoutingModuleModule,
    TranslateModule,
    NgxPaginationModule,
    MatInputModule,
    MatDialogModule,
    NgMultiSelectDropDownModule.forRoot(),
    MatDatepickerModule,
    ReactiveFormsModule
  ],
  entryComponents: [
    AddRegionalOrganiztionComponent,
    EditComponent,
    AddComponent,
    ChangeComponent,
    DialogMessageComponent
  ]
})
export class RegionalOrganizationModule {
}
