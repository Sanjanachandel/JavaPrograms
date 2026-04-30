import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { App } from './app';
import { AuthService } from './core/services/auth.service';

const mockUser = {
  id: 1, name: 'Sanjana', email: 'sanjana@example.com',
  role: 'ROLE_USER', phoneNumber: '9876543210', createdAt: '2024-01-01T00:00:00Z',
};

describe('App (Root Component)', () => {
  let component: App;
  let fixture: ComponentFixture<App>;
  let authService: AuthService;

  beforeEach(async () => {
    localStorage.clear();
    TestBed.resetTestingModule();
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        AuthService,
      ],
    }).compileComponents();

    authService = TestBed.inject(AuthService);
    fixture     = TestBed.createComponent(App);
    component   = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    localStorage.clear();
    TestBed.resetTestingModule();
  });

  // ─── Creation ─────────────────────────────────────────────────────────────
  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  // ─── isLoggedIn computed ──────────────────────────────────────────────────
  it('should have isLoggedIn = false when no token', () => {
    expect(component.isLoggedIn()).toBe(false);
  });

  it('should have isLoggedIn = true after saveAuth', () => {
    authService.saveAuth({ token: 'jwt', user: mockUser as any });
    expect(component.isLoggedIn()).toBe(true);
  });

  it('should have isLoggedIn = false after logout', () => {
    authService.saveAuth({ token: 'jwt', user: mockUser as any });
    authService.logout();
    expect(component.isLoggedIn()).toBe(false);
  });

  // ─── auth service binding ─────────────────────────────────────────────────
  it('should expose auth service instance', () => {
    expect(component.auth).toBeTruthy();
  });
});
