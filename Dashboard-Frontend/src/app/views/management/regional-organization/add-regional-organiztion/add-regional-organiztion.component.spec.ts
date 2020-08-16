import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRegionalOrganiztionComponent } from './add-regional-organiztion.component';

describe('AddRegionalOrganiztionComponent', () => {
  let component: AddRegionalOrganiztionComponent;
  let fixture: ComponentFixture<AddRegionalOrganiztionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddRegionalOrganiztionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddRegionalOrganiztionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
