export class LoadTreeRegionModel {
  code: string;
  name: string;
  parent: string;
  type: string;
  parentName: string;
  partnerCode: string;
  partnerName: string;
  shortName: string;
  expiredDate: any;
  children: LoadTreeRegionModel[];

  constructor(code?: string, name?: string, parent?: string,
              type?: string, parentName?: string,
              partnerCode?: string, partnerName?: string, shortName?: string, expiredDate?: any) {
    this.code = code;
    this.name = name;
    this.parent = parent;
    this.type = type;
    this.parentName = parentName;
    this.partnerCode = partnerCode;
    this.partnerName = partnerName;
    this.shortName = shortName;
    this.expiredDate = expiredDate;
  }
}
