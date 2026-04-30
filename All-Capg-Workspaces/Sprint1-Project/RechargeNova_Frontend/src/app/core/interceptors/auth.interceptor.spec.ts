import { TestBed } from '@angular/core/testing';
import { provideHttpClient, withInterceptors, HttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { authInterceptor } from './auth.interceptor';
import { AuthService } from '../services/auth.service';
import { signal } from '@angular/core';
import { vi } from 'vitest';

describe('authInterceptor', () => {
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let authServiceMock: any;

  beforeEach(() => {
    authServiceMock = {
      token: signal(null)
    };

    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
        { provide: AuthService, useValue: authServiceMock }
      ]
    });

    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should not add Authorization header for public login path', () => {
    authServiceMock.token.set('some-token');
    
    httpClient.get('/api/users/login').subscribe();

    const req = httpMock.expectOne('/api/users/login');
    expect(req.request.headers.has('Authorization')).toBe(false);
  });

  it('should not add Authorization header when token is missing', () => {
    authServiceMock.token.set(null);
    
    httpClient.get('/api/protected').subscribe();

    const req = httpMock.expectOne('/api/protected');
    expect(req.request.headers.has('Authorization')).toBe(false);
  });

  it('should add Authorization header for protected paths when token exists', () => {
    authServiceMock.token.set('test-token');
    
    httpClient.get('/api/recharges').subscribe();

    const req = httpMock.expectOne('/api/recharges');
    expect(req.request.headers.has('Authorization')).toBe(true);
    expect(req.request.headers.get('Authorization')).toBe('Bearer test-token');
  });

  it('should not add Authorization header for public register path', () => {
    authServiceMock.token.set('some-token');
    
    httpClient.get('/api/users/register').subscribe();

    const req = httpMock.expectOne('/api/users/register');
    expect(req.request.headers.has('Authorization')).toBe(false);
  });
});
