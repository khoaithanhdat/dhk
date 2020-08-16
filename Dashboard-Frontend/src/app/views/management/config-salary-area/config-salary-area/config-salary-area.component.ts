import { Component, OnInit, TemplateRef } from "@angular/core";
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import {
  DateAdapter,
  MAT_DATE_FORMATS,
  MAT_DATE_LOCALE,
} from "@angular/material/core";
import {
  MomentDateAdapter,
  MAT_MOMENT_DATE_ADAPTER_OPTIONS,
} from "@angular/material-moment-adapter";
import { MatDatepicker } from "@angular/material/datepicker";
import * as _moment from 'moment';
import { Moment } from 'moment';
import { ConfigSalaryAreaService } from "../../../../services/management/config-salary-area.service";
import { SalAreaModel } from "../../../../models/salArea.model";
import { salAreSalaryModel } from "../../../../models/salAreaSalary.model";
import { salaryTimeModel } from "../../../../models/salaryTime.model";
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';

export const MY_FORMATS = {
  parse: {
    dateInput: "MM/YYYY",
  },
  display: {
    dateInput: "MM/YYYY",
    monthYearLabel: "MMM YYYY",
    dateA11yLabel: "LL",
    monthYearA11yLabel: "MMMM YYYY",
  },
};

@Component({
  selector: "app-config-salary-area",
  templateUrl: "./config-salary-area.component.html",
  styleUrls: ["./config-salary-area.component.scss"],
  providers: [
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS],
    },
    { provide: MAT_DATE_FORMATS, useValue: MY_FORMATS },
  ],
})
export class ConfigSalaryAreaComponent implements OnInit {

  mobjModalRef: BsModalRef;
  leaderForm: FormGroup;
  salaryLeaderAreaForm: FormGroup;
  salaryEmployeeAreaForm: FormGroup;

  salaryLeaderAreaModel = {
    salaryLeaderArea:0,
    expired_month_leader: '',
  }

  salaryEmployeeAreaModel = {
    salaryEmployeeArea:0,
    expired_month_employee: '',
  }

  salArea: SalAreaModel;
  salAreaSalary: salAreSalaryModel;
  salaryTime: salaryTimeModel;
  listSalaryTime: salaryTimeModel[] = [];

  selected: string;
  hardSalary: number =0;
  targetSalary: number =0;
  btnsubmit;
  yearOut= [];
  mblnChec = false;
  mblnvaLi = [];
  isNullDate = [];
  mblnCur = [];
  mdtDate = [[]];
  mdtDateSalaryLeader = [];
  mdtDateSalaryEmployee = [];
  mdtMinDate = new Date(1899, 0, 1);
  mdtMaxDate = new Date(2101, 0, 1);
  mblnvaLi1 = [];
  isNullDate1 = [];
  mblnCur1 = [];
  yearOut1 =[]

  checkLeader:boolean;
  checkEmployee:boolean;
  addLeader:boolean;
  addEmployee:boolean;
  checkLeaderSalary = []
  checkEmployeeSalary = []
  checkAll = false

  stpcial = '';
  special = false;
  preventSpace = false;
  stpcial1 = '';
  special1 = false;
  preventSpace1 = false;
  special2 = [];
  preventSpace2 = [];
  special3 = [];
  preventSpace3 = [];
  currentVal = '';
  valueString = [];
  valueString1 = [];

  message='';

  constructor(
    private formBuilder: FormBuilder,
    private configSalaryAreaService: ConfigSalaryAreaService,
    private modalService: BsModalService,
    private translate: TranslateService,
    private toastr: ToastrService,
  ) {}

  startView: "month" | "year" | "multi-year";

  ngOnInit() {
    this.startView = 'year';
    this.btnsubmit = true;
    this.checkAll = true;

    this.createFormSalaryLeaderArea();
    this.createFormSalaryEmployeeArea();
    this.getArea();
    this.checkLeader = false
    this.checkEmployee = false
    this.addLeader = false
    this.addEmployee = false

  }

  checkLeaderForm(checkLeader){
    this.checkLeader = checkLeader.target.checked
    console.log(checkLeader)
    if(this.checkLeader){
      this.addLeader = true
      //this.btnsubmit = false
      this.setLeaderSalaryTime();
    }else{
      this.addLeader = false
      this.btnsubmit = true
      this.checkAll = true
      this.createFormSalaryLeaderArea();
    }
  }

