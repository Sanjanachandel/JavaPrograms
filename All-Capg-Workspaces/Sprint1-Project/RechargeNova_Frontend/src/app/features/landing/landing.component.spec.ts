import { vi } from 'vitest';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter, Router } from '@angular/router';
import { LandingComponent } from './landing.component';
import { AuthService } from '../../core/services/auth.service';
import { OperatorApiService } from '../../core/services/operator-api.service';
import { UserApiService } from '../../core/services/user-api.service';
import { RechargeApiService } from '../../core/services/recharge-api.service';
import { ThemeService } from '../../core/services/theme.service';
import { OperatorDto } from '../../shared/models/models';
import { environment } from '../../../environments/environment';

const BASE = environment.apiBaseUrl;

const mockOperators: OperatorDto[] = [
  { id: 1, name: 'Jio',    type: 'Prepaid', circle: 'All India', plans: [{ id: 1, operatorId: 1, amount: 199, validity: '28d', description: 'All-in-One' }] },
  { id: 2, name: 'Airtel', type: 'Prepaid', circle: 'All India', plans: [{ id: 2, operatorId: 2, amount: 299, validity: '28d', description: 'Data' }, { id: 3, operatorId: 2, amount: 99, validity: '14d', description: 'Talktime' }] },
];

describe('LandingComponent', () => {
  let component: LandingComponent;
  let fixture: ComponentFixture<LandingComponent>;
  let httpMock: HttpTestingController;
  let authService: AuthService;

  beforeEach(async () => {
    localStorage.clear();
    TestBed.resetTestingModule();
    document.body.classList.remove('light-theme', 'dark-theme');

    await TestBed.configureTestingModule({
      imports: [LandingComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
        OperatorApiService,
        UserApiService,
        RechargeApiService,
        ThemeService,
      ],
    }).compileComponents();

    authService = TestBed.inject(AuthService);
    fixture     = TestBed.createComponent(LandingComponent);
    component   = fixture.componentInstance;
    httpMock    = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
    document.body.classList.remove('light-theme', 'dark-theme');
    TestBed.resetTestingModule();
  });

  const flushAll = (
    ops = mockOperators, 
    opCount = 10, 
    planCount = 50, 
    userCount = 100, 
    rechCount = 500
  ) => {
    // Note: The order of requests in the component matches the order of flushes here
    httpMock.expectOne(`${BASE}/operators/count`).flush(opCount);
    httpMock.expectOne(`${BASE}/operators/plans/count`).flush(planCount);
    httpMock.expectOne(`${BASE}/users/count`).flush(userCount);
    httpMock.expectOne(`${BASE}/recharges/count`).flush(rechCount);
    httpMock.expectOne(`${BASE}/operators`).flush(ops);
  };

  // ─── Creation ─────────────────────────────────────────────────────────────
  it('should create', () => {
    flushAll();
    expect(component).toBeTruthy();
  });

  it('should define 6 features', () => {
    flushAll();
    expect(component.features.length).toBe(6);
  });

  it('should define 4 steps', () => {
    flushAll();
    expect(component.steps.length).toBe(4);
  });

  // ─── ngOnInit stats ───────────────────────────────────────────────────────
  it('should set operatorCount from API response', () => {
    flushAll(mockOperators, 5);
    expect(component.operatorCount()).toBe(5);
  });

  it('should set planCount from API response', () => {
    flushAll(mockOperators, 5, 25);
    expect(component.planCount()).toBe(25);
  });

  it('should set userCount from API response', () => {
    flushAll(mockOperators, 5, 25, 123);
    expect(component.userCount()).toBe(123);
  });

  it('should set rechargeCount from API response', () => {
    flushAll(mockOperators, 5, 25, 123, 999);
    expect(component.rechargeCount()).toBe(999);
  });

  it('should cap supportedOperators to 8 unique entries', () => {
    const many = Array.from({ length: 12 }, (_, i) => ({
      id: i, name: `Op${i}`, type: 'Prepaid', circle: 'India', plans: [],
    }));
    flushAll(many);
    expect(component.supportedOperators().length).toBeLessThanOrEqual(8);
  });

  // ─── getIconForOperator ───────────────────────────────────────────────────
  it('should return 📱 for Jio', () => {
    flushAll();
    expect(component.getIconForOperator('Jio')).toBe('📱');
  });

  it('should return 📡 for Airtel', () => {
    flushAll();
    expect(component.getIconForOperator('Airtel')).toBe('📡');
  });

  it('should return 📶 for Vi', () => {
    flushAll();
    expect(component.getIconForOperator('Vi')).toBe('📶');
  });

  it('should return 📠 for BSNL', () => {
    flushAll();
    expect(component.getIconForOperator('BSNL')).toBe('📠');
  });

  it('should return 📞 for MTNL', () => {
    flushAll();
    expect(component.getIconForOperator('MTNL')).toBe('📞');
  });

  it('should return 💫 for Tata', () => {
    flushAll();
    expect(component.getIconForOperator('Tata')).toBe('💫');
  });

  it('should return ☀️ for Sun', () => {
    flushAll();
    expect(component.getIconForOperator('Sun')).toBe('☀️');
  });

  it('should return 🔌 for unknown operator', () => {
    flushAll();
    expect(component.getIconForOperator('Unknown Telecom')).toBe('🔌');
  });

  // ─── onLogoClick ─────────────────────────────────────────────────────────
  it('should navigate to / when not logged in on logo click', () => {
    flushAll();
    const router = TestBed.inject(Router);
    vi.spyOn(router, 'navigate');
    component.onLogoClick();
    expect(router.navigate).toHaveBeenCalledWith(['/']);
  });

  it('should navigate to /dashboard when logged in on logo click', () => {
    flushAll();
    authService.saveAuth({ token: 'jwt', user: { id: 1, name: 'Sanjana', email: 'x@x.com', role: 'ROLE_USER', phoneNumber: '9876543210', createdAt: '2024-01-01T00:00:00Z' } });
    const router = TestBed.inject(Router);
    vi.spyOn(router, 'navigate');
    component.onLogoClick();
    expect(router.navigate).toHaveBeenCalledWith(['/dashboard']);
  });
});

describe('LandingComponent Error Handling', () => {
  let component: LandingComponent;
  let fixture: ComponentFixture<LandingComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LandingComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
        OperatorApiService,
        UserApiService,
        RechargeApiService,
        ThemeService,
      ],
    }).compileComponents();

    fixture   = TestBed.createComponent(LandingComponent);
    component = fixture.componentInstance;
    httpMock  = TestBed.inject(HttpTestingController);
    // Note: NOT calling detectChanges here to control the flushes
  });

  it('should default to 0 and [] when APIs fail', () => {
    fixture.detectChanges(); // Trigger ngOnInit

    httpMock.expectOne(`${BASE}/operators/count`).flush({}, { status: 500, statusText: 'Error' });
    httpMock.expectOne(`${BASE}/operators/plans/count`).flush({}, { status: 500, statusText: 'Error' });
    httpMock.expectOne(`${BASE}/users/count`).flush({}, { status: 500, statusText: 'Error' });
    httpMock.expectOne(`${BASE}/recharges/count`).flush({}, { status: 500, statusText: 'Error' });
    httpMock.expectOne(`${BASE}/operators`).flush({}, { status: 500, statusText: 'Error' });

    expect(component.operatorCount()).toBe(0);
    expect(component.planCount()).toBe(0);
    expect(component.userCount()).toBe(0);
    expect(component.rechargeCount()).toBe(0);
    expect(component.supportedOperators().length).toBe(0);
  });
});
