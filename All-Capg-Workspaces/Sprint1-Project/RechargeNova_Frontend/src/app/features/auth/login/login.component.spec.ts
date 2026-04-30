import { vi } from 'vitest';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter, Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './login.component';
import { UserApiService } from '../../../core/services/user-api.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { ThemeService } from '../../../core/services/theme.service';
import { environment } from '../../../../environments/environment';

const BASE = environment.apiBaseUrl;

const mockUser = {
  id: 1, name: 'Sanjana', email: 'sanjana@example.com',
  role: 'ROLE_USER', phoneNumber: '9876543210', createdAt: '2024-01-01T00:00:00Z',
};

const mockAdmin = {
  id: 2, name: 'Admin', email: 'admin@example.com',
  role: 'ADMIN', phoneNumber: '9999999999', createdAt: '2024-01-01T00:00:00Z',
};

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    localStorage.clear();
    TestBed.resetTestingModule();
    await TestBed.configureTestingModule({
      imports: [LoginComponent, ReactiveFormsModule],
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

    fixture   = TestBed.createComponent(LoginComponent);
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

  // ─── Initial state ────────────────────────────────────────────────────────
  it('should start in LOGIN mode', () => {
    expect(component.mode()).toBe('LOGIN');
  });

  it('should start with loading = false', () => {
    expect(component.loading()).toBe(false);
  });

  it('should start with showPassword = false', () => {
    expect(component.showPassword()).toBe(false);
  });

  it('should have an initially invalid form', () => {
    expect(component.form.invalid).toBe(true);
  });

  // ─── Form validation ──────────────────────────────────────────────────────
  it('should mark form invalid when email is empty', () => {
    component.form.get('email')!.setValue('');
    component.form.get('password')!.setValue('pass123');
    expect(component.form.invalid).toBe(true);
  });

  it('should mark form invalid when email format is wrong', () => {
    component.form.get('email')!.setValue('not-an-email');
    component.form.get('password')!.setValue('pass123');
    expect(component.form.invalid).toBe(true);
  });

  it('should mark form invalid when password is too short', () => {
    component.form.get('email')!.setValue('sanjana@example.com');
    component.form.get('password')!.setValue('abc');
    expect(component.form.invalid).toBe(true);
  });

  it('should mark form valid with correct email and password', () => {
    component.form.get('email')!.setValue('sanjana@example.com');
    component.form.get('password')!.setValue('pass123');
    expect(component.form.valid).toBe(true);
  });

  // ─── emailError getter ────────────────────────────────────────────────────
  it('should return "Email is required" for empty email after touch', () => {
    const ctrl = component.form.get('email')!;
    ctrl.setValue('');
    ctrl.markAsTouched();
    expect(component.emailError).toBe('Email is required');
  });

  it('should return "Enter a valid email" for bad email format', () => {
    const ctrl = component.form.get('email')!;
    ctrl.setValue('bad-email');
    ctrl.markAsTouched();
    expect(component.emailError).toBe('Enter a valid email');
  });

  it('should return empty string for valid email', () => {
    component.form.get('email')!.setValue('valid@test.com');
    expect(component.emailError).toBe('');
  });

  // ─── isInvalid ────────────────────────────────────────────────────────────
  it('isInvalid should return false when field is pristine and untouched', () => {
    expect(component.isInvalid('email')).toBe(false);
  });

  it('isInvalid should return true when touched invalid field', () => {
    const ctrl = component.form.get('email')!;
    ctrl.setValue('');
    ctrl.markAsTouched();
    expect(component.isInvalid('email')).toBe(true);
  });

  // ─── setMode ──────────────────────────────────────────────────────────────
  it('should switch mode to FORGOT_EMAIL', () => {
    component.setMode('FORGOT_EMAIL');
    expect(component.mode()).toBe('FORGOT_EMAIL');
  });

  it('should reset form and showPassword when switching mode', () => {
    component.form.get('email')!.setValue('test@test.com');
    component.showPassword.set(true);
    component.setMode('FORGOT_EMAIL');
    expect(component.showPassword()).toBe(false);
  });

  // ─── onSubmit ─────────────────────────────────────────────────────────────
  it('should not make HTTP call when form is invalid on submit', () => {
    component.onSubmit();
    httpMock.expectNone(`${BASE}/users/login`);
  });

  it('should navigate to /dashboard after successful ROLE_USER login', () => {
    const router = TestBed.inject(Router);
    vi.spyOn(router, 'navigate');
    component.form.get('email')!.setValue('sanjana@example.com');
    component.form.get('password')!.setValue('pass123');
    component.onSubmit();
    const req = httpMock.expectOne(`${BASE}/users/login`);
    req.flush({ token: 'jwt', user: mockUser });
    expect(router.navigate).toHaveBeenCalledWith(['/dashboard']);
  });

  it('should navigate to /admin after successful ADMIN login', () => {
    const router = TestBed.inject(Router);
    vi.spyOn(router, 'navigate');
    component.form.get('email')!.setValue('admin@example.com');
    component.form.get('password')!.setValue('admin123');
    component.onSubmit();
    const req = httpMock.expectOne(`${BASE}/users/login`);
    req.flush({ token: 'jwt', user: mockAdmin });
    expect(router.navigate).toHaveBeenCalledWith(['/admin']);
  });

  it('should set loading=false and show toast on login error', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    component.form.get('email')!.setValue('sanjana@example.com');
    component.form.get('password')!.setValue('wrongPass');
    component.onSubmit();
    const req = httpMock.expectOne(`${BASE}/users/login`);
    req.flush({ message: 'Invalid credentials' }, { status: 401, statusText: 'Unauthorized' });
    expect(component.loading()).toBe(false);
    expect(toast.error).toHaveBeenCalled();
  });

  // ─── onSendOtp ────────────────────────────────────────────────────────────
  it('should not call API if forgotEmailForm is invalid', () => {
    component.forgotEmailForm.get('email')!.setValue('');
    component.onSendOtp();
    httpMock.expectNone(`${BASE}/users/forgot-password`);
  });

  it('should call API and switch to FORGOT_OTP mode on success', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'success');
    component.forgotEmailForm.get('email')!.setValue('test@test.com');
    component.onSendOtp();
    const req = httpMock.expectOne(`${BASE}/users/forgot-password`);
    expect(req.request.body).toEqual({ email: 'test@test.com' });
    req.flush({});
    expect(component.mode()).toBe('FORGOT_OTP');
    expect(toast.success).toHaveBeenCalledWith('OTP sent successfully to your email!');
    expect(component.loading()).toBe(false);
  });

  it('should show error toast on sendOtp failure', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    component.forgotEmailForm.get('email')!.setValue('test@test.com');
    component.onSendOtp();
    const req = httpMock.expectOne(`${BASE}/users/forgot-password`);
    req.flush({ message: 'User not found' }, { status: 404, statusText: 'Not Found' });
    expect(toast.error).toHaveBeenCalledWith('User not found');
    expect(component.loading()).toBe(false);
  });

  // ─── onResetPassword ──────────────────────────────────────────────────────
  it('should not call API if resetPasswordForm is invalid', () => {
    component.resetPasswordForm.get('otp')!.setValue('');
    component.onResetPassword();
    httpMock.expectNone(`${BASE}/users/reset-password`);
  });

  it('should call API and switch back to LOGIN on success', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'success');
    component.forgotEmailForm.get('email')!.setValue('test@test.com');
    component.resetPasswordForm.get('otp')!.setValue('123456');
    component.resetPasswordForm.get('newPassword')!.setValue('newPass123');
    component.onResetPassword();
    const req = httpMock.expectOne(`${BASE}/users/reset-password`);
    req.flush({});
    expect(component.mode()).toBe('LOGIN');
    expect(toast.success).toHaveBeenCalled();
    expect(component.loading()).toBe(false);
  });

  it('should show error toast on resetPassword failure', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    component.forgotEmailForm.get('email')!.setValue('test@test.com');
    component.resetPasswordForm.get('otp')!.setValue('wrong-otp');
    component.resetPasswordForm.get('newPassword')!.setValue('newPass123');
    component.onResetPassword();
    const req = httpMock.expectOne(`${BASE}/users/reset-password`);
    req.flush({ message: 'Invalid OTP' }, { status: 400, statusText: 'Bad Request' });
    expect(toast.error).toHaveBeenCalledWith('Invalid OTP');
    expect(component.loading()).toBe(false);
  });
});
