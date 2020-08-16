import {Component, Inject, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {MomentDateAdapter} from '@angular/material-moment-adapter';
import {TreeviewItem} from 'ngx-treeview';
import {IDropdownSettings} from 'ng-multiselect-dropdown/multiselect.model';
import {TreeVDSService} from '../../../../../../services/management/tree-VDS.service';
import {AddRegionalOrganiztionComponent} from '../../../add-regional-organiztion/add-regional-organiztion.component';
import {PartnerModel} from '../../../../../../models/org.model';
import {SaleFeeModel} from '../../../../../../models/SaleFee.model';
import {ConfigSalaryLeaderService} from '../../../../../../services/management/config-salary-leader.service';
import {ConfigRegionalOrganization} from '../../../../../../services/management/config-regional-organization';
import {SalAreaModel} from '../../../../../../models/salArea.model';
import {BsModalRef, BsModalService} from 'ngx-bootstrap';
import {TranslateService} from '@ngx-translate/core';
import {ToastrService} from 'ngx-toastr';
import {AreaManageRegionalOrganiztionComponent} from '../manage-regional-organiztion.component';
import {log} from 'util';
import {DataService} from '../../../../../../services/data.service';
import {FormControl} from '@angular/forms';
import * as _moment from 'moment';
import {ServiceService} from '../../../../../../services/management/service.service';

export const MY_FORMATS = {
  parse: {
    dateInput: 'DD/MM/YYYY',
  },
  display: {
    dateInput: 'DD/MM/YYYY',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.scss'],
  providers: [{provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]}
  ]
})
export class EditComponent implements OnInit {
  marrNodeTreeviewServices: TreeviewItem[] = [];
  mobjNodeTreeviewService: TreeviewItem;
  dropdownList = [];
  selectedItems = [];
  lstDataProvince = [];
  partner: PartnerModel;
  salAreaModel = [];
  dateToInvalid = false;
  selectedArea = null;
  dateMoreFrom = false;
  mobjModalRef: BsModalRef;
  butDisabled = true;
  disbleButton = true;
  mblnvaLi = false;
  isNullDate = false;
  mdtDate = new FormControl(_moment());

  dropdownSettings: IDropdownSettings = {
    singleSelection: false,
    idField: 'id',
    textField: 'shopName',
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    itemsShowLimit: 5,
    allowSearchFilter: true
  };

  constructor(private dialogRef: MatDialogRef<AddRegionalOrganiztionComponent>,
              private treeVDSService: TreeVDSService,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private configSalaryLeaderService: ConfigSalaryLeaderService,
              private configRegionalOrganization: ConfigRegionalOrganization,
              private modalService: BsModalService,
              private translate: TranslateService,
              private toastr: ToastrService,
              private serviceService: ServiceService
              ) {
  }

  ngOnInit() {
    this.partner = this.simpleClone(this.data.model);
    // this.partner = this.data.model;
    console.log(this.partner);
    this.loadArea();
// gan lai du lieu vung
    this.selectedArea = this.partner.parent;
    this.disbleButton = true;
    this.mdtDate = new FormControl(_moment());
    this.loadDataInput();

  }

  loadDataInput() {
    const timestampv2 = new Date(this.partner.expiredDate).getTime();
    console.log('this.partner.expiredDate', timestampv2);
    if (timestampv2 <= 0) {
      this.mdtDate =  new FormControl();
    } else {
      this.mdtDate.value._d.setTime(timestampv2);
    }

  }


  onClose() {
    this.dialogRef.close();
  }

  confirmAd(pobjTemplate: TemplateRef<any>) {
    console.log('restsdasda', this.mdtDate);
    if (this.mobjModalRef) {
      this.mobjModalRef.hide();
    }
    this.mobjModalRef = this.modalService.show(pobjTemplate, {
        ignoreBackdropClick: true
      }
    );
  }

  onSave() {
    console.log('them_test', this.lstDataProvince);
    console.log('partner: ', this.partner);
    if (!this.mdtDate.value) {
      this.partner.expiredDate = null;
    } else if (this.mdtDate.value && this.mdtDate.valid) {
      this.partner.expiredDate = this.mdtDate.value._d;
      console.log(this.mdtDate.value._d);
    }
    this.configRegionalOrganization.updateProvince(this.partner).subscribe(data => {
      console.log('edit: ', data);
      if (data['code'] === 200) {
        // gan lai du lieu da update
        this.data.model = this.partner;
        this.serviceService.setReloadWarning(true);
        console.log('this.data.model', this.data.model);
        console.log('his.partner', this.partner);
        this.dialogRef.close();
        this.closeModal();

        // this.warningReceiveService.setReloadWarning(0);
        this.showSuccess(this.translate.instant('management.group.message.updateSuccess'));
      } else if (data['code'] === 500) {
        this.showError(data['errors']);
        this.closeModal();
      }

    });
  }

  loadArea() {
    this.configRegionalOrganization.loadArea().subscribe((fee: any) => {
      this.salAreaModel = fee.data;

    });
  }
  validateDate() {
    if (!this.mdtDate.valid) {
      // this.mblnvaLi = true;
      // this.isNullDate = false;
      this.disbleButton = true;
    } else if (!this.mdtDate.value) {
      // this.mblnvaLi = false;
      // this.isNullDate = true;
      this.disbleButton = false;
    } else if (this.mdtDate.value && this.mdtDate.valid) {
      this.mblnvaLi = false;
      this.isNullDate = false;
      this.disbleButton = false;
    }
  }
  dateChangeTo(value: any) {
    if (!value.value) {
      if (!value.targetElement.value) {
        this.dateToInvalid = false;
        this.dateMoreFrom = false; // den ngay phai hon hon tu ngay
        this.disbleButton = false;
      } else if (value.targetElement.value) {
        this.dateToInvalid = true;
        this.dateMoreFrom = false; // den ngay phai hon hon tu ngay
        this.disbleButton = true;
      }
      return;
    }
    // console.log(value.value._d.getTime()); disbleButton
    if (!value.targetElement.value) {
      this.dateToInvalid = true;
      this.dateMoreFrom = false; // den ngay phai hon hon tu ngay
      this.disbleButton = true;
    } else if (value.value) {
      // todo
      const nowDate = new Date();
      const timestamp = nowDate.getTime();
      console.log('fromdatr-----', timestamp);
      console.log('fromdatrv2-----', value.value._d.getTime());
      if (value.value._d.getTime() < timestamp) {
        this.dateMoreFrom = true;
        this.dateToInvalid = false;
        this.disbleButton = true;
      } else {
        // this.declareVds.controls['mdtDateTo'].setValue(value.value._d);
        this.dateMoreFrom = false;
        this.dateToInvalid = false;
        this.partner.expiredDate = value.value._d;
        this.disbleButton = false;
      }
    } else {
      this.dateToInvalid = false;
      this.dateMoreFrom = false;
      this.disbleButton = false;
    }
  }

  closeModal() {
    this.mobjModalRef.hide();
  }

  showSuccess(message: string) {
    this.toastr.success(message,
      this.translate.instant('management.warningconfig.success'), {
        timeOut: 2000,
        positionClass: 'toast-top-center',
      });
  }

  showError(message: string) {
    this.toastr.error(message,
      this.translate.instant('management.warningconfig.fail'), {
        timeOut: 2000,
        positionClass: 'toast-top-center',
      });
  }


// clond obj
  simpleClone(obj: any) {
    return Object.assign({}, obj);
  }
}
