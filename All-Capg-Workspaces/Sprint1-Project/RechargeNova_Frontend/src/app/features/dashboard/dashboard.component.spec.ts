import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { DashboardComponent } from './dashboard.component';
import { AuthService } from '../../core/services/auth.service';
import { RechargeApiService } from '../../core/services/recharge-api.service';
import { RechargeResponse, AuthResponse } from '../../shared/models/models';
import { environment } from '../../../environments/environment';

const BASE = environment.apiBaseUrl;

const mockUser = {
  id: 1, name: 'Sanjana Chandel', email: 'sanjana@example.com',
  role: 'ROLE_USER', phoneNumber: '9876543210', createdAt: '2024-01-01T00:00:00Z',
};

const makeRecharge = (id: number, status: 'SUCCESS' | 'FAILED', amount: number): RechargeResponse => ({
  id, userId: 1, operatorId: 2, planId: 3,
  mobileNumber: '9876543210', amount, status,
  createdAt: '2024-06-01T10:00:00Z', message: '',
  paymentMethod: 'UPI',
});

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let httpMock: HttpTestingController;
  let authService: AuthService;

  beforeEach(async () => {
    localStorage.clear();
    TestBed.resetTestingModule();
    await TestBed.configureTestingModule({
      imports: [DashboardComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
        RechargeApiService,
      ],
    }).compileComponents();

    authService = TestBed.inject(AuthService);
    authService.saveAuth({ token: 'jwt', user: mockUser as any });

    fixture   = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    httpMock  = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
    // Flush the HTTP call made in ngOnInit
    const req = httpMock.expectOne(`${BASE}/recharges/user/1`);
    req.flush([]);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
    TestBed.resetTestingModule();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // ─── Computed signals ─────────────────────────────────────────────────────
  it('should compute totalRecharges correctly', () => {
    component.recharges.set([makeRecharge(1, 'SUCCESS', 199), makeRecharge(2, 'FAILED', 99)]);
    expect(component.totalRecharges()).toBe(2);
  });

  it('should compute successCount correctly', () => {
    component.recharges.set([makeRecharge(1, 'SUCCESS', 199), makeRecharge(2, 'FAILED', 99)]);
    expect(component.successCount()).toBe(1);
  });

  it('should compute totalSpent from SUCCESS recharges only', () => {
    component.recharges.set([makeRecharge(1, 'SUCCESS', 199), makeRecharge(2, 'FAILED', 100)]);
    expect(component.totalSpent()).toBe('199');
  });

  it('should return last mobile number from first recharge', () => {
    component.recharges.set([makeRecharge(1, 'SUCCESS', 199)]);
    expect(component.lastMobile()).toBe('9876543210');
  });

  it('should return "—" for lastMobile when no recharges', () => {
    component.recharges.set([]);
    expect(component.lastMobile()).toBe('—');
  });

  it('should return only first 5 recharges for recentRecharges', () => {
    const many = Array.from({ length: 8 }, (_, i) => makeRecharge(i + 1, 'SUCCESS', 100));
    component.recharges.set(many);
    expect(component.recentRecharges().length).toBe(5);
  });

  // ─── greeting ─────────────────────────────────────────────────────────────
  it('should return a non-empty greeting', () => {
    const g = component.greeting();
    expect(['morning', 'afternoon', 'evening']).toContain(g);
  });

  // ─── firstName ────────────────────────────────────────────────────────────
  it('should return first word of user name', () => {
    expect(component.firstName()).toBe('Sanjana');
  });

  it('should return "User" when no user is logged in', () => {
    authService.logout();
    expect(component.firstName()).toBe('User');
  });

  // ─── statusBadge ──────────────────────────────────────────────────────────
  it('should return "badge-success" for SUCCESS status', () => {
    expect(component.statusBadge('SUCCESS')).toBe('badge-success');
  });

  it('should return "badge-error" for FAILED status', () => {
    expect(component.statusBadge('FAILED')).toBe('badge-error');
  });

  // ─── evening branch ────────────────────────────────────────────────────────
  it('should return "evening" when hour is 18', () => {
    vi.useFakeTimers();
    const date = new Date(2024, 1, 1, 18, 0, 0);
    vi.setSystemTime(date);
    expect(component.greeting()).toBe('evening');
    vi.useRealTimers();
  });

  it('should return "morning" when hour is 9', () => {
    vi.useFakeTimers();
    const date = new Date(2024, 1, 1, 9, 0, 0);
    vi.setSystemTime(date);
    expect(component.greeting()).toBe('morning');
    vi.useRealTimers();
  });

  it('should return "afternoon" when hour is 14', () => {
    vi.useFakeTimers();
    const date = new Date(2024, 1, 1, 14, 0, 0);
    vi.setSystemTime(date);
    expect(component.greeting()).toBe('afternoon');
    vi.useRealTimers();
  });
});

describe('DashboardComponent Initialization Error', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
        RechargeApiService,
      ],
    }).compileComponents();

    const auth = TestBed.inject(AuthService);
    auth.saveAuth({ token: 'jwt', user: mockUser as any });

    fixture   = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    httpMock  = TestBed.inject(HttpTestingController);
  });

  it('should set loading=false when API fails', () => {
    fixture.detectChanges();
    const req = httpMock.expectOne(`${BASE}/recharges/user/1`);
    req.flush({}, { status: 500, statusText: 'Error' });
    expect(component.loading()).toBe(false);
  });
});
