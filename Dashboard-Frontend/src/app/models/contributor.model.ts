import { Timestamp } from 'rxjs/internal/operators/timestamp';

export class contributorModel {

  id?: number;
  code?: string;
  name?: string;
  status?: string;
  createdDate?: string;
  createdUser?: string;
  updatedDate?: string;
  updatedUser?: string;
  checked?: boolean;


  constructor(
    code?: string,
    name?: string,
    status?: string,) {

      this.code = code;
      this.status = status;
      this.name = name;
  }
}
