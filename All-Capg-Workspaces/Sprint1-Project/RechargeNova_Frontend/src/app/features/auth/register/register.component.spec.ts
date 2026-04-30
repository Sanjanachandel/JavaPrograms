import { vi } from 'vitest';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter, Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { RegisterComponent } from './register.component';
import { UserApiService } from '../../../core/services/user-api.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { ThemeService } from '../../../core/services/theme.service';
import { environment } from '../../../../environments/environment';

const BASE = environment.apiBaseUrl;

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    localStorage.clear();
    TestBed.resetTestingModule();
    await TestBed.configureTestingModule({
      imports: [RegisterComponent, ReactiveFormsModule],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        UserApiService,
        AuthService,
        ToastService,
        ThemeService,
      ],
    }).compileComponents();

    fixture   = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    httpMock  = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
    TestBed.resetTestingModule();
  });

  // ─── Creation ─────────────────────────────────────────────────────────────
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should start with loading = false', () => {
    expect(component.loading()).toBe(false);
  });

  it('should start with showPassword = false', () => {
    expect(component.showPassword()).toBe(false);
  });

  it('should have initially invalid form', () => {
    expect(component.form.invalid).toBe(true);
  });

  // ─── Form validation ──────────────────────────────────────────────────────
  it('should be invalid with empty name', () => {
    component.form.patchValue({ name: '', email: 'a@b.com', phoneNumber: '9876543210', password: 'pass123' });
    expect(component.form.get('name')!.invalid).toBe(true);
  });

  it('should be invalid when name contains numbers', () => {
    component.form.get('name')!.setValue('Sanjana123');
    expect(component.form.get('name')!.invalid).toBe(true);
  });

  it('should be valid when name has only alphabets and spaces', () => {
    component.form.get('name')!.setValue('Sanjana Chandel');
    expect(component.form.get('name')!.valid).toBe(true);
  });

  it('should be invalid with invalid email', () => {
    component.form.get('email')!.setValue('not-email');
    expect(component.form.get('email')!.invalid).toBe(true);
  });

  it('should be invalid for phoneNumber not starting with 6-9', () => {
    component.form.get('phoneNumber')!.setValue('1234567890');
    expect(component.form.get('phoneNumber')!.invalid).toBe(true);
  });

  it('should be invalid for phoneNumber shorter than 10 digits', () => {
    component.form.get('phoneNumber')!.setValue('987654321');
    expect(component.form.get('phoneNumber')!.invalid).toBe(true);
  });

  it('should be valid for correct phoneNumber starting with 9', () => {
    component.form.get('phoneNumber')!.setValue('9876543210');
    expect(component.form.get('phoneNumber')!.valid).toBe(true);
  });

  it('should be invalid for password shorter than 6 chars', () => {
    component.form.get('password')!.setValue('abc');
    expect(component.form.get('password')!.invalid).toBe(true);
  });

  // ─── isInvalid ────────────────────────────────────────────────────────────
  it('isInvalid should return false for pristine field', () => {
    expect(component.isInvalid('name')).toBe(false);
  });

  it('isInvalid should return true for touched invalid field', () => {
    component.form.get('name')!.setValue('');
    component.form.get('name')!.markAsTouched();
    expect(component.isInvalid('name')).toBe(true);
  });

  // ─── strengthPct ──────────────────────────────────────────────────────────
  it('should return 0 for empty password', () => {
    component.form.get('password')!.setValue('');
    expect(component.strengthPct()).toBe(0);
  });

  it('should return ≥30 for 6+ char password', () => {
    component.form.get('password')!.setValue('abcdef');
    expect(component.strengthPct()).toBeGreaterThanOrEqual(30);
  });

  it('should return 100 for strong complex password', () => {
    component.form.get('password')!.setValue('StrongP@ss123');
    expect(component.strengthPct()).toBe(100);
  });

  // ─── strengthLabel ────────────────────────────────────────────────────────
  it('should return "weak" label for short password', () => {
    component.form.get('password')!.setValue('abc');
    expect(component.strengthLabel()).toBe('weak');
  });

  it('should return "strong" label for complex password', () => {
    component.form.get('password')!.setValue('StrongP@ss123');
    expect(component.strengthLabel()).toBe('strong');
  });

  // ─── onSubmit ─────────────────────────────────────────────────────────────
  it('should not make HTTP call when form is invalid', () => {
    component.onSubmit();
    httpMock.expectNone(`${BASE}/users/register`);
  });

  it('should POST to /users/register on valid form submit', () => {
    component.form.patchValue({
      name: 'Sanjana', email: 'sanjana@example.com',
      phoneNumber: '9876543210', password: 'pass123',
    });
    const router = TestBed.inject(Router);
    vi.spyOn(router, 'navigate');
    component.onSubmit();
    const req = httpMock.expectOne(`${BASE}/users/register`);
    expect(req.request.method).toBe('POST');
    req.flush({
      id: 1, name: 'Sanjana', email: 'sanjana@example.com',
      role: 'ROLE_USER', phoneNumber: '9876543210', createdAt: '2024-01-01T00:00:00Z',
    });
    expect(router.navigate).toHaveBeenCalledWith(['/auth/login']);
  });

  it('should show error toast and reset loading on registration failure', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    component.form.patchValue({
      name: 'Sanjana', email: 'existing@example.com',
      phoneNumber: '9876543210', password: 'pass123',
    });
    component.onSubmit();
    const req = httpMock.expectOne(`${BASE}/users/register`);
    req.flush({ message: 'Email already exists' }, { status: 400, statusText: 'Bad Request' });
    expect(toast.error).toHaveBeenCalledWith('Email already exists');
    expect(component.loading()).toBe(false);
  });

  it('should return "fair" label for medium password', () => {
    // 30 (length) + 15 (number) = 45 (which is >= 40 and < 70)
    component.form.get('password')!.setValue('abc123');
    expect(component.strengthPct()).toBe(45);
    expect(component.strengthLabel()).toBe('fair');
  });

  it('should handle registration failure without message', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    component.form.patchValue({
      name: 'Sanjana', email: 'e@e.com', phoneNumber: '9876543210', password: 'pass123'
    });
    component.onSubmit();
    const req = httpMock.expectOne(`${BASE}/users/register`);
    req.flush(null, { status: 500, statusText: 'Error' });
    expect(toast.error).toHaveBeenCalledWith('Registration failed. Please try again.');
  });
});
