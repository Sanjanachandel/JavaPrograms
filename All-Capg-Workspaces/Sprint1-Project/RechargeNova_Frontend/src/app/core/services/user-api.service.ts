import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { environment } from '../../../environments/environment';
import {
  AuthResponse, LoginRequest, UserRegistrationRequest, UserResponse, PaginatedResponse
} from '../../shared/models/models';

const BASE = environment.apiBaseUrl;

@Injectable({ providedIn: 'root' })
export class UserApiService {
  constructor(private http: HttpClient, private auth: AuthService) {}

  register(payload: UserRegistrationRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(`${BASE}/users/register`, payload);
  }

  login(payload: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${BASE}/users/login`, payload);
  }

  getUserById(id: number): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${BASE}/users/${id}`, {
      headers: this.authHeaders()
    });
  }

  getAllUsers(page: number = 0, size: number = 10): Observable<PaginatedResponse<UserResponse>> {
    return this.http.get<PaginatedResponse<UserResponse>>(`${BASE}/users?page=${page}&size=${size}`, {
      headers: this.authHeaders()
    });
  }

  getUserCount(): Observable<number> {
    return this.http.get<number>(`${BASE}/users/count`);
  }

  uploadProfilePicture(userId: number, file: File): Observable<{ profilePictureUrl: string }> {
    const form = new FormData();
    form.append('picture', file);
    return this.http.put<{ profilePictureUrl: string }>(
      `${BASE}/users/profile/picture`,
      form,
      { headers: new HttpHeaders({ 'X-User-Id': userId.toString() }) }
    );
  }

  /** Step 1: Send OTP to user's email */
  forgotPassword(email: string): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${BASE}/users/forgot-password`, { email });
  }

  /** Step 2: Verify OTP + set new password */
  resetPassword(payload: { email: string; otp: string; newPassword: string }): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${BASE}/users/reset-password`, payload);
  }

  private authHeaders(): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Bearer ${this.auth.token() ?? ''}`
    });
  }
}
