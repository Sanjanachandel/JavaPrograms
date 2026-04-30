import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { environment } from '../../../environments/environment';
import { RechargeRequest, RechargeResponse, PaginatedResponse } from '../../shared/models/models';

const BASE = environment.apiBaseUrl;

@Injectable({ providedIn: 'root' })
export class RechargeApiService {
  constructor(private http: HttpClient, private auth: AuthService) {}

  initiateRecharge(userId: number, payload: RechargeRequest): Observable<RechargeResponse> {
    return this.http.post<RechargeResponse>(
      `${BASE}/recharges`,
      payload,
      { headers: this.headers(userId) }
    );
  }

  getRechargeById(id: number): Observable<RechargeResponse> {
    return this.http.get<RechargeResponse>(`${BASE}/recharges/${id}`, {
      headers: this.bearerHeaders()
    });
  }

  getRechargesByUserId(userId: number, page: number = 0, size: number = 10): Observable<PaginatedResponse<RechargeResponse>> {
    return this.http.get<PaginatedResponse<RechargeResponse>>(`${BASE}/recharges/user/${userId}?page=${page}&size=${size}`, {
      headers: this.bearerHeaders()
    });
  }

  getAllRecharges(page: number = 0, size: number = 10): Observable<PaginatedResponse<RechargeResponse>> {
    return this.http.get<PaginatedResponse<RechargeResponse>>(`${BASE}/recharges?page=${page}&size=${size}`, {
      headers: this.bearerHeaders()
    });
  }

  getRechargeCount(): Observable<number> {
    return this.http.get<number>(`${BASE}/recharges/count`);
  }

  getTotalRevenue(): Observable<number> {
    return this.http.get<number>(`${BASE}/recharges/revenue`);
  }

  private headers(userId: number): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Bearer ${this.auth.token() ?? ''}`,
      'X-User-Id': userId.toString()
    });
  }

  private bearerHeaders(): HttpHeaders {
    return new HttpHeaders({ Authorization: `Bearer ${this.auth.token() ?? ''}` });
  }
}
