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
  paymentMethod: 'UPI', rechargeType: 'PREPAID'
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
    const req = httpMock.expectOne(`${BASE}/recharges/user/1?page=0&size=10`);
    req.flush({ content: [], totalPages: 0, totalElements: 0, number: 0 });
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
    hm.expectNone(`${BASE}/recharges/user/1?page=0&size=10`);
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
    const req = httpMock.expectOne(`${BASE}/recharges/user/1?page=0&size=10`);
    req.flush({}, { status: 500, statusText: 'Error' });
    
    expect(toast.error).toHaveBeenCalledWith('Failed to load history.');
    expect(component.loading()).toBe(false);
  });
});

describe('HistoryComponent Pagination', () => {
  let component: HistoryComponent;
  let fixture: ComponentFixture<HistoryComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
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

    const auth = TestBed.inject(AuthService);
    auth.saveAuth({ token: 'jwt', user: mockUser as any });

    fixture   = TestBed.createComponent(HistoryComponent);
    component = fixture.componentInstance;
    httpMock  = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
    httpMock.expectOne(`${BASE}/recharges/user/1?page=0&size=10`).flush({ content: [], totalPages: 3, totalElements: 0, number: 0 });
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
    TestBed.resetTestingModule();
  });

  it('should load next page when nextPage() called and not on last page', () => {
    component.currentPage.set(0);
    component.totalPages.set(3);
    component.nextPage();
    const req = httpMock.expectOne(`${BASE}/recharges/user/1?page=1&size=10`);
    req.flush({ content: [], totalPages: 3, totalElements: 0, number: 1 });
  });

  it('should NOT load next page when on last page', () => {
    component.currentPage.set(2);
    component.totalPages.set(3);
    component.nextPage();
    httpMock.expectNone(`${BASE}/recharges/user/1?page=3&size=10`);
  });

  it('should load prev page when prevPage() called and not on first page', () => {
    component.currentPage.set(2);
    component.totalPages.set(3);
    component.prevPage();
    const req = httpMock.expectOne(`${BASE}/recharges/user/1?page=1&size=10`);
    req.flush({ content: [], totalPages: 3, totalElements: 0, number: 1 });
  });

  it('should NOT load prev page when on first page', () => {
    component.currentPage.set(0);
    component.prevPage();
    httpMock.expectNone(`${BASE}/recharges/user/1?page=-1&size=10`);
  });

  it('should set activeFilter when signal is updated', () => {
    component.activeFilter.set('SUCCESS');
    expect(component.activeFilter()).toBe('SUCCESS');
    component.activeFilter.set('ALL');
    expect(component.activeFilter()).toBe('ALL');
  });

  it('should transform date and payment method in loadHistory', () => {
    const rawRecharge = { 
      id: 99, 
      mobileNumber: '123', 
      amount: 100, 
      status: 'SUCCESS', 
      createdAt: '2024-01-01 10:00:00', // No Z
      paymentMethod: null 
    };
    component.loadHistory(0);
    const req = httpMock.expectOne(`${BASE}/recharges/user/1?page=0&size=10`);
    req.flush({ content: [rawRecharge], totalPages: 1, totalElements: 1, number: 0 });
    
    const transformed = component.recharges()[0];
    expect(transformed.paymentMethod).toBe('UPI');
    expect(transformed.createdAt).toContain('T');
    expect(transformed.createdAt).toContain('Z');
  });

  it('should return empty string for null date in ensureIst', () => {
    // Since ensureIst is private, we test it via loadHistory
    const rawRecharge = { 
      id: 99, mobileNumber: '123', amount: 100, status: 'SUCCESS', 
      createdAt: null,
      paymentMethod: 'UPI' 
    };
    component.loadHistory(0);
    const req = httpMock.expectOne(`${BASE}/recharges/user/1?page=0&size=10`);
    req.flush({ content: [rawRecharge], totalPages: 1, totalElements: 1, number: 0 });
    expect(component.recharges()[0].createdAt).toBe('');
  });

  it('should not modify date if it already contains Z', () => {
    const rawRecharge = { 
      id: 99, mobileNumber: '123', amount: 100, status: 'SUCCESS', 
      createdAt: '2024-01-01T10:00:00Z',
      paymentMethod: 'UPI' 
    };
    component.loadHistory(0);
    const req = httpMock.expectOne(`${BASE}/recharges/user/1?page=0&size=10`);
    req.flush({ content: [rawRecharge], totalPages: 1, totalElements: 1, number: 0 });
    expect(component.recharges()[0].createdAt).toBe(new Date('2024-01-01T10:00:00Z').toISOString());
  });
});



