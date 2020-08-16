import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigSalaryAreaComponent } from './config-salary-area.component';

describe('ConfigSalaryAreaComponent', () => {
  let component: ConfigSalaryAreaComponent;
  let fixture: ComponentFixture<ConfigSalaryAreaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigSalaryAreaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigSalaryAreaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
