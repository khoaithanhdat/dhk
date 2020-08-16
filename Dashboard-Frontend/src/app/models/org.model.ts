import {Partner} from './Partner';


export class OrgModel {
    functionType: number;
    id: number;
    code: string;
    name: string;
    ord: number;
    parentId: number;
    status: number;
    check: boolean;
    expiredDate: any;
    create: boolean;
    read: boolean;
    update: boolean;
    delete: boolean;
    parent: string;
    lstPartner =  [];
}

export class AreaModel extends OrgModel {
    children: PartnerModel[];
}

export class PartnerModel extends OrgModel {
  shortName: string;
  areaName: string;
  effectiveDate: any;
  parentName: string;
  expiredDateNew: any;
}
export class ClusterModel extends OrgModel {
    partnerName: string;
    partnerCode: string;
}
export class LeadModel extends OrgModel {
    partnerName: string;
    partnerCode: string;
    clusterName: string;
    clusterCode: string;
}
export class StaffModel extends OrgModel {
    partnerName: string;
    partnerCode: string;
    leadName: string;
    leadCode: string;
}
export class ProvinceModel extends OrgModel {
  children: PartnerModel[];
}
