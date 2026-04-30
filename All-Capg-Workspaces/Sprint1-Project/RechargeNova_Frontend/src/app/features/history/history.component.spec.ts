import { vi } from 'vitest';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { HistoryComponent } from './history.component';
import { AuthService } from '../../core/services/auth.service';
import { RechargeApiService } from '../../core/services/recharge-api.service';
import { ToastService } from '../../core/services/toast.service';
import { RechargeResponse } from '../../shared/models/models';
import { environment } from '../../../environments/environment';

const BASE = environment.apiBaseUrl;

const mockUser = {
  id: 1, name: 'Sanjana', email: 'sanjana@example.com',
  role: 'ROLE_USER', phoneNumber: '9876543210', createdAt: '2024-01-01T00:00:00Z',
};

const makeRecharge = (id: number, status: 'SUCCESS' | 'FAILED', amount: number): RechargeResponse => ({
  id, userId: 1, operatorId: 2, planId: 3,
  mobileNumber: '9876543210', amount, status,
  createdAt: '2024-06-01T10:00:00Z', message: '',
});

describe('HistoryComponent', () => {
  let component: HistoryComponent;
  let fixture: ComponentFixture<HistoryComponent>;
  let httpMock: HttpTestingController;
  let authService: AuthService;

  beforeEach(async () => {
    localStorage.clear();
    TestBed.resetTestingModule();
    await TestBed.configureTestingModule({
      imports: [HistoryComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
        RechargeApiService,
        ToastService,
      ],
    }).compileComponents();

    authService = TestBed.inject(AuthService);
    authService.saveAuth({ token: 'jwt', user: mockUser as any });

    fixture   = TestBed.createComponent(HistoryComponent);
    component = fixture.componentInstance;
    httpMock  = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
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

  it('should have default filter as ALL', () => {
    expect(component.activeFilter()).toBe('ALL');
  });

  it('should define 3 filter options', () => {
    expect(component.filters.length).toBe(3);
  });

  // ─── filtered computed ────────────────────────────────────────────────────
  it('should return all recharges when filter is ALL', () => {
    component.recharges.set([makeRecharge(1, 'SUCCESS', 199), makeRecharge(2, 'FAILED', 99)]);
    component.activeFilter.set('ALL');
    expect(component.filtered().length).toBe(2);
  });

  it('should return only SUCCESS recharges when filter is SUCCESS', () => {
    component.recharges.set([makeRecharge(1, 'SUCCESS', 199), makeRecharge(2, 'FAILED', 99)]);
    component.activeFilter.set('SUCCESS');
    expect(component.filtered().length).toBe(1);
    expect(component.filtered()[0].status).toBe('SUCCESS');
  });

  it('should return only FAILED recharges when filter is FAILED', () => {
    component.recharges.set([makeRecharge(1, 'SUCCESS', 199), makeRecharge(2, 'FAILED', 99)]);
    component.activeFilter.set('FAILED');
    expect(component.filtered().length).toBe(1);
    expect(component.filtered()[0].status).toBe('FAILED');
  });

  // ─── totalShown ───────────────────────────────────────────────────────────
  it('should compute totalShown as sum of filtered amounts', () => {
    component.recharges.set([makeRecharge(1, 'SUCCESS', 200), makeRecharge(2, 'SUCCESS', 100)]);
    component.activeFilter.set('ALL');
    expect(component.totalShown()).toBe('300');
  });

  // ─── successShown ─────────────────────────────────────────────────────────
  it('should compute successShown as count of SUCCESS in filtered list', () => {
    component.recharges.set([makeRecharge(1, 'SUCCESS', 200), makeRecharge(2, 'FAILED', 100)]);
    component.activeFilter.set('ALL');
    expect(component.successShown()).toBe(1);
  });

  it('should not make HTTP call when userId is null', async () => {
    authService.logout();
    TestBed.resetTestingModule();
    await TestBed.configureTestingModule({
      imports: [HistoryComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
        RechargeApiService,
        ToastService,
      ],
    }).compileComponents();
    const f = TestBed.createComponent(HistoryComponent);
    const hm = TestBed.inject(HttpTestingController);
    f.detectChanges();
    hm.expectNone(`${BASE}/recharges/user/1`);
    expect(f.componentInstance.loading()).toBe(false);
  });
});

describe('HistoryComponent Initialization Error', () => {
  let component: HistoryComponent;
  let fixture: ComponentFixture<HistoryComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HistoryComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
        RechargeApiService,
        ToastService,
      ],
    }).compileComponents();

    const auth = TestBed.inject(AuthService);
    auth.saveAuth({ token: 'jwt', user: mockUser as any });

    fixture   = TestBed.createComponent(HistoryComponent);
    component = fixture.componentInstance;
    httpMock  = TestBed.inject(HttpTestingController);
  });

  it('should show error toast and set loading=false when init API fails', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    
    fixture.detectChanges(); // Trigger ngOnInit
    const req = httpMock.expectOne(`${BASE}/recharges/user/1`);
    req.flush({}, { status: 500, statusText: 'Error' });
    
    expect(toast.error).toHaveBeenCalledWith('Failed to load history.');
    expect(component.loading()).toBe(false);
  });
});
