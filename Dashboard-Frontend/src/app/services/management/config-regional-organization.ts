import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {config} from '../../config/application.config';
import {PartnerModel} from '../../models/org.model';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConfigRegionalOrganization {
  constructor(private http: HttpClient) {
  }

  updateProvince(obj: PartnerModel) {
    return this.http.post(config.updateProvince, obj);
  }

  loadArea() {
    return this.http.get(config.loadArea);
  }

  createProvince(obj) {
    return this.http.post<any>(config.createProvince, obj);
  }

  searchTree(obj) {
    return this.http.post<any>(config.searchTree, obj);
  }

  changeArea(obj) {
    return this.http.post<any>(config.changeArea, obj);
  }

}
