import { UnitTreeviewI18n } from './../../vtt-target/unit-treeview-i18n';
import { DropdownTreeview } from './../../../dashboard/togger/config-tree/dropdown-treeview';
import { isNil } from 'lodash';
import { TreeviewConfig, TreeviewItem, DropdownTreeviewComponent, TreeviewI18n, TreeviewHelper, TreeviewComponent } from 'ngx-treeview';
import { Component, OnInit, OnChanges, Input, Output, EventEmitter, ViewChild, SimpleChanges } from '@angular/core';
import {TreeVDSService} from '../../../../services/management/tree-VDS.service';
import {log} from 'util';

@Component({
  selector: 'app-config-tree-vds-v2',
  templateUrl: './config-tree-select.component.html',
  styleUrls: ['./config-tree-select.component.scss'],
  providers: [
    { provide: TreeviewI18n, useClass: UnitTreeviewI18n }
  ],
})
export class ConfigTreeComponent implements OnChanges {

  @Input() config: TreeviewConfig;
  @Input() items: TreeviewItem[];
  @Input() value: any;
  @Input() types: [];

  @Output() valueChange = new EventEmitter<any>();
  @Output() filterChangeVDS = new EventEmitter<string>();
  @ViewChild(TreeviewComponent) dropdownTreeviewComponent: TreeviewComponent;
  filterText: string;
  private treeviewSelectI18n: DropdownTreeview;
  selectedItem: string;

  constructor(public i18n: TreeviewI18n,
              private treeVDSService: TreeVDSService) {
    this.config = TreeviewConfig.create({
      hasAllCheckBox: false,
      hasCollapseExpand: false,
      hasFilter: true,
      maxHeight: 850
    });
    // @ts-ignore
    this.treeviewSelectI18n = i18n as DropdownTreeview;
  }
  ngOnChanges(changes: SimpleChanges) {
    if (!isNil(this.value)) {
      this.updateSelectedItem();
    }
  }

  select(item: TreeviewItem) {
    this.selectItem(item);
  }

  private updateSelectedItem() {
    if (!isNil(this.items)) {
      const selectedItem = TreeviewHelper.findItemInList(this.items, this.value);
      if (selectedItem) {
        this.selectItem(selectedItem);
      }
    }
  }

  private selectItem(item: TreeviewItem) {
    console.log(item);
    if (this.treeviewSelectI18n.selectedItem !== item) {
      this.treeviewSelectI18n.selectedItem = item;
      if (this.value !== item) {
        this.value = item.value;
        let type = this.types.find(x=>x['code'] === item.value);
        console.log(type);
        item['type'] = type['type'];
        this.valueChange.emit(item);
        this.selectedItem = item.value;
      }
    }
  }

  searchTree(value: string) {
    this.filterChangeVDS.emit(this.filterText);
  }

}
