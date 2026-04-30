import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LogoutModalComponent } from './logout-modal.component';

describe('LogoutModalComponent', () => {
  let component: LogoutModalComponent;
  let fixture: ComponentFixture<LogoutModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LogoutModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LogoutModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should emit confirm event on onConfirm', () => {
    const spy = vi.spyOn(component.confirm, 'emit');
    component.onConfirm();
    expect(spy).toHaveBeenCalled();
  });

  it('should emit cancel event on onCancel', () => {
    const spy = vi.spyOn(component.cancel, 'emit');
    component.onCancel();
    expect(spy).toHaveBeenCalled();
  });
});
