import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CreateSpace } from './create-space';

describe('CreateSpace', () => {
  let component: CreateSpace;
  let fixture: ComponentFixture<CreateSpace>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateSpace],
    }).compileComponents();

    fixture = TestBed.createComponent(CreateSpace);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
