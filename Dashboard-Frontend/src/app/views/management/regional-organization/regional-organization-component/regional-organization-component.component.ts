import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {TreeItem, TreeviewConfig, TreeviewI18n, TreeviewItem} from 'ngx-treeview';
import {FormBuilder} from '@angular/forms';
import {ServiceService} from '../../../../services/management/service.service';
import {TreeVDSService} from '../../../../services/management/tree-VDS.service';
import {UnitModel} from '../../../../models/unit.model';
import {Subject} from 'rxjs';
import {UnitTreeviewI18n} from '../../vtt-target/unit-treeview-i18n';
import {SingleChartAddComponent} from '../../config-dashboard/config-single-chart/single-chart-add/single-chart-add.component';
import {MatDialog} from '@angular/material';
import {AddRegionalOrganiztionComponent} from '../add-regional-organiztion/add-regional-organiztion.component';
import {LoadTreeRegionModel} from '../../../../models/loadTreeRegion.model';
import {EditComponent} from '../manage-regional-organiztion/area/edit/edit.component';
import {DataService} from '../../../../services/data.service';

@Component({
  selector: 'app-regional-organization-component',
  templateUrl: './regional-organization-component.component.html',
  styleUrls: ['./regional-organization-component.component.scss'],
  providers: [
    {provide: TreeviewI18n, useClass: UnitTreeviewI18n}
  ],
})
export class RegionalOrganizationComponentComponent implements OnInit, OnDestroy {

  constructor(private fb: FormBuilder,
              private serviceService: ServiceService,
              private treeVDSService: TreeVDSService,
              private dialog: MatDialog
  ) {
  }

  nodeTreeView: TreeviewItem;
  nodeTreeViews: TreeviewItem[] = [];
  nodeItem: TreeItem;
  nodeItems: TreeItem[] = [];
  dataTree = [];
  dataUnit = [];
  dataUnitTree = [];
  // nodeTrees: UnitModel[] = [];
  // unitIitem: UnitModel;
  nodeTrees: LoadTreeRegionModel[] = [];
  unitIitem: LoadTreeRegionModel;
  valueUnit;
  mobjNodeItemService: TreeItem;
  marrIndexNodeService = [];
  mobjConfigVDS = TreeviewConfig.create({
    hasFilter: true,
    hasCollapseExpand: false,
    maxHeight: 850,
  });
  valueVds;
  unsubscribe$ = new Subject();
  screen = '';
  manage_item: TreeviewItem;
  manage_children: {};

  ngOnInit() {
    this.unitTreeData('', 'VDS');
    this.serviceService.reloadWarning$
    // .pipe(takeUntil(this.unsubscribe$))
      .subscribe(res => {
        if (res) {
          // this.currentP = 1;
          this.unitTreeData('');
        }
      });
  }

  no() {

  }

  unitTreeData(searchText: string, defaultValue?: string) {
    this.nodeTrees = [];
    this.nodeTreeViews = [];
    this.treeVDSService.getunitsRegion().subscribe(
      next => {
        if (!next['data']) {
          return;
        }
        console.log(next['data']);
        this.nodeTrees = [];
        this.nodeTreeViews = [];
        this.dataUnit = next['data'];
        this.dataUnitTree = [];
        this.dataTree = next['data'];
        this.createNode(this.nodeTrees, this.dataTree, this.unitIitem).forEach(
          note => {
            this.nodeTrees.push(note);
          }
        );

        setTimeout(() => {
          this.dataTree = this.nodeTrees;
          setTimeout(() => {
            this.createTree(this.nodeTrees, this.dataTree);
            setTimeout(() => {
              this.nodeTrees.forEach(valuess => {
                this.nodeTreeView = new TreeviewItem(this.forwardData(valuess, this.nodeItem, this.nodeItems));

                this.nodeTreeViews.push(this.nodeTreeView);
              });
              this.valueVds = defaultValue;
            }, 2);
          }, 2);
        }, 5);
        console.log('sadsad');
        console.log(this.nodeTrees);
        this.setDataUnitTree(this.nodeTrees);
      }
    );


  }

  setDataUnitTree(dataU) {
    for (let unit of dataU) {
      let x = Object.assign({}, unit);
      if (x.children) {
        let xchild = x.children;
        setTimeout(() => {
          xchild.forEach(child => {
            if (child.children) {
              this.setDataUnitTree(child.children);
            }
          });
        });
        this.dataUnitTree.push(x);
      }
      else {
        return;
      }
    }
  }

