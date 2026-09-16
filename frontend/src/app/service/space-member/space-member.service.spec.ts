import { TestBed } from '@angular/core/testing';
import { SpaceMemberService } from './space-member.service';

describe('SpaceMemberService', () => {
  let service: SpaceMemberService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SpaceMemberService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
