import { Component, OnInit ,TemplateRef} from '@angular/core';
import {config} from '../../../config/application.config';
import {FormArray, FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from '@angular/material/core';
import {MomentDateAdapter, MAT_MOMENT_DATE_ADAPTER_OPTIONS} from '@angular/material-moment-adapter';
import {FileService} from '../../../services/management/file.service';
import {SearchModel} from '../../../models/Search.model';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BsModalRef, BsModalService} from 'ngx-bootstrap';
import {MatDatepicker} from '@angular/material/datepicker';
import * as _moment from 'moment';
import * as fileSaver from 'file-saver';
import {Moment} from 'moment';
import { DownloadService } from '../../../services/management/download.service';
import { TranslateService } from '@ngx-translate/core';

export const MY_FORMATS = {
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
  selector: 'app-import',
  templateUrl: './import.component.html',
  styleUrls: ['./import.component.scss'],
  providers: [
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS]
    },

    {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
  ],
})

export class ImportComponent implements OnInit {

  mnbrCode;
  mstrMessageUpload;
  mnbrSumSuccessfulRecord;
  mnbrSumRecord;
  mobjDataUpload;
  mstrResultFileName;
  selected: number =0;
  mblnIsClickHere = false;
  mblnCheckFileNull = false;
  mobjFileList = false;
  mblnIsSelectFile  = false;
  fileName="";

  mobjSearchModel: SearchModel;
  mobjModalRef: BsModalRef;

  constructor(
    private modalService: BsModalService,
    private http: HttpClient,
    private fileService: FileService,
    private downloadService: DownloadService,
    private translate: TranslateService,
  ) { }

  startView: 'month' | 'year' | 'multi-year';
  date = new FormControl(_moment());

  ngOnInit() {
  }

  //event handler for the select element's change event
  selectChangeHandler (event: any) {
    //update the ui
    this.selected = event.target.value;
    //alert(this.selected);
  }

  chosenYearHandler(normalizedYear: Moment) {
    const ctrlValue = this.date.value;
    ctrlValue.year(normalizedYear.year());
    this.date.setValue(ctrlValue);
    alert(ctrlValue);
  }

  chosenMonthHandler(normalizedMonth: Moment, datepicker: MatDatepicker<Moment>) {
    const ctrlValue = this.date.value;
    ctrlValue.month(normalizedMonth.month());
    this.date.setValue(ctrlValue);
    alert(ctrlValue);
    datepicker.close();
  }

  confirm(pobjTemplate: TemplateRef<any>) {
    this.mobjModalRef = this.modalService.show(pobjTemplate, {
        ignoreBackdropClick: true
      }
    );
  }

  // Lưu file đã chọn vào mobjFileList để upload
  selectFile(pobjEvent) {
    this.mnbrCode = null;
    if (!this.mobjFileList) {
      this.mblnIsSelectFile = true;
    } else if (this.mobjFileList[0] !== pobjEvent.target.files[0]) {
      this.mblnIsSelectFile = false;
    }
    this.mobjFileList = pobjEvent.target.files;
    this.mblnCheckFileNull = false;
  }

  onBack() {
    this.mblnIsClickHere = false;
    this.mnbrCode = null;
    this.mobjFileList = null;
    this.mblnCheckFileNull = false;
    this.mobjModalRef.hide();
  }


  // Tìm kiếm dữ liệu theo thông tin nhập từ giao diện
  search() {
    // this.cycleCodeSearch = this.cycleCode;
    // this.disable = true;
    // this.checked = false;
    // this.ckall = false;
    // this.marrDataDelte = [];
    // if (!this.mdtDate.invalid) {
    //   this.mnbrPageSize = config.pageSize;
    //   this.searchModalForm();
    //   this.showDataSearch(this.mobjSearchModel);

    // }
    // console.log('search: ', this.mobjSearchModel);
  }

  // Upload dữ liệu theo file đã chọn
  upLoad(importType : number,dateMilisecond: number ) {
    if (this.mobjFileList) {
      const vfileFile: File = this.mobjFileList[0];
      const vobjFormData: FormData = new FormData();
      vobjFormData.append('file', vfileFile);
      const vobjHeaders = new HttpHeaders();
      /** In Angular 5, including the header Content-Type can invalidate your request */
      vobjHeaders.append('Content-type', 'multipart/form-data');
      vobjHeaders.append('Accept', 'application/json');
      const options = {headers: vobjHeaders};
      this.http.post(`${config.upload_Import_File_API}/${importType}/${dateMilisecond}`, vobjFormData, options)
        .subscribe(
          vobjData => (this.resultUpload(vobjData)),
          vobjError => (console.log(vobjError)),
          () => {
            this.search();
          }
        );
    } else {
      this.mblnCheckFileNull = true;
    }
    this.mblnIsSelectFile = true;
    return this.mobjDataUpload;
  }

  //Tách thông tin từ api sau khi upload: message, code
  resultUpload(pobjData) {
    this.mobjDataUpload = pobjData['data'];
    this.mnbrCode = pobjData.code;
    this.mstrMessageUpload = this.mobjDataUpload.message;
    this.showError(this.mnbrCode);
    // this.modalRef.hide();
  }

  showError(pnbrCode: number) {
    if (pnbrCode === undefined) {
      console.log('Waining: code is undefine');
    } else if (pnbrCode === 200) {
      this.mnbrSumSuccessfulRecord = this.mobjDataUpload.sumSuccessfulRecord;
      this.mnbrSumRecord = this.mobjDataUpload.sumRecord;
      this.mstrResultFileName = this.mobjDataUpload.resultFileName;
    } else {
      console.log('Lỗi server');
    }
  }

  // Download file tổng hợp lỗi sau khi upload
  downloadResult(pstrNameFileResult: string) {
    this.mblnIsClickHere = true;
    this.http.get(`${config.download_Result_File_API}?fileName=${pstrNameFileResult}`);
    this.downloadService.downloadResult(pstrNameFileResult)
      .subscribe(vobjResponse => {
        let vstrFilename: string;
        vstrFilename = this.mstrResultFileName;
        // console.log(vstrFilename);
        this.saveFile(vobjResponse.body, vstrFilename);
      });
  }

  //Lưu file download theo tên Tháng_ChiTieuVDS.xlsx
  saveFile(pobjData: any, pstrFilename?: string) {
    const blob = new Blob([pobjData], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
    fileSaver.saveAs(blob, pstrFilename);
  }

  fileDownload(importType : number) {
    this.http.get(`${config.download_Template_File_API}/${importType}`);
    this.fileService.downloadTemplate(importType)
      .subscribe(vobjResponse => {
        let vstrFilename: string;
        vstrFilename = this.mstrResultFileName;
        // console.log(vstrFilename);
        this.saveFile(vobjResponse.body, vstrFilename);
      });
  }

}
