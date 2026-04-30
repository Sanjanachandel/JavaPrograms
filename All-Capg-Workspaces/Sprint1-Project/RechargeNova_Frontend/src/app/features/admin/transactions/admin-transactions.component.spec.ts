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
  paymentMethod: 'UPI',
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
    httpMock.expectOne(`${BASE}/recharges`).flush([]);
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

    const req = httpMock.expectOne(`${BASE}/recharges`);
    req.flush({ message: 'Error' }, { status: 500, statusText: 'Server Error' });

    expect(toast.error).toHaveBeenCalledWith('Failed to load transactions');
    expect(component.loading()).toBe(false);
  });
});
