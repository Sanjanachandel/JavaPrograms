import { Injectable, signal, computed } from '@angular/core';
import { AuthResponse, UserResponse } from '../../shared/models/models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly TOKEN_KEY = 'rn_token';
  private readonly USER_KEY  = 'rn_user';

  // Reactive signals
  private _token   = signal<string | null>(this.loadToken());
  private _user    = signal<UserResponse | null>(this.loadUser());

  readonly isLoggedIn = computed(() => !!this._token());
  readonly currentUser = computed(() => this._user());
  readonly token = computed(() => this._token());

  private loadToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  private loadUser(): UserResponse | null {
    const raw = localStorage.getItem(this.USER_KEY);
    try { return raw ? JSON.parse(raw) : null; } catch { return null; }
  }

  saveAuth(auth: AuthResponse): void {
    localStorage.setItem(this.TOKEN_KEY, auth.token);
    localStorage.setItem(this.USER_KEY, JSON.stringify(auth.user));
    this._token.set(auth.token);
    this._user.set(auth.user);
  }

  updateUser(user: UserResponse): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
    this._user.set(user);
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this._token.set(null);
    this._user.set(null);
  }

  getUserId(): number | null {
    return this._user()?.id ?? null;
  }

  getRole(): string | null {
    return this._user()?.role ?? null;
  }
}
