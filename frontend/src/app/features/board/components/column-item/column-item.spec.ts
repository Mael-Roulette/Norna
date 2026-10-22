import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ColumnItem } from './column-item';

describe('ColumnItem', () => {
  let component: ColumnItem;
  let fixture: ComponentFixture<ColumnItem>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ColumnItem],
    }).compileComponents();

    fixture = TestBed.createComponent(ColumnItem);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