  checkEmployeeForm(checkEmployee){
    this.checkEmployee = checkEmployee.target.checked
    console.log(checkEmployee)
    if(this.checkEmployee){
      this.addEmployee = true
      //this.btnsubmit = false
      this.setEmployeeSalaryTime();
    }else{
      this.addEmployee = false
      this.btnsubmit = true
      this.checkAll = true
      this.createFormSalaryEmployeeArea();
    }
  }

  getArea() {
    this.configSalaryAreaService.getAllArea().subscribe((res: any) => {
      this.salArea = res.data;
    });
  }

  selectChangeHandler(event: any) {
    //update the ui
    this.special = false;
    this.preventSpace = false;
    this.special1 = false;
    this.preventSpace1 = false;
    this.selected = event.target.value;
    this.createFormSalaryEmployeeArea()
    this.createFormSalaryLeaderArea()
    this.configSalaryAreaService
      .getSalByArea(this.selected)
      .subscribe((res: any) => {
        this.salAreaSalary = res.data;
        console.log(this.salAreaSalary);
        this.hardSalary = this.salAreaSalary.hardSalary;
        this.targetSalary = this.salAreaSalary.targetSalary;
        console.log(this.salAreaSalary.updatedUser)
        this.listSalaryTime = this.salAreaSalary.lstSalaryTimeDTOS;
        console.log(this.salAreaSalary.lstSalaryTimeDTOS)
        this.btnsubmit = true;
        if(this.checkLeader){
          this.addLeader = true
          this.setLeaderSalaryTime()
        }
        if(this.checkEmployee){
          this.addEmployee = true
          this.setEmployeeSalaryTime()
        }
      });
  }

  setLeaderSalaryTime() {
    if(this.checkEmployee == false){
      this.checkAll = true
      this.btnsubmit = true
    }else{
      this.checkAll = false
      this.btnsubmit = false
    }
    for (let i = 0; i < this.listSalaryTime.length; i++) {
      if (this.listSalaryTime[i].staffType == "2") {
        let target1 = {
          id: 0,
          salaryLeaderArea: 0,
          expired_month_leader: '',
          expired_mili_leader:0,
          areaCode:'',
          updatedUser:'',
        }
        target1.id = this.listSalaryTime[i].id;
        target1.salaryLeaderArea = this.listSalaryTime[i].salary;
        target1.expired_month_leader = this.listSalaryTime[i].expiredMonth;
        target1.expired_mili_leader = this.listSalaryTime[i].expiredMilis;
        target1.areaCode = this.salAreaSalary.areaCode
        target1.updatedUser = this.salAreaSalary.updatedUser

        console.log(target1)
        const control = <FormArray> this.salaryLeaderAreaForm.controls['itemLeaderSalaryArea'];
        control.push(this.salaryLeaderAreaModal(target1));

        this.salaryLeaderAreaForm.value.itemLeaderSalaryArea.forEach((element,i) => {
          if (element.expired_month_leader) {
            this.setDateLeader(element.expired_month_leader, i)
          }
        });
      }
    }
  }

  setEmployeeSalaryTime(){
    if(this.checkLeader == false){
      this.checkAll = true
      this.btnsubmit = true
    }else{
      this.checkAll = false
      this.btnsubmit = false
    }
    for (let i = 0; i < this.listSalaryTime.length; i++) {
      if (this.listSalaryTime[i].staffType == "1") {
        let target = {
          id: 0,
          salaryEmployeeArea: 0,
          expired_month_employee: '',
          expired_mili_employee:0,
          areaCode:'',
          updatedUser:''
        }
        target.id = this.listSalaryTime[i].id;
        target.salaryEmployeeArea = this.listSalaryTime[i].salary;
        target.expired_month_employee = this.listSalaryTime[i].expiredMonth;
        target.expired_mili_employee = this.listSalaryTime[i].expiredMilis;
        target.areaCode= this.salAreaSalary.areaCode
        target.updatedUser = this.salAreaSalary.updatedUser

        const control = <FormArray>this.salaryEmployeeAreaForm.controls['itemEmployeeSalaryArea'];
        control.push(this.salaryEmployeeAreaModal(target));
        //console.log('abc'+this.salaryEmployeeAreaModal(target))
        this.salaryEmployeeAreaForm.value.itemEmployeeSalaryArea.forEach((element,i) => {
          if (element.expired_month_employee) {
            this.setDateEmployee(element.expired_month_employee, i);
          }
        });
      }
    }
  }

