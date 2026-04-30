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
  type: 'Prepaid', category: 'All-in-One'
};

const mockOperator: OperatorDto = {
  id: 2, name: 'Jio', circle: 'All India', plans: [mockPlan],
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
    component.operatorForm = { name: 'Airtel', circle: 'Mumbai' };
    component.saveOperator();
    const req = httpMock.expectOne(`${BASE}/operators`);
    expect(req.request.method).toBe('POST');
    req.flush({ id: 3, name: 'Airtel', circle: 'Mumbai' });
    // Reload call
    httpMock.expectOne(`${BASE}/operators`).flush([mockOperator]);
    expect(toast.success).toHaveBeenCalledWith('Operator created successfully!');
  });

  it('should show error toast when operator creation fails', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    component.operatorForm = { name: 'New Op', circle: 'Mumbai' };
    component.saveOperator();
    const req = httpMock.expectOne(`${BASE}/operators`);
    req.flush({ message: 'Error' }, { status: 400, statusText: 'Bad Request' });
    expect(toast.error).toHaveBeenCalled();
  });
  it('should show error toast when operator update fails', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    component.editOperator(mockOperator);
    component.saveOperator();
    const req = httpMock.expectOne(`${BASE}/operators/2`);
    req.flush({ message: 'Error' }, { status: 400, statusText: 'Bad Request' });
    expect(toast.error).toHaveBeenCalled();
  });

  it('should show error toast when operator name is missing or too short', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    
    component.operatorForm = { name: '', circle: 'All India' };
    component.saveOperator();
    expect(toast.error).toHaveBeenCalledWith('Operator name must be at least 2 characters');

    component.operatorForm = { name: 'A', circle: 'All India' };
    component.saveOperator();
    expect(toast.error).toHaveBeenCalledWith('Operator name must be at least 2 characters');
  });

  it('should show error toast when operator circle is missing', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    
    component.operatorForm = { name: 'Airtel', circle: '' };
    component.saveOperator();
    expect(toast.error).toHaveBeenCalledWith('Operating circle is required');
  });

  it('should show error toast when operator is duplicate', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    
    // mockOperator has name 'Jio' and circle 'All India'
    component.operatorForm = { name: 'Jio', circle: 'All India' };
    component.isEditingOperator = false;
    component.saveOperator();
    expect(toast.error).toHaveBeenCalledWith('An operator with this name, type, and circle already exists!');
  });

  it('should show error toast when plan creation is duplicate', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    component.operatorForm = { name: 'Jio', circle: 'All India' }; // Same as mockOperator
    component.saveOperator();
    expect(toast.error).toHaveBeenCalledWith('An operator with this name, type, and circle already exists!');
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

  it('should not delete operator if id is undefined', () => {
    component.deleteOperator(undefined);
    expect(component.showDeleteConfirm).toBe(false);
  });

  it('should reset plan form and show it when togglePlanForm is called', () => {
    component.togglePlanForm();
    expect(component.showPlanForm).toBe(true);
    expect(component.isEditingPlan).toBe(false);
    expect(component.planForm.amount).toBe(0);
  });

  it('should delete operator and reload when confirmed', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'success');
    vi.spyOn(window, 'confirm').mockReturnValue(true);
    component.selectedOperatorId = 2;

    component.deleteOperator(2);
    // Operator uses confirmDelete modal flow - trigger confirm
    component.confirmDelete();
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
    const req = httpMock.expectOne(`${BASE}/operators/2/plans/paginated?page=0&size=10`);
    req.flush({ content: [mockPlan], totalPages: 1, totalElements: 1, number: 0 });
    expect(component.operatorPlans()).toEqual([mockPlan]);
  });

  it('should show fallback error message when plan creation fails', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    component.selectedOperatorId = 2;
    component.planForm = { category: 'Data', amount: 399, validity: 56, description: 'Test plan', type: 'Prepaid' };
    component.savePlan();
    const req = httpMock.expectOne(`${BASE}/operators/2/plans`);
    req.flush(null, { status: 500, statusText: 'Error' });
    expect(toast.error).toHaveBeenCalledWith('Http failure response for http://localhost:8989/operators/2/plans: 500 Error');
  });

  it('should update an existing plan', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'success');
    component.selectedOperatorId = 2;
    component.editPlan(mockPlan);
    component.planForm.amount = 299;
    component.savePlan();
    const req = httpMock.expectOne(`${BASE}/operators/plans/5`);
    expect(req.request.method).toBe('PUT');
    req.flush({ ...mockPlan, amount: 299 });
    httpMock.expectOne(`${BASE}/operators/2/plans/paginated?page=0&size=10`).flush({ content: [], totalElements: 0 });
    expect(toast.success).toHaveBeenCalledWith('Plan updated successfully!');
  });

  it('should delete a plan', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'success');
    component.selectedOperatorId = 2; // FIX: Ensure ID is set
    component.deletePlan(5);
    expect(component.showDeleteConfirm).toBe(true);
    expect(component.itemToDelete?.type).toBe('PLAN');
    component.confirmDelete();
    const req = httpMock.expectOne(`${BASE}/operators/plans/5`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
    httpMock.expectOne(`${BASE}/operators/2/plans/paginated?page=0&size=10`).flush({ content: [], totalElements: 0 });
    expect(toast.success).toHaveBeenCalledWith('Plan deleted');
  });

  it('should show error toast when loadPlans fails', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    component.selectedOperatorId = 2;
    component.loadPlans(0);
    const req = httpMock.expectOne(`${BASE}/operators/2/plans/paginated?page=0&size=10`);
    req.flush({}, { status: 500, statusText: 'Error' });
    expect(toast.error).toHaveBeenCalledWith('Failed to load plans');
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
    component.planForm = { category: 'Data', amount: 399, validity: 56, description: 'Data plan', type: 'Prepaid' };
    component.savePlan();
    const req = httpMock.expectOne(`${BASE}/operators/2/plans`);
    expect(req.request.method).toBe('POST');
    req.flush({ ...mockPlan, amount: 399 });
    // loadPlans re-request
    httpMock.expectOne(`${BASE}/operators/2/plans/paginated?page=0&size=10`).flush({ content: [mockPlan], totalPages: 1, totalElements: 1, number: 0 });
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
    httpMock.expectOne(`${BASE}/operators/2/plans/paginated?page=0&size=10`).flush({ content: [mockPlan], totalPages: 1, totalElements: 1, number: 0 });
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
    // Plan uses confirmDelete modal flow - trigger confirm
    component.confirmDelete();
    const req = httpMock.expectOne(`${BASE}/operators/plans/5`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
    
    // loadPlans reload
    httpMock.expectOne(`${BASE}/operators/2/plans/paginated?page=0&size=10`).flush({ content: [mockPlan], totalPages: 1, totalElements: 1, number: 0 });
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
    component.confirmDelete();
    const req = httpMock.expectOne(`${BASE}/operators/2`);
    req.flush({});
    httpMock.expectOne(`${BASE}/operators`).flush([]);
    expect(component.selectedOperatorId).toBe(99);
  });

  // ─── cancelDelete ─────────────────────────────────────────────────────────
  it('should hide delete confirm modal on cancelDelete', () => {
    component.showDeleteConfirm = true;
    component.itemToDelete = { id: 1, type: 'OPERATOR' };
    component.cancelDelete();
    expect(component.showDeleteConfirm).toBe(false);
    expect(component.itemToDelete).toBeNull();
  });

  it('should do nothing on confirmDelete when itemToDelete is null', () => {
    component.itemToDelete = null;
    component.confirmDelete();
    httpMock.expectNone(`${BASE}/operators/2`);
  });

  // ─── consoleLog ───────────────────────────────────────────────────────────
  it('should call consoleLog without errors', () => {
    const consoleSpy = vi.spyOn(console, 'log').mockImplementation(() => {});
    component.consoleLog('test message');
    expect(consoleSpy).toHaveBeenCalledWith('[DEBUG]:', 'test message');
    consoleSpy.mockRestore();
  });

  // ─── Plan creation error path ─────────────────────────────────────────────
  it('should show error toast when plan creation fails', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    component.selectedOperatorId = 2;
    component.planForm = { category: 'Data', amount: 399, validity: 56, description: 'Test plan', type: 'Prepaid' };
    component.savePlan();
    const req = httpMock.expectOne(`${BASE}/operators/2/plans`);
    req.flush({ message: 'Error' }, { status: 400, statusText: 'Bad Request' });
    expect(toast.error).toHaveBeenCalled();
  });

  // ─── Plan update error path ────────────────────────────────────────────────
  it('should show error toast when plan update fails', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    component.selectedOperatorId = 2;
    component.editPlan(mockPlan);
    component.planForm = { ...component.planForm, amount: 499 } as any;
    component.savePlan();
    const req = httpMock.expectOne(`${BASE}/operators/plans/5`);
    req.flush({ message: 'Error' }, { status: 400, statusText: 'Bad Request' });
    expect(toast.error).toHaveBeenCalled();
  });

  // ─── Operator update error path ────────────────────────────────────────────
  it('should show error toast when operator update fails', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    component.editOperator(mockOperator);
    component.operatorForm.name = 'Jio Updated';
    component.saveOperator();
    const req = httpMock.expectOne(`${BASE}/operators/2`);
    req.flush({ message: 'Error' }, { status: 400, statusText: 'Bad Request' });
    expect(toast.error).toHaveBeenCalled();
  });

  // ─── Plan pagination ──────────────────────────────────────────────────────
  it('should load next plan page on nextPlanPage when not on last page', () => {
    component.selectedOperatorId = 2;
    component.currentPlanPage.set(0);
    component.totalPlanPages.set(3);
    component.nextPlanPage();
    const req = httpMock.expectOne(`${BASE}/operators/2/plans/paginated?page=1&size=10`);
    req.flush({ content: [], totalPages: 3, totalElements: 0, number: 1 });
  });

  it('should NOT load next plan page when already on last page', () => {
    component.selectedOperatorId = 2;
    component.currentPlanPage.set(2);
    component.totalPlanPages.set(3);
    component.nextPlanPage();
    httpMock.expectNone(`${BASE}/operators/2/plans/paginated?page=3&size=10`);
  });

  it('should load prev plan page on prevPlanPage when not on first page', () => {
    component.selectedOperatorId = 2;
    component.currentPlanPage.set(2);
    component.totalPlanPages.set(3);
    component.prevPlanPage();
    const req = httpMock.expectOne(`${BASE}/operators/2/plans/paginated?page=1&size=10`);
    req.flush({ content: [], totalPages: 3, totalElements: 0, number: 1 });
  });

  it('should NOT load prev plan page when already on first page', () => {
    component.selectedOperatorId = 2;
    component.currentPlanPage.set(0);
    component.prevPlanPage();
    httpMock.expectNone(`${BASE}/operators/2/plans/paginated?page=-1&size=10`);
  });

  it('should show error toast when plan creation is duplicate', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    component.selectedOperatorId = 2;
    component.operatorPlans.set([mockPlan]);  // mockPlan has type:'Prepaid', category:'All-in-One'
    // Same amount, validity, category, type as mockPlan
    component.planForm = { category: 'All-in-One', amount: 199, validity: 28, description: 'All-in-One plan', type: 'Prepaid' };
    component.savePlan();
    expect(toast.error).toHaveBeenCalledWith('A plan with this amount, validity, and category already exists!');
  });

  // ─── Plan Form Validation ──────────────────────────────────────────────────
  it('should show error toast for invalid plan amount', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    component.selectedOperatorId = 2;
    component.planForm.amount = 0;
    component.savePlan();
    expect(toast.error).toHaveBeenCalledWith('Please enter a valid amount (> 0)');
  });

  it('should show error toast for invalid plan validity', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    component.selectedOperatorId = 2;
    component.planForm.amount = 199;
    component.planForm.validity = 0;
    component.savePlan();
    expect(toast.error).toHaveBeenCalledWith('Please enter a valid validity (> 0)');
  });

  it('should show error toast for short plan description', () => {
    const toast = TestBed.inject(ToastService);
    vi.spyOn(toast, 'error');
    component.selectedOperatorId = 2;
    component.planForm.amount = 199;
    component.planForm.validity = 28;
    component.planForm.description = 'abc';
    component.savePlan();
    expect(toast.error).toHaveBeenCalledWith('Description must be at least 5 characters');
  });
});

