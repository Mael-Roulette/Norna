import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TaskMODAL } from './task-modal';

describe('TaskMODAL', () => {
  let component: TaskMODAL;
  let fixture: ComponentFixture<TaskMODAL>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaskMODAL],
    }).compileComponents();

    fixture = TestBed.createComponent(TaskMODAL);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
