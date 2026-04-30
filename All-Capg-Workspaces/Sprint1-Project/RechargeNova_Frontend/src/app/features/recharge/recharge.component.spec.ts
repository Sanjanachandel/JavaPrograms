import { vi } from 'vitest';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { RechargeComponent } from './recharge.component';
import { AuthService } from '../../core/services/auth.service';
import { OperatorApiService } from '../../core/services/operator-api.service';
import { RechargeApiService } from '../../core/services/recharge-api.service';
import { ToastService } from '../../core/services/toast.service';
import { OperatorDto, PlanDto, RechargeResponse } from '../../shared/models/models';
import { environment } from '../../../environments/environment';

const BASE = environment.apiBaseUrl;

const mockUser = {
  id: 1, name: 'Sanjana', email: 'sanjana@example.com',
  role: 'ROLE_USER', phoneNumber: '9876543210', createdAt: '2024-01-01T00:00:00Z',
};

const mockPlan: PlanDto = {
  id: 5, operatorId: 2, amount: 199, validity: 28,
  description: 'All-in-One plan',
};

const mockOperator: OperatorDto = {
  id: 2, name: 'Jio', type: 'Prepaid', circle: 'All India', plans: [mockPlan],
};

const mockRechargeResult: RechargeResponse = {
  id: 100, userId: 1, operatorId: 2, planId: 5, mobileNumber: '9876543210',
  amount: 199, status: 'SUCCESS', createdAt: '2024-06-01T10:00:00.000Z', message: 'Recharge successful',
  paymentMethod: 'UPI',
};

