import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { OperatorDto, PlanDto, PaginatedResponse } from '../../shared/models/models';
import { AuthService } from './auth.service';

const BASE = environment.apiBaseUrl;

@Injectable({ providedIn: 'root' })
export class OperatorApiService {
  private auth = inject(AuthService);

  constructor(private http: HttpClient) {}

  private getHeaders() {
    const role = this.auth.getRole() || '';
    return new HttpHeaders().set('X-User-Role', role);
  }

  getAllOperators(): Observable<OperatorDto[]> {
    return this.http.get<OperatorDto[]>(`${BASE}/operators`);
  }

  getOperatorCount(): Observable<number> {
    return this.http.get<number>(`${BASE}/operators/count`);
  }

  getPlanCount(): Observable<number> {
    return this.http.get<number>(`${BASE}/operators/plans/count`);
  }

  getOperatorById(id: number): Observable<OperatorDto> {
    return this.http.get<OperatorDto>(`${BASE}/operators/${id}`);
  }

  getPlanById(planId: number): Observable<PlanDto> {
    return this.http.get<PlanDto>(`${BASE}/operators/plans/${planId}`);
  }

  createOperator(data: Partial<OperatorDto>): Observable<OperatorDto> {
    return this.http.post<OperatorDto>(`${BASE}/operators`, data, { headers: this.getHeaders() });
  }

  updateOperator(id: number, data: Partial<OperatorDto>): Observable<OperatorDto> {
    return this.http.put<OperatorDto>(`${BASE}/operators/${id}`, data, { headers: this.getHeaders() });
  }

  deleteOperator(id: number): Observable<string> {
    return this.http.delete(`${BASE}/operators/${id}`, { headers: this.getHeaders(), responseType: 'text' });
  }

  createPlan(operatorId: number, data: Partial<PlanDto>): Observable<PlanDto> {
    return this.http.post<PlanDto>(`${BASE}/operators/${operatorId}/plans`, data, { headers: this.getHeaders() });
  }

  updatePlan(planId: number, data: Partial<PlanDto>): Observable<PlanDto> {
    return this.http.put<PlanDto>(`${BASE}/operators/plans/${planId}`, data, { headers: this.getHeaders() });
  }

  deletePlan(planId: number): Observable<string> {
    return this.http.delete(`${BASE}/operators/plans/${planId}`, { headers: this.getHeaders(), responseType: 'text' });
  }

  getPlansByOperatorPaginated(operatorId: number, page: number = 0, size: number = 10): Observable<PaginatedResponse<PlanDto>> {
    return this.http.get<PaginatedResponse<PlanDto>>(`${BASE}/operators/${operatorId}/plans/paginated?page=${page}&size=${size}`);
  }
}


