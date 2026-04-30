import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { RechargeApiService } from './recharge-api.service';
import { AuthService } from './auth.service';
import { environment } from '../../../environments/environment';
import { RechargeRequest, RechargeResponse, AuthResponse, UserResponse } from '../../shared/models/models';

const BASE = environment.apiBaseUrl;

const mockUser: UserResponse = {
  id: 1,
  name: 'Sanjana',
  email: 'sanjana@example.com',
  role: 'ROLE_USER',
  phoneNumber: '9876543210',
  createdAt: '2024-01-01T00:00:00Z',
};

const mockAuth: AuthResponse = { token: 'test-jwt', user: mockUser };

const mockRecharge: RechargeResponse = {
  id: 10,
  userId: 1,
  operatorId: 2,
  planId: 3,
  mobileNumber: '9876543210',
  amount: 299,
  status: 'SUCCESS',
  paymentMethod: 'UPI',
  createdAt: '2024-06-01T10:00:00Z',
  message: 'Recharge successful',
};

describe('RechargeApiService', () => {
  let service: RechargeApiService;
  let httpMock: HttpTestingController;
  let authService: AuthService;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        RechargeApiService,
        AuthService,
      ],
    });
    service     = TestBed.inject(RechargeApiService);
    httpMock    = TestBed.inject(HttpTestingController);
    authService = TestBed.inject(AuthService);
    authService.saveAuth(mockAuth);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
    TestBed.resetTestingModule();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // ─── initiateRecharge ────────────────────────────────────────────────────
  it('should POST to /recharges with Authorization and X-User-Id headers', () => {
    const payload: RechargeRequest = {
      operatorId: 2,
      planId: 3,
      mobileNumber: '9876543210',
      paymentMethod: 'UPI',
    };
    service.initiateRecharge(1, payload).subscribe(res => {
      expect(res).toEqual(mockRecharge);
    });
    const req = httpMock.expectOne(`${BASE}/recharges`);
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.get('Authorization')).toBe('Bearer test-jwt');
    expect(req.request.headers.get('X-User-Id')).toBe('1');
    expect(req.request.body).toEqual(payload);
    req.flush(mockRecharge);
  });

  // ─── getRechargeById ────────────────────────────────────────────────────
  it('should GET /recharges/10 with Authorization header', () => {
    service.getRechargeById(10).subscribe(res => {
      expect(res).toEqual(mockRecharge);
    });
    const req = httpMock.expectOne(`${BASE}/recharges/10`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe('Bearer test-jwt');
    req.flush(mockRecharge);
  });

  // ─── getRechargesByUserId ────────────────────────────────────────────────
  it('should GET /recharges/user/1 with Authorization header', () => {
    service.getRechargesByUserId(1).subscribe(res => {
      expect(res).toEqual([mockRecharge]);
    });
    const req = httpMock.expectOne(`${BASE}/recharges/user/1`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe('Bearer test-jwt');
    req.flush([mockRecharge]);
  });

  // ─── getAllRecharges ──────────────────────────────────────────────────────
  it('should GET /recharges with Authorization header', () => {
    service.getAllRecharges().subscribe(res => {
      expect(res).toEqual([mockRecharge]);
    });
    const req = httpMock.expectOne(`${BASE}/recharges`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe('Bearer test-jwt');
    req.flush([mockRecharge]);
  });

  // ─── No token scenario ───────────────────────────────────────────────────
  it('should still make request with empty Bearer when not logged in', () => {
    authService.logout();
    service.getAllRecharges().subscribe();
    const req = httpMock.expectOne(`${BASE}/recharges`);
    expect(req.request.headers.get('Authorization')).toBe('Bearer ');
    req.flush([]);
  });
});
