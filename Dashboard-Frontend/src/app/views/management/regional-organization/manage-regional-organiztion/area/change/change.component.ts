import {Component, Inject, OnInit, TemplateRef} from '@angular/core';
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {AddRegionalOrganiztionComponent} from '../../../add-regional-organiztion/add-regional-organiztion.component';
import {TreeVDSService} from '../../../../../../services/management/tree-VDS.service';
import {PartnerModel} from '../../../../../../models/org.model';
import {IDropdownSettings} from 'ng-multiselect-dropdown/multiselect.model';
import {ConfigRegionalOrganization} from '../../../../../../services/management/config-regional-organization';
import {MomentDateAdapter} from '@angular/material-moment-adapter';
import {TranslateService} from '@ngx-translate/core';
import {BsModalRef, BsModalService} from 'ngx-bootstrap';
import {FormControl} from '@angular/forms';
import * as _moment from 'moment';
import {ServiceService} from '../../../../../../services/management/service.service';
import {ToastrService} from 'ngx-toastr';

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
  selector: 'app-change',
  templateUrl: './change.component.html',
  styleUrls: ['./change.component.scss'],
  providers: [{provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]}
  ]
})
export class ChangeComponent implements OnInit {
  partner: PartnerModel;
  salAreaModel = [];
  dateMoreFrom = false;
  dateMoreFromNowDate = false;
  disbleButton = false;
  dateToInvalid = false;
  mobjModalRef: BsModalRef;
  mdtDateOld = new FormControl(_moment());
  mdtDateNew = new FormControl(_moment());
  selectedArea = null
  validArea = false;

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
              private configRegionalOrganization: ConfigRegionalOrganization,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private translate: TranslateService,
              private modalService: BsModalService,
              private serviceService: ServiceService,
              private toastr: ToastrService
              ) { }

  ngOnInit() {
    this.loadArea();

    this.partner = this.simpleClone(this.data.model);
    this.loadDataInput();
    this.mdtDateNew = new FormControl();
    this.validArea = false;
  }

  onSave() {

    const objDataProvince = new PartnerModel();
    console.log('--------', this.partner);
    objDataProvince.code = this.partner.code;
    objDataProvince.parent = this.selectedArea;
    if (this.mdtDateOld.value && this.mdtDateOld.valid) {
      objDataProvince.expiredDate = this.mdtDateOld.value._d;
    }
    if (!this.mdtDateNew.value) {
      objDataProvince.expiredDateNew = null;
    } else if (this.mdtDateNew.value && this.mdtDateNew.valid) {
      objDataProvince.expiredDateNew = this.mdtDateNew.value._d;
    }
    this.configRegionalOrganization.changeArea(objDataProvince).subscribe(data => {
    console.log('data', data['data']);
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
  loadDataInput() {
    const timestampv2 = new Date(this.partner.expiredDate).getTime();
    console.log('this.partner.expiredDate', timestampv2);
    if (timestampv2 <= 0) {
      this.mdtDateOld =  new FormControl();
    } else {
      this.mdtDateOld.value._d.setTime(timestampv2);
    }
  }
  loadArea() {
    this.configRegionalOrganization.loadArea().subscribe((fee: any) => {
      this.salAreaModel = fee.data;
      // remove bo? vung cu
      console.log('addddd', this.salAreaModel);
      this.salAreaModel = this.salAreaModel.filter(s => s.areaCode !== this.partner.parent);
    });
  }
  onClose() {
    this.dialogRef.close();
  }
  dateChangeTo(value: any) {

    // console.log(value.value._d.getTime()); disbleButton
    if (!value.targetElement.value) {
      this.dateToInvalid = true;
      this.dateMoreFrom = false; // den ngay phai hon hon tu ngay
      this.dateMoreFromNowDate = false;
      this.disbleButton = true;
    } else if (value.value) {
      // todo
      const nowDate = new Date();
      const timestamp = nowDate.getTime();
      if (value.value._d.getTime() > timestamp) {
        this.dateMoreFrom = true;
        this.dateToInvalid = false;
        this.dateMoreFromNowDate = false;
        this.disbleButton = true;
      } else {
       this.partner.effectiveDate = value.value._d;
        this.dateMoreFrom = false;
        this.dateToInvalid = false;
        this.dateMoreFromNowDate = false;
        this.disbleButton = false;
      }
    } else {
      this.dateToInvalid = true;
      this.dateMoreFrom = false;
      this.disbleButton = true;
      this.dateMoreFromNowDate = false;
    }
  }

  dateChangeToNow(value: any) {
    if (!value.targetElement.value) {
      this.dateToInvalid = true;
      this.dateMoreFrom = false;
      this.dateMoreFromNowDate = false;
      this.disbleButton = true;
    } else if (value.value) {
      // todo
      const nowDate = new Date();
      const timestampNow = nowDate.getTime();
      const expiredDateOld = new Date(this.partner.expiredDate).getTime();
      // Ngay het han moi phai lon ngay hien tai va Ngay hat han cu
      if (value.value._d.getTime() > timestampNow && value.value._d.getTime() > expiredDateOld) {
        this.partner.expiredDateNew = value._d;
        this.dateMoreFrom = false;
        this.dateToInvalid = false;
        this.disbleButton = false;
        this.dateMoreFromNowDate = false;
      } else {
        this.dateMoreFrom = false;
        this.dateMoreFromNowDate = true;
        this.dateToInvalid = false;
        this.disbleButton = true;
      }
    } else {
      this.dateToInvalid = true;
      this.dateMoreFrom = false;
      this.disbleButton = true;
      this.dateMoreFromNowDate = false;
    }
  }


  validateDateOld() {
    if (!this.mdtDateOld.valid) {
      this.disbleButton = true;
    } else if (!this.mdtDateOld.value) {
      // this.mblnvaLi = false;
      // this.isNullDate = true;
      this.disbleButton = false;
    } else if (this.mdtDateOld.value && this.mdtDateOld.valid) {
      this.disbleButton = false;
    }
  }


  confirmAd(pobjTemplate: TemplateRef<any>) {
    if (this.selectedArea == null) {
      this.validArea = true;
      return;
    } else {
      this.validArea = false;
    }

    if (this.mobjModalRef) {
      this.mobjModalRef.hide();
    }
    this.mobjModalRef = this.modalService.show(pobjTemplate, {
        ignoreBackdropClick: true
      }
    );
  }
  closeModal() {
    this.mobjModalRef.hide();
  }
  // clond obj
  simpleClone(obj: any) {
    return Object.assign({}, obj);
  }
  showError(message: string) {
    this.toastr.error(message,
      this.translate.instant('management.warningconfig.fail'), {
        timeOut: 2000,
        positionClass: 'toast-top-center',
      });
  }
  showSuccess(message: string) {
    this.toastr.success(message,
      this.translate.instant('management.warningconfig.success'), {
        timeOut: 2000,
        positionClass: 'toast-top-center',
      });
  }
}
