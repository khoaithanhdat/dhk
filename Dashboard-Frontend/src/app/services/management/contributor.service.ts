import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, from } from 'rxjs';
import { Injectable } from '@angular/core';

import { config } from '../../config/application.config';
import {contributorModel} from '../../models/contributor.model';

@Injectable()
export class ContributorService {

  constructor(private http: HttpClient) {
  }

  getAllContributor() {
    return this.http.get(config.contributor_API);
  }

  getDatas(search: contributorModel[]) {
    return this.http.post<contributorModel>(config.get_contributor_API, search);
  }

  deleteContributor(id : number) {
    return this.http.get(`${config.delete_contributor_API}/${id}`);
  }

  downloadTemplate(): Observable<HttpResponse<Object>> {
    let headers = new HttpHeaders();
    headers = headers.append('Content-Type', 'application/json');
    headers = headers.append('Access-Control-Allow-Headers', '*');
    return this.http.get(config.contributor_Download_File_API, {
      headers: headers,
      observe: 'response',
      responseType: 'arraybuffer'
    });
  }

  insertData(data: contributorModel): Observable<contributorModel> {
    //console.log('data', data);
    return this.http.post<contributorModel>(`${config.add_contributor_API}`, data);
  }

  updateData(data: contributorModel): Observable<contributorModel> {
    //console.log('data', data);
    return this.http.post<contributorModel>(`${config.contributor_Upload_File_API}`, data);
  }

}
4
