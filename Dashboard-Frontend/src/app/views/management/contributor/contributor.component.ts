import { Component, OnInit ,TemplateRef} from '@angular/core';

import { MatDialog, MatCheckboxChange } from '@angular/material';
import { AddContributorComponent } from './add-contributor/add-contributor.component';
import {BsModalRef, BsModalService} from 'ngx-bootstrap';
import {config} from '../../../config/application.config';
import {DownloadService} from '../../../services/management/download.service';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import * as fileSaver from 'file-saver';
import * as _moment from 'moment';
import { Pager } from '../../../models/Pager';
import { contributorModel } from '../../../models/contributor.model';
import { FormArray, FormGroup, FormBuilder, FormControl } from '@angular/forms';
import { ContributorService } from '../../../services/management/contributor.service';
import { Timestamp } from 'rxjs/internal/operators/timestamp';
import { DatePipe } from '@angular/common';
import { data } from 'jquery';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';


@Component({
  selector: 'app-contributor',
  templateUrl: './contributor.component.html',
  styleUrls: ['./contributor.component.scss']
})
export class ContributorComponent implements OnInit {

  mnbrCode:any;
  mstrNewName = '';
  mstrstatus='';
  mstrMessageUpload;
  mnbrSumSuccessfulRecord;
  mnbrSumRecord;
  mobjDataUpload;
  mstrResultFileName;
  mblnIsClickHere = false;
  mblnCheckFileNull = false;
  mobjFileList = false;
  mblnIsSelectFile  = false;
  mblnIsClickSearch = false;
  mblnIsReadOnly: boolean;
  mobjModalRef : BsModalRef;
  mobjForm: FormGroup;
  ckall = false;
  checked = true;
  disable = true;
  marrDataTable: contributorModel[] = [];
  marrDataDelte: contributorModel[] = [];
  marrchild: contributorModel;
  mblnChec = false;
  mdtDate = new FormControl(_moment());
  mobjSearchModel: contributorModel;
  mapModel : contributorModel = new contributorModel();
  hle = 0;
  marrArrReadponly :boolean[] = [];
  mblnCheckAll: boolean = false;
  vcheckchild = false;
  contributorId;
  contributorCode;
  message;
  stpcial='';
  special=false;
  special1=false;

  mnbrPageSize = config.pageSize;
  mnbrP = 1;
  mnbrTotal;
  mobjPager: Pager;


  constructor(private dialog : MatDialog,
    private modalService: BsModalService,
    private http: HttpClient,
    private downloadService: DownloadService,
    private contributorService: ContributorService,
    private fb: FormBuilder,
    private datePipe: DatePipe,
    private toastr: ToastrService,
    private translate: TranslateService,) { }

  ngOnInit() {
    this.marrDataTable = [];
    this.marrDataDelte = [];
    this.mobjForm = this.fb.group({
      formArray: this.fb.array([])
    });
    this.getContributor();
    this.mobjPager = new Pager(this.mnbrP, this.mnbrPageSize);


  }

  noMethod() {
  }

  transformDate(date) {
    return this.datePipe.transform(date, 'dd-MM-yyyy');
  }

  openDialog(marrDataTable : contributorModel, h : boolean) {
    const dialogRef = this.dialog.open(AddContributorComponent, {
      data:{
        contributor : marrDataTable,
        h : h,
      },
      maxWidth: '85vw',
      maxHeight: '85vh',
      width: '77vw',
      }).afterClosed().subscribe(res =>{
        this.search();
      }) ;
  }

  confirm(pobjTemplate: TemplateRef<any>) {
    this.mobjModalRef = this.modalService.show(pobjTemplate, {
        ignoreBackdropClick: true
      }
    );
  }

  delete(pobjTemplate: TemplateRef<any>) {
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
    this.search();
  }


  // Tìm kiếm dữ liệu theo thông tin nhập từ giao diện
  search() {
    this.hle = 0;
    this.disable = true;
    this.checked = false;
    this.ckall = false;
    this.marrDataDelte = [];
    this.mnbrPageSize = config.pageSize;
    this.searchModalForm();
    this.showDataSearch(this.mobjSearchModel);
  }

  searchModalForm() {
    if (!this.mnbrCode) {
      this.mnbrCode = '';
    }
    if (!this.mstrNewName){
      this.mstrNewName = '';
    }
    let status = "1";
    this.mobjPager = new Pager(this.mnbrP, this.mnbrPageSize);
    this.mobjSearchModel = new contributorModel(this.mnbrCode,this.mstrNewName,status);
    console.log('model search:', this.mobjSearchModel);
  }

  showDataSearch(pobjData) {
    this.contributorService.getDatas(pobjData).subscribe(
      vobjNext => {
        this.marrDataTable = vobjNext['data'];
        console.log(this.marrDataTable)
        this.marrDataTable.map(
          table => {
            table.checked = false;
            return table;
          }
        );
        this.mnbrTotal = vobjNext['totalRow'];
      }, () => this.marrDataTable = null);
  }

