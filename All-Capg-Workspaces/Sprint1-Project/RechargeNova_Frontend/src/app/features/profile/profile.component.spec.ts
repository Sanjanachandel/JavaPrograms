import { vi } from 'vitest';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter, Router } from '@angular/router';
import { ProfileComponent } from './profile.component';
import { AuthService } from '../../core/services/auth.service';
import { UserApiService } from '../../core/services/user-api.service';
import { ToastService } from '../../core/services/toast.service';
import { environment } from '../../../environments/environment';

const BASE = environment.apiBaseUrl;

const mockUser = {
  id: 1, name: 'Sanjana Chandel', email: 'sanjana@example.com',
  role: 'ROLE_USER', phoneNumber: '9876543210',
  createdAt: '2024-03-15T00:00:00Z',
  profilePictureUrl: 'https://cdn.example.com/pic.jpg',
};

describe('ProfileComponent', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let httpMock: HttpTestingController;
  let authService: AuthService;

  beforeEach(async () => {
    localStorage.clear();
    TestBed.resetTestingModule();
    await TestBed.configureTestingModule({
      imports: [ProfileComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
        UserApiService,
        ToastService,
      ],
    }).compileComponents();

    authService = TestBed.inject(AuthService);
    authService.saveAuth({ token: 'jwt', user: mockUser as any });

    fixture   = TestBed.createComponent(ProfileComponent);
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

  // ─── initials() ──────────────────────────────────────────────────────────
  it('should return initials from user name "Sanjana Chandel" → "SC"', () => {
    expect(component.initials()).toBe('SC');
  });

  it('should return single initial for single word name', () => {
    authService.saveAuth({ token: 'jwt', user: { ...mockUser, name: 'Sanjana' } as any });
    // recreate fixture to pick up new user
    const f = TestBed.createComponent(ProfileComponent);
    f.detectChanges();
    expect(f.componentInstance.initials()).toBe('S');
  });

  it('should return "U" when user has no name', () => {
    authService.logout();
    expect(component.initials()).toBe('U');
  });

  it('should cap initials at 2 characters', () => {
    authService.saveAuth({ token: 'jwt', user: { ...mockUser, name: 'Abc Def Ghi' } as any });
    const f = TestBed.createComponent(ProfileComponent);
    f.detectChanges();
    expect(f.componentInstance.initials().length).toBeLessThanOrEqual(2);
  });

  // ─── memberSince() ───────────────────────────────────────────────────────
  it('should return formatted date string for memberSince', () => {
    const result = component.memberSince();
    expect(result).toContain('2024');
    expect(result).toMatch(/March|Mar/);
  });

  it('should return "—" for memberSince when no user', () => {
    authService.logout();
    expect(component.memberSince()).toBe('—');
  });

  // ─── onFileChange (no file selected) ─────────────────────────────────────
  it('should do nothing when no file is selected in onFileChange', () => {
    const event = { target: { files: [] } } as unknown as Event;
    expect(() => component.onFileChange(event)).not.toThrow();
    httpMock.expectNone(`${BASE}/users/profile/picture`);
  });

  // ─── onFileChange (with file) ─────────────────────────────────────────────
  it('should PUT profile picture and update user on success', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'success');
    const file = new File(['content'], 'avatar.png', { type: 'image/png' });
    const event = { target: { files: [file] } } as unknown as Event;
    component.onFileChange(event);
    const req = httpMock.expectOne(`${BASE}/users/profile/picture`);
    expect(req.request.method).toBe('PUT');
    req.flush({ profilePictureUrl: 'https://cdn.example.com/new.jpg' });
    expect(authService.currentUser()?.profilePictureUrl).toBe('https://cdn.example.com/new.jpg');
    expect(toast.success).toHaveBeenCalledWith('Profile picture updated! 📸');
  });

  it('should show error toast when profile picture upload fails', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    const file = new File(['content'], 'avatar.png', { type: 'image/png' });
    const event = { target: { files: [file] } } as unknown as Event;
    component.onFileChange(event);
    const req = httpMock.expectOne(`${BASE}/users/profile/picture`);
    req.flush({}, { status: 500, statusText: 'Server Error' });
    expect(toast.error).toHaveBeenCalled();
  });

  // ─── logout() ─────────────────────────────────────────────────────────────
  it('should call auth.logout() and navigate to login when logout() is called', () => {
    const router = TestBed.inject(Router);
    vi.spyOn(router, 'navigate');
    vi.spyOn(authService, 'logout');
    
    component.logout();
    
    expect(authService.logout).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/auth/login']);
  });

  // ─── onFileChange (no uid) ────────────────────────────────────────────────
  it('should return early if userId is missing', () => {
    vi.spyOn(authService, 'getUserId').mockReturnValue(null);
    const file = new File(['content'], 'avatar.png', { type: 'image/png' });
    const event = { target: { files: [file] } } as unknown as Event;
    component.onFileChange(event);
    httpMock.expectNone(`${BASE}/users/profile/picture`);
  });
});
