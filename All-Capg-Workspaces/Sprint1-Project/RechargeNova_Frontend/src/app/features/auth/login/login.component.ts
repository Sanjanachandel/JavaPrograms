import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { UserApiService } from '../../../core/services/user-api.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { ThemeService } from '../../../core/services/theme.service';

type AuthMode = 'LOGIN' | 'FORGOT_EMAIL' | 'FORGOT_OTP';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  private fb      = inject(FormBuilder);
  private userApi = inject(UserApiService);
  private authSvc = inject(AuthService);
  private toast   = inject(ToastService);
  private router  = inject(Router);
  themeSvc        = inject(ThemeService);

  mode         = signal<AuthMode>('LOGIN');
  loading      = signal(false);
  showPassword = signal(false);

  form = this.fb.group({
    email:    ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  forgotEmailForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]]
  });

  resetPasswordForm = this.fb.group({
    otp: ['', [Validators.required, Validators.minLength(6)]],
    newPassword: ['', [Validators.required, Validators.minLength(6)]]
  });

  get emailError(): string {
    const ctrl = this.form.get('email')!;
    if (ctrl.hasError('required')) return 'Email is required';
    if (ctrl.hasError('email'))    return 'Enter a valid email';
    return '';
  }

  isInvalid(field: string): boolean {
    const c = this.form.get(field)!;
    return c.invalid && (c.dirty || c.touched);
  }

  setMode(newMode: AuthMode): void {
    this.mode.set(newMode);
    this.form.reset();
    this.forgotEmailForm.reset();
    this.resetPasswordForm.reset();
    this.showPassword.set(false);
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.loading.set(true);

    const { email, password } = this.form.value;
    this.userApi.login({ email: email!, password: password! }).subscribe({
      next: (auth) => {
        this.authSvc.saveAuth(auth);
        this.toast.success(`Welcome back, ${auth.user.name}! 🎉`);
        if (auth.user.role === 'ADMIN' || auth.user.role === 'ROLE_ADMIN') {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/dashboard']);
        }
      },
      error: (err) => {
        this.toast.error(err?.error?.message ?? 'Login failed. Check credentials.');
        this.loading.set(false);
      }
    });
  }

  onSendOtp(): void {
    if (this.forgotEmailForm.invalid) { this.forgotEmailForm.markAllAsTouched(); return; }
    
    this.loading.set(true);
    const email = this.forgotEmailForm.value.email!;

    this.userApi.forgotPassword(email).subscribe({
      next: () => {
        this.loading.set(false);
        this.toast.success('OTP sent successfully to your email!');
        this.mode.set('FORGOT_OTP');
      },
      error: (err) => {
        this.loading.set(false);
        this.toast.error(err?.error?.message ?? 'Failed to send OTP. Is the email registered?');
      }
    });
  }

  onResetPassword(): void {
    if (this.resetPasswordForm.invalid) { this.resetPasswordForm.markAllAsTouched(); return; }

    this.loading.set(true);
    const payload = {
      email: this.forgotEmailForm.value.email!,
      otp: this.resetPasswordForm.value.otp!,
      newPassword: this.resetPasswordForm.value.newPassword!
    };

    this.userApi.resetPassword(payload).subscribe({
      next: () => {
        this.loading.set(false);
        this.toast.success('Password reset successfully! You can now log in.');
        this.setMode('LOGIN');
      },
      error: (err) => {
        this.loading.set(false);
        this.toast.error(err?.error?.message ?? 'Failed to reset password. Check your OTP.');
      }
    });
  }
}
