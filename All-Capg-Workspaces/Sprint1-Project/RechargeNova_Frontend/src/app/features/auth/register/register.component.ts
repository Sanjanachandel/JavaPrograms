import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { UserApiService } from '../../../core/services/user-api.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { ThemeService } from '../../../core/services/theme.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  private fb      = inject(FormBuilder);
  private userApi = inject(UserApiService);
  private authSvc = inject(AuthService);
  private toast   = inject(ToastService);
  private router  = inject(Router);
  themeSvc        = inject(ThemeService);

  loading      = signal(false);
  showPassword = signal(false);

  form = this.fb.group({
    name:        ['', [Validators.required, Validators.pattern(/^[A-Za-z ]+$/)]],
    email:       ['', [Validators.required, Validators.email]],
    phoneNumber: ['', [Validators.required, Validators.pattern(/^[6-9]\d{9}$/)]],
    password:    ['', [Validators.required, Validators.minLength(6)]]
  });

  isInvalid(field: string): boolean {
    const c = this.form.get(field)!;
    return c.invalid && (c.dirty || c.touched);
  }

  strengthPct(): number {
    const pw = this.form.get('password')?.value ?? '';
    if (!pw) return 0;
    let score = 0;
    if (pw.length >= 6)  score += 30;
    if (pw.length >= 10) score += 20;
    if (/[A-Z]/.test(pw)) score += 20;
    if (/[0-9]/.test(pw)) score += 15;
    if (/[^A-Za-z0-9]/.test(pw)) score += 15;
    return Math.min(score, 100);
  }

  strengthLabel(): string {
    const pct = this.strengthPct();
    if (pct < 40) return 'weak';
    if (pct < 70) return 'fair';
    return 'strong';
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.loading.set(true);

    const { name, email, password, phoneNumber } = this.form.value;
    this.userApi.register({ name: name!, email: email!, password: password!, phoneNumber: phoneNumber! })
      .subscribe({
        next: () => {
          this.toast.success('Account created! Please sign in.');
          this.router.navigate(['/auth/login']);
        },
        error: (err) => {
          this.toast.error(err?.error?.message ?? 'Registration failed. Please try again.');
          this.loading.set(false);
        }
      });
  }
}
