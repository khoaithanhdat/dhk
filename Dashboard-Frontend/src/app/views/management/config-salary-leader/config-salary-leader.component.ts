import { Component, OnInit, TemplateRef, ViewChild, ElementRef, ChangeDetectorRef } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { FormBuilder, FormGroup, FormArray, Validators, FormControl } from '@angular/forms';
import { DateAdapter, MAT_DATE_LOCALE, MAT_DATE_FORMATS, MatDatepicker } from '@angular/material';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { Pager } from '../../../models/Pager';
import { config } from '../../../config/application.config';
import { ConfigSalaryLeaderService } from '../../../services/management/config-salary-leader.service';
import { SalConfigStaffTargetModel } from '../../../models/SalConfigStaffTarget.model';
import { ConfigSalaryLeaderDTO } from '../../../models/SalaryLeader.model';
import { SalConfigHsTinhLuongModel } from '../../../models/SalConfigHsTinhLuong.model';
import { SalConfigSaleFeeModel } from '../../../models/SalConfigSaleFee.model';
import { ServiceService } from '../../../services/management/service.service';
import { ServiceModel } from '../../../models/service.model';
import { SaleFeeModel } from '../../../models/SaleFee.model';
import * as _moment from 'moment';
import { Moment } from 'moment';
import { ToastrService } from 'ngx-toastr';
import { DatePipe } from '@angular/common';

