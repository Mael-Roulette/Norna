import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CreateColumn } from './create-column';

describe('CreateColumn', () => {
  let component: CreateColumn;
  let fixture: ComponentFixture<CreateColumn>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateColumn],
    }).compileComponents();

    fixture = TestBed.createComponent(CreateColumn);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
