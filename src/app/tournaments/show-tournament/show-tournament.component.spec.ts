import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowTournamentComponent } from './show-tournament.component';

describe('ShowTournamentComponent', () => {
  let component: ShowTournamentComponent;
  let fixture: ComponentFixture<ShowTournamentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowTournamentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowTournamentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
