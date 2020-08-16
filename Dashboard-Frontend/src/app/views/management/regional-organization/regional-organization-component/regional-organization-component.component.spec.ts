import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegionalOrganizationComponentComponent } from './regional-organization-component.component';

describe('RegionalOrganizationComponentComponent', () => {
  let component: RegionalOrganizationComponentComponent;
  let fixture: ComponentFixture<RegionalOrganizationComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RegionalOrganizationComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegionalOrganizationComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
