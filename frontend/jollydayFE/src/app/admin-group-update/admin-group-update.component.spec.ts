import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminGroupUpdateComponent } from './admin-group-update.component';

describe('AdminGroupUpdateComponent', () => {
  let component: AdminGroupUpdateComponent;
  let fixture: ComponentFixture<AdminGroupUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminGroupUpdateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AdminGroupUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