describe('RechargeComponent', () => {
  let component: RechargeComponent;
  let fixture: ComponentFixture<RechargeComponent>;
  let httpMock: HttpTestingController;
  let authService: AuthService;

  beforeEach(async () => {
    localStorage.clear();
    TestBed.resetTestingModule();
    await TestBed.configureTestingModule({
      imports: [RechargeComponent, ReactiveFormsModule],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
        OperatorApiService,
        RechargeApiService,
        ToastService,
      ],
    }).compileComponents();

    authService = TestBed.inject(AuthService);
    authService.saveAuth({ token: 'jwt', user: mockUser as any });

    fixture   = TestBed.createComponent(RechargeComponent);
    component = fixture.componentInstance;
    httpMock  = TestBed.inject(HttpTestingController);
    fixture.detectChanges();

    // Flush ngOnInit operators call
    httpMock.expectOne(`${BASE}/operators`).flush([mockOperator]);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
    TestBed.resetTestingModule();
  });

  // ─── Creation & initial state ──────────────────────────────────────────────
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should start at step 1', () => {
    expect(component.step()).toBe(1);
  });

  it('should start with loading=false', () => {
    expect(component.loading()).toBe(false);
  });

  it('should load operators on init', () => {
    expect(component.operators()).toEqual([mockOperator]);
    expect(component.loadingOperators()).toBe(false);
  });

  it('should define 4 steps', () => {
    expect(component.steps.length).toBe(4);
  });

  it('should define 5 payment methods', () => {
    expect(component.paymentMethods.length).toBe(5);
  });

  it('should define 4 plan categories', () => {
    expect(component.planCategories.length).toBe(4);
  });

  // ─── Mobile form validation ───────────────────────────────────────────────
  it('should have invalid mobileForm initially', () => {
    expect(component.mobileForm.invalid).toBe(true);
  });

  it('should reject phone number not starting with 6-9', () => {
    component.mobileForm.get('mobile')!.setValue('1234567890');
    expect(component.mobileForm.invalid).toBe(true);
  });

  it('should accept valid 10-digit phone starting with 9', () => {
    component.mobileForm.get('mobile')!.setValue('9876543210');
    expect(component.mobileForm.valid).toBe(true);
  });

  it('should reject mobile shorter than 10 digits', () => {
    component.mobileForm.get('mobile')!.setValue('987654321');
    expect(component.mobileForm.invalid).toBe(true);
  });

  // ─── isMobileInvalid ─────────────────────────────────────────────────────
  it('should return false for isMobileInvalid when pristine', () => {
    expect(component.isMobileInvalid()).toBe(false);
  });

  it('should return true for isMobileInvalid after touch with invalid value', () => {
    component.mobileForm.get('mobile')!.setValue('');
    component.mobileForm.get('mobile')!.markAsTouched();
    expect(component.isMobileInvalid()).toBe(true);
  });

  // ─── goToStep2 ────────────────────────────────────────────────────────────
  it('should not advance to step 2 when mobile is invalid', () => {
    component.goToStep2();
    expect(component.step()).toBe(1);
  });

  it('should advance to step 2 when mobile is valid', () => {
    component.mobileForm.get('mobile')!.setValue('9876543210');
    component.goToStep2();
    expect(component.step()).toBe(2);
  });

  // ─── selectOperator / goToStep3 ───────────────────────────────────────────
  it('should set selectedOperator and clear selectedPlan when operator is selected', () => {
    component.selectedPlan.set(mockPlan);
    component.selectOperator(mockOperator);
    expect(component.selectedOperator()).toEqual(mockOperator);
    expect(component.selectedPlan()).toBeNull();
  });

  it('should not advance to step 3 when no operator is selected', () => {
    component.step.set(2);
    component.goToStep3();
    expect(component.step()).toBe(2);
  });

  it('should advance to step 3 when operator is selected', () => {
    component.step.set(2);
    component.selectOperator(mockOperator);
    component.goToStep3();
    expect(component.step()).toBe(3);
  });

  // ─── selectPlan ──────────────────────────────────────────────────────────
  it('should set selectedPlan when a plan is selected', () => {
    component.selectPlan(mockPlan);
    expect(component.selectedPlan()).toEqual(mockPlan);
  });

  // ─── opColor ─────────────────────────────────────────────────────────────
  it('should return a gradient string for opColor', () => {
    const color = component.opColor(0);
    expect(color).toContain('linear-gradient');
  });

  it('should wrap opColor index using modulo', () => {
    expect(component.opColor(0)).toBe(component.opColor(6));
  });

  // ─── filteredPlans ────────────────────────────────────────────────────────
  it('should return all plans when category is ALL and no search', () => {
    component.selectOperator(mockOperator);
    component.planCategory.set('ALL');
    component.planSearch.set('');
    expect(component.filteredPlans().length).toBe(1);
  });

  it('should return empty when search matches nothing', () => {
    component.selectOperator(mockOperator);
    component.planSearch.set('xyz-nonexistent');
    expect(component.filteredPlans().length).toBe(0);
  });

  it('should filter by amount in planSearch', () => {
    component.selectOperator(mockOperator);
    component.planSearch.set('199');
    expect(component.filteredPlans().length).toBe(1);
  });

  // ─── mobileNum computed ───────────────────────────────────────────────────
  it('should return the current mobile value from mobileNum computed', () => {
    component.mobileForm.get('mobile')!.setValue('9876543210');
    expect(component.mobileNum()).toBe('9876543210');
  });

  // ─── processRecharge ──────────────────────────────────────────────────────
  it('should not POST when required fields are missing', () => {
    component.processRecharge();
    httpMock.expectNone(`${BASE}/recharges`);
  });

  it('should handle the full Razorpay flow and advance to step 5', async () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'success');

    // Mock Razorpay as a constructor
    const mockRzpInstance = { open: vi.fn() };
    (window as any).Razorpay = vi.fn().mockImplementation(function(options: any) {
      // Simulate successful payment by calling the handler
      setTimeout(() => {
        if (options.handler) {
          options.handler({
            razorpay_order_id: 'order_123',
            razorpay_payment_id: 'pay_123',
            razorpay_signature: 'sig_123'
          });
        }
      }, 10);
      return mockRzpInstance;
    });

    component.mobileForm.get('mobile')!.setValue('9876543210');
    component.selectOperator(mockOperator);
    component.selectPlan(mockPlan);
    component.paymentMethod.set('UPI');

    // Start process
    component.processRecharge();

    // 1. initiateRecharge
    const req1 = httpMock.expectOne(`${BASE}/recharges`);
    req1.flush(mockRechargeResult);

    // 2. createOrder
    const req2 = httpMock.expectOne(`http://localhost:8989/api/payments/create-order`);
    req2.flush({ orderId: 'order_123', keyId: 'key_123', amount: 19900, currency: 'INR' });

    // Wait for the setTimeout in mock handler
    await new Promise(resolve => setTimeout(resolve, 50));

    // 3. verifyPayment
    const req3 = httpMock.expectOne(`http://localhost:8989/api/payments/verify-payment`);
    req3.flush({ status: 'SUCCESS' });

    // 4. getRechargeById (final fetch)
    const req4 = httpMock.expectOne(`${BASE}/recharges/100`);
    req4.flush(mockRechargeResult);

    expect(component.step()).toBe(5);
    expect(component.rechargeResult()).toEqual(mockRechargeResult);
    expect(toast.success).toHaveBeenCalledWith('Recharge successful! 🎉');
  });

  it('should show error toast when initiateRecharge fails', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    component.mobileForm.get('mobile')!.setValue('9876543210');
    component.selectOperator(mockOperator);
    component.selectPlan(mockPlan);
    component.paymentMethod.set('UPI');
    component.processRecharge();
    const req = httpMock.expectOne(`${BASE}/recharges`);
    req.flush({ message: 'Error' }, { status: 400, statusText: 'Bad Request' });
    expect(toast.error).toHaveBeenCalledWith('Failed to initiate recharge record.');
    expect(component.loading()).toBe(false);
  });
  // ─── resetFlow ───────────────────────────────────────────────────────────
  it('should reset all state on resetFlow()', () => {
    component.mobileForm.get('mobile')!.setValue('9876543210');
    component.selectOperator(mockOperator);
    component.selectPlan(mockPlan);
    component.paymentMethod.set('UPI');
    component.step.set(4);

    component.resetFlow();

    expect(component.step()).toBe(1);
    expect(component.mobileForm.value.mobile).toBeFalsy();
    expect(component.selectedOperator()).toBeNull();
    expect(component.selectedPlan()).toBeNull();
    expect(component.paymentMethod()).toBe('');
    expect(component.rechargeResult()).toBeNull();
    expect(component.planCategory()).toBe('ALL');
    expect(component.planSearch()).toBe('');
  });
});

describe('RechargeComponent Operator Load Error', () => {
  let component: RechargeComponent;
  let fixture: ComponentFixture<RechargeComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RechargeComponent, ReactiveFormsModule],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
        OperatorApiService,
        RechargeApiService,
        ToastService,
      ],
    }).compileComponents();

    const auth = TestBed.inject(AuthService);
    auth.saveAuth({ token: 'jwt', user: mockUser as any });

    fixture   = TestBed.createComponent(RechargeComponent);
    component = fixture.componentInstance;
    httpMock  = TestBed.inject(HttpTestingController);
  });

  it('should show error toast when operators fail to load on init', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    fixture.detectChanges(); // Trigger ngOnInit
    const req = httpMock.expectOne(`${BASE}/operators`);
    req.flush({}, { status: 500, statusText: 'Error' });
    expect(toast.error).toHaveBeenCalledWith('Failed to load operators. Is the backend running?');
    expect(component.loadingOperators()).toBe(false);
  });
});
