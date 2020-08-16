import {Component, Inject, Injectable, OnInit, TemplateRef,}  from '@angular/core';
import {MatDialogRef, MAT_DIALOG_DATA, MatDialog} from '@angular/material';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { contributorModel } from '../../../../models/contributor.model';
import { FormGroup, FormBuilder } from '@angular/forms';
import { ContributorService } from '../../../../services/management/contributor.service';
import { WarningReceiveService } from '../../../../services/management/warning-receive.service';

@Component({
  selector: 'app-add-contributor',
  templateUrl: './add-contributor.component.html',
  styleUrls: ['./add-contributor.component.scss']
})
export class AddContributorComponent implements OnInit {

  mobjModalRef: BsModalRef;
  contributorModel :contributorModel;
  newSerivceGroupForm: FormGroup;
  value1: any;
  products: any;
  isAd = false;
  dsb: boolean;
  mstrContributorCode = '';
  mstrNewName = '';
  stpcial = '';
  special1 = false;
  special = false;
  preventSpace = false;
  mblnIsValidCodeField = true;
  mblnIsValidNameField = true;
  checkAllNull = false;
  conflictCode = false;
  preventSpace1 = false;

  constructor(
    private dialogRef: MatDialogRef<AddContributorComponent>,
    private fb: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data : any,
    private modalService: BsModalService,
    private toastr: ToastrService,
    private translate: TranslateService,
    private contributorService: ContributorService,
    private warningReceiveService: WarningReceiveService,
  ) {
    dialogRef.disableClose = true;
   }

  ngOnInit() {
    this.contributorModel = this.data.contributor;
    this.dsb = this.data.h;
    if (!this.dsb) {
      if (this.contributorModel) {
        this.mstrContributorCode = this.contributorModel.code;
        this.mstrNewName = this.contributorModel.name;
      }
    }
    this.createNewSerivceGroupForm();
    this.onChanges();
  }

  colose() {
    this.dialogRef.close();
  }

  closeModal() {
    this.mobjModalRef.hide();
  }

  confirmAd(pobjTemplate: TemplateRef<any>) {
    if (this.mobjModalRef) {
      this.mobjModalRef.hide();
    }
    this.mobjModalRef = this.modalService.show(pobjTemplate, {
        ignoreBackdropClick: true
      }
    );
  }

  showSuccess(message: string) {
    this.toastr.success(message,
      this.translate.instant('management.warningconfig.success'), {
        timeOut: 2000,
        positionClass: 'toast-top-center',
      });
  }

  createNewSerivceGroupForm() {
    this.newSerivceGroupForm = this.fb.group({
      code: [''],
      name: [''],
      status: ['1']
    });
  }

  checkSpecial(event: any) {
    this.stpcial = event.target.value;
    if ((event.target.value || '').trim().length === 0) {
      this.special = true;
      this.preventSpace1 = false;
    } else if (!this.stpcial.match('^[a-zA-Z0-9 ]{1,50}$')) {
      this.preventSpace1 = true;
      this.special = false;
    } else {
      this.special = false;
      this.preventSpace1 = false;
    }
  }

  checkCodeConflict(event: any) {
    this.contributorService.getAllContributor().subscribe((res: any) => {
      let marr: contributorModel[] = res.data;
      this.contributorModel = {}
      marr = marr.filter(item => item.code !== this.contributorModel.code);
      this.conflictCode = false;
      for (let i = 0; i < marr.length; i++) {
        if (this.mstrContributorCode.toLocaleUpperCase() === marr[i].code.trim()) {
          this.conflictCode = true;
          break;
        }
      }
    });
    this.stpcial = event.target.value;
    if ((event.target.value || '').trim().length === 0) {
      this.special1 = true;
      this.preventSpace = false;
    } else if (!this.stpcial.match('^[A-Za-z0-9 ]{1,20}$')) {
      this.special1 = false;
      this.preventSpace = true;
    } else {
      this.preventSpace = false;
      this.special1 = false;
    }
  }

  insertData() {
    const message = this.translate.instant('management.group.message.insert');
    const messageSuccess = message + this.translate.instant('management.group.message.success');
    const messageFail = messageSuccess + this.translate.instant('management.group.message.fail');

    this.contributorModel.code = (this.newSerivceGroupForm.get('code').value.toString().trim().toUpperCase().replace(/ /g, ''));
    this.contributorModel.name = this.newSerivceGroupForm.get('name').value;
    this.contributorModel.status = "1";
    if (!this.contributorModel.id) {
      this.contributorService.insertData(this.contributorModel).subscribe(data => {
        this.newSerivceGroupForm.reset();
        this.showSuccess(this.translate.instant('management.group.message.insertSuccess'));
        this.warningReceiveService.setReloadWarning(1);
        this.value1 = -1;
        this.isAd = true;
        this.mobjModalRef.hide();
        this.dialogRef.close();
      });
      this.contributorModel = this.data.contributor;
    }
  }

  focusField(pstrField) {
    if (pstrField == 'code') {
      this.mblnIsValidCodeField = true;
    } else if (pstrField == 'name') {
      this.mblnIsValidNameField = true;
    }
  }

  onChanges(): void {
    this.newSerivceGroupForm.valueChanges.subscribe(val => {
      console.log(val.name);
      const code = val.code;
      const name = val.name;
      if (code === '' || name === '') {
        this.checkAllNull = true;
      } else {
        this.checkAllNull = false;
      }
    });
  }

  ngOnDestroy(): void {
  }
}
