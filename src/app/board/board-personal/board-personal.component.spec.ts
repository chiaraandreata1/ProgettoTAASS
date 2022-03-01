import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BoardPersonalComponent } from './board-personal.component';

describe('BoardPersonalComponent', () => {
  let component: BoardPersonalComponent;
  let fixture: ComponentFixture<BoardPersonalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BoardPersonalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BoardPersonalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