  types = [];

  forwardData(pobjNodeTree: LoadTreeRegionModel, pobjitem: TreeItem, parrItems: TreeItem[] = []) {
    try {
      parrItems = [];
      // pobjNodeTree = null;
      if (pobjNodeTree.children) {
        pobjNodeTree.children.forEach(value => {
          this.mobjNodeItemService = this.forwardData(value, null, []);
          parrItems.push(this.mobjNodeItemService);
        });
      }
      let text = '';
      this.types.push({
        code: pobjNodeTree.code,
        type: pobjNodeTree.type
      });
      if (pobjNodeTree.type === 'AREA') {
        text = '🌎' + pobjNodeTree.name;
      } else if (pobjNodeTree.type === 'PARTNER') {
        text = '🏢' + pobjNodeTree.code + ' - ' + pobjNodeTree.name;
      } else if (pobjNodeTree.type === 'CLUSTER') {
        text = '🏣' + pobjNodeTree.code + ' - ' + pobjNodeTree.name;
      } else if (pobjNodeTree.type === 'LEAD') {
        text = '👤 CT: ' + pobjNodeTree.code + ' - ' + pobjNodeTree.name;
      } else if (pobjNodeTree.type === 'STAFF') {
        text = '👥 NVKD: ' + pobjNodeTree.code + ' - ' + pobjNodeTree.name;
      } else if (pobjNodeTree.type === 'COLLABORATOR') {
        text = '👥 CTV: ' + pobjNodeTree.code + ' - ' + pobjNodeTree.name;
      }

      return {
        value: pobjNodeTree.code,
        text: text,
        children: parrItems,
        checked: false
      };
    } catch (e) {
      console.log(e);
    }
  }

  createNode(parrItems: LoadTreeRegionModel[] = [], parrDataTree: LoadTreeRegionModel[], pobjItem: LoadTreeRegionModel) {
    try {
      pobjItem = null;
      parrItems = parrDataTree.map(value => {
        pobjItem = {
          code: value.code,
          name: value.name,
          parent: value.parent,
          type: value.type,
          parentName: value.parentName,
          partnerCode: value.partnerCode,
          partnerName: value.partnerName,
          shortName: value.shortName,
          expiredDate: value.expiredDate,
          children: []
        };
        return pobjItem;
      });
      console.log(parrItems);
      return parrItems;
    } catch (e) {
    }
  }

  createTree(parrNodeTrees: LoadTreeRegionModel[], parrDataTree: LoadTreeRegionModel[]) {
    this.marrIndexNodeService = [];
    try {
      const len = parrNodeTrees.length;
      for (let i = 0; i < len; i++) {
        for (let j = 0; j < len; j++) {
          if (parrNodeTrees[i].code === parrDataTree[j].parent) {
            parrNodeTrees[i].children.push(parrDataTree[j]);
            this.marrIndexNodeService.push(j);
          }
        }
      }
      const c = (a: number, b: number) => (a - b);
      this.marrIndexNodeService.sort(c);
      for (let i = this.marrIndexNodeService.length - 1; i >= 0; i--) {
        parrNodeTrees.splice(this.marrIndexNodeService[i], 1);
      }
    } catch (e) {
    }
  }

  // screen = 'AREA';
  // manage_item: TreeviewItem;
  // manage_children: {};

  unitChange(value) {
    this.screen = '';
    this.valueUnit = value.value;
    this.dataUnitTree.forEach(
      tree => {
        if (tree.code === value.value) {
          this.screen = value.type;
          this.manage_item = value;

          this.manage_children = tree.children ? tree.children : [];
          console.log('vao DO');
        }
      }
    );

  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  searchTree(value: string) {
    this.unitTreeData(value);
  }

  // view dialog
  openDialog() {
    const dialogRef = this.dialog.open(AddRegionalOrganiztionComponent, {
        maxWidth: '95vw',
        maxHeight: '95vh',
        width: '60vw',
        height: '70vh',
        data: {
          // chartSize: this.listCardSize,
          //           // chartType: this.chartType,
          //           // drilldownObject: this.groupCard,
          //           // card: this.singleCard
        }
      }
    );
  }

}
