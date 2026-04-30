import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { AdminDashboardComponent } from './admin-dashboard.component';
import { UserApiService } from '../../../core/services/user-api.service';
import { RechargeApiService } from '../../../core/services/recharge-api.service';
import { OperatorApiService } from '../../../core/services/operator-api.service';
import { AuthService } from '../../../core/services/auth.service';
import { RechargeResponse, UserResponse, OperatorDto } from '../../../shared/models/models';
import { environment } from '../../../../environments/environment';

const BASE = environment.apiBaseUrl;

const today = new Date().toISOString().split('T')[0];

const mockUser: UserResponse = {
  id: 1, name: 'Sanjana', email: 'sanjana@example.com',
  role: 'ROLE_USER', phoneNumber: '9876543210', createdAt: `${today}T10:00:00Z`,
};

const oldUser: UserResponse = {
  id: 2, name: 'Old User', email: 'old@example.com',
  role: 'ROLE_USER', phoneNumber: '9999999999', createdAt: '2020-01-01T00:00:00Z',
};

const mockAdmin: UserResponse = {
  id: 3, name: 'Admin', email: 'admin@example.com',
  role: 'ADMIN', phoneNumber: '8888888888', createdAt: '2024-01-01T00:00:00Z',
};

const makeRecharge = (id: number, status: 'SUCCESS' | 'FAILED', amount: number, createdAt = '2024-06-01T10:00:00Z'): RechargeResponse => ({
  id, userId: 1, operatorId: 2, planId: 3,
  mobileNumber: '9876543210', amount, status, createdAt, message: '',
  paymentMethod: 'UPI', rechargeType: 'PREPAID',
});

const mockOperator: OperatorDto = {
  id: 1, name: 'Jio', circle: 'All India',
};

describe('AdminDashboardComponent', () => {
  let component: AdminDashboardComponent;
  let fixture: ComponentFixture<AdminDashboardComponent>;
  let httpMock: HttpTestingController;

  const setupAndFlush = (users: UserResponse[], recharges: RechargeResponse[], operators: OperatorDto[]) => {
    httpMock.expectOne(`${BASE}/users/count`).flush(users.length);
    httpMock.expectOne(`${BASE}/recharges/count`).flush(recharges.length);
    httpMock.expectOne(`${BASE}/recharges/revenue`).flush(recharges.reduce((sum, r) => sum + r.amount, 0));
    httpMock.expectOne(`${BASE}/operators/count`).flush(operators.length);

    httpMock.expectOne(`${BASE}/users?page=0&size=10`).flush({ content: users, totalPages: 1, totalElements: users.length });
    httpMock.expectOne(`${BASE}/recharges?page=0&size=10`).flush({ content: recharges, totalPages: 1, totalElements: recharges.length });
    httpMock.expectOne(`${BASE}/operators`).flush(operators);
  };

  beforeEach(async () => {
    localStorage.clear();
    localStorage.setItem('rn_token', 'admin-jwt');
    localStorage.setItem('rn_user', JSON.stringify(mockAdmin));

    await TestBed.configureTestingModule({
      imports: [AdminDashboardComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
        UserApiService,
        RechargeApiService,
        OperatorApiService,
      ],
    }).compileComponents();

    fixture   = TestBed.createComponent(AdminDashboardComponent);
    component = fixture.componentInstance;
    httpMock  = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
    setupAndFlush([mockUser, oldUser], [], [mockOperator]);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
    TestBed.resetTestingModule();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // ─── totalVolume ──────────────────────────────────────────────────────────
  it('should compute totalVolume from SUCCESS recharges only', () => {
    component.recharges.set([makeRecharge(1, 'SUCCESS', 299), makeRecharge(2, 'FAILED', 100)]);
    expect(component.totalVolume()).toBe(299);
  });

  it('should return 0 totalVolume when no recharges', () => {
    component.recharges.set([]);
    expect(component.totalVolume()).toBe(0);
  });

  // ─── newUsersToday ────────────────────────────────────────────────────────
  it('should count users created today', () => {
    component.users.set([mockUser, oldUser]);
    expect(component.newUsersToday()).toBe(1);
  });

  it('should return 0 newUsersToday when no users registered today', () => {
    component.users.set([oldUser]);
    expect(component.newUsersToday()).toBe(0);
  });

  // ─── recentRecharges ──────────────────────────────────────────────────────
  it('should return at most 4 recentRecharges', () => {
    const r1 = makeRecharge(1, 'SUCCESS', 100, '2024-06-01T08:00:00Z');
    const r2 = makeRecharge(2, 'SUCCESS', 200, '2024-06-02T08:00:00Z');
    const r3 = makeRecharge(3, 'SUCCESS', 300, '2024-06-03T08:00:00Z');
    const r4 = makeRecharge(4, 'SUCCESS', 400, '2024-06-04T08:00:00Z');
    const r5 = makeRecharge(5, 'SUCCESS', 500, '2024-06-05T08:00:00Z');
    // Set newest-first order (as backend would return)
    component.recharges.set([r5, r4, r3, r2, r1]);
    const recent = component.recentRecharges();
    expect(recent.length).toBe(4);
    expect(recent[0].id).toBe(5); // newest first
  });

  // ─── Signal state ─────────────────────────────────────────────────────────
  it('should set operators signal correctly', () => {
    expect(component.operators()).toEqual([mockOperator]);
  });

  it('should set users signal correctly', () => {
    expect(component.users()).toContain(mockUser);
  });
});
