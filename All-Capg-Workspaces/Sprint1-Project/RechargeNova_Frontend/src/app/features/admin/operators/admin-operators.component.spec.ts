import { vi } from 'vitest';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AdminOperatorsComponent } from './admin-operators.component';
import { OperatorApiService } from '../../../core/services/operator-api.service';
import { ToastService } from '../../../core/services/toast.service';
import { AuthService } from '../../../core/services/auth.service';
import { OperatorDto, PlanDto } from '../../../shared/models/models';
import { environment } from '../../../../environments/environment';

const BASE = environment.apiBaseUrl;

const mockPlan: PlanDto = {
  id: 5, operatorId: 2, amount: 199, validity: 28, description: 'All-in-One',
};

const mockOperator: OperatorDto = {
  id: 2, name: 'Jio', type: 'Prepaid', circle: 'All India', plans: [mockPlan],
};

describe('AdminOperatorsComponent', () => {
  let component: AdminOperatorsComponent;
  let fixture: ComponentFixture<AdminOperatorsComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    localStorage.clear();
    localStorage.setItem('rn_token', 'admin-jwt');

    await TestBed.configureTestingModule({
      imports: [AdminOperatorsComponent, FormsModule],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
        OperatorApiService,
        ToastService,
      ],
    }).compileComponents();

    fixture   = TestBed.createComponent(AdminOperatorsComponent);
    component = fixture.componentInstance;
    httpMock  = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
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

  it('should load operators on init', () => {
    expect(component.operators()).toEqual([mockOperator]);
  });

  it('should start with showOperatorForm = false', () => {
    expect(component.showOperatorForm).toBe(false);
  });

  it('should start with isEditingOperator = false', () => {
    expect(component.isEditingOperator).toBe(false);
  });

  // ─── toggleOperatorForm ───────────────────────────────────────────────────
  it('should open operator form on toggleOperatorForm', () => {
    component.toggleOperatorForm();
    expect(component.showOperatorForm).toBe(true);
  });

  it('should close and reset operator form on second toggle', () => {
    component.toggleOperatorForm();
    component.toggleOperatorForm();
    expect(component.showOperatorForm).toBe(false);
    expect(component.operatorForm.name).toBe('');
  });

  // ─── editOperator ─────────────────────────────────────────────────────────
  it('should populate operatorForm when editOperator is called', () => {
    component.editOperator(mockOperator);
    expect(component.showOperatorForm).toBe(true);
    expect(component.isEditingOperator).toBe(true);
    expect(component.editOperatorId).toBe(2);
    expect(component.operatorForm.name).toBe('Jio');
  });

  // ─── resetOperatorForm ────────────────────────────────────────────────────
  it('should reset operator form state on resetOperatorForm', () => {
    component.editOperator(mockOperator);
    component.resetOperatorForm();
    expect(component.showOperatorForm).toBe(false);
    expect(component.isEditingOperator).toBe(false);
    expect(component.editOperatorId).toBeNull();
    expect(component.operatorForm.name).toBe('');
    expect(component.operatorForm.type).toBe('Prepaid');
    expect(component.operatorForm.circle).toBe('All India');
  });

  // ─── saveOperator (create) ────────────────────────────────────────────────
  it('should not POST when operator name is empty', () => {
    component.showOperatorForm = true;
    component.operatorForm.name = '';
    component.saveOperator();
    httpMock.expectNone(`${BASE}/operators`);
  });

  it('should POST to create a new operator', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'success');
    component.operatorForm = { name: 'Airtel', type: 'Prepaid', circle: 'Mumbai' };
    component.saveOperator();
    const req = httpMock.expectOne(`${BASE}/operators`);
    expect(req.request.method).toBe('POST');
    req.flush({ id: 3, name: 'Airtel', type: 'Prepaid', circle: 'Mumbai' });
    // Reload call
    httpMock.expectOne(`${BASE}/operators`).flush([mockOperator]);
    expect(toast.success).toHaveBeenCalledWith('Operator created successfully!');
  });

  // ─── saveOperator (update) ────────────────────────────────────────────────
  it('should PUT to update an existing operator', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'success');
    component.editOperator(mockOperator);
    component.operatorForm.name = 'Jio Updated';
    component.saveOperator();
    const req = httpMock.expectOne(`${BASE}/operators/2`);
    expect(req.request.method).toBe('PUT');
    req.flush({ ...mockOperator, name: 'Jio Updated' });
    httpMock.expectOne(`${BASE}/operators`).flush([mockOperator]);
    expect(toast.success).toHaveBeenCalledWith('Operator updated successfully!');
  });

  // ─── deleteOperator ───────────────────────────────────────────────────────
  it('should skip delete when id is undefined', () => {
    component.deleteOperator(undefined);
    httpMock.expectNone(`${BASE}/operators/undefined`);
  });

  it('should delete operator and reload when confirmed', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'success');
    vi.spyOn(window, 'confirm').mockReturnValue(true);
    component.selectedOperatorId = 2;

    component.deleteOperator(2);
    const req = httpMock.expectOne(`${BASE}/operators/2`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
    
    // Reload call
    httpMock.expectOne(`${BASE}/operators`).flush([]);
    expect(toast.success).toHaveBeenCalledWith('Operator deleted');
    expect(component.selectedOperatorId).toBeNull();
  });

  // ─── viewPlans / loadPlans ────────────────────────────────────────────────
  it('should set selectedOperatorId and name on viewPlans', () => {
    component.viewPlans(mockOperator);
    expect(component.selectedOperatorId).toBe(2);
    expect(component.selectedOperatorName).toBe('Jio');
    const req = httpMock.expectOne(`${BASE}/operators/2`);
    req.flush(mockOperator);
    expect(component.operatorPlans()).toEqual([mockPlan]);
  });

  // ─── togglePlanForm ──────────────────────────────────────────────────────
  it('should toggle showPlanForm', () => {
    component.togglePlanForm();
    expect(component.showPlanForm).toBe(true);
    component.togglePlanForm();
    expect(component.showPlanForm).toBe(false);
  });

  // ─── editPlan ────────────────────────────────────────────────────────────
  it('should populate planForm when editPlan is called', () => {
    component.editPlan(mockPlan);
    expect(component.showPlanForm).toBe(true);
    expect(component.isEditingPlan).toBe(true);
    expect(component.editPlanId).toBe(5);
  });

  // ─── resetPlanForm ────────────────────────────────────────────────────────
  it('should reset planForm on resetPlanForm', () => {
    component.editPlan(mockPlan);
    component.resetPlanForm();
    expect(component.showPlanForm).toBe(false);
    expect(component.isEditingPlan).toBe(false);
    expect(component.editPlanId).toBeNull();
    expect(component.planForm.amount).toBe(0);
  });

  // ─── savePlan (create) ────────────────────────────────────────────────────
  it('should not POST plan when no operator is selected', () => {
    component.selectedOperatorId = null;
    component.savePlan();
    httpMock.expectNone(`${BASE}/operators/2/plans`);
  });

  it('should POST to create a new plan', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'success');
    component.selectedOperatorId = 2;
    component.planForm = { category: 'Data', amount: 399, validity: 56, description: 'Data plan' };
    component.savePlan();
    const req = httpMock.expectOne(`${BASE}/operators/2/plans`);
    expect(req.request.method).toBe('POST');
    req.flush({ ...mockPlan, amount: 399 });
    // loadPlans re-request
    httpMock.expectOne(`${BASE}/operators/2`).flush(mockOperator);
    expect(toast.success).toHaveBeenCalledWith('Plan added successfully!');
  });

  // ─── savePlan (update) ────────────────────────────────────────────────────
  it('should PUT to update an existing plan', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'success');
    component.selectedOperatorId = 2;
    component.editPlan(mockPlan);
    component.planForm = { ...component.planForm, amount: 499 } as any;
    component.savePlan();
    const req = httpMock.expectOne(`${BASE}/operators/plans/5`);
    expect(req.request.method).toBe('PUT');
    req.flush({ ...mockPlan, amount: 499 });
    httpMock.expectOne(`${BASE}/operators/2`).flush(mockOperator);
    expect(toast.success).toHaveBeenCalledWith('Plan updated successfully!');
  });

  // ─── deletePlan ──────────────────────────────────────────────────────────
  it('should skip deletePlan when id is undefined', () => {
    component.deletePlan(undefined);
    httpMock.expectNone(`${BASE}/operators/plans/undefined`);
  });

  it('should delete plan and reload when confirmed', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'success');
    vi.spyOn(window, 'confirm').mockReturnValue(true);
    component.selectedOperatorId = 2;

    component.deletePlan(5);
    const req = httpMock.expectOne(`${BASE}/operators/plans/5`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
    
    // loadPlans reload
    httpMock.expectOne(`${BASE}/operators/2`).flush(mockOperator);
    expect(toast.success).toHaveBeenCalledWith('Plan deleted');
  });

  // ─── Error Handling ───────────────────────────────────────────────────────
  it('should show error toast when loadOperators fails', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    // Trigger another load
    component.loadOperators();
    const req = httpMock.expectOne(`${BASE}/operators`);
    req.flush({}, { status: 500, statusText: 'Error' });
    expect(toast.error).toHaveBeenCalledWith('Failed to load operators');
  });

  // ─── Edge cases for missing IDs ──────────────────────────────────────────
  it('should handle missing id in editOperator', () => {
    component.editOperator({ name: 'No ID', type: 'Prepaid', circle: 'India' } as any);
    expect(component.editOperatorId).toBeNull();
  });

  it('should handle missing id in viewPlans', () => {
    component.viewPlans({ name: 'No ID' } as any);
    expect(component.selectedOperatorId).toBeNull();
  });

  it('should handle missing id in editPlan', () => {
    component.editPlan({ amount: 100 } as any);
    expect(component.editPlanId).toBeNull();
  });

  it('should not reset selectedOperatorId if deleting a different one', () => {
    vi.spyOn(window, 'confirm').mockReturnValue(true);
    component.selectedOperatorId = 99; // different ID
    component.deleteOperator(2);
    const req = httpMock.expectOne(`${BASE}/operators/2`);
    req.flush({});
    httpMock.expectOne(`${BASE}/operators`).flush([]);
    expect(component.selectedOperatorId).toBe(99);
  });
});
