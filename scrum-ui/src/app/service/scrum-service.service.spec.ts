import { TestBed } from '@angular/core/testing';

import { ScrumService } from './scrum-service.service';

describe('ScrumServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ScrumService = TestBed.get(ScrumService);
    expect(service).toBeTruthy();
  });
});
