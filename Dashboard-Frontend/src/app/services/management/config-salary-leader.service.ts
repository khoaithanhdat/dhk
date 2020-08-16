import { config } from './../../config/application.config';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SalConfigStaffTargetModel } from '../../models/SalConfigStaffTarget.model';
import { SalConfigSaleFeeModel } from '../../models/SalConfigSaleFee.model';
import { SalConfigHsTinhLuongModel } from '../../models/SalConfigHsTinhLuong.model';
import { ConfigSalaryLeaderDTO } from '../../models/SalaryLeader.model';
import { AddStaff } from '../../models/AddStaff.model';

@Injectable({
  providedIn: 'root'
})
export class ConfigSalaryLeaderService {

  constructor(private http: HttpClient) { }

  updateConfigSalaryLeader(salaryLeader: ConfigSalaryLeaderDTO) {
    return this.http.post<ConfigSalaryLeaderDTO>(`${config.updateConfigSalaryLeader}`, salaryLeader);
  }

  getSalConfigStaffTargetById(id) {
    return this.http.get<any>(config.getSalConfigStaffTargetById + '/' + id);
  }

  getAllSalConfigStaffTarget() {
    return this.http.get<any>(config.getAllSalConfigStaffTarget);
  }

  getAllSalConfigStaffTargetByService() {
    return this.http.get<any>(config.getAllSalConfigStaffTargetByService);
  }

  getAllSalConfigStaffTargetCompleteByService(serviceId: number) {
    let url = '?serviceId=' + serviceId;
    return this.http.get<any>(config.getAllSalConfigStaffTargetCompleteByService + url);
  }

  updateSalConfigStaffTarget(salConfigStaffTargetModel: SalConfigStaffTargetModel) {
    return this.http.post<SalConfigStaffTargetModel>(`${config.updateSalConfigStaffTarget}`, salConfigStaffTargetModel);
  }

  getAllSalConfigSaleFee() {
    return this.http.get<any>(config.getAllSalConfigSaleFee);
  }

  getSalConfigSaleFeeByCondition(feeName: string, receiveFrom: string) {

    let url = '?';

    if (feeName && feeName !== '') {
        url = url + '&feeName=' + feeName;
    }
    if (receiveFrom && receiveFrom !== '') {
        url = url + '&receiveFrom=' + receiveFrom;
    }
    return this.http.get<any>(config.getSalConfigSaleFeeByCondition + url);
  }

  updateSalConfigSaleFee(salConfigSaleFeeModel: SalConfigSaleFeeModel) {
    return this.http.post<SalConfigSaleFeeModel>(`${config.updateSalConfigSaleFee}`, salConfigSaleFeeModel);
  }

  getAllSalConfigHsTinhLuong() {
    return this.http.get<any>(config.getAllSalConfigHsTinhLuong);
  }

  getSalConfigHsTinhLuongById(id) {
    return this.http.get<any>(config.getSalConfigHsTinhLuongById + '/' + id);
  }

  updateSalConfigHsTinhLuong(salConfigHsTinhLuongModel: SalConfigHsTinhLuongModel) {
    return this.http.post<SalConfigHsTinhLuongModel>(`${config.updateSalConfigSaleFee}`, salConfigHsTinhLuongModel);
  }

  getAllSaleFee() {
    return this.http.get<any>(config.getAllSaleFee);
  }

  getSalConfigSaleFeeById(id) {
    return this.http.get<any>(config.getSalConfigSaleFeeById + '/' + id);
  }

  getByIdAndSaleFee(id: number) {
    return this.http.get<any>(`${config.getAllSaleFee}?id=`+id);
  }
}
