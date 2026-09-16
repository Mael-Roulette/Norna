import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InternLayout } from './intern-layout';

describe('InternLayout', () => {
  let component: InternLayout;
  let fixture: ComponentFixture<InternLayout>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InternLayout],
    }).compileComponents();

    fixture = TestBed.createComponent(InternLayout);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