  getContributor() {
    this.contributorService.getAllContributor().subscribe(
      vobjNext => {
        console.log(vobjNext)
        this.marrDataTable = vobjNext['data'];
      });
  }

  // Upload dữ liệu theo file đã chọn
  upLoad() {
    if (this.mobjFileList) {
      const vfileFile: File = this.mobjFileList[0];
      const vobjFormData: FormData = new FormData();
      vobjFormData.append('file', vfileFile);
      const vobjHeaders = new HttpHeaders();
      /** In Angular 5, including the header Content-Type can invalidate your request */
      vobjHeaders.append('Content-type', 'multipart/form-data');
      vobjHeaders.append('Accept', 'application/json');
      const options = {headers: vobjHeaders};
      this.http.post(config.contributor_Upload_File_API, vobjFormData, options)
        .subscribe(
          vobjData => (this.resultUpload(vobjData)),
          vobjError => (console.log(vobjError)),
          () => {
            if(this.mnbrCode == 200 && this.mstrMessageUpload == null && this.mblnIsSelectFile){
              this.downloadResult(this.mstrResultFileName);
              this.mobjModalRef.hide();
              this.mnbrCode = null;
              this.search();
            }
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

  fileDownload() {
    this.http.get(config.contributor_Download_File_API);
    this.contributorService.downloadTemplate()
      .subscribe(vobjResponse => {
        let vstrFilename: string;
        vstrFilename = this.mstrResultFileName;
        this.saveFile(vobjResponse.body, vstrFilename);
      });
  }

  //Lưu file download theo tên Tháng_ChiTieuVDS.xlsx
  saveFile(pobjData: any, pstrFilename?: string) {
    const blob = new Blob([pobjData], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
    fileSaver.saveAs(blob, pstrFilename);
  }

  get formArray(): FormArray {
    return this.mobjForm.get('formArray') as FormArray;
  }

  selectAll() {
    let vblnCheck = false;
    if (this.ckall === false) {
      vblnCheck = true;
    }
    for (let i = 0; i < this.marrDataTable.length; i++) {
      if (vblnCheck === true) {
        this.marrDataTable[i].checked = true;
        this.disable = false;
      } else {
        this.marrDataTable[i].checked = false;
        this.disable = true;
      }
    }
    this.CheckStatus();
  }

  select(id: number) {
    for (let i = 0; i < this.marrDataTable.length; i++) {
      if (this.marrDataTable[i].id === id) {
        if (this.marrDataTable[i].checked === true) {
          this.marrDataTable[i].checked = false;
          this.disable = true;
        } else {
          console.log(this.marrDataTable[i])
          this.marrDataTable[i].checked = true;
          this.disable = false;
        }
        break;
      }
    }
    this.CheckStatus();
  }

  CheckStatus() {
    // console.log(this.marrDataTable)
    this.mstrstatus = '0';
    this.marrDataDelte = this.marrDataTable.filter(item => item.checked === true);
    console.log(this.marrDataDelte)
    if (this.marrDataDelte.length > 0) {
      this.mstrstatus = this.marrDataDelte[0].status;
      for (let i = 1; i < this.marrDataDelte.length; i++) {
        if (this.marrDataDelte[i].status !== this.mstrstatus) {
          this.mstrstatus = '-1';
          break;
        }
      }
    } else {
      this.mstrstatus = '';
    }
  }

  pageChange(pnbrP: number) {
    this.mnbrP = pnbrP;
    this.mstrstatus = '';
    this.ckall = false;
    this.disable = true;
    //this.getSearch(false);
    let total = this.mnbrP * 10;
    if (this.mnbrP * 10 > this.marrDataTable.length) {
      total = this.marrDataTable.length;
    }
    for (let i = (this.mnbrP - 1) * 10; i < total; i++) {
      this.marrDataTable[i].checked = false;
    }
  }

  clickSearch() {
    this.mblnIsClickSearch = true;
    this.mnbrP = 1;
    this.search();
  }

  openConfirm(pobjTemplate: TemplateRef<any>, id: number,code: number) {
    this.contributorId = id;
    this.contributorCode = code;
    console.log(this.contributorId)
    this.mobjModalRef = this.modalService.show(pobjTemplate, {
        ignoreBackdropClick: true
      }
    );
  }

  onDelete() {
    this.contributorService.deleteContributor(this.contributorId).subscribe(
      del => {
        this.message = del['data'];
        console.log('messageDelete', this.message);
        if (this.message === 'SUCCESS') {
          this.showDelSuccess(this.translate.instant("Xoá CTV thành công"));
        }else {
          this.showDelError(this.translate.instant('management.warningconfig.serverError'));
        }
        this.mobjModalRef.hide();
        this.search();
        // this.getAllGroupCard();
      }
    );
  }

  showDelSuccess(message: string) {
    this.toastr.success(message,
      this.translate.instant(" "), {
        timeOut: 2000,
        positionClass: 'toast-top-center',
      });
  }

  showDelError(message: string) {
    this.toastr.error(message,
      this.translate.instant('management.service.message.fail'), {
        timeOut: 2000,
        positionClass: 'toast-top-center',
      });
  }
}
