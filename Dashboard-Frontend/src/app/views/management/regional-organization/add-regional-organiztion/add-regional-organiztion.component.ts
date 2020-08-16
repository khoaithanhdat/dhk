import {Component, OnInit} from '@angular/core';
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatDialogRef} from '@angular/material';
import {TreeviewItem} from 'ngx-treeview';
import {IDropdownSettings} from 'ng-multiselect-dropdown/multiselect.model';
import {MomentDateAdapter} from '@angular/material-moment-adapter';
import {TreeVDSService} from '../../../../services/management/tree-VDS.service';

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
  selector: 'app-add-regional-organiztion',
  templateUrl: './add-regional-organiztion.component.html',
  styleUrls: ['./add-regional-organiztion.component.scss'],
  providers: [{provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]}
  ]
})
export class AddRegionalOrganiztionComponent implements OnInit {
  marrNodeTreeviewServices: TreeviewItem[] = [];
  mobjNodeTreeviewService: TreeviewItem;
  dropdownList = [];
  selectedItems = [];
  lstDataProvince = [];


  dropdownSettings: IDropdownSettings = {
    singleSelection: false,
    idField: 'id',
    textField: 'shopName',
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    itemsShowLimit: 5,
    allowSearchFilter: true
  };

  constructor(
    private dialogRef: MatDialogRef<AddRegionalOrganiztionComponent>,
    private treeVDSService: TreeVDSService
  ) {
  }

  ngOnInit() {
    this.onLoadSelect();
    // this.selectedItems = [
    //   {item_id: 3, item_text: 'Pune'},
    //   {item_id: 4, item_text: 'Navsari'}
    // ];
  }

  onItemSelect(item: any) {
    this.lstDataProvince.push(item);
    console.log('slect_one', this.lstDataProvince);
  }

  onSelectAll(items: any) {
    console.log('onSelectAll', items);
    this.lstDataProvince = items;
    console.log('slect_all', this.lstDataProvince);
  }

  onDeSelect(item: any) {
    console.log('onSelectAll', item);
     this.lstDataProvince = this.lstDataProvince.filter(s => s.id !== item.id);
    // this.lstDataProvince.slice(item, 1);

    console.log('unselect_all', this.lstDataProvince);
  }

  onDeSelectAll(items: any) {
    this.lstDataProvince = [];
    console.log('unslect_all', this.lstDataProvince);
  }
  onClose() {
    this.dialogRef.close();
  }

  onSave() {
    // console.log("helo");
    console.log('them_test', this.lstDataProvince);

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
        console.log(this.dropdownList);
      }
    );
  }
}
