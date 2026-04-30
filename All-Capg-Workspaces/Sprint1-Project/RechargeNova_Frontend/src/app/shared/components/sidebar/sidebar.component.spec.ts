import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SidebarComponent } from './sidebar.component';
import { AuthService } from '../../../core/services/auth.service';
import { ThemeService } from '../../../core/services/theme.service';
import { provideRouter, Router } from '@angular/router';
import { vi } from 'vitest';
import { signal } from '@angular/core';

describe('SidebarComponent', () => {
  let component: SidebarComponent;
  let fixture: ComponentFixture<SidebarComponent>;
  let authServiceMock: any;
  let themeServiceMock: any;
  let router: Router;

  const mockUser = {
    id: 1,
    name: 'John Doe',
    email: 'john@example.com',
    role: 'ROLE_USER',
    profilePictureUrl: ''
  };

  const mockAdmin = {
    id: 2,
    name: 'Admin User',
    email: 'admin@example.com',
    role: 'ROLE_ADMIN',
    profilePictureUrl: ''
  };

  beforeEach(async () => {
    authServiceMock = {
      currentUser: signal(null),
      logout: vi.fn()
    };

    themeServiceMock = {
      isDarkMode: signal(false),
      theme: signal('light-theme')
    };

    await TestBed.configureTestingModule({
      imports: [SidebarComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: ThemeService, useValue: themeServiceMock },
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SidebarComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should filter links for a regular user', () => {
    authServiceMock.currentUser.set(mockUser);
    fixture.detectChanges();
    
    const links = component.filteredNavLinks();
    expect(links.some(l => l.path === '/dashboard')).toBe(true);
    expect(links.some(l => l.path === '/admin')).toBe(false);
  });

  it('should filter links for an admin', () => {
    authServiceMock.currentUser.set(mockAdmin);
    fixture.detectChanges();
    
    const links = component.filteredNavLinks();
    expect(links.some(l => l.path === '/admin')).toBe(true);
    // Note: In your SidebarComponent, '/dashboard' also has ROLE_USER. 
    // If you want it exclusive, you'd need to change the component logic.
    // Based on current logic link.role === user?.role
  });

  it('should return correct userInitial', () => {
    authServiceMock.currentUser.set(mockUser);
    expect(component.userInitial()).toBe('J');
    
    authServiceMock.currentUser.set(null);
    expect(component.userInitial()).toBe('U');
  });

  it('should return correct dashboardLink for user', () => {
    authServiceMock.currentUser.set(mockUser);
    expect(component.dashboardLink()).toBe('/dashboard');
  });

  it('should return correct dashboardLink for admin', () => {
    authServiceMock.currentUser.set(mockAdmin);
    expect(component.dashboardLink()).toBe('/admin');
  });

  it('should show logout modal on logout()', () => {
    component.logout();
    expect(component.isLogoutModalVisible()).toBe(true);
  });

  it('should logout and navigate to login on onLogoutConfirm()', () => {
    const navigateSpy = vi.spyOn(router, 'navigate');
    component.onLogoutConfirm();
    expect(component.isLogoutModalVisible()).toBe(false);
    expect(authServiceMock.logout).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/auth/login']);
  });

  it('should hide modal on onLogoutCancel()', () => {
    component.isLogoutModalVisible.set(true);
    component.onLogoutCancel();
    expect(component.isLogoutModalVisible()).toBe(false);
  });
});
