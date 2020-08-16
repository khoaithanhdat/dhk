import { config } from './../../config/application.config';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigSalaryStaffService {

  constructor(private http: HttpClient) { }

  
}