import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SpaceSelector } from './space-selector';

describe('SpaceSelector', () => {
  let component: SpaceSelector;
  let fixture: ComponentFixture<SpaceSelector>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SpaceSelector],
    }).compileComponents();

    fixture = TestBed.createComponent(SpaceSelector);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
