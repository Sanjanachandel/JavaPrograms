import { Component, inject, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ThemeService } from '../../../core/services/theme.service';
import { Router } from '@angular/router';
import { LogoutModalComponent } from '../logout-modal/logout-modal.component';

interface NavLink {
  path: string;
  label: string;
  icon: string;
  role?: string;
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, LogoutModalComponent],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  auth     = inject(AuthService);
  themeSvc = inject(ThemeService);
  router   = inject(Router);

  navLinks: NavLink[] = [
    { path: '/dashboard',          label: 'Dashboard',   icon: '🏠', role: 'ROLE_USER' },
    { path: '/admin',              label: 'Admin Panel', icon: '🛡️', role: 'ROLE_ADMIN' },
    { path: '/admin/users',        label: 'Users',       icon: '👥', role: 'ROLE_ADMIN' },
    { path: '/admin/operators',    label: 'Operators',   icon: '📡', role: 'ROLE_ADMIN' },
    { path: '/admin/transactions', label: 'History',     icon: '📋', role: 'ROLE_ADMIN' },
    { path: '/recharge',           label: 'Recharge',    icon: '⚡', role: 'ROLE_USER' },
    { path: '/history',            label: 'History',     icon: '📋', role: 'ROLE_USER' },
    { path: '/profile',            label: 'Profile',     icon: '👤', role: 'ROLE_USER' },
  ];

  filteredNavLinks = computed(() => {
    const user = this.auth.currentUser();
    return this.navLinks.filter(link => !link.role || link.role === user?.role);
  });

  isMobileMenuOpen = signal(false);

  toggleMobileMenu(): void {
    this.isMobileMenuOpen.update(v => !v);
  }

  closeMobileMenu(): void {
    if (this.isMobileMenuOpen()) {
      this.isMobileMenuOpen.set(false);
    }
  }

  userInitial(): string {
    const name = this.auth.currentUser()?.name ?? 'U';
    return name.charAt(0).toUpperCase();
  }

  dashboardLink = computed(() => {
    const user = this.auth.currentUser();
    return (user?.role === 'ADMIN' || user?.role === 'ROLE_ADMIN') ? '/admin' : '/dashboard';
  });

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
