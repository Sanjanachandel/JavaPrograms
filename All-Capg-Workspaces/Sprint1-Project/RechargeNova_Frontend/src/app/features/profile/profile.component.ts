import { Component, inject, signal, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UserApiService } from '../../core/services/user-api.service';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';
import { LogoutModalComponent } from '../../shared/components/logout-modal/logout-modal.component';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, LogoutModalComponent],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  auth      = inject(AuthService);
  private userApi   = inject(UserApiService);
  private toast     = inject(ToastService);
  private router    = inject(Router);

  ngOnInit(): void {}

  initials(): string {
    const name = this.auth.currentUser()?.name ?? 'U';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }

  memberSince(): string {
    const d = this.auth.currentUser()?.createdAt;
    if (!d) return '—';
    return new Date(d).toLocaleDateString('en-IN', { month: 'long', year: 'numeric' });
  }

  onFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;
    const file = input.files[0];
    const uid  = this.auth.getUserId();
    if (!uid) return;

    this.userApi.uploadProfilePicture(uid, file).subscribe({
      next: (res: any) => {
        const updated = { ...this.auth.currentUser()!, profilePictureUrl: res.profilePictureUrl };
        this.auth.updateUser(updated);
        this.toast.success('Profile picture updated! 📸');
      },
      error: () => this.toast.error('Failed to upload picture. Check Cloudinary config.')
    });
  }

  isLogoutModalVisible = signal(false);

  logout(): void {
    this.isLogoutModalVisible.set(true);
  }

  onLogoutConfirm(): void {
    this.isLogoutModalVisible.set(false);
    this.auth.logout();
    this.router.navigate(['/auth/login']);
  }

  onLogoutCancel(): void {
    this.isLogoutModalVisible.set(false);
  }
}
