import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SlotReservationButtonComponent } from './slot-reservation-button.component';

describe('SlotReservationButtonComponent', () => {
  let component: SlotReservationButtonComponent;
  let fixture: ComponentFixture<SlotReservationButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SlotReservationButtonComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SlotReservationButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
