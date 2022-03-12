import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseAdmincontrollerComponent } from './course-admincontroller.component';

describe('CourseAdmincontrollerComponent', () => {
  let component: CourseAdmincontrollerComponent;
  let fixture: ComponentFixture<CourseAdmincontrollerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CourseAdmincontrollerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CourseAdmincontrollerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
