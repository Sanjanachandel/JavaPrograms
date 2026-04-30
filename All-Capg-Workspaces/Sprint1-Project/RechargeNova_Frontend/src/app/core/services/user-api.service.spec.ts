import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { UserApiService } from './user-api.service';
import { AuthService } from './auth.service';
import { environment } from '../../../environments/environment';
import { AuthResponse, UserResponse } from '../../shared/models/models';

const BASE = environment.apiBaseUrl;

const mockUser: UserResponse = {
  id: 1,
  name: 'Sanjana',
  email: 'sanjana@example.com',
  role: 'ROLE_USER',
  phoneNumber: '9876543210',
  createdAt: '2024-01-01T00:00:00Z',
};

const mockAuthResponse: AuthResponse = {
  token: 'jwt-token',
  user: mockUser,
};

describe('UserApiService', () => {
  let service: UserApiService;
  let httpMock: HttpTestingController;
  let authService: AuthService;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        UserApiService,
        AuthService,
      ],
    });
    service     = TestBed.inject(UserApiService);
    httpMock    = TestBed.inject(HttpTestingController);
    authService = TestBed.inject(AuthService);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
    TestBed.resetTestingModule();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // ─── register ───────────────────────────────────────────────────────────
  it('should POST to /users/register', () => {
    const payload = { name: 'Sanjana', email: 'sanjana@example.com', password: 'pass123', phoneNumber: '9876543210' };
    service.register(payload).subscribe(res => {
      expect(res).toEqual(mockUser);
    });
    const req = httpMock.expectOne(`${BASE}/users/register`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush(mockUser);
  });

  // ─── login ──────────────────────────────────────────────────────────────
  it('should POST to /users/login', () => {
    const payload = { email: 'sanjana@example.com', password: 'pass123' };
    service.login(payload).subscribe(res => {
      expect(res).toEqual(mockAuthResponse);
    });
    const req = httpMock.expectOne(`${BASE}/users/login`);
    expect(req.request.method).toBe('POST');
    req.flush(mockAuthResponse);
  });

  // ─── getUserById ─────────────────────────────────────────────────────────
  it('should GET /users/1 with Authorization header', () => {
    authService.saveAuth(mockAuthResponse);
    service.getUserById(1).subscribe(res => {
      expect(res).toEqual(mockUser);
    });
    const req = httpMock.expectOne(`${BASE}/users/1`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe('Bearer jwt-token');
    req.flush(mockUser);
  });

  // ─── getAllUsers ─────────────────────────────────────────────────────────
  it('should GET /users with Authorization header', () => {
    authService.saveAuth(mockAuthResponse);
    service.getAllUsers().subscribe(res => {
      expect(res.content).toEqual([mockUser]);
    });
    const req = httpMock.expectOne(`${BASE}/users?page=0&size=10`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe('Bearer jwt-token');
    req.flush({ content: [mockUser], totalElements: 1, totalPages: 1, number: 0 });
  });

  // ─── forgotPassword ──────────────────────────────────────────────────────
  it('should POST to /users/forgot-password with email', () => {
    service.forgotPassword('sanjana@example.com').subscribe(res => {
      expect(res.message).toBe('OTP sent');
    });
    const req = httpMock.expectOne(`${BASE}/users/forgot-password`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ email: 'sanjana@example.com' });
    req.flush({ message: 'OTP sent' });
  });

  // ─── resetPassword ───────────────────────────────────────────────────────
  it('should POST to /users/reset-password with otp payload', () => {
    const payload = { email: 'sanjana@example.com', otp: '123456', newPassword: 'newPass@1' };
    service.resetPassword(payload).subscribe(res => {
      expect(res.message).toBe('Password reset');
    });
    const req = httpMock.expectOne(`${BASE}/users/reset-password`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush({ message: 'Password reset' });
  });

  // ─── uploadProfilePicture ─────────────────────────────────────────────
  it('should PUT to /users/profile/picture with X-User-Id header', () => {
    const file = new File(['content'], 'avatar.png', { type: 'image/png' });
    service.uploadProfilePicture(1, file).subscribe(res => {
      expect(res.profilePictureUrl).toBe('https://cdn.example.com/pic.jpg');
    });
    const req = httpMock.expectOne(`${BASE}/users/profile/picture`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.headers.get('X-User-Id')).toBe('1');
    req.flush({ profilePictureUrl: 'https://cdn.example.com/pic.jpg' });
  });

  it('should use empty string if token is null in getHeaders', () => {
    const auth = TestBed.inject(AuthService);
    vi.spyOn(auth, 'token').mockReturnValue(null);
    service.getUserById(1).subscribe();
    const req = httpMock.expectOne(`${BASE}/users/1`);
    expect(req.request.headers.get('Authorization')).toBe('Bearer ');
  });
});


