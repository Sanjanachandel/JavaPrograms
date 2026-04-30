import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { OperatorApiService } from './operator-api.service';
import { environment } from '../../../environments/environment';
import { OperatorDto, PlanDto } from '../../shared/models/models';

const BASE = environment.apiBaseUrl;

const mockPlan: PlanDto = {
  id: 5,
  operatorId: 2,
  amount: 199,
  validity: 28,
  description: 'All-in-One plan',
};

const mockOperator: OperatorDto = {
  id: 2,
  name: 'Jio',
  type: 'Prepaid',
  circle: 'All India',
  plans: [mockPlan],
};

describe('OperatorApiService', () => {
  let service: OperatorApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        OperatorApiService,
      ],
    });
    service  = TestBed.inject(OperatorApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    TestBed.resetTestingModule();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // ─── getAllOperators ──────────────────────────────────────────────────────
  it('should GET /operators', () => {
    service.getAllOperators().subscribe(res => {
      expect(res).toEqual([mockOperator]);
    });
    const req = httpMock.expectOne(`${BASE}/operators`);
    expect(req.request.method).toBe('GET');
    req.flush([mockOperator]);
  });

  // ─── getOperatorById ─────────────────────────────────────────────────────
  it('should GET /operators/2', () => {
    service.getOperatorById(2).subscribe(res => {
      expect(res).toEqual(mockOperator);
    });
    const req = httpMock.expectOne(`${BASE}/operators/2`);
    expect(req.request.method).toBe('GET');
    req.flush(mockOperator);
  });

  // ─── getPlanById ─────────────────────────────────────────────────────────
  it('should GET /operators/plans/5', () => {
    service.getPlanById(5).subscribe(res => {
      expect(res).toEqual(mockPlan);
    });
    const req = httpMock.expectOne(`${BASE}/operators/plans/5`);
    expect(req.request.method).toBe('GET');
    req.flush(mockPlan);
  });

  // ─── createOperator ──────────────────────────────────────────────────────
  it('should POST to /operators', () => {
    const payload: Partial<OperatorDto> = { name: 'Airtel', type: 'Prepaid', circle: 'Mumbai' };
    service.createOperator(payload).subscribe(res => {
      expect(res.name).toBe('Airtel');
    });
    const req = httpMock.expectOne(`${BASE}/operators`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush({ ...mockOperator, name: 'Airtel' });
  });

  // ─── updateOperator ──────────────────────────────────────────────────────
  it('should PUT to /operators/2', () => {
    const patch: Partial<OperatorDto> = { name: 'Jio Updated' };
    service.updateOperator(2, patch).subscribe(res => {
      expect(res.name).toBe('Jio Updated');
    });
    const req = httpMock.expectOne(`${BASE}/operators/2`);
    expect(req.request.method).toBe('PUT');
    req.flush({ ...mockOperator, name: 'Jio Updated' });
  });

  // ─── deleteOperator ──────────────────────────────────────────────────────
  it('should DELETE /operators/2', () => {
    service.deleteOperator(2).subscribe(res => {
      expect(res).toBe('Deleted');
    });
    const req = httpMock.expectOne(`${BASE}/operators/2`);
    expect(req.request.method).toBe('DELETE');
    req.flush('Deleted');
  });

  // ─── createPlan ──────────────────────────────────────────────────────────
  it('should POST to /operators/2/plans', () => {
    const payload: Partial<PlanDto> = { amount: 299, validity: 28, description: 'Data plan' };
    service.createPlan(2, payload).subscribe(res => {
      expect(res.amount).toBe(299);
    });
    const req = httpMock.expectOne(`${BASE}/operators/2/plans`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush({ ...mockPlan, amount: 299 });
  });

  // ─── updatePlan ──────────────────────────────────────────────────────────
  it('should PUT to /operators/plans/5', () => {
    const patch: Partial<PlanDto> = { amount: 399 };
    service.updatePlan(5, patch).subscribe(res => {
      expect(res.amount).toBe(399);
    });
    const req = httpMock.expectOne(`${BASE}/operators/plans/5`);
    expect(req.request.method).toBe('PUT');
    req.flush({ ...mockPlan, amount: 399 });
  });

  // ─── deletePlan ──────────────────────────────────────────────────────────
  it('should DELETE /operators/plans/5', () => {
    service.deletePlan(5).subscribe(res => {
      expect(res).toBe('Plan deleted');
    });
    const req = httpMock.expectOne(`${BASE}/operators/plans/5`);
    expect(req.request.method).toBe('DELETE');
    req.flush('Plan deleted');
  });
});
