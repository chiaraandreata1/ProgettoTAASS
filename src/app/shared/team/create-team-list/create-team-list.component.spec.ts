import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateTeamListComponent } from './create-team-list.component';

describe('CreateTeamListComponent', () => {
  let component: CreateTeamListComponent;
  let fixture: ComponentFixture<CreateTeamListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateTeamListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateTeamListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
