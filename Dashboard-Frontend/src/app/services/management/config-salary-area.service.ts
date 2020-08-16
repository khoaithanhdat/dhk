import { Injectable } from '@angular/core';
import { config } from './../../config/application.config';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { salAreSalaryModel } from '../../models/salAreaSalary.model';
import { salaryTimeModel } from '../../models/salaryTime.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConfigSalaryAreaService {

  constructor(private http: HttpClient) { }

  getAllArea() {
    return this.http.get<any>(config.get_Area_API);
  }

  getSalByArea(areaCode : string){
    let headers = new HttpHeaders();
    return this.http.post(`${config.get_salByArea_API}/${areaCode}`, {
      headers: headers,
      observe: 'response',
      responseType: 'json'
    });
  }

  updateData(data: salAreSalaryModel): Observable<salaryTimeModel> {
    //console.log('data', data);
    return this.http.post<salAreSalaryModel>(`${config.update_salByArea_API}`, data);
  }
}
