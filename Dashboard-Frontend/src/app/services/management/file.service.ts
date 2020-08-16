import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { config } from '../../config/application.config';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileService {

    constructor(private http: HttpClient) {
        // nothing to do
    }

    downloadTemplate(importType: number): Observable<HttpResponse<Object>> {
      let headers = new HttpHeaders();
      // headers = headers.append('Accept', '*');
      headers = headers.append('Content-Type', 'application/json');
      headers = headers.append('Access-Control-Allow-Headers', '*');

      return this.http.get(`${config.download_Template_File_API}/${importType}`, {
        headers: headers,
        observe: 'response',
        responseType: 'arraybuffer'
      });
    }
}
