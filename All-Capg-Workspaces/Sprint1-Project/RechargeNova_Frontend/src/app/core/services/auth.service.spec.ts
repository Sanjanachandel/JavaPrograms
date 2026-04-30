import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';
import { AuthResponse, UserResponse } from '../../shared/models/models';

const mockUser: UserResponse = {
  id: 1,
  name: 'Sanjana',
  email: 'sanjana@example.com',
  role: 'ROLE_USER',
  phoneNumber: '9876543210',
  createdAt: '2024-01-01T00:00:00Z',
  profilePictureUrl: 'https://example.com/pic.jpg',
};

const mockAdmin: UserResponse = {
  id: 2,
  name: 'Admin',
  email: 'admin@example.com',
  role: 'ADMIN',
  phoneNumber: '9999999999',
  createdAt: '2024-01-01T00:00:00Z',
};

const mockAuth: AuthResponse = {
  token: 'mock-jwt-token',
  user: mockUser,
};

describe('AuthService', () => {
  let service: AuthService;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthService);
  });

  afterEach(() => {
    localStorage.clear();
    TestBed.resetTestingModule();
  });

  // ─── Creation ───────────────────────────────────────────────────────────
  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // ─── Initial state (empty localStorage) ─────────────────────────────────
  it('should have isLoggedIn as false when no token in localStorage', () => {
    expect(service.isLoggedIn()).toBeFalsy();
  });

  it('should have currentUser as null when no user in localStorage', () => {
    expect(service.currentUser()).toBeNull();
  });

  it('should have token as null when no token in localStorage', () => {
    expect(service.token()).toBeNull();
  });

  // ─── saveAuth ───────────────────────────────────────────────────────────
  it('should save token and user to localStorage on saveAuth', () => {
    service.saveAuth(mockAuth);
    expect(localStorage.getItem('rn_token')).toBe('mock-jwt-token');
    expect(JSON.parse(localStorage.getItem('rn_user')!)).toEqual(mockUser);
  });

  it('should update isLoggedIn to true after saveAuth', () => {
    service.saveAuth(mockAuth);
    expect(service.isLoggedIn()).toBeTruthy();
  });

  it('should update currentUser signal after saveAuth', () => {
    service.saveAuth(mockAuth);
    expect(service.currentUser()).toEqual(mockUser);
  });

  it('should update token signal after saveAuth', () => {
    service.saveAuth(mockAuth);
    expect(service.token()).toBe('mock-jwt-token');
  });

  // ─── updateUser ─────────────────────────────────────────────────────────
  it('should update user in localStorage and signal on updateUser', () => {
    service.saveAuth(mockAuth);
    const updatedUser: UserResponse = { ...mockUser, name: 'Updated Sanjana' };
    service.updateUser(updatedUser);
    expect(service.currentUser()).toEqual(updatedUser);
    expect(JSON.parse(localStorage.getItem('rn_user')!)).toEqual(updatedUser);
  });

  // ─── logout ─────────────────────────────────────────────────────────────
  it('should clear token and user from localStorage on logout', () => {
    service.saveAuth(mockAuth);
    service.logout();
    expect(localStorage.getItem('rn_token')).toBeNull();
    expect(localStorage.getItem('rn_user')).toBeNull();
  });

  it('should set isLoggedIn to false after logout', () => {
    service.saveAuth(mockAuth);
    service.logout();
    expect(service.isLoggedIn()).toBeFalsy();
  });

  it('should set currentUser to null after logout', () => {
    service.saveAuth(mockAuth);
    service.logout();
    expect(service.currentUser()).toBeNull();
  });

  it('should set token to null after logout', () => {
    service.saveAuth(mockAuth);
    service.logout();
    expect(service.token()).toBeNull();
  });

  // ─── getUserId ──────────────────────────────────────────────────────────
  it('should return null from getUserId when not logged in', () => {
    expect(service.getUserId()).toBeNull();
  });

  it('should return user id from getUserId when logged in', () => {
    service.saveAuth(mockAuth);
    expect(service.getUserId()).toBe(1);
  });

  // ─── getRole ────────────────────────────────────────────────────────────
  it('should return null from getRole when not logged in', () => {
    expect(service.getRole()).toBeNull();
  });

  it('should return ROLE_USER from getRole for regular user', () => {
    service.saveAuth(mockAuth);
    expect(service.getRole()).toBe('ROLE_USER');
  });

  it('should return ADMIN from getRole for admin user', () => {
    service.saveAuth({ token: 'admin-token', user: mockAdmin });
    expect(service.getRole()).toBe('ADMIN');
  });

  // ─── Persistence from localStorage ──────────────────────────────────────
  it('should restore token from localStorage on service init', () => {
    localStorage.setItem('rn_token', 'persisted-token');
    localStorage.setItem('rn_user', JSON.stringify(mockUser));
    // Re-create the service to simulate page reload
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({});
    const freshService = TestBed.inject(AuthService);
    expect(freshService.token()).toBe('persisted-token');
    expect(freshService.isLoggedIn()).toBeTruthy();
  });

  it('should handle corrupt JSON in localStorage gracefully', () => {
    localStorage.setItem('rn_token', 'some-token');
    localStorage.setItem('rn_user', 'NOT_VALID_JSON');
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({});
    const freshService = TestBed.inject(AuthService);
    expect(freshService.currentUser()).toBeNull();
  });
});
