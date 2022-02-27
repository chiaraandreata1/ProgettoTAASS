import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationMainComponent } from './reservation-main.component';

describe('ReservationMainComponent', () => {
  let component: ReservationMainComponent;
  let fixture: ComponentFixture<ReservationMainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReservationMainComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReservationMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
