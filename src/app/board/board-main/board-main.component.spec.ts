import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BoardMainComponent } from './board-main.component';

describe('BoardMainComponent', () => {
  let component: BoardMainComponent;
  let fixture: ComponentFixture<BoardMainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BoardMainComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BoardMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
