import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { config } from '../../config/application.config';

@Injectable({
  providedIn: 'root'
})
export class ShopService {
  
  constructor(
    private http: HttpClient
  ) { }

  getAll(){
    return this.http.get<any>(config.shop_GetAll_API);
  }
}