export const MONTH_FORMATS = {
  parse: {
    dateInput: 'MM/YYYY',
  },
  display: {
    dateInput: 'MM/YYYY',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

@Component({
  selector: 'app-config-salary-leader',
  templateUrl: './config-salary-leader.component.html',
  styleUrls: ['./config-salary-leader.component.scss'],
  providers: [
    { provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE] },
    // tslint:disable-next-line:max-line-length
    { provide: MAT_DATE_FORMATS, useValue: MONTH_FORMATS },
  ],
})

export class ConfigSalaryLeaderComponent implements OnInit {

  @ViewChild('overleft') overleftModal: ElementRef;
  @ViewChild('confirmSearchSaleFee') confirmSearchSaleFee: ElementRef;
  @ViewChild('confirmGlobal') confirmGlobal: ElementRef;

  mdtDate = [];
  mdtDateComplete = [];
  mdtDateCoefficient = [];
  mdtDateSaleFee = [];
  mblnChec = false;
  mdtMinDate = new Date(1899, 0, 1);
  mdtMaxDate = new Date(2101, 0, 1);
  startView: 'month' | 'year' | 'multi-year';
  yearOut: boolean;
  mblnvaLi = false;
  isNullDate = false;
  acceptAction = false;
  titleAction;
  checkDuplicateServiceId = [];
  checkDuplicateFeeId = [];
  checkSubmit = false;
  checkMonthTarget = [];
  checkMonthComplete = [];
  checkMonthCoefficient = [];
  checkMonthSaleFee = [];

  checkTargetFormValid = [[
    { require: '' },
    { partten: '' }
  ]];

  salaryLeaderModel: ConfigSalaryLeaderDTO;
  salConfigStaffTarget: SalConfigStaffTargetModel;
  modalRef: BsModalRef;
  modalRefError: BsModalRef;
  modalRefConfirm: BsModalRef;
  modalRefConfirmGlobal: BsModalRef;
  targetForm: FormGroup;
  targetCompleteForm: FormGroup;
  salaryCoefficientForm: FormGroup;
  feeForm: FormGroup;
  formulaValue = '';
  serviceModel: ServiceModel;
  saleFeeModel: SaleFeeModel;
  currentPage = 1;
  pageSize = config.pageSize;
  submited;
  checkFormulaValue = '';
  checkFormulaValueRegex = '';
  checkForm
  valueFeeSearch = '';
  receiveSearch = 'null';
  checkOverLeft = [];

  comparisonDefault = config.comparisonDefault;

  receiveFee = config.receiveFee;
  formula = config.formula;
  checkChangeValueSaleFee = false;
  checkSearchSaleFee = false;
  confirmSearchFee = false;
  checkChangeFormulaValue = false;

  formulaType = null;
  serviceType = null;

  checkComparison = [];
  checkComparisonComplete = [];
  checkComparisonCoefficient = [];
  checkFormula = [];
  checkValueToTargetForm = [];
  checkValueToCompleteForm = [];
  checkValueToCoefficientForm = [];
  checkAll = [];

  checkOverLeftTargetForm = false;
  checkOverLeftTargetCompleteForm = false;
  checkOverLeftTargetCoefficientForm = false;

  target = {
    comparison: 'null',
    comparison_value_from: null,
    comparison_value_to: null,
    comparison_value: null,
    expired_month: null
  }
  target1 = {
    comparison: this.comparisonDefault[0].value,
    comparison_value_from: 0,
    comparison_value_to: '',
    comparison_value: 0,
    expired_month: null
  }
  target2 = {
    comparison: this.comparisonDefault[1].value,
    comparison_value_from: 120,
    comparison_value_to: '',
    comparison_value: 120,
    expired_month: null
  }

  coefficientModel = {
    comparison: null,
    comparison_value_from: null,
    comparison_value_to: null,
    value_type: '0',
    comparison_value: '',
    formula: '',
    expired_month: null
  }

  saleFee = {
    fee_id: 'null',
    receive_from_fee: 'null',
    receive_percent_fee: '',
    expired_month_fee: null
  }

  constructor(private translate: TranslateService, private formBuilder: FormBuilder, private modalService: BsModalService,
    private configSalaryLeaderService: ConfigSalaryLeaderService, private serviceService: ServiceService,
    private toastr: ToastrService, private datePipe: DatePipe) { }

  ngOnInit() {
    this.yearOut = false;
    this.startView = 'year';
    this.salaryLeaderModel = new ConfigSalaryLeaderDTO();
    this.salaryLeaderModel.salConfigStaffTargetModel = [];
    this.salaryLeaderModel.salConfigHsTinhLuongModel = [];
    this.salaryLeaderModel.salConfigStaffGiffModel = [];
    this.salaryLeaderModel.salConfigSaleFeeModel = [];

    this.createFormTargetGlobal();
    this.createFormTargetComplete();
    this.createFormSalaryCoefficient();
    this.createFormFee();
    this.getData();
    this.fieldGlobalIndex(0);
  }

  setDateTargetForm(date, i, j?) {
    if (date) {
      if (this.mdtDate.length > 0) {
        if (this.mdtDate[i]) {
          if (this.mdtDate[i][j]) {
            this.mdtDate[i][j] = new FormControl(_moment(date));
          }
        }
      }
    } else {
      if (this.mdtDate[i]) {
        if (this.mdtDate[i][j]) {
          this.mdtDate[i][j] = null;
        }
      }
    }
  }

  setDateCompleteForm(date, i) {
    if (date) {
      if (this.mdtDateComplete.length > 0) {
        if (this.mdtDateComplete[i]) {
          this.mdtDateComplete[i] = new FormControl(_moment(date));
        }
      }
    } else {
      if (this.mdtDateComplete[i]) {
        this.mdtDateComplete[i] = new FormControl(null);
      }
    }
  }

  setDateCoefficient(date, i) {
    if (date) {
      if (this.mdtDateCoefficient.length > 0) {
        if (this.mdtDateCoefficient[i]) {
          this.mdtDateCoefficient[i] = new FormControl(_moment(date));
        }
      }
    } else {
      if (this.mdtDateCoefficient[i]) {
        this.mdtDateCoefficient[i] = new FormControl(null);
      }
    }
  }

  setDateSaleFee(date, i) {
    if (date) {
      if (this.mdtDateSaleFee.length > 0) {
        if (this.mdtDateSaleFee[i]) {
          this.mdtDateSaleFee[i] = new FormControl(_moment(date));
        } else {
          this.mdtDateSaleFee.push(new FormControl(_moment(date)));
        }
      } else {
        this.mdtDateSaleFee.push(new FormControl(_moment(date)));
      }
    } else {
      if (this.mdtDateSaleFee[i]) {
        this.mdtDateSaleFee[i] = new FormControl(null);
      } else {
        this.mdtDateSaleFee.push(new FormControl(null));
      }
    }
  }

  getData() {
    this.loadDataTargetForm();
    this.loadDataTargetCompleteForm();
    this.loadDataCoefficientForm();
    this.loadDataSaleFeeForm();

    this.serviceService.getAllServiceByStatus().subscribe((res: any) => {
      this.serviceModel = res.data;
    });
    this.configSalaryLeaderService.getAllSaleFee().subscribe((fee: any) => {
      this.saleFeeModel = fee.data;
    });
  }

  loadDataTargetForm() {
    this.configSalaryLeaderService.getAllSalConfigStaffTargetByService().subscribe(resTarget => {
      if (resTarget.data && resTarget.data.length > 0) {
        resTarget.data.forEach(element => {
          let dataModal = {
            target_id: null,
            items: []
          }
          element.forEach(item => {
            this.salaryLeaderModel.salConfigStaffTargetModel.push(item);
            let target = {
              id: null,
              comparison: 'null',
              comparison_value_from: null,
              comparison_value_to: null,
              comparison_value: null,
              expired_month: null
            }
            target.id = item.id;
            target.comparison = item.comparison ? item.comparison : 'null';

            if (item.comparison == '<' || item.comparison == '<=') {
              target.comparison_value_from = (item.comparisonValueTo || item.comparisonValueTo == 0) ? item.comparisonValueTo : null;
            } else {
              target.comparison_value_from = (item.comparisonValueFrom || item.comparisonValueFrom == 0) ? item.comparisonValueFrom : null;
            }
            target.comparison_value_to = (item.comparisonValueTo || item.comparisonValueTo == 0) ? item.comparisonValueTo : null;
            target.comparison_value = (item.value || item.value == 0) ? item.value : null;
            target.expired_month = item.expiredMonth ? item.expiredMonth : null;

            dataModal.target_id = item.serviceId ? item.serviceId : null;
            dataModal.items.push(target);
          });

          const control = <FormArray>this.targetForm.controls['itemsGlobal'];
          if (dataModal.target_id) {
            if (control.value.length > 0) {
              let arrCheck = control.value.filter(item => item.target_id == dataModal.target_id);
              if (arrCheck.length == 0) {
                control.push(this.targetGlobalModal(dataModal));
              }
            } else {
              control.push(this.targetGlobalModal(dataModal));
            }
          }
        });

        let valueOverLeft = [];
        this.targetForm.value.itemsGlobal.forEach((element, i) => {
          this.checkComparison.push([]);
          this.checkDuplicateServiceId.push(false);
          this.mdtDate.push([]);
          this.checkMonthTarget.push([]);
          element.items.forEach((items, j) => {

            this.checkComparison[i].push([]);
            this.mdtDate[i].push([null]);
            this.checkMonthTarget[i].push(false);
            let salConfigStaffTarget = new SalConfigStaffTargetModel();
            salConfigStaffTarget.comparison = items.comparison;
            salConfigStaffTarget.comparisonValueFrom = items.comparison_value_from;
            salConfigStaffTarget.comparisonValueTo = items.comparison_value_to;
            salConfigStaffTarget.expiredMonth = items.expired_month;
            salConfigStaffTarget.status = items.status;
            valueOverLeft.push(salConfigStaffTarget);

            this.setDateTargetForm(items.expired_month, i, j);

            this.checkValueToTargetForm.push([false]);
            this.checkComparisonForm(0, items.comparison, i, j);
            this.checkDateLoadValid(0, items.expired_month, i, j);
          });
        });
        this.validOverLeft(valueOverLeft, 1);
      }
    });
  }

  checkComparisonForm(type, comparison, index, indexChild?) {
    if (type == 0) {
      let checkMultiInput = this.comparisonDefault.filter(item => item.value == comparison && item.checkMultiInput);

      const form = this.targetForm.get('itemsGlobal');
      const formChild = (form as FormArray).controls[index].get('items');
      if (checkMultiInput.length > 0) {
        this.checkComparison[index][indexChild] = true;
        (formChild as FormArray).controls[indexChild].get('comparison_value_to').setValidators([Validators.required, Validators.pattern(/^-?(0|[1-9]\d*)?$/)]);
      } else {
        (formChild as FormArray).controls[indexChild].get('comparison_value_to').setValue('');
        (formChild as FormArray).controls[indexChild].get('comparison_value_to').clearValidators();
        (formChild as FormArray).controls[indexChild].get('comparison_value_to').updateValueAndValidity();
        this.checkComparison[index][indexChild] = false;
      }
    } else if (type == 1) {
      let checkMultiInput = this.comparisonDefault.filter(item => item.value == comparison && item.checkMultiInput);

      const form = this.targetCompleteForm.get('itemsComplete');
      if (checkMultiInput.length > 0) {
        this.checkComparisonComplete[index] = true;
        (form as FormArray).controls[index].get('comparison_value_to_complete').setValidators([Validators.required, Validators.pattern(/^-?(0|[1-9]\d*)?$/)]);
      } else {
        (form as FormArray).controls[index].get('comparison_value_to_complete').setValue(null);
        (form as FormArray).controls[index].get('comparison_value_to_complete').clearValidators();
        (form as FormArray).controls[index].get('comparison_value_to_complete').updateValueAndValidity();
        this.checkComparisonComplete[index] = false;
      }
    } else if (type == 2) {
      let checkMultiInput = this.comparisonDefault.filter(item => item.value == comparison && item.checkMultiInput);

      const form = this.salaryCoefficientForm.get('coefficient');
      if (checkMultiInput.length > 0) {
        this.checkComparisonCoefficient[index] = true;
        console.log((form as FormArray).controls[index].get('comparison_value_to_coefficient'));
        (form as FormArray).controls[index].get('comparison_value_to_coefficient').setValidators([Validators.required, Validators.pattern(/^-?(0|[1-9]\d*)?$/)]);
      } else {
        (form as FormArray).controls[index].get('comparison_value_to_coefficient').setValue('');
        (form as FormArray).controls[index].get('comparison_value_to_coefficient').clearValidators();
        (form as FormArray).controls[index].get('comparison_value_to_coefficient').updateValueAndValidity();
        this.checkComparisonCoefficient[index] = false;
      }
    }
  }

  loadDataTargetCompleteForm() {
    this.configSalaryLeaderService.getAllSalConfigStaffTargetCompleteByService(0).subscribe(resTargetComplete => {
      if (resTargetComplete.data.length > 0) {
        resTargetComplete.data.forEach((element, i) => {
          this.salaryLeaderModel.salConfigStaffTargetModel.push(element);
          let target = {
            id: null,
            comparison: 'null',
            comparison_value_from: null,
            comparison_value_to: null,
            comparison_value: null,
            expired_month: null
          }
          target.id = element.id ? element.id : null;
          target.comparison = element.comparison ? element.comparison : 'null';

          if (element.comparison == '<' || element.comparison == '<=') {
            target.comparison_value_from = (element.comparisonValueTo || element.comparisonValueTo == 0) ? element.comparisonValueTo : null;
          } else {
            target.comparison_value_from = (element.comparisonValueFrom || element.comparisonValueFrom == 0) ? element.comparisonValueFrom : null;
          }
          target.comparison_value_to = (element.comparisonValueTo || element.comparisonValueTo == 0) ? element.comparisonValueTo : null;
          target.comparison_value = (element.value || element.value == 0) ? element.value : null;
          target.expired_month = element.expiredMonth ? element.expiredMonth : null;

          const control = <FormArray>this.targetCompleteForm.controls['itemsComplete'];

          if (target.id) {
            if (control.value.length > 0) {
              let arrCheck = control.value.filter(item => item.id == target.id);
              if (arrCheck.length == 0) {
                control.push(this.targetCompleteModal(target));
              }
            } else {
              control.push(this.targetCompleteModal(target));
            }
          }
        });

        let valueOverLeft = [];
        this.targetCompleteForm.value.itemsComplete.forEach((element, i) => {
          this.checkComparisonComplete.push([]);
          this.mdtDateComplete.push([]);
          this.checkMonthComplete.push(false);
          let salConfigStaffTarget = new SalConfigStaffTargetModel();
          salConfigStaffTarget.comparison = element.comparison_complete;
          salConfigStaffTarget.comparisonValueFrom = element.comparison_value_from_complete;
          salConfigStaffTarget.comparisonValueTo = element.comparison_value_to_complete;
          salConfigStaffTarget.expiredMonth = element.expired_month_complete;
          valueOverLeft.push(salConfigStaffTarget);

          if (element.expired_month_complete) {
            this.setDateCompleteForm(element.expired_month_complete, i);
          }

          this.checkValueToCompleteForm.push(false);
          let checkMultiInput = this.comparisonDefault.filter(item => item.value == element.comparison_complete && item.checkMultiInput);
          const form = this.targetCompleteForm.get('itemsComplete');
          if (checkMultiInput.length > 0) {
            (form as FormArray).controls[i].get('comparison_value_to_complete').setValidators(Validators.required);
            this.checkComparisonComplete[i] = true;
          } else {
            (form as FormArray).controls[i].get('comparison_value_to_complete').clearValidators();
            this.checkComparisonComplete[i] = false;
          }
          this.checkComparisonForm(1, element.comparison, i);
          this.checkDateLoadValid(1, element.expired_month_complete, i);
        });
        this.validOverLeft(valueOverLeft, 1);
      }
    });
  }

  loadDataCoefficientForm() {
    this.configSalaryLeaderService.getAllSalConfigHsTinhLuong().subscribe(resCoefficient => {
      if (resCoefficient.data.length > 0) {
        resCoefficient.data.forEach((element, i) => {
          this.salaryLeaderModel.salConfigHsTinhLuongModel.push(element);
          let target = {
            id: null,
            comparison: 'null',
            comparison_value_from: null,
            comparison_value_to: null,
            comparison_value: null,
            expired_month: null,
            value_type: null,
            formula: '',
          }
          target.id = element.id ? element.id : null;
          target.comparison = element.comparison ? element.comparison : 'null';

          if (element.comparison == '<' || element.comparison == '<=') {
            target.comparison_value_from = (element.comparisonValueTo || element.comparisonValueTo == 0) ? element.comparisonValueTo : null;
          } else {
            target.comparison_value_from = (element.comparisonValueFrom || element.comparisonValueFrom == 0) ? element.comparisonValueFrom : null;
          }
          target.comparison_value_to = (element.comparisonValueTo || element.comparisonValueTo == 0) ? element.comparisonValueTo : null;
          target.comparison_value = (element.value || element.value == 0) ? element.value : null;
          target.expired_month = element.expiredMonth ? element.expiredMonth : null;
          target.value_type = element.valueType ? element.valueType : null;
          target.formula = element.formula ? element.formula : null;

          const control = <FormArray>this.salaryCoefficientForm.controls['coefficient'];
          if (target.id) {
            if (control.value.length > 0) {
              let arrCheck = control.value.filter(item => item.id == target.id);
              if (arrCheck.length == 0) {
                control.push(this.salaryCoefficientModal(target));
              }
            } else {
              control.push(this.salaryCoefficientModal(target));
            }
          }
        });

        let valueOverLeft = [];
        this.salaryCoefficientForm.value.coefficient.forEach((element, i) => {
          this.mdtDateCoefficient.push([]);
          this.checkComparisonCoefficient.push([]);
          this.checkFormula.push(0);
          this.checkMonthCoefficient.push(false);
          let salConfigHsTinhLuongModel = new SalConfigHsTinhLuongModel();
          salConfigHsTinhLuongModel.comparison = element.comparison_coefficient;
          salConfigHsTinhLuongModel.comparisonValueFrom = element.comparison_value_from_coefficient;
          salConfigHsTinhLuongModel.comparisonValueTo = element.comparison_value_to_coefficient;
          salConfigHsTinhLuongModel.expiredMonth = element.expired_month_coefficient;
          valueOverLeft.push(salConfigHsTinhLuongModel);

          if (element.expired_month_coefficient) {
            this.setDateCoefficient(element.expired_month_coefficient, i);
          }

          this.checkValueToCoefficientForm.push(false);
          let checkMultiInput = this.comparisonDefault.filter(item => item.value == element.comparison_coefficient && item.checkMultiInput);
          const form = this.salaryCoefficientForm.get('coefficient');
          if (checkMultiInput.length > 0) {
            (form as FormArray).controls[i].get('comparison_value_to_coefficient').setValidators(Validators.required);
            this.checkComparisonCoefficient[i] = true;
          } else {
            (form as FormArray).controls[i].get('comparison_value_to_coefficient').clearValidators();
            this.checkComparisonCoefficient[i] = false;
          }
          this.checkComparisonForm(2, element.comparison, i);
          this.checkDateLoadValid(2, element.expired_month_coefficient, i);
        });
        resCoefficient.data.forEach((item, i) => {
          this.checkFormulaLoad(i, item.valueType);
        });
        this.validOverLeft(valueOverLeft, 2);
      }
    });
  }

  loadDataSaleFeeForm() {
    this.configSalaryLeaderService.getAllSalConfigSaleFee().subscribe(resSaleFee => {
      if (resSaleFee.data.length > 0) {
        this.setDataInSaleFeeForm(resSaleFee.data);
      }
    });
  }

  setDataInSaleFeeForm(data) {
    if (data.length > 0) {
      data.forEach((element, i) => {
        this.checkDuplicateFeeId.push(false);
        this.checkMonthSaleFee.push(false);
        if (this.checkSearchSaleFee) {
          let a = this.salaryLeaderModel.salConfigSaleFeeModel.filter(item => item.id == element.id);
          if (a.length > 0) {
            this.salaryLeaderModel.salConfigSaleFeeModel.forEach(item => {
              if (item.id == a[0].id) {
                item = a[0];
              }
            });
          } else {
            this.salaryLeaderModel.salConfigSaleFeeModel.push(element);
          }
        } else {
          this.salaryLeaderModel.salConfigSaleFeeModel.push(element);
        }
        let target = {
          id: null,
          fee_id: 'null',
          receive_from_fee: 'null',
          receive_percent_fee: '',
          expired_month_fee: null
        }
        target.id = element.id ? element.id : null;
        target.fee_id = element.feeId ? element.feeId : null;
        target.receive_from_fee = element.receiveFrom ? element.receiveFrom : null;
        target.receive_percent_fee = (element.receivePercent || element.receivePercent == 0) ? element.receivePercent : null;
        target.expired_month_fee = element.expiredMonth ? element.expiredMonth : null;

        const control = <FormArray>this.feeForm.controls['fees'];
        if (target.id) {
          if (control.value.length > 0) {
            let arrCheck = control.value.filter(item => item.id == target.id);
            if (arrCheck.length == 0) {
              control.push(this.feeModal(target));
            }
          } else {
            control.push(this.feeModal(target));
          }
        }
      });
      this.feeForm.value.fees.forEach((element, i) => {

        if (element.expired_month_fee) {
          this.setDateSaleFee(element.expired_month_fee, i);
          this.checkDateLoadValid(3, element.expired_month_fee, i);
        }
      });
    }
  }

  comparisonChange(indexParent, indexChild) {
    var comparisonValue = this.targetForm.value.itemsGlobal[indexParent].items[indexChild].comparison;
    let checkMultiInput = this.comparisonDefault.filter(item => item.value == comparisonValue && item.checkMultiInput);

    const form = this.targetForm.get('itemsGlobal');
    const formChild = (form as FormArray).controls[indexParent].get('items');
    if (checkMultiInput.length > 0) {
      this.checkComparison[indexParent][indexChild] = true;
      (formChild as FormArray).controls[indexChild].get('comparison_value_to').setValue('');
      (formChild as FormArray).controls[indexChild].get('comparison_value_to').setValidators([Validators.required, Validators.pattern(/^-?(0|[1-9]\d*)?$/)]);
    } else {
      (formChild as FormArray).controls[indexChild].get('comparison_value_to').setValue('');
      (formChild as FormArray).controls[indexChild].get('comparison_value_to').clearValidators();
      (formChild as FormArray).controls[indexChild].get('comparison_value_to').updateValueAndValidity();
      this.checkComparison[indexParent][indexChild] = false;
    }
  }

  comparisonCompleteChange(index) {
    var comparisonValueComplete = this.targetCompleteForm.value.itemsComplete[index].comparison_complete;
    let checkMultiInput = this.comparisonDefault.filter(item => item.value == comparisonValueComplete && item.checkMultiInput);

    const form = this.targetCompleteForm.get('itemsComplete');
    if (checkMultiInput.length > 0) {
      this.checkComparisonComplete[index] = true;
      (form as FormArray).controls[index].get('comparison_value_to_complete').setValidators([Validators.required, Validators.pattern(/^-?(0|[1-9]\d*)?$/)]);
      (form as FormArray).controls[index].get('comparison_value_to_complete').setValue('');
    } else {
      (form as FormArray).controls[index].get('comparison_value_to_complete').setValue('');
      (form as FormArray).controls[index].get('comparison_value_to_complete').clearValidators();
      (form as FormArray).controls[index].get('comparison_value_to_complete').updateValueAndValidity();
      this.checkComparisonComplete[index] = false;
    }
  }

  comparisonCoefficientChange(index) {
    let comparisonValueCoefficient = this.salaryCoefficientForm.value.coefficient[index].comparison_coefficient;

    let checkMultiInput = this.comparisonDefault.filter(item => item.value == comparisonValueCoefficient && item.checkMultiInput);

    const form = this.salaryCoefficientForm.get('coefficient');
    if (checkMultiInput.length > 0) {
      this.checkComparisonCoefficient[index] = true;
      (form as FormArray).controls[index].get('comparison_value_to_coefficient').setValidators([Validators.required, Validators.pattern(/^-?(0|[1-9]\d*)?$/)]);
      (form as FormArray).controls[index].get('comparison_value_to_coefficient').setValue('');
    } else {
      (form as FormArray).controls[index].get('comparison_value_to_coefficient').setValue('');
      (form as FormArray).controls[index].get('comparison_value_to_coefficient').clearValidators();
      (form as FormArray).controls[index].get('comparison_value_to_coefficient').updateValueAndValidity();
      this.checkComparisonCoefficient[index] = false;
    }
  }

  createFormTargetGlobal() {
    this.targetForm = this.formBuilder.group({
      itemsGlobal: this.formBuilder.array([])
    });
  }

  createFormTargetComplete() {
    this.targetCompleteForm = this.formBuilder.group({
      itemsComplete: this.formBuilder.array([])
    });
    // itemsComplete: this.formBuilder.array([this.targetCompleteModal(this.target1), this.targetCompleteModal(this.target2)], [Validators.required])
  }

  createFormSalaryCoefficient() {
    this.salaryCoefficientForm = this.formBuilder.group({
      coefficient: this.formBuilder.array([])
    });
  }

  createFormFee() {
    this.feeForm = this.formBuilder.group({
      fees: this.formBuilder.array([])
    });
  }

  targetGlobalModal(modalLoad?) {
    let arr = [];
    if (modalLoad && modalLoad.items.length > 0) {
      modalLoad.items.forEach(item => {
        arr.push(this.targetModal(item));
      });
    }
    return this.formBuilder.group({
      target_id: new FormControl(modalLoad && modalLoad.target_id ? modalLoad.target_id : 'null', Validators.pattern('^[^null]*$')),
      items: this.formBuilder.array(modalLoad && modalLoad.items ? arr : [this.targetModal(this.target1), this.targetModal(this.target2)])
    });
  }

  targetModal(target) {
    return this.formBuilder.group({
      id: new FormControl(target.id ? target.id : null),
      comparison: new FormControl(target.comparison, Validators.pattern('^[<>=In]+$')),
      comparison_value_from: new FormControl(target.comparison_value_from, [Validators.required, Validators.maxLength(5), Validators.pattern(/^-?(0|[1-9]\d*)?$/)]),
      comparison_value_to: new FormControl(target.comparison_value_to, [Validators.maxLength(5), Validators.pattern(/^-?(0|[1-9]\d*)?$/)]),
      comparison_value: new FormControl(target.comparison_value, [Validators.required, Validators.maxLength(5), Validators.pattern(/^-?(0|[1-9]\d*)?$/)]),
      expired_month: new FormControl(target.expired_month)
    });
  }

  targetCompleteModal(targetComplete) {
    return this.formBuilder.group({
      id: new FormControl(targetComplete.id ? targetComplete.id : null),
      comparison_complete: new FormControl(targetComplete.comparison, Validators.pattern('^[<>=In]+$')),
      comparison_value_from_complete: new FormControl(targetComplete.comparison_value_from, [Validators.required, Validators.maxLength(5), Validators.pattern(/^-?(0|[1-9]\d*)?$/)]),
      comparison_value_to_complete: new FormControl(targetComplete.comparison_value_to, [Validators.maxLength(5), Validators.pattern(/^-?(0|[1-9]\d*)?$/)]),
      comparison_value_complete: new FormControl(targetComplete.comparison_value, [Validators.required, Validators.maxLength(5), Validators.pattern(/^-?(0|[1-9]\d*)?$/)]),
      expired_month_complete: new FormControl(targetComplete.expired_month)
    });
  }

  salaryCoefficientModal(targetCoefficient) {
    return this.formBuilder.group({
      id: new FormControl(targetCoefficient.id ? targetCoefficient.id : null),
      comparison_coefficient: new FormControl(targetCoefficient.comparison, Validators.pattern('^[<>=In]+$')),
      comparison_value_from_coefficient: new FormControl(targetCoefficient.comparison_value_from, [Validators.required, Validators.maxLength(5), Validators.pattern(/^-?(0|[1-9]\d*)?$/)]),
      comparison_value_to_coefficient: new FormControl(targetCoefficient.comparison_value_to, [Validators.maxLength(5), Validators.pattern(/^-?(0|[1-9]\d*)?$/)]),
      value_type_coefficient: new FormControl(targetCoefficient.value_type, Validators.pattern('^[^null]*$')),
      comparison_value_coefficient: new FormControl(targetCoefficient.comparison_value || targetCoefficient.comparison_value == 0 ? targetCoefficient.comparison_value : '', [Validators.maxLength(5), Validators.pattern(/^-?(0|[1-9]\d*)?$/)]),
      formula: new FormControl(targetCoefficient.formula ? targetCoefficient.formula : '', Validators.maxLength(100)),
      expired_month_coefficient: new FormControl(targetCoefficient.expired_month)
    });
  }

  feeModal(feeModal) {
    return this.formBuilder.group({
      id: new FormControl(feeModal.id ? feeModal.id : null),
      fee_id: new FormControl(feeModal.fee_id, Validators.pattern('^[^null]*$')),
      receive_from_fee: new FormControl(feeModal.receive_from_fee, Validators.pattern('^[^null]*$')),
      receive_percent_fee: new FormControl(feeModal.receive_percent_fee, [Validators.required, Validators.pattern(/^-?(0|[1-9]\d*)?$/)]),
      expired_month_fee: new FormControl(feeModal.expired_month_fee),
    })
  }

  addTarget() {
    const control = <FormArray>this.targetForm.controls['itemsGlobal'];
    control.push(this.targetGlobalModal());
    this.mdtDate.push([]);
    this.mdtDate[control.length - 1].push(new FormControl(null))
    this.checkComparison.push([]);
    let index = control.length - 1;

    this.targetForm.value.itemsGlobal[index].items.forEach((item, j) => {
      this.checkComparison[index].push(false);
      this.checkComparisonForm(0, item.comparison, index, j);
    });
    this.submited = false;
  }

  addTargetChild(index) {
    const control = (<FormArray>this.targetForm.controls['itemsGlobal']).at(index).get('items') as FormArray;
    control.push(this.targetModal(this.target));
    this.mdtDate[index].push(new FormControl(null));
    this.setDateTargetForm(null, index, control.length - 1);

    this.targetForm.value.itemsGlobal[index].items.forEach((item, j) => {
      this.checkComparison[index].push(false);
      this.checkComparisonForm(0, item.comparison, index, j);
    });
    this.submited = false;
  }

  addTargetComplete() {
    const control = <FormArray>this.targetCompleteForm.controls['itemsComplete'];
    control.push(this.targetCompleteModal(this.target));
    this.mdtDateComplete.push(new FormControl(null));
    this.setDateCompleteForm(null, control.length - 1);

    this.targetCompleteForm.value.itemsComplete.forEach((element, i) => {
      this.checkComparisonForm(1, element.comparison_complete, i);
    });
    this.submited = false;
  }

  addSalaryCoefficient() {
    const control = <FormArray>this.salaryCoefficientForm.controls['coefficient'];
    control.push(this.salaryCoefficientModal(this.coefficientModel));
    this.mdtDateCoefficient.push(new FormControl(null));
    this.setDateCoefficient(null, control.length - 1);

    this.salaryCoefficientForm.value.coefficient.forEach((element, i) => {
      this.checkComparisonForm(2, element.comparison_coefficient, i);
    });
    this.checkFormula.push(0);
    this.submited = false;
  }

  addFee() {
    const control = <FormArray>this.feeForm.controls['fees'];
    control.push(this.feeModal(this.saleFee));
    this.mdtDateSaleFee.push(new FormControl(null));
    this.setDateSaleFee(null, control.length - 1);
    this.submited = false;
  }

  removeTargetChild(data, index) {
    this.titleAction = 'xóa';
    this.modalRefConfirmGlobal = this.modalService.show(this.confirmGlobal);
    let type = 0;
    this.modalRefConfirmGlobal.content = { type, data, index };
  }

  removeTargetChildConfirm(data, index) {
    let target = data.value;
    if (target.target_id && target.target_id != 'null') {
      this.configSalaryLeaderService.getAllSalConfigStaffTargetCompleteByService(target.target_id).subscribe(res => {
        if (res && res.data.length > 0) {
          res.data.forEach(element => {
            this.salaryLeaderModel.salConfigStaffTargetModel.forEach(item => {
              if (item.id == element.id) {
                item.status = '0';
              }
            });
          });
        }
      });
    }

    let indexOf = this.mdtDate.indexOf(this.mdtDate[index]);
    this.mdtDate.splice(indexOf, 1);

    const control = <FormArray>this.targetForm.controls['itemsGlobal'];
    control.removeAt(index);

    this.submited = false;
  }

  removeItems(item, indexParent, indexChild) {
    this.titleAction = 'xóa';
    this.modalRefConfirmGlobal = this.modalService.show(this.confirmGlobal);
    let type = 1;
    this.modalRefConfirmGlobal.content = { type, item, indexParent, indexChild };
  }

  removeItemsConfirm(item, indexParent, indexChild) {
    let target = item.value;
    if (target.id) {
      this.configSalaryLeaderService.getSalConfigStaffTargetById(target.id).subscribe(res => {
        if (res.data) {
          let value = res.data;
          this.salaryLeaderModel.salConfigStaffTargetModel.forEach(item => {
            if (item.id == value.id) {
              item.status = '0';
            }
          });
        }
      });
    }

    let indexOf = this.mdtDate[indexParent].indexOf(this.mdtDate[indexParent][indexChild]);
    this.mdtDate[indexParent].splice(indexOf, 1);

    const control = (<FormArray>this.targetForm.controls['itemsGlobal']).at(indexParent).get('items') as FormArray;
    control.removeAt(indexChild);

    this.submited = false;
  }

  removeItemComplete(item, index) {
    this.titleAction = 'xóa';
    this.modalRefConfirmGlobal = this.modalService.show(this.confirmGlobal);
    let type = 2;

    this.modalRefConfirmGlobal.content = { type, item, index };
  }

  removeItemCompleteConfirm(item, index) {
    let target = item.value;

    if (target.id) {
      this.configSalaryLeaderService.getSalConfigStaffTargetById(target.id).subscribe(res => {
        if (res.data) {
          let value = res.data;
          this.salaryLeaderModel.salConfigStaffTargetModel.forEach(item => {
            if (item.id == value.id) {
              item.status = '0';
            }
          });
        }
      });
    }

    let indexOf = this.mdtDateComplete.indexOf(this.mdtDateComplete[index]);
    this.mdtDateComplete.splice(indexOf, 1);

    const control = <FormArray>this.targetCompleteForm.controls['itemsComplete'];
    control.removeAt(index);

    this.submited = false;
  }

  removeSalaryCoefficient(item, index) {
    this.titleAction = 'xóa';
    this.modalRefConfirmGlobal = this.modalService.show(this.confirmGlobal);
    let type = 3;

    this.modalRefConfirmGlobal.content = { type, item, index };
  }

  removeSalaryCoefficientConfirm(item, index) {
    let target = item.value;
    if (target.id) {
      this.configSalaryLeaderService.getSalConfigHsTinhLuongById(target.id).subscribe(res => {
        if (res.data) {
          let value = res.data;
          this.salaryLeaderModel.salConfigHsTinhLuongModel.forEach(item => {
            if (item.id == value.id) {
              item.status = '0';
            }
          });
        }
      });
    }

    let indexOf = this.mdtDateCoefficient.indexOf(this.mdtDateCoefficient[index]);
    this.mdtDateCoefficient.splice(indexOf, 1);

    const control = <FormArray>this.salaryCoefficientForm.controls['coefficient'];
    control.removeAt(index);

    this.submited = false;
  }

  removeFee(item, index) {
    this.titleAction = 'xóa';
    this.modalRefConfirmGlobal = this.modalService.show(this.confirmGlobal);
    let type = 4;

    this.modalRefConfirmGlobal.content = { type, item, index };
  }

  removeFeeConfirm(item, index) {
    let target = item.value;

    if (target.id) {
      this.configSalaryLeaderService.getSalConfigSaleFeeById(target.id).subscribe(resFee => {
        if (resFee.data) {
          let value = resFee.data;

          this.salaryLeaderModel.salConfigSaleFeeModel.forEach(item => {
            if (item.id == value.id) {
              item.status = '0';
            }
          });
        }
        this.checkChangeValueSaleFee = true;
      });
    }

    let indexOf = this.mdtDateSaleFee.indexOf(this.mdtDateSaleFee[index]);
    this.mdtDateSaleFee.splice(indexOf, 1);

    const control = <FormArray>this.feeForm.controls['fees'];
    control.removeAt(index);

    this.submited = false;
  }

  editFormulaCoefficient(index, template: TemplateRef<any>) {
    let comparison = this.salaryCoefficientForm.value.coefficient[index].comparison_coefficient;

    const form = this.salaryCoefficientForm.get('coefficient');
    if (!comparison && comparison == 'null') {
      (form as FormArray).controls[index].get('comparison_coefficient').markAsTouched();
      return;
    }
    let value = this.salaryCoefficientForm.value.coefficient[index].formula
    this.formulaValue = value;
    this.modalRef = this.modalService.show(template);
    this.modalRef.content = index;
    this.checkChangeFormulaValue = false;
  }

  getFormula() {
    let check;
    if (!this.modalRef) {
      return;
    }
    let comparison = this.salaryCoefficientForm.value.coefficient[this.modalRef.content].comparison_coefficient;

    let indexOfFromNumber = this.formulaValue.indexOf('{formNumber}');
    let indexOfToNumber = this.formulaValue.indexOf('{toNumber}');
    let indexOfX = this.formulaValue.indexOf('{x}');

    let checkRegex = false;
    let arrValue = this.formulaValue.split('');
    let indexOfStart;
    let indexOfEnd;
    arrValue.forEach((item, i) => {
      if (item == '{') {
        indexOfStart = i;
      }
      if (item == '}') {
        indexOfEnd = i;
      }

      if ((indexOfStart || indexOfStart == 0) && (indexOfEnd || indexOfEnd == 0) && indexOfStart < indexOfEnd) {
        let a = this.formulaValue.slice(indexOfStart, indexOfEnd + 1);
        if (a != '{formNumber}' && a != '{toNumber}' && a != '{x}') {
          checkRegex = true;
        }
      }
    });

    if (checkRegex) {
      this.checkFormulaValueRegex = 'Các từ khóa được phép: {formNumber}, {toNumber} và {x}';
      this.checkFormulaValue = '';
    } else {
      this.checkFormulaValueRegex = '';
      if (comparison == '<=' || comparison == '<') {
        if (indexOfFromNumber != -1) {
          this.checkFormulaValue = 'Điều kiện ' + comparison + ' không tồn tại {formNumber}';
          check = false;
        } else {
          if (indexOfToNumber != -1 && indexOfX != -1) {
            check = true;
            this.checkFormulaValue = '';
          } else {
            this.checkFormulaValue = '{toNumber} và {x} là bắt buộc';
            check = false;
          }
        }
      } else if (comparison == '>=' || comparison == '>') {
        if (indexOfToNumber != -1) {
          this.checkFormulaValue = 'Điều kiện ' + comparison + ' không tồn tại {toNumber}';
          check = false;
        } else {
          if (indexOfFromNumber != -1 && indexOfX != -1) {
            check = true;
            this.checkFormulaValue = '';
          } else {
            this.checkFormulaValue = '{formNumber} và {x} là bắt buộc';
            check = false;
          }
        }
      } else {
        if (indexOfFromNumber != -1 && indexOfToNumber != -1 && indexOfX != -1) {
          check = true;
          this.checkFormulaValue = '';
        } else {
          this.checkFormulaValue = '{formNumber}, {toNumber} và {x} là bắt buộc';
          check = false;
        }
      }
    }

    if (check || !checkRegex) {
      this.salaryCoefficientForm.value.coefficient[this.modalRef.content].formula = this.formulaValue;
      this.modalRef.hide();
      this.modalRef = null;
    }
  }

  closeModal() {
    this.modalRef.hide();
    this.modalRef = null;
  }

  searchFee() {
    if (this.checkChangeValueSaleFee) {
      this.modalRefConfirm = this.modalService.show(this.confirmSearchSaleFee);
      if (this.confirmSearchFee) {
        this.configSalaryLeaderService.getSalConfigSaleFeeByCondition(this.valueFeeSearch.trim(), this.receiveSearch).subscribe(res => {
          if (res.data && res.data.length > 0) {
            this.checkSearchSaleFee = true;
            const control = <FormArray>this.feeForm.controls['fees'];

            let size = control.length;
            for (let i = 0; i < size; i++) {
              control.removeAt(0);
            }
            this.setDataInSaleFeeForm(res.data);
          }
        });
      }
    } else {
      this.configSalaryLeaderService.getSalConfigSaleFeeByCondition(this.valueFeeSearch.trim(), this.receiveSearch).subscribe(res => {
        if (res.data && res.data.length > 0) {
          const control = <FormArray>this.feeForm.controls['fees'];

          this.checkSearchSaleFee = true;
          let size = control.length;
          for (let i = 0; i < size; i++) {
            control.removeAt(0);
          }
          this.setDataInSaleFeeForm(res.data);
        }
      });
    }
    this.checkChangeValueSaleFee = false;
    this.submited = false;
  }

  changePercentSaleFee(index) {
    this.checkChangeValueSaleFee = true;
  }

  feeIdChange(index) {
    this.checkChangeValueSaleFee = true;
    let id = +this.feeForm.value.fees[index].fee_id;

    let arr = this.feeForm.value.fees.filter(item => item.fee_id == id);
    if (arr.length > 1) {
      this.checkSubmit = true;
      this.checkDuplicateFeeId[index] = true;
    } else {
      this.checkDuplicateFeeId[index] = false;
    }
  }

  feeReceiveChange(index) {
    this.checkChangeValueSaleFee = true;
  }

  confirmSearch() {
    this.confirmSearchFee = true;
    this.configSalaryLeaderService.getSalConfigSaleFeeByCondition(this.valueFeeSearch.trim(), this.receiveSearch).subscribe(res => {
      if (res.data && res.data.length > 0) {
        const control = <FormArray>this.feeForm.controls['fees'];

        let size = control.length;
        for (let i = 0; i < size; i++) {
          control.removeAt(0);
        }
        this.setDataInSaleFeeForm(res.data);
        this.checkSearchSaleFee = true;
      }
    });
    this.modalRefConfirm.hide();
  }

  rejectSearch() {
    this.confirmSearchFee = false;
    this.modalRefConfirm.hide();
  }

  pageChange(page) {

  }

  onSubmit() {
    this.submited = true;
    if (this.targetForm.invalid || this.targetCompleteForm.invalid || this.feeForm.invalid) {
      return;
    }

    let checkFalse = false;
    this.checkDuplicateServiceId.forEach(item => {
      if (item == true) {
        this.checkSubmit = true;
        checkFalse = true;
      }
    });
    this.checkDuplicateFeeId.forEach(item => {
      if (item == true) {
        this.checkSubmit = true;
        checkFalse = true;
      }
    });
    this.checkMonthTarget.forEach(item => {
      if (item == true) {
        this.checkSubmit = true;
        checkFalse = true;
      }
    }); this.checkMonthComplete.forEach(item => {
      if (item == true == true) {
        this.checkSubmit = true;
        checkFalse = true;
      }
    }); this.checkMonthCoefficient.forEach(item => {
      if (item == true) {
        this.checkSubmit = true;
        checkFalse = true;
      }
    }); this.checkMonthSaleFee.forEach(item => {
      if (item == true) {
        this.checkSubmit = true;
        checkFalse = true;
      }
    });
    if (checkFalse) {
      return;
    }

    this.submitTargetForm(this.targetForm.value.itemsGlobal, this.targetCompleteForm.value.itemsComplete);
    this.submitFeeForm(this.feeForm.value.fees);
    this.submitSalaryCoefficientForm(this.salaryCoefficientForm.value.coefficient);

    if (this.checkOverLeftTargetForm || this.checkOverLeftTargetCompleteForm || this.checkOverLeftTargetCoefficientForm) {
      this.modalRefError = this.modalService.show(this.overleftModal);
      return;
    }

    this.titleAction = 'lưu';
    this.modalRefConfirmGlobal = this.modalService.show(this.confirmGlobal);
    let type = 5;
    this.modalRefConfirmGlobal.content = { type };
  }

  saveFormConfirm() {
    this.configSalaryLeaderService.updateConfigSalaryLeader(this.salaryLeaderModel).subscribe((res: any) => {
      if (res.code == 200) {
        // this.showSuccess(this.translate.instant('management.warningconfig.addsuccess'));
        this.showSuccess('Đã lưu thành công');
        this.getData();
      } else {
        this.showError(res.errors);
      }
    });
    this.formulaValue = '';
    this.checkSearchSaleFee = false;
    this.checkChangeValueSaleFee = false;
    this.confirmSearchFee = false;
    this.checkSubmit = false;
  }

  submitFeeForm(data: any) {
    data.forEach(element => {
      let salConfigSaleFeeModel = new SalConfigSaleFeeModel();
      salConfigSaleFeeModel.id = element.id ? element.id : null;
      salConfigSaleFeeModel.feeId = element.fee_id;
      salConfigSaleFeeModel.receiveFrom = element.receive_from_fee;
      salConfigSaleFeeModel.receivePercent = Number(element.receive_percent_fee);
      salConfigSaleFeeModel.expiredMonth = element.expired_month_fee;
      salConfigSaleFeeModel.status = '1';

      if (salConfigSaleFeeModel.id) {
        this.salaryLeaderModel.salConfigSaleFeeModel.forEach(item => {
          if (item.id == salConfigSaleFeeModel.id) {
            item.receivePercent = salConfigSaleFeeModel.receivePercent;
            item.feeId = salConfigSaleFeeModel.feeId;
            item.receiveFrom = salConfigSaleFeeModel.receiveFrom;
            item.expiredMonth = salConfigSaleFeeModel.expiredMonth;
            item.status = salConfigSaleFeeModel.status;
          }
        });
      } else {
        this.salaryLeaderModel.salConfigSaleFeeModel.push(salConfigSaleFeeModel);
      }
    });
  }

  submitSalaryCoefficientForm(data: any) {
    let salaryCoefficientArr = [];
    data.forEach((element, i) => {
      let salConfigHsTinhLuong = new SalConfigHsTinhLuongModel();
      salConfigHsTinhLuong.id = element.id ? element.id : null;
      salConfigHsTinhLuong.staffType = '2';
      salConfigHsTinhLuong.status = '1';
      salConfigHsTinhLuong.comparison = element.comparison_coefficient;
      salConfigHsTinhLuong.comparisonValueFrom = element.comparison_value_from_coefficient;
      salConfigHsTinhLuong.comparisonValueTo = element.comparison_value_to_coefficient;
      salConfigHsTinhLuong.valueType = element.value_type_coefficient;
      salConfigHsTinhLuong.formula = element.formula;
      salConfigHsTinhLuong.value = element.comparison_value_coefficient;
      salConfigHsTinhLuong.expiredMonth = this.mdtDateCoefficient[i] && this.mdtDateCoefficient[i].value ? this.mdtDateCoefficient[i].value._d : null;
      salaryCoefficientArr.push(salConfigHsTinhLuong);

      if (salConfigHsTinhLuong.id) {
        this.salaryLeaderModel.salConfigHsTinhLuongModel.forEach(item => {
          if (item.id == salConfigHsTinhLuong.id) {
            item.staffType = salConfigHsTinhLuong.staffType;
            item.valueType = salConfigHsTinhLuong.valueType;
            item.status = salConfigHsTinhLuong.status;
            item.comparison = salConfigHsTinhLuong.comparison;
            item.value = salConfigHsTinhLuong.value;
            item.expiredMonth = salConfigHsTinhLuong.expiredMonth;
            item.comparisonValueFrom = salConfigHsTinhLuong.comparisonValueFrom;
            item.comparisonValueTo = salConfigHsTinhLuong.comparisonValueTo;
            item.formula = salConfigHsTinhLuong.formula;
          }
        });
      } else {
        this.salaryLeaderModel.salConfigHsTinhLuongModel.push(salConfigHsTinhLuong);
      }
    });
    this.validOverLeft(salaryCoefficientArr, 2);
  }

  submitTargetForm(data: any, dataComplete: any) {
    let arrTarget = [];
    data.forEach((element, i) => {
      element.items.forEach((item, j) => {
        let salConfigStaffTarget = new SalConfigStaffTargetModel();
        salConfigStaffTarget.id = item.id ? item.id : null;
        salConfigStaffTarget.serviceId = element.target_id;
        salConfigStaffTarget.staffType = '2';
        salConfigStaffTarget.status = '1';
        salConfigStaffTarget.comparison = item.comparison;
        this.checkValueFromToTarget(salConfigStaffTarget, item.comparison, item.comparison_value_from, item.comparison_value_to);
        salConfigStaffTarget.value = item.comparison_value;
        salConfigStaffTarget.expiredMonth = this.mdtDate[i] && this.mdtDate[i][j] && this.mdtDate[i][j].value ? this.mdtDate[i][j].value._d : null;
        arrTarget.push(salConfigStaffTarget);

        if (salConfigStaffTarget.id) {
          if (this.salaryLeaderModel.salConfigStaffTargetModel.length > 0) {
            this.salaryLeaderModel.salConfigStaffTargetModel.forEach(item => {
              if (item.id == salConfigStaffTarget.id) {
                item.serviceId = salConfigStaffTarget.serviceId;
                item.staffType = salConfigStaffTarget.staffType;
                item.status = salConfigStaffTarget.status;
                item.comparison = salConfigStaffTarget.comparison;
                item.value = salConfigStaffTarget.value;
                item.expiredMonth = salConfigStaffTarget.expiredMonth;
                item.comparisonValueFrom = salConfigStaffTarget.comparisonValueFrom;
                item.comparisonValueTo = salConfigStaffTarget.comparisonValueTo;
              }
            });
          } else {
            this.salaryLeaderModel.salConfigStaffTargetModel.push(salConfigStaffTarget);
          }
        } else {
          this.salaryLeaderModel.salConfigStaffTargetModel.push(salConfigStaffTarget);
        }
      });
    });

    dataComplete.forEach((elementComplete, i) => {
      let salConfigStaffTarget = new SalConfigStaffTargetModel();
      salConfigStaffTarget.id = elementComplete.id
      salConfigStaffTarget.serviceId = 0;
      salConfigStaffTarget.staffType = '1';
      salConfigStaffTarget.status = '1';
      salConfigStaffTarget.comparison = elementComplete.comparison_complete;
      this.checkValueFromToTarget(salConfigStaffTarget, elementComplete.comparison_complete, elementComplete.comparison_value_from_complete, elementComplete.comparison_value_to_complete);
      salConfigStaffTarget.value = elementComplete.comparison_value_complete;
      salConfigStaffTarget.expiredMonth = this.mdtDateComplete[i] && this.mdtDateComplete[i].value ? this.mdtDateComplete[i].value._d : null;
      arrTarget.push(salConfigStaffTarget);

      if (salConfigStaffTarget.id) {
        if (this.salaryLeaderModel.salConfigStaffTargetModel.length > 0) {
          this.salaryLeaderModel.salConfigStaffTargetModel.forEach(item => {
            if (item.id == salConfigStaffTarget.id) {
              item.serviceId = salConfigStaffTarget.serviceId;
              item.staffType = salConfigStaffTarget.staffType;
              item.status = salConfigStaffTarget.status;
              item.comparison = salConfigStaffTarget.comparison;
              item.value = salConfigStaffTarget.value;
              item.expiredMonth = salConfigStaffTarget.expiredMonth;
              item.comparisonValueFrom = salConfigStaffTarget.comparisonValueFrom;
              item.comparisonValueTo = salConfigStaffTarget.comparisonValueTo;
            }
          });
        } else {
          this.salaryLeaderModel.salConfigStaffTargetModel.push(salConfigStaffTarget);
        }
      } else {
        this.salaryLeaderModel.salConfigStaffTargetModel.push(salConfigStaffTarget);
      }
    });
    this.validOverLeft(arrTarget, 1);
  }

  changeFormula(index) {
    let value = this.salaryCoefficientForm.value.coefficient[index].value_type_coefficient;
    this.checkFormulaLoad(index, value);
  }

  checkFormulaLoad(index, valueType) {
    const form = (this.salaryCoefficientForm.get('coefficient') as FormArray).controls[index];

    if (valueType == '0') {
      form.get('comparison_value_coefficient').setValidators(Validators.required);
      form.get('formula').clearValidators();
      this.salaryCoefficientForm.value.coefficient[index].formula = null;
      this.setCheckFormula(index, 0);
    } else if (valueType == '1') {
      form.get('comparison_value_coefficient').clearValidators();
      form.get('formula').setValidators(Validators.required);
      this.salaryCoefficientForm.value.coefficient[index].comparison_value_coefficient = null;
      this.setCheckFormula(index, 1);
    }
  }

  setCheckFormula(index, type) {
    if (this.checkFormula.length > 0) {
      this.checkFormula[index] = type;
    }
  }

  changeService(index) {
    let id = +this.targetForm.value.itemsGlobal[index].target_id;

    let arr = this.targetForm.value.itemsGlobal.filter(item => item.target_id == id);
    if (arr.length > 1) {
      this.checkSubmit = true;
      this.checkDuplicateServiceId[index] = true;
    } else {
      this.checkDuplicateServiceId[index] = false;
    }
  }

  dateChange(value: any, indexParent, indexChild) {
    const valueString = value.target.value;
    const valueMonth = +valueString.split('/')[0];
    const valueYear = +valueString.split('/')[1];
    const monthCompare = (new Date()).getMonth() + 1;
    const yearCompare = (new Date()).getFullYear();

    if (valueYear >= yearCompare) {
      if (valueMonth >= monthCompare) {
        this.checkMonthTarget[indexParent][indexChild] = false;
        this.targetForm.value.itemsGlobal[indexParent].items[indexChild].expired_month = this.mdtDate[indexParent][indexChild].value._d;
        this.checkSubmit = false;
      } else {
        this.checkMonthTarget[indexParent][indexChild] = true;
      }
    } else {
      this.checkMonthTarget[indexParent][indexChild] = true;
    }
    this.mdtDate[indexParent][indexChild].setValue(_moment().set(valueString));
  }

  dateChangeComplete(value: any, index) {
    const valueString = value.target.value;

    const valueMonth = +valueString.split('/')[0];
    const valueYear = +valueString.split('/')[1];
    const monthCompare = (new Date()).getMonth() + 1;
    const yearCompare = (new Date()).getFullYear();

    if (valueYear >= yearCompare) {
      if (valueMonth >= monthCompare) {
        this.checkMonthComplete[index] = false;
        this.targetCompleteForm.value.itemsComplete[index].expired_month_complete = this.mdtDateComplete[index].value._d;
        this.checkSubmit = false;
      } else {
        this.checkMonthComplete[index] = true;
      }
    } else {
      this.checkMonthComplete[index] = true;
    }
    this.mdtDateComplete[index].setValue(_moment().set(valueString));
  }

  dateChangeCoefficient(value: any, index) {
    const valueString = value.target.value;

    const valueMonth = +valueString.split('/')[0];
    const valueYear = +valueString.split('/')[1];
    const monthCompare = (new Date()).getMonth() + 1;
    const yearCompare = (new Date()).getFullYear();

    if (valueYear >= yearCompare) {
      if (valueMonth >= monthCompare) {
        this.checkMonthCoefficient[index] = false;
        this.salaryCoefficientForm.value.coefficient[index].expired_month_coefficient = this.mdtDateCoefficient[index].value._d;
        this.checkSubmit = false;
      } else {
        this.checkMonthCoefficient[index] = true;
      }
    } else {
      this.checkMonthCoefficient[index] = true;
    }
    this.mdtDateCoefficient[index].setValue(_moment().set(valueString));
  }

  dateChangeSaleFee(value: any, index) {
    const valueString = value.target.value;

    const valueMonth = +valueString.split('/')[0];
    const valueYear = +valueString.split('/')[1];
    const monthCompare = (new Date()).getMonth() + 1;
    const yearCompare = (new Date()).getFullYear();

    if (valueYear >= yearCompare) {
      if (valueMonth >= monthCompare) {
        this.checkMonthSaleFee[index] = false;
        this.feeForm.value.fees[index].expired_month_fee = this.mdtDateSaleFee[index].value._d;
        this.checkSubmit = false;
      } else {
        this.checkMonthSaleFee[index] = true;
      }
    } else {
      this.checkMonthSaleFee[index] = true;
    }
    this.mdtDateSaleFee[index].setValue(_moment().set(valueString));
  }

  checkDateLoadValid(type, value, index, indexChild?) {
    const valueMonth = +new Date(value).getMonth() + 1;
    const valueYear = +new Date(value).getFullYear();
    const monthCompare = (new Date()).getMonth() + 1;
    const yearCompare = (new Date()).getFullYear();
    if (type == 0) {

      if (valueYear >= yearCompare) {
        if (valueMonth >= monthCompare) {
          this.checkMonthTarget[index][indexChild] = false;
        } else {
          this.checkMonthTarget[index][indexChild] = true;
        }
      } else {
        this.checkMonthTarget[index][indexChild] = true;
      }

    } else if (type == 1) {

      if (valueYear >= yearCompare) {
        if (valueMonth >= monthCompare) {
          this.checkMonthComplete[index] = false;
        } else {
          this.checkMonthComplete[index] = true;
        }
      } else {
        this.checkMonthComplete[index] = true;
      }

    } else if (type == 2) {

      if (valueYear >= yearCompare) {
        if (valueMonth >= monthCompare) {
          this.checkMonthCoefficient[index] = false;
        } else {
          this.checkMonthCoefficient[index] = true;
        }
      } else {
        this.checkMonthCoefficient[index] = true;
      }

    } else if (type == 3) {

      if (valueYear >= yearCompare) {
        if (valueMonth >= monthCompare) {
          this.checkMonthSaleFee[index] = false;
        } else {
          this.checkMonthSaleFee[index] = true;
        }
      } else {
        this.checkMonthSaleFee[index] = true;
      }

    }
  }

  chosenMonthHandler($event, pobjDatepicker: MatDatepicker<Moment>, indexParent, indexChild) {
    this.mdtDate[indexParent][indexChild].setValue($event);
    pobjDatepicker.close();

    const valueMonth = +this.mdtDate[indexParent][indexChild].value._d.getMonth() + 1;
    const valueYear = +this.mdtDate[indexParent][indexChild].value._d.getFullYear();
    const monthCompare = (new Date()).getMonth() + 1;
    const yearCompare = (new Date()).getFullYear();

    if (valueYear >= yearCompare) {
      if (valueMonth >= monthCompare) {
        this.checkMonthTarget[indexParent][indexChild] = false;
        this.targetForm.value.itemsGlobal[indexParent].items[indexChild].expired_month = this.mdtDate[indexParent][indexChild].value._d;
      } else {
        this.checkMonthTarget[indexParent][indexChild] = true;
      }
    } else {
      this.checkMonthTarget[indexParent][indexChild] = true;
    }
  }

  chosenMonthHandlerComplete($event, pobjDatepicker: MatDatepicker<Moment>, index) {
    this.mdtDateComplete[index].setValue($event);
    pobjDatepicker.close();

    const valueMonth = +this.mdtDateComplete[index].value._d.getMonth() + 1;
    const valueYear = +this.mdtDateComplete[index].value._d.getFullYear();
    const monthCompare = (new Date()).getMonth() + 1;
    const yearCompare = (new Date()).getFullYear();

    if (valueYear >= yearCompare) {
      if (valueMonth >= monthCompare) {
        this.checkMonthComplete[index] = false;
        this.targetCompleteForm.value.itemsComplete[index].expired_month_complete = this.mdtDateComplete[index].value._d;
      } else {
        this.checkMonthComplete[index] = true;
      }
    } else {
      this.checkMonthComplete[index] = true;
    }
  }

  chosenMonthHandlerCoefficient($event, pobjDatepicker: MatDatepicker<Moment>, index) {
    this.mdtDateCoefficient[index].setValue($event);
    pobjDatepicker.close();

    const valueMonth = +this.mdtDateCoefficient[index].value._d.getMonth() + 1;
    const valueYear = +this.mdtDateCoefficient[index].value._d.getFullYear();
    const monthCompare = (new Date()).getMonth() + 1;
    const yearCompare = (new Date()).getFullYear();

    if (valueYear >= yearCompare) {
      if (valueMonth >= monthCompare) {
        this.checkMonthCoefficient[index] = false;
        this.salaryCoefficientForm.value.coefficient[index].expired_month_coefficient = this.mdtDateCoefficient[index].value._d;
      } else {
        this.checkMonthCoefficient[index] = true;
      }
    } else {
      this.checkMonthCoefficient[index] = true;
    }
  }

  chosenMonthHandlerSaleFee($event, pobjDatepicker: MatDatepicker<Moment>, index) {
    this.checkChangeValueSaleFee = true;
    this.mdtDateSaleFee[index].setValue($event);
    pobjDatepicker.close();

    const valueMonth = +this.mdtDateSaleFee[index].value._d.getMonth() + 1;
    const valueYear = +this.mdtDateSaleFee[index].value._d.getFullYear();
    const monthCompare = (new Date()).getMonth() + 1;
    const yearCompare = (new Date()).getFullYear();

    if (valueYear >= yearCompare) {
      if (valueMonth >= monthCompare) {
        this.checkMonthSaleFee[index] = false;
        this.feeForm.value.fees[index].expired_month_fee = this.mdtDateSaleFee[index].value._d;
      } else {
        this.checkMonthSaleFee[index] = true;
      }
    } else {
      this.checkMonthSaleFee[index] = true;
    }

  }

  noMethod() {
  }

  get itemsGlobal(): FormArray {
    return this.targetForm.get('itemsGlobal') as FormArray;
  }

  get itemsComplete(): FormArray {
    return this.targetCompleteForm.get('itemsComplete') as FormArray;
  }

  get fees(): FormArray {
    return this.feeForm.get('fees') as FormArray;
  }

  get coefficient(): FormArray {
    return this.salaryCoefficientForm.get('coefficient') as FormArray;
  }

  checkValueFromToTarget(modelSave, comparison, valueFrom, valueTo) {
    switch (comparison) {
      case '<':
        modelSave.comparisonValueTo = valueFrom;
        break;
      case '<=':
        modelSave.comparisonValueTo = valueFrom;
        break;
      case '>':
        modelSave.comparisonValueFrom = valueFrom;
        break;
      case '>=':
        modelSave.comparisonValueFrom = valueFrom;
        break;
      case '==':
        modelSave.comparisonValueFrom = valueFrom;
        modelSave.comparisonValueTo = valueFrom;
        break;
      default:
        modelSave.comparisonValueFrom = valueFrom;
        modelSave.comparisonValueTo = valueTo;
    }
  }

  validOverLeft(dataCompare, check) {
    if (dataCompare && dataCompare.length > 0) {
      let arrCompare = [];
      if (check == 1) {
        let month;
        let serviceId;

        dataCompare.forEach((e, index) => {
          let dataObj = {
            serviceId: null,
            month: '',
            type: null,
            data: [],
            check: null
          }
          if (e.serviceId || e.serviceId == 0) {
            month = e.expiredMonth;
            serviceId = e.serviceId
            if (e.serviceId != 0) {
              if (e.status != '0') {
                if (month) {
                  dataObj.serviceId = e.serviceId;
                  dataObj.month = month;
                  dataObj.type = 1;
                  dataObj.data = dataCompare.filter(item => (item.serviceId && item.serviceId == serviceId) && (item.expiredMonth ? item.expiredMonth.toString() : null) == month.toString());

                  if (arrCompare.length > 0) {
                    let index = arrCompare.filter(item => item.serviceId == e.serviceId);
                    if (index.length == 0) {
                      arrCompare.push(dataObj);
                    }
                  } else {
                    arrCompare.push(dataObj);
                  }
                }
              }
            } else {
              if (e.status != '0') {
                if (month) {
                  dataObj.serviceId = 0;
                  dataObj.month = month;
                  dataObj.type = 2;

                  dataObj.data = dataCompare.filter(item => item.serviceId == 0 && (item.expiredMonth ? item.expiredMonth.toString() : null) == month.toString());

                  if (arrCompare.length > 0) {
                    let index = arrCompare.filter(item => item.serviceId == dataObj.serviceId);
                    if (index.length == 0) {
                      arrCompare.push(dataObj);
                    }
                  } else {
                    arrCompare.push(dataObj);
                  }
                }
              }
            }
          }
        });
        if (arrCompare.length > 0) {
          this.filterOverleft(arrCompare);
        }
      } else {
        dataCompare.forEach(e => {
          let dataObj = {
            serviceId: '',
            month: '',
            type: null,
            data: [],
            check: null
          }
          if (e.status != '0') {
            const month = e.expiredMonth;
            if (month) {

              dataObj.month = month;
              dataObj.type = 3;
              dataObj.data = dataCompare.filter(item => (!item.serviceId || item.serviceId != '') && (item.expiredMonth ? item.expiredMonth.toString() : null) == month.toString());

              if (arrCompare.length > 0) {
                let index = arrCompare.filter(item => item.serviceId == '' || item.serviceId == null);
                if (index.length == 0) {
                  arrCompare.push(dataObj);
                }
              } else {
                arrCompare.push(dataObj);
              }
            }
          }
        });
        if (arrCompare.length > 0) {
          this.filterOverleft(arrCompare);
        }
      }
    }
  }

  caseArrCompare(arrCompare) {
    let arrValue = [];
    arrCompare.forEach(element => {
      let arr = [];
      switch (element.comparison) {
        case '<':
          arr.push((element.comparisonValueFrom || element.comparisonValueFrom == 0) ? +element.comparisonValueFrom : null);
          arr.push((element.comparisonValueTo || element.comparisonValueTo == 0) ? +element.comparisonValueTo - 1 : null);
          break;
        case '<=':
          arr.push((element.comparisonValueFrom || element.comparisonValueFrom == 0) ? +element.comparisonValueFrom : null);
          arr.push((element.comparisonValueTo || element.comparisonValueTo == 0) ? +element.comparisonValueTo : null);
          break;
        case '>':
          arr.push((element.comparisonValueFrom || element.comparisonValueFrom == 0) ? +element.comparisonValueFrom + 1 : null);
          arr.push((element.comparisonValueTo || element.comparisonValueTo == 0) ? +element.comparisonValueTo : null);
          break;
        case '>=':
          arr.push((element.comparisonValueFrom || element.comparisonValueFrom == 0) ? +element.comparisonValueFrom : null);
          arr.push((element.comparisonValueTo || element.comparisonValueTo == 0) ? +element.comparisonValueTo : null);
          break;
        case '==':
          arr.push((element.comparisonValueFrom || element.comparisonValueFrom == 0) ? +element.comparisonValueFrom : null);
          arr.push((element.comparisonValueTo || element.comparisonValueTo == 0) ? +element.comparisonValueTo : null);
          break;
        case 'In':
          arr.push((element.comparisonValueFrom || element.comparisonValueFrom == 0) ? +element.comparisonValueFrom + 1 : null);
          arr.push((element.comparisonValueTo || element.comparisonValueTo == 0) ? +element.comparisonValueTo - 1 : null);
          break;
        case '>=In':
          arr.push((element.comparisonValueFrom || element.comparisonValueFrom == 0) ? +element.comparisonValueFrom : null);
          arr.push((element.comparisonValueTo || element.comparisonValueTo == 0) ? +element.comparisonValueTo - 1 : null);
          break;
        case '<=In':
          arr.push((element.comparisonValueFrom || element.comparisonValueFrom == 0) ? +element.comparisonValueFrom + 1 : null);
          arr.push((element.comparisonValueTo || element.comparisonValueTo == 0) ? +element.comparisonValueTo : null);
          break;
        case '==In':
          arr.push((element.comparisonValueFrom || element.comparisonValueFrom == 0) ? +element.comparisonValueFrom : null);
          arr.push((element.comparisonValueTo || element.comparisonValueTo == 0) ? +element.comparisonValueTo : null);
          break;
      };
      arrValue.push(arr);
    });
    return arrValue;
  }

  filterOverleft(arrCompare) {
    if (arrCompare.length > 0) {
      let data = [];
      arrCompare.forEach(element => {
        element.check = false;
        data = this.caseArrCompare(element.data);
        data.sort((n1, n2) => n1[0] - n2[0]).forEach((item, i) => {
          if (i >= 1) {
            if (data[i][0] <= data[i - 1][1]) {
              element.check = true;
            }
          }
        });
        this.checkTypeOverLeft(element);
      });
    }
  }

  checkTypeOverLeft(element) {
    switch (element.type) {
      case 1:
        if (element.check) {
          if (element.serviceId && element.serviceId != 0) {
            let modal = {
              serviceName: '',
              month: ''
            }
            modal.month = element.month;
            this.serviceService.getServiceByID(element.serviceId).subscribe((s: any) => {
              modal.serviceName = s.data.name;
              this.checkOverLeft.push(modal);
            });
          }
          this.checkOverLeftTargetForm = true;
        } else {
          this.checkOverLeftTargetForm = false;
        }
        break;
      case 2:
        if (element.check) {
          let modal = {
            serviceName: null,
            month: ''
          }
          modal.month = element.month;
          modal.serviceName = 0;
          this.checkOverLeft.push(modal);
          this.checkOverLeftTargetCompleteForm = true;
        } else {
          this.checkOverLeftTargetCompleteForm = false;
        }
        break;
      case 3:
        if (element.check) {
          let modal = {
            serviceName: null,
            month: ''
          }
          modal.month = element.month;
          modal.serviceName = 'null';
          this.checkOverLeft.push(modal);
          this.checkOverLeftTargetCoefficientForm = true;
        } else {
          this.checkOverLeftTargetCoefficientForm = false;
        }
        break;
    }
  }

  validValueTo(type, indexParent, indexChild?) {
    if (type == 0) {
      let valueFrom = +this.targetForm.value.itemsGlobal[indexParent].items[indexChild].comparison_value_from;
      let valueTo = +this.targetForm.value.itemsGlobal[indexParent].items[indexChild].comparison_value_to;
      let comparison = this.targetForm.value.itemsGlobal[indexParent].items[indexChild].comparison;
      if (this.targetForm.value.itemsGlobal[indexParent].items[indexChild].comparison_value_to != '') {
        if (comparison == 'In' || comparison == '>=In' || comparison == '<=In' || comparison == '==In') {
          if (valueFrom) {
            if (valueTo < valueFrom) {
              this.checkValueToTargetForm[indexParent][indexChild] = true;
              this.checkSubmit = true;
            } else {
              this.checkValueToTargetForm[indexParent][indexChild] = false;
              this.checkSubmit = false;
            }
          }
        }
      } else {
        this.checkValueToTargetForm[indexParent][indexChild] = false;
        this.checkSubmit = false;
      }
    } else if (type == 1) {
      let valueFrom = +this.targetCompleteForm.value.itemsComplete[indexParent].comparison_value_from_complete;
      let valueTo = +this.targetCompleteForm.value.itemsComplete[indexParent].comparison_value_to_complete;
      let comparison = this.targetCompleteForm.value.itemsComplete[indexParent].comparison_complete;

      if (comparison == 'In' || comparison == '>=In' || comparison == '<=In' || comparison == '==In') {
        if (this.targetCompleteForm.value.itemsComplete[indexParent].comparison_value_to_complete != '') {
          if (valueFrom) {
            if (valueTo < valueFrom) {
              this.checkValueToCompleteForm[indexParent] = true;
              this.checkSubmit = true;
            } else {
              this.checkValueToCompleteForm[indexParent] = false;
              this.checkSubmit = false;
            }
          }
        } else {
          this.checkValueToCompleteForm[indexParent] = false;
          this.checkSubmit = false;
        }
      }
    } else if (type == 2) {
      if (this.salaryCoefficientForm.value.coefficient[indexParent].comparison_value_to_coefficient != '') {
        let valueFrom = +this.salaryCoefficientForm.value.coefficient[indexParent].comparison_value_from_coefficient;
        let valueTo = +this.salaryCoefficientForm.value.coefficient[indexParent].comparison_value_to_coefficient;
        let comparison = this.salaryCoefficientForm.value.coefficient[indexParent].comparison_coefficient;

        if (comparison == 'In' || comparison == '>=In' || comparison == '<=In' || comparison == '==In') {
          if (this.salaryCoefficientForm.value.coefficient[indexParent].comparison_value_to_coefficient != '') {
            if (valueFrom) {
              if (valueTo < valueFrom) {
                this.checkValueToCoefficientForm[indexParent] = true;
                this.checkSubmit = true;
              } else {
                this.checkValueToCoefficientForm[indexParent] = false;
                this.checkSubmit = false;
              }
            }
          } else {
            this.checkValueToCoefficientForm[indexParent] = false;
            this.checkSubmit = false;
          }
        }
      }
    }
  }

  getReceiveFeeSearch() {

  }

  closeOverleftModal() {
    this.checkOverLeft = [];
    this.modalRefError.hide();
  }

  accept() {
    this.acceptAction = true;
    if (this.modalRefConfirmGlobal.content.type == 0) {
      this.removeTargetChildConfirm(this.modalRefConfirmGlobal.content.data, this.modalRefConfirmGlobal.content.index);
    } else if (this.modalRefConfirmGlobal.content.type == 1) {
      this.removeItemsConfirm(this.modalRefConfirmGlobal.content.item, this.modalRefConfirmGlobal.content.indexParent, this.modalRefConfirmGlobal.content.indexChild)
    } else if (this.modalRefConfirmGlobal.content.type == 2) {
      this.removeItemCompleteConfirm(this.modalRefConfirmGlobal.content.item, this.modalRefConfirmGlobal.content.index);
    } else if (this.modalRefConfirmGlobal.content.type == 3) {
      this.removeSalaryCoefficientConfirm(this.modalRefConfirmGlobal.content.item, this.modalRefConfirmGlobal.content.index);
    } else if (this.modalRefConfirmGlobal.content.type == 4) {
      this.removeFeeConfirm(this.modalRefConfirmGlobal.content.item, this.modalRefConfirmGlobal.content.index);
    } else if (this.modalRefConfirmGlobal.content.type == 5) {
      this.saveFormConfirm();
    }
    this.modalRefConfirmGlobal.hide();
  }

  reject() {
    this.acceptAction = false;
    this.modalRefConfirmGlobal.hide();
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

  fieldGlobalIndex(index) {
    return this.pageSize * (this.currentPage - 1) + index;
  }

  checkChangeFormula() {
    this.checkChangeFormulaValue = true;
  }
}
