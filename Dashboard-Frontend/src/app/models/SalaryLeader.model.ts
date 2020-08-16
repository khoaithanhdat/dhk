import {SalConfigHsTinhLuongModel} from '../models/SalConfigHsTinhLuong.model';
import {SalConfigSaleFeeModel} from '../models/SalConfigSaleFee.model';
import {SalConfigStaffGiffModel} from '../models/SalConfigStaffGiff.model';
import {SalConfigStaffTargetModel} from '../models/SalConfigStaffTarget.model';

export class ConfigSalaryLeaderDTO {
    salConfigHsTinhLuongModel?: SalConfigHsTinhLuongModel[];
    salConfigSaleFeeModel?: SalConfigSaleFeeModel[];
    salConfigStaffGiffModel?: SalConfigStaffGiffModel[];
    salConfigStaffTargetModel?: SalConfigStaffTargetModel[];
}