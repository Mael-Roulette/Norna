import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ColumnMenu } from './column-menu';

describe('ColumnMenu', () => {
  let component: ColumnMenu;
  let fixture: ComponentFixture<ColumnMenu>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ColumnMenu],
    }).compileComponents();

    fixture = TestBed.createComponent(ColumnMenu);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
