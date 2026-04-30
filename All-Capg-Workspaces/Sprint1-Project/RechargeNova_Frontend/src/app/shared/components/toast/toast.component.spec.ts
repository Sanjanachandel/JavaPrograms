import { TestBed } from '@angular/core/testing';
import { ComponentFixture } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { ToastComponent } from './toast.component';
import { ToastService } from '../../../core/services/toast.service';

describe('ToastComponent', () => {
  let component: ToastComponent;
  let fixture: ComponentFixture<ToastComponent>;
  let toastService: ToastService;

  beforeEach(async () => {
    TestBed.resetTestingModule();
    await TestBed.configureTestingModule({
      imports: [ToastComponent],
      providers: [provideRouter([])],
    }).compileComponents();

    fixture      = TestBed.createComponent(ToastComponent);
    component    = fixture.componentInstance;
    toastService = TestBed.inject(ToastService);
    fixture.detectChanges();
  });

  afterEach(() => {
    TestBed.resetTestingModule();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // ─── iconFor ─────────────────────────────────────────────────────────────
  it('should return ✅ icon for "success" type', () => {
    expect(component.iconFor('success')).toBe('✅');
  });

  it('should return ❌ icon for "error" type', () => {
    expect(component.iconFor('error')).toBe('❌');
  });

  it('should return ℹ️ icon for "info" type', () => {
    expect(component.iconFor('info')).toBe('ℹ️');
  });

  it('should return ℹ️ icon for unknown types', () => {
    expect(component.iconFor('warning')).toBe('ℹ️');
  });

  // ─── toastSvc binding ────────────────────────────────────────────────────
  it('should expose the toastSvc signal', () => {
    expect(component.toastSvc).toBeTruthy();
  });

  it('should reflect toasts added via the service', () => {
    toastService.success('Test message');
    expect(component.toastSvc.toasts().length).toBe(1);
    expect(component.toastSvc.toasts()[0].message).toBe('Test message');
  });

  it('should reflect empty toasts initially', () => {
    expect(component.toastSvc.toasts().length).toBe(0);
  });
});