  onSubmit(){
    const message = this.translate.instant('management.group.message.insert');
    const messageSuccess = message + this.translate.instant('management.group.message.success');
    const messageFail = messageSuccess + this.translate.instant('management.group.message.fail');

    this.salAreaSalary.lstSalaryTimeDTOS = []

    this.salaryLeaderAreaForm.value.itemLeaderSalaryArea.forEach((element,i) => {
      if (element.expired_month_leader) {
        this.setDateLeader(element.expired_month_leader, i)
        element.expired_mili_leader = this.mdtDateSalaryLeader[i].value._d.getTime()
      }
    });

    this.salaryEmployeeAreaForm.value.itemEmployeeSalaryArea.forEach((element,i) => {
      if (element.expired_month_employee) {
        this.setDateEmployee(element.expired_month_employee, i);
        element.expired_mili_employee = this.mdtDateSalaryEmployee[i].value._d.getTime()
      }
    });
    this.salAreaSalary.hardSalary = this.hardSalary
    this.salAreaSalary.targetSalary = this.targetSalary
    this.submitLeaderForm(this.salaryLeaderAreaForm.value.itemLeaderSalaryArea)
    this.submitEmployeeForm(this.salaryEmployeeAreaForm.value.itemEmployeeSalaryArea)
    console.log(this.salAreaSalary)

    this.configSalaryAreaService.updateData(this.salAreaSalary).subscribe(data => {
      this.message = data['data'];
      if (!this.message) {
        this.showSuccess('Đã lưu thành công');
      } else {
        this.showError(this.message);
      }
      console.log(this.salAreaSalary)
      this.mobjModalRef.hide();
    });
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

  submitLeaderForm(data: any) {
    data.forEach(element => {
      let salaryTimeLeader = new salaryTimeModel();
      salaryTimeLeader.id = element.id;
      salaryTimeLeader.salary = element.salaryLeaderArea;
      salaryTimeLeader.expiredMilis = element.expired_mili_leader
      salaryTimeLeader.staffType = "2"
      salaryTimeLeader.areaCode = this.salAreaSalary.areaCode
      salaryTimeLeader.updatedUser = this.salAreaSalary.updatedUser

      console.log(salaryTimeLeader.expiredMilis)
      this.salAreaSalary.lstSalaryTimeDTOS.push(salaryTimeLeader);
    });
    //alert(JSON.stringify (this.salAreaSalary.lstSalaryTimeDTOS))
  }

  submitEmployeeForm(data: any) {
    data.forEach(element => {
      let salaryTimeEmployee = new salaryTimeModel();
      salaryTimeEmployee.id = element.id;
      salaryTimeEmployee.salary = element.salaryEmployeeArea;
      salaryTimeEmployee.expiredMilis = element.expired_mili_employee
      salaryTimeEmployee.staffType = "1"
      salaryTimeEmployee.areaCode = this.salAreaSalary.areaCode
      salaryTimeEmployee.updatedUser = this.salAreaSalary.updatedUser

      this.salAreaSalary.lstSalaryTimeDTOS.push(salaryTimeEmployee);
    });
    //console.log(this.salAreaSalary.lstSalaryTimeDTOS)
  }

  setDateLeader(date, i) {
    if (date) {
      if (this.mdtDateSalaryLeader.length > 0) {
        if (this.mdtDateSalaryLeader[i]) {
          this.mdtDateSalaryLeader[i] = new FormControl(_moment(date));
        } else {
          this.mdtDateSalaryLeader.push(new FormControl(_moment(date)));
        }
      } else {
        this.mdtDateSalaryLeader.push(new FormControl(_moment(date)));
      }
    } else {
      if (this.mdtDateSalaryLeader[i]) {
        this.mdtDateSalaryLeader[i] = new FormControl(_moment(new Date()));
      } else {
        this.mdtDateSalaryLeader.push(new FormControl(_moment(new Date())));
      }
    }
  }

  setDateEmployee(date, i) {
    if (date) {
      if (this.mdtDateSalaryEmployee.length > 0) {
        if (this.mdtDateSalaryEmployee[i]) {
          this.mdtDateSalaryEmployee[i] = new FormControl(_moment(date));
        } else {
          this.mdtDateSalaryEmployee.push(new FormControl(_moment(date)));
        }
      } else {
        this.mdtDateSalaryEmployee.push(new FormControl(_moment(date)));
      }
    } else {
      if (this.mdtDateSalaryEmployee[i]) {
        this.mdtDateSalaryEmployee[i] = new FormControl(_moment(new Date()));
      } else {
        this.mdtDateSalaryEmployee.push(new FormControl(_moment(new Date())));
      }
    }
  }

  chosenMonthHandlerLeader($event, pobjDatepicker: MatDatepicker<Moment>, index) {
    this.isNullDate[index] = false
    this.mblnCur1[index] = false
    this.mblnvaLi1[index] = false
    this.yearOut1[index] = false
    console.log(this.isNullDate[index])
    this.mdtDateSalaryLeader[index].setValue($event);
    pobjDatepicker.close();
    this.salaryLeaderAreaForm.value.itemLeaderSalaryArea[index].expired_month_leader = this.mdtDateSalaryLeader[index].value._d.getTime();
    //console.log(this.mdtDateSalaryLeader[index].value)
  }

  get itemLeaderSalaryArea(): FormArray {
    return this.salaryLeaderAreaForm.get('itemLeaderSalaryArea') as FormArray;
  }

  chosenMonthHandlerEmployee($event, pobjDatepicker: MatDatepicker<Moment>, index) {
    this.mdtDateSalaryEmployee[index].setValue($event);
    pobjDatepicker.close();
    this.salaryEmployeeAreaForm.value.itemEmployeeSalaryArea[index].expired_month_employee =this.mdtDateSalaryEmployee[index].value._d.getTime();
  }

  get itemEmployeeSalaryArea(): FormArray {
    return this.salaryEmployeeAreaForm.get('itemEmployeeSalaryArea') as FormArray;
  }

  validateDate(value,i) {
    if (!value[i].valid) {
      this.mblnvaLi[i] = true;
      this.isNullDate[i] = false;
    } else if (!value.value) {
      this.mblnvaLi[i] = false;
      this.isNullDate[i] = true;
    } else if (value.value && value.valid) {
      this.mblnvaLi[i] = false;
      this.isNullDate[i] = false;
    }
  }

  dateEmployeeChange(value: any,index) {

    this.valueString[index] = value.target.value;
    console.log(this.valueString)
    if (this.valueString[index].length === 0) {
      this.yearOut[index] = false;
      this.mblnvaLi[index] = false;
      this.isNullDate[index] = true;
      this.btnsubmit = true
      console.log(this.isNullDate)
    } else {
      const arrayMonths = this.valueString[index].split('/');
      const monthS = arrayMonths[0];
      const yearS = arrayMonths[1];
      // tslint:disable-next-line:radix
      const yearN = parseInt(yearS);
      // tslint:disable-next-line:radix
      const monthN = parseInt(monthS);

      const currentMonth = parseInt( _moment(new Date()).format('M'))
      const currentYear =  parseInt( _moment(new Date()).format('Y'))

      if (isNaN(yearN)) {
        this.mblnvaLi[index] = true;
        this.yearOut[index] = false;
        this.isNullDate[index] = false;
        this.mblnCur[index] = false;
        this.btnsubmit = true
      } else if (isNaN(monthN)) {
        this.mblnvaLi[index] = true;
        this.yearOut[index] = false;
        this.isNullDate[index] = false;
        this.mblnCur[index] = false;
        this.btnsubmit = true
      } else if (yearN < 1900 || yearN > 2100) {
        this.mblnvaLi[index] = false;
        this.yearOut[index] = true;
        this.isNullDate[index] = false;
        this.mblnCur[index] = false;
        this.btnsubmit = true
      } else if (monthN < 1 || monthN > 12) {
        this.yearOut[index] = false;
        this.mblnvaLi[index] = true;
        this.isNullDate[index] = false;
        this.mblnCur[index] = false;
        this.btnsubmit = true
      } else if(monthN < currentMonth && yearN <=currentYear){
        this.mblnCur[index] = true;
        this.yearOut[index] = false;
        this.mblnvaLi[index] = false;
        this.isNullDate[index] = false;
        this.btnsubmit = true
      } else if(monthN <= currentMonth && yearN <=currentYear){
        this.mblnCur[index] = false;
        this.yearOut[index] = false;
        this.mblnvaLi[index] = false;
        this.isNullDate[index] = false;
        this.btnsubmit = false
      } else{
        this.btnsubmit = false
        this.mblnCur[index] = false;
        this.yearOut[index] = false;
        this.mblnvaLi[index]= false;
        this.isNullDate [index]= false;
      }
    }
  }

  dateLeaderChange(value: any,index) {

    this.valueString1[index] = value.target.value;
    if (this.valueString1[index].length === 0) {
      this.yearOut1[index] = false;
      this.mblnvaLi1[index] = false;
      this.isNullDate1[index] = true;
      this.btnsubmit = true
    } else {
      const arrayMonths = this.valueString1[index].split('/');
      const monthS = arrayMonths[0];
      const yearS = arrayMonths[1];
      // tslint:disable-next-line:radix
      const yearN = parseInt(yearS);
      // tslint:disable-next-line:radix
      const monthN = parseInt(monthS);

      const currentMonth = parseInt( _moment(new Date()).format('M'))
      const currentYear =  parseInt( _moment(new Date()).format('Y'))

      if (isNaN(yearN)) {
        this.mblnvaLi1[index] = true;
        this.yearOut1[index] = false;
        this.isNullDate1[index] = false;
        this.mblnCur1[index] = false;
        this.btnsubmit = true
      } else if (isNaN(monthN)) {
        this.mblnvaLi1[index] = true;
        this.yearOut1[index] = false;
        this.isNullDate1[index] = false;
        this.mblnCur1[index] = false;
        this.btnsubmit = true
      } else if (yearN < 1900 || yearN > 2100) {
        this.mblnvaLi1[index] = false;
        this.yearOut1[index] = true;
        this.isNullDate1[index] = false;
        this.mblnCur1[index] = false;
        this.btnsubmit = true
      } else if (monthN < 1 || monthN > 12) {
        this.yearOut1[index] = false;
        this.mblnvaLi1[index] = true;
        this.isNullDate1[index] = false;
        this.mblnCur1[index] = false;
        this.btnsubmit = true
      } else if(monthN < currentMonth && yearN <=currentYear){
        this.mblnCur1[index] = true;
        this.yearOut1[index] = false;
        this.mblnvaLi1[index] = false;
        this.isNullDate1[index] = false;
        this.btnsubmit = true
      } else if(monthN <= currentMonth && yearN <=currentYear){
        this.mblnCur1[index] = false;
        this.yearOut1[index] = false;
        this.mblnvaLi1[index] = false;
        this.isNullDate1[index] = false;
        this.btnsubmit = false
      } else{
        this.btnsubmit = false
        this.checkAll = false
        this.mblnCur1[index] = false;
        this.yearOut1[index] = false;
        this.mblnvaLi1[index]= false;
        this.isNullDate1[index]= false;
      }
    }
  }

  salaryLeaderAreaModal(targetSalaryLeader) {
    return this.formBuilder.group({
      id: new FormControl(targetSalaryLeader.id ? targetSalaryLeader.id : null),
      expired_month_leader: new FormControl(targetSalaryLeader.expired_month_leader),
      salaryLeaderArea: new FormControl(targetSalaryLeader.salaryLeaderArea, [Validators.required, Validators.maxLength(10)]),
      expired_mili_leader: new FormControl(targetSalaryLeader.expired_mili_leader)
    });
  }

  salaryEmployeeAreaModal(targetSalaryEmployee) {
    return this.formBuilder.group({
      id: new FormControl(targetSalaryEmployee.id ? targetSalaryEmployee.id : null),
      expired_month_employee: new FormControl(targetSalaryEmployee.expired_month_employee),
      salaryEmployeeArea: new FormControl(targetSalaryEmployee.salaryEmployeeArea, [Validators.required, Validators.maxLength(10)]),
      expired_mili_employee: new FormControl(targetSalaryEmployee.expired_mili_employee)
    });
  }

  createFormSalaryLeaderArea() {
    this.salaryLeaderAreaForm = this.formBuilder.group({
      itemLeaderSalaryArea: this.formBuilder.array([])
    });
  }

  createFormSalaryEmployeeArea() {
    this.salaryEmployeeAreaForm = this.formBuilder.group({
      itemEmployeeSalaryArea: this.formBuilder.array([])
    });
  }

  addSalaryLeader() {
    const control = <FormArray> this.salaryLeaderAreaForm.controls["itemLeaderSalaryArea"]
    control.push(this.salaryLeaderAreaModal(this.salaryLeaderAreaModel));
    this.setDateLeader(null, control.length - 1);
    this.btnsubmit = false;
  }

  addSalaryEmployee() {
    const control = <FormArray> this.salaryEmployeeAreaForm.controls["itemEmployeeSalaryArea"]
    control.push(this.salaryEmployeeAreaModal(this.salaryEmployeeAreaModel));
    this.setDateEmployee(null, control.length - 1);
    this.btnsubmit = false;
  }

  removeSalaryLeader(index) {
    const control = <FormArray>(
      this.salaryLeaderAreaForm.controls["itemLeaderSalaryArea"]
    );
    control.removeAt(index);
  }

  removeSalaryEmployee(index) {
    const control = <FormArray>(
      this.salaryEmployeeAreaForm.controls["itemEmployeeSalaryArea"]
    );
    control.removeAt(index);
  }

  checkSpecialLeader(event: any) {
    this.stpcial = event.target.value;
    if ((event.target.value || '').trim().length === 0) {
      this.btnsubmit = true
      this.special = true;
      this.preventSpace = false;
    } else if (this.stpcial.match('[0-9]+([\.,][0-9]+)?')) {
      this.btnsubmit = false
      this.preventSpace = false;
      this.special = false;
    } else {
      this.btnsubmit = true
      this.special = false;
      this.preventSpace = true;
    }
  }

  checkLeaderSalaryTime(event, index){
    this.checkLeaderSalary[index] = event.target.value;
    if(this.checkLeaderSalary[index].length === 0) {
      this.btnsubmit = true
      this.special2[index] = true;
      this.preventSpace2[index] = false;
    } else if (this.checkLeaderSalary[index].match('[0-9]+([\.,][0-9]+)?')) {
      console.log(this.checkLeaderSalary[index])
      this.btnsubmit = false
      this.preventSpace2[index] = false;
      this.special2[index] = false;
    } else {
      this.btnsubmit = true
      this.special2[index] = false;
      this.preventSpace2[index]  = true;
    }
  }

  checkEmployeeSalaryTime(event, index){
    this.checkEmployeeSalary[index] = event.target.value;
    if(this.checkEmployeeSalary[index].length === 0) {
      this.btnsubmit = true
      this.special3[index] = true;
      this.preventSpace3[index] = false;
    } else if (this.checkEmployeeSalary[index].match('^[0-9]+([\.,][0-9]+)?')) {
      this.btnsubmit = false
      this.preventSpace3[index] = false;
      this.special3[index]  = false;
    } else {
      this.btnsubmit = true
      this.special3[index]  = false;
      this.preventSpace3[index] = true;
    }
  }

  checkSpecialEmployee(event: any) {
    this.stpcial1 = event.target.value;
    if ((event.target.value || '').trim().length === 0) {
      this.special1 = true;
      this.preventSpace1 = false;
      this.btnsubmit = true
    } else if (this.stpcial1.match('[0-9]+([\.,][0-9]+)?')) {
      this.btnsubmit = false
      this.preventSpace1 = false;
      this.special1 = false;
    } else {
      this.btnsubmit = true
      this.special1 = false;
      this.preventSpace1 = true;
    }
  }

  keyUp(event: any) {
    this.currentVal = event.target.value
    return this.numberWithCommas(this.currentVal),
    console.log(this.numberWithCommas(this.currentVal))
  };

  numberWithCommas(x) {
    var parts = x.toString().split(".");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return parts.join(".");
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

}
