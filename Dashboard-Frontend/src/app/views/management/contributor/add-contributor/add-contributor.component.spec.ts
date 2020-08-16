import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddContributorComponent } from './add-contributor.component';

describe('AddContributorComponent', () => {
  let component: AddContributorComponent;
  let fixture: ComponentFixture<AddContributorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddContributorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddContributorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
