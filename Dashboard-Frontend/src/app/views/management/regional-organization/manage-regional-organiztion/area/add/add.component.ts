import {Component, Inject, Input, OnInit, TemplateRef} from '@angular/core';
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {MomentDateAdapter} from '@angular/material-moment-adapter';
import {IDropdownSettings} from 'ng-multiselect-dropdown/multiselect.model';
import {AddRegionalOrganiztionComponent} from '../../../add-regional-organiztion/add-regional-organiztion.component';
import {TreeVDSService} from '../../../../../../services/management/tree-VDS.service';
import {TreeviewItem} from 'ngx-treeview';
import {BsModalRef, BsModalService} from 'ngx-bootstrap';
import {ConfigRegionalOrganization} from '../../../../../../services/management/config-regional-organization';
import {PartnerModel} from '../../../../../../models/org.model';
import {FormControl} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {TranslateService} from '@ngx-translate/core';
import {DialogMessageComponent} from '../../dialog-message/dialog-message.component';
import {isEmpty} from 'rxjs/operators';
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
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.scss'],
  providers: [{provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]}
  ]
})
export class AddComponent implements OnInit {
  marrNodeTreeviewServices: TreeviewItem[] = [];
  mobjNodeTreeviewService: TreeviewItem;
  dropdownList = [];
  selectedItems = [];
  lstDataProvince = [];
  mobjModalRef: BsModalRef;
  areacode = null;
  expiredDate = new Date();
  dateMoreFrom = false;
  disbleButton = false;
  dateToInvalid = false;
  bsModalRefMessage: BsModalRef;
  test = null;
  messErro = null;
  messSucess = null;
  validateProvice = false;
  dropdownSettings: IDropdownSettings = {
    singleSelection: false,
    idField: 'id',
    textField: 'shopName',
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    itemsShowLimit: 3,
    allowSearchFilter: true
  };
  constructor(private dialogRef: MatDialogRef<AddRegionalOrganiztionComponent>,
              private treeVDSService: TreeVDSService,
              private modalServiceMessage: BsModalService,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private modalService: BsModalService,
              private configRegionalOrganization: ConfigRegionalOrganization,
              private toastr: ToastrService,
              private translate: TranslateService) { }

  ngOnInit() {
    this.areacode = this.data.model.value;
    console.log('phc---', this.data.model.value);
    this.validateProvice = false;
    this.onLoadSelect();

  }
  onItemSelect(item: any) {
    this.lstDataProvince.push(item);
    console.log('slect_one', this.lstDataProvince);
    this.validateProvice = false;
  }

  onSelectAll(items: any) {
    console.log('onSelectAll', items);
    this.lstDataProvince = items;
    console.log('slect_all', this.lstDataProvince);
    this.validateProvice = false;
  }

  onDeSelect(item: any) {
    console.log('onSelectAll', item);
    this.lstDataProvince = this.lstDataProvince.filter(s => s.id !== item.id);
    // this.lstDataProvince.slice(item, 1);

    console.log('unselect_all', this.lstDataProvince);
    if (this.lstDataProvince.length < 0) {
      this.validateProvice = true;
    }
  }

  onDeSelectAll(items: any) {
    this.lstDataProvince = [];
    console.log('unslect_all', this.lstDataProvince);
    if (this.lstDataProvince.length < 0) {
      this.validateProvice = true;
    }
  }
  onClose() {
    this.dialogRef.close();
  }

  onSave() {
    // console.log("helo"); createProvince
    // validate

    const objDataProvince = new PartnerModel();
    objDataProvince.lstPartner = this.lstDataProvince;
    objDataProvince.parent = this.areacode;
    objDataProvince.expiredDate = this.expiredDate;
    console.log(this.lstDataProvince);
    this.configRegionalOrganization.createProvince(objDataProvince).subscribe(data => {
      console.log('insert: ', data);
     // this.showSuccess(data['errors']);
      this.test = data['errors'];
      this.test.lastIndexOf(';');
     // console.log('cutchuoi', this.test.substring(0, this.test.lastIndexOf(';')));
      this.messErro = this.test.substring(0, this.test.lastIndexOf(';'));
      this.messSucess = this.test.substring(this.test.lastIndexOf(';') + 1,);
     // console.log('cutchuoi', this.test.substring(this.test.lastIndexOf(';') + 1,));
      this.closeModal();
      this.openModalWithComponent(this.messErro, this.messSucess);
    });
  }

  openModalWithComponent(erro: any, sucess: any) {
    const initialState = {
      list: [
        erro,
        sucess,

      ],
      title: 'Thông báo'
    };
    this.bsModalRefMessage = this.modalServiceMessage.show(DialogMessageComponent, {initialState});
    this.bsModalRefMessage.content.closeBtnName = 'Close';
  }

  // load 64 tinh
  onLoadSelect() {
    this.treeVDSService.getAllProvince().subscribe(
      next => {
        // console.log(next['data']);
        if (!next['data']) {
          return;
        }
        this.dropdownList = next['data'];
        console.log('---------', this.dropdownList);
      }
    );
  }

  confirmAd(pobjTemplate: TemplateRef<any>) {
    // validate
    if (this.lstDataProvince.length > 0 ) {
      if (this.mobjModalRef) {
        this.mobjModalRef.hide();
      }
      this.mobjModalRef = this.modalService.show(pobjTemplate, {
          ignoreBackdropClick: true
        }
      );
    } else {
        this.validateProvice = true;
        return;
    }


  }
  closeModal() {
    this.mobjModalRef.hide();
  }
  // dateChangeTo(value: any) {
  //
  //   // console.log(value.value._d.getTime()); disbleButton
  //   if (!value.targetElement.value) {
  //     this.dateToInvalid = true;
  //     this.dateMoreFrom = false; //den ngay phai hon hon tu ngay
  //     this.disbleButton = true;
  //   } else if (value.value) {
  //     // todo
  //     const nowDate = new Date();
  //     const timestamp = nowDate.getTime();
  //     console.log('fromdatr-----', timestamp);
  //     console.log('fromdatrv2-----', value.value._d.getTime());
  //     if (value.value._d.getTime() < timestamp) {
  //       // this.dateMoreFrom = true;
  //       this.dateToInvalid = false;
  //    //   this.disbleButton = true;
  //     } else {
  //       // this.declareVds.controls['mdtDateTo'].setValue(value.value._d);
  //       this.dateMoreFrom = false;
  //       this.dateToInvalid = false;
  //     }
  //   } else {
  //     this.dateToInvalid = true;
  //     this.dateMoreFrom = false;
  //     this.disbleButton = true;
  //   }
  // }
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
      this.dateMoreFrom = false;
      this.disbleButton = true;
    } else if (value.value) {
      // todo
      const nowDate = new Date();
      const timestamp = nowDate.getTime();
      console.log('fromdatr-----', timestamp);
      console.log('fromdatrv2-----', value.value._d.getTime());
      if (value.value._d.getTime() < timestamp) {
        this.dateMoreFrom = true; // ngay het han khong duoc nho hon ngay hien tai
        this.dateToInvalid = false;
        this.disbleButton = true;
      } else {
        // this.declareVds.controls['mdtDateTo'].setValue(value.value._d);
        this.dateMoreFrom = false;
        this.dateToInvalid = false;
      }
    } else {
      this.dateToInvalid = false;
      this.dateMoreFrom = false;
      this.disbleButton = false;
    }
  }

  showSuccess(message: string) {
    this.toastr.show(message,
      this.translate.instant('management.warningconfig.success'), {
        timeOut: 2000,
        positionClass: 'toast-top-center',
      });
  }
}
