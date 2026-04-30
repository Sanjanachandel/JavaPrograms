import { vi } from 'vitest';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { AdminUsersComponent } from './admin-users.component';
import { UserApiService } from '../../../core/services/user-api.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { UserResponse } from '../../../shared/models/models';
import { environment } from '../../../../environments/environment';

const BASE = environment.apiBaseUrl;

const mockUser: UserResponse = {
  id: 1, name: 'Sanjana', email: 'sanjana@example.com',
  role: 'ROLE_USER', phoneNumber: '9876543210', createdAt: '2024-01-01T00:00:00Z',
};

const mockAdmin: UserResponse = {
  id: 2, name: 'Admin', email: 'admin@example.com',
  role: 'ADMIN', phoneNumber: '8888888888', createdAt: '2024-01-01T00:00:00Z',
};

describe('AdminUsersComponent', () => {
  let component: AdminUsersComponent;
  let fixture: ComponentFixture<AdminUsersComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    localStorage.clear();
    localStorage.setItem('rn_token', 'admin-jwt');

    await TestBed.configureTestingModule({
      imports: [AdminUsersComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
        UserApiService,
        ToastService,
      ],
    }).compileComponents();

    fixture   = TestBed.createComponent(AdminUsersComponent);
    component = fixture.componentInstance;
    httpMock  = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
    httpMock.expectOne(`${BASE}/users?page=0&size=10`).flush({ content: [mockUser, mockAdmin], totalPages: 1, totalElements: 2, number: 0 });
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
    TestBed.resetTestingModule();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load and set users on init', () => {
    expect(component.users().length).toBe(2);
    expect(component.users()[0]).toEqual(mockUser);
  });

  it('should include all user roles in the list', () => {
    const roles = component.users().map(u => u.role);
    expect(roles).toContain('ROLE_USER');
    expect(roles).toContain('ADMIN');
  });

  // ─── loadUsers error ──────────────────────────────────────────────────────
  it('should show error toast when loading users fails', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    
    component.loadUsers();
    const req = httpMock.expectOne(`${BASE}/users?page=0&size=10`);
    req.flush({}, { status: 500, statusText: 'Server Error' });
    
    expect(toast.error).toHaveBeenCalledWith('Failed to load users');
  });

  // ─── Reload via loadUsers ──────────────────────────────────────────────────
  it('should update users signal when loadUsers is called again', () => {
    component.loadUsers();
    const req = httpMock.expectOne(`${BASE}/users?page=0&size=10`);
    req.flush({ content: [mockUser], totalPages: 1, totalElements: 1, number: 0 });
    expect(component.users().length).toBe(1);
  });

  // ─── Pagination ──────────────────────────────────────────────────────────
  it('should call loadUsers with page+1 on nextPage when not on last page', () => {
    component.currentPage.set(0);
    component.totalPages.set(3);
    component.nextPage();
    const req = httpMock.expectOne(`${BASE}/users?page=1&size=10`);
    req.flush({ content: [mockUser], totalPages: 3, totalElements: 1, number: 1 });
  });

  it('should NOT load next page when already on last page', () => {
    component.currentPage.set(2);
    component.totalPages.set(3);
    component.nextPage();
    httpMock.expectNone(`${BASE}/users?page=3&size=10`);
  });

  it('should call loadUsers with page-1 on prevPage when not on first page', () => {
    component.currentPage.set(2);
    component.totalPages.set(3);
    component.prevPage();
    const req = httpMock.expectOne(`${BASE}/users?page=1&size=10`);
    req.flush({ content: [mockUser], totalPages: 3, totalElements: 1, number: 1 });
  });

  it('should NOT load prev page when already on first page', () => {
    component.currentPage.set(0);
    component.prevPage();
    httpMock.expectNone(`${BASE}/users?page=-1&size=10`);
  });
});

