import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlayersReservationComponent } from './players-reservation.component';

describe('PlayersReservationComponent', () => {
  let component: PlayersReservationComponent;
  let fixture: ComponentFixture<PlayersReservationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PlayersReservationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PlayersReservationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
