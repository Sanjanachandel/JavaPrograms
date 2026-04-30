import { vi } from 'vitest';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { AdminTransactionsComponent } from './admin-transactions.component';
import { RechargeApiService } from '../../../core/services/recharge-api.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { RechargeResponse } from '../../../shared/models/models';
import { environment } from '../../../../environments/environment';

const BASE = environment.apiBaseUrl;

const makeRecharge = (id: number, status: 'SUCCESS' | 'FAILED', mobile: string, userId: number): RechargeResponse => ({
  id, userId, operatorId: 2, planId: 3,
  mobileNumber: mobile, amount: 199, status,
  createdAt: '2024-06-01T10:00:00Z', message: '',
  paymentMethod: 'UPI', rechargeType: 'PREPAID',
});

describe('AdminTransactionsComponent', () => {
  let component: AdminTransactionsComponent;
  let fixture: ComponentFixture<AdminTransactionsComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    localStorage.clear();
    localStorage.setItem('rn_token', 'admin-jwt');

    await TestBed.configureTestingModule({
      imports: [AdminTransactionsComponent, ReactiveFormsModule],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
        RechargeApiService,
        ToastService,
      ],
    }).compileComponents();

    fixture   = TestBed.createComponent(AdminTransactionsComponent);
    component = fixture.componentInstance;
    httpMock  = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
    httpMock.expectOne(`${BASE}/recharges?page=0&size=10`).flush({ content: [], totalPages: 0, totalElements: 0, number: 0 });
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
    TestBed.resetTestingModule();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should start with empty searchQuery', () => {
    expect(component.searchQuery()).toBe('');
  });

  // ─── filteredTransactions ─────────────────────────────────────────────────
  it('should return all transactions when searchTerm is empty', () => {
    component.transactions.set([
      makeRecharge(1, 'SUCCESS', '9876543210', 1),
      makeRecharge(2, 'FAILED',  '8765432109', 2),
    ]);
    component.searchQuery.set('');
    expect(component.filteredTransactions().length).toBe(2);
  });

  it('should filter by mobile number', () => {
    component.transactions.set([
      makeRecharge(1, 'SUCCESS', '9876543210', 1),
      makeRecharge(2, 'FAILED',  '8765432109', 2),
    ]);
    component.searchQuery.set('9876');
    expect(component.filteredTransactions().length).toBe(1);
    expect(component.filteredTransactions()[0].mobileNumber).toBe('9876543210');
  });

  it('should filter by status (case-insensitive)', () => {
    component.transactions.set([
      makeRecharge(1, 'SUCCESS', '9876543210', 1),
      makeRecharge(2, 'FAILED',  '8765432109', 2),
    ]);
    component.searchQuery.set('success');
    expect(component.filteredTransactions().length).toBe(1);
  });

  it('should filter by userId', () => {
    component.transactions.set([
      makeRecharge(1, 'SUCCESS', '9876543210', 42),
      makeRecharge(2, 'FAILED',  '8765432109', 99),
    ]);
    component.searchQuery.set('42');
    expect(component.filteredTransactions().length).toBe(1);
    expect(component.filteredTransactions()[0].userId).toBe(42);
  });

  it('should return empty array when no matches found', () => {
    component.transactions.set([makeRecharge(1, 'SUCCESS', '9876543210', 1)]);
    component.searchQuery.set('xyz');
    expect(component.filteredTransactions().length).toBe(0);
  });

  // ─── Error handling ───────────────────────────────────────────────────────
  it('should show error toast and set loading=false when API fails', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    
    component.loadTransactions();
    expect(component.loading()).toBe(true);

    const req = httpMock.expectOne(`${BASE}/recharges?page=0&size=10`);
    req.flush({ message: 'Error' }, { status: 500, statusText: 'Server Error' });

    expect(toast.error).toHaveBeenCalledWith('Failed to load transactions');
    expect(component.loading()).toBe(false);
  });

  // ─── Pagination ──────────────────────────────────────────────────────────
  it('should call loadTransactions with page+1 on nextPage when not on last page', () => {
    component.currentPage.set(0);
    component.totalPages.set(3);
    component.nextPage();
    const req = httpMock.expectOne(`${BASE}/recharges?page=1&size=10`);
    req.flush({ content: [], totalPages: 3, totalElements: 0, number: 1 });
  });

  it('should NOT call loadTransactions on nextPage when already on last page', () => {
    component.currentPage.set(2);
    component.totalPages.set(3);
    component.nextPage();
    httpMock.expectNone(`${BASE}/recharges?page=3&size=10`);
  });

  it('should call loadTransactions with page-1 on prevPage when not on first page', () => {
    component.currentPage.set(2);
    component.totalPages.set(3);
    component.prevPage();
    const req = httpMock.expectOne(`${BASE}/recharges?page=1&size=10`);
    req.flush({ content: [], totalPages: 3, totalElements: 0, number: 1 });
  });

  it('should NOT call loadTransactions on prevPage when already on first page', () => {
    component.currentPage.set(0);
    component.prevPage();
    httpMock.expectNone(`${BASE}/recharges?page=-1&size=10`);
  });

  it('should transform date in loadTransactions', () => {
    const rawRecharge = { 
      id: 99, userId: 1, operatorId: 2, planId: 3, mobileNumber: '123', 
      amount: 100, status: 'SUCCESS', 
      createdAt: '2024-01-01 10:00:00', // No Z, has space
      paymentMethod: 'UPI', message: ''
    };
    component.loadTransactions(0);
    const req = httpMock.expectOne(`${BASE}/recharges?page=0&size=10`);
    req.flush({ content: [rawRecharge], totalPages: 1, totalElements: 1, number: 0 });
    
    expect(component.transactions()[0].createdAt).toContain('T');
    expect(component.transactions()[0].createdAt).toContain('Z');
  });

  it('should default paymentMethod to UPI if missing', () => {
    const raw = makeRecharge(1, 'SUCCESS', '123', 1);
    (raw as any).paymentMethod = null;
    component.loadTransactions(0);
    const req = httpMock.expectOne(`${BASE}/recharges?page=0&size=10`);
    req.flush({ content: [raw], totalPages: 1, totalElements: 1, number: 0 });
    expect(component.transactions()[0].paymentMethod).toBe('UPI');
  });

    expect(component.transactions()[0].createdAt).toBe('');
  });

  it('should handle date string already containing Z in ensureIst', () => {
    const raw = makeRecharge(1, 'SUCCESS', '123', 1);
    raw.createdAt = '2024-01-01T10:00:00Z';
    component.loadTransactions(0);
    const req = httpMock.expectOne(`${BASE}/recharges?page=0&size=10`);
    req.flush({ content: [raw], totalPages: 1, totalElements: 1, number: 0 });
    expect(component.transactions()[0].createdAt).toBe('2024-01-01T10:00:00.000Z');
  });
});



