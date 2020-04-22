import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScrumDialogComponent } from './scrum-dialog.component';

describe('ScrumDialogComponent', () => {
  let component: ScrumDialogComponent;
  let fixture: ComponentFixture<ScrumDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScrumDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScrumDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
