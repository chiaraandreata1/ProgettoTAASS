import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CoursesCompletedComponent } from './courses-completed.component';

describe('CoursesCompletedComponent', () => {
  let component: CoursesCompletedComponent;
  let fixture: ComponentFixture<CoursesCompletedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CoursesCompletedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CoursesCompletedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
