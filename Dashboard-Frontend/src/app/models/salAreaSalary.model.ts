import { salaryTimeModel } from './salaryTime.model';

export class salAreSalaryModel {

  id?: number;
  areaCode?: string;
  hardSalary?: number;
  hardSalaryByTime?: string;
  targetSalary?: number;
  targetSalaryByTime?: string;
  createdDate?: string;
  createdUser?: string;
  updatedDate?: string;
  updatedUser?: string;
  lstSalaryTimeDTOS?: salaryTimeModel[];

  constructor() {}
}
