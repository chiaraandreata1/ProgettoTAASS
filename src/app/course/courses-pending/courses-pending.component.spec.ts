import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CoursesPendingComponent } from './courses-pending.component';

describe('CoursesPendingComponent', () => {
  let component: CoursesPendingComponent;
  let fixture: ComponentFixture<CoursesPendingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CoursesPendingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CoursesPendingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
