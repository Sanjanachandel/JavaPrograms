import { vi } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { provideRouter } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { authGuard, guestGuard, adminGuard, userGuard } from './auth.guard';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

const mockRoute   = {} as ActivatedRouteSnapshot;
const mockState   = {} as RouterStateSnapshot;

const mockUser = {
  id: 1, name: 'Sanjana', email: 'sanjana@example.com',
  role: 'ROLE_USER', phoneNumber: '9876543210', createdAt: '2024-01-01T00:00:00Z',
};

const mockAdmin = {
  id: 2, name: 'Admin', email: 'admin@example.com',
  role: 'ADMIN', phoneNumber: '9999999999', createdAt: '2024-01-01T00:00:00Z',
};

function runGuard(guardFn: Function): boolean | any {
  return TestBed.runInInjectionContext(() => guardFn(mockRoute, mockState));
}

describe('Auth Guards', () => {
  let authService: AuthService;
  let router: Router;

  beforeEach(() => {
    localStorage.clear();
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        AuthService,
      ],
    });
    authService = TestBed.inject(AuthService);
    router      = TestBed.inject(Router);
    vi.spyOn(router, 'navigate');
  });

  afterEach(() => {
    localStorage.clear();
    TestBed.resetTestingModule();
  });

  // ══════════════════════════════════════════════
  // authGuard
  // ══════════════════════════════════════════════
  describe('authGuard', () => {
    it('should return true when user is logged in', () => {
      authService.saveAuth({ token: 'jwt', user: mockUser as any });
      const result = runGuard(authGuard);
      expect(result).toBe(true);
    });

    it('should navigate to /auth/login when not logged in', () => {
      const result = runGuard(authGuard);
      expect(result).toBe(false);
      expect(router.navigate).toHaveBeenCalledWith(['/auth/login']);
    });
  });

  // ══════════════════════════════════════════════
  // guestGuard
  // ══════════════════════════════════════════════
  describe('guestGuard', () => {
    it('should return true when user is NOT logged in (guest)', () => {
      const result = runGuard(guestGuard);
      expect(result).toBe(true);
    });

    it('should redirect ROLE_USER to /dashboard', () => {
      authService.saveAuth({ token: 'jwt', user: mockUser as any });
      const result = runGuard(guestGuard);
      expect(result).toBe(false);
      expect(router.navigate).toHaveBeenCalledWith(['/dashboard']);
    });

    it('should redirect ADMIN to /admin', () => {
      authService.saveAuth({ token: 'jwt', user: mockAdmin as any });
      const result = runGuard(guestGuard);
      expect(result).toBe(false);
      expect(router.navigate).toHaveBeenCalledWith(['/admin']);
    });

    it('should redirect ROLE_ADMIN to /admin', () => {
      const roleAdmin = { ...mockAdmin, role: 'ROLE_ADMIN' };
      authService.saveAuth({ token: 'jwt', user: roleAdmin as any });
      const result = runGuard(guestGuard);
      expect(result).toBe(false);
      expect(router.navigate).toHaveBeenCalledWith(['/admin']);
    });
  });

  // ══════════════════════════════════════════════
  // adminGuard
  // ══════════════════════════════════════════════
  describe('adminGuard', () => {
    it('should return true for logged-in ADMIN user', () => {
      authService.saveAuth({ token: 'jwt', user: mockAdmin as any });
      const result = runGuard(adminGuard);
      expect(result).toBe(true);
    });

    it('should return true for logged-in ROLE_ADMIN user', () => {
      const roleAdmin = { ...mockAdmin, role: 'ROLE_ADMIN' };
      authService.saveAuth({ token: 'jwt', user: roleAdmin as any });
      const result = runGuard(adminGuard);
      expect(result).toBe(true);
    });

    it('should navigate to /forbidden for regular ROLE_USER', () => {
      authService.saveAuth({ token: 'jwt', user: mockUser as any });
      const result = runGuard(adminGuard);
      expect(result).toBe(false);
      expect(router.navigate).toHaveBeenCalledWith(['/forbidden']);
    });

    it('should navigate to /forbidden when not logged in', () => {
      const result = runGuard(adminGuard);
      expect(result).toBe(false);
      expect(router.navigate).toHaveBeenCalledWith(['/forbidden']);
    });
  });

  // ══════════════════════════════════════════════
  // userGuard
  // ══════════════════════════════════════════════
  describe('userGuard', () => {
    it('should return true for logged-in ROLE_USER', () => {
      authService.saveAuth({ token: 'jwt', user: mockUser as any });
      const result = runGuard(userGuard);
      expect(result).toBe(true);
    });

    it('should navigate to /forbidden for ADMIN trying to access user route', () => {
      authService.saveAuth({ token: 'jwt', user: mockAdmin as any });
      const result = runGuard(userGuard);
      expect(result).toBe(false);
      expect(router.navigate).toHaveBeenCalledWith(['/forbidden']);
    });

    it('should navigate to /forbidden when not logged in', () => {
      const result = runGuard(userGuard);
      expect(result).toBe(false);
      expect(router.navigate).toHaveBeenCalledWith(['/forbidden']);
    });
  });
});

