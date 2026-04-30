import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = () => {
  const auth   = inject(AuthService);
  const router = inject(Router);
  if (auth.isLoggedIn()) return true;
  router.navigate(['/auth/login']);
  return false;
};

export const guestGuard: CanActivateFn = () => {
  const auth   = inject(AuthService);
  const router = inject(Router);
  
  if (!auth.isLoggedIn()) return true;

  const user = auth.currentUser();
  if (user?.role === 'ADMIN' || user?.role === 'ROLE_ADMIN') {
    router.navigate(['/admin']);
  } else {
    router.navigate(['/dashboard']);
  }
  
  return false;
};

export const adminGuard: CanActivateFn = () => {
  const auth   = inject(AuthService);
  const router = inject(Router);
  const user   = auth.currentUser();
  
  if (auth.isLoggedIn() && (user?.role === 'ADMIN' || user?.role === 'ROLE_ADMIN')) {
    return true;
  }
  
  router.navigate(['/forbidden']);
  return false;
};

export const userGuard: CanActivateFn = () => {
  const auth   = inject(AuthService);
  const router = inject(Router);
  const user   = auth.currentUser();
  
  if (auth.isLoggedIn() && user?.role === 'ROLE_USER') {
    return true;
  }
  
  router.navigate(['/forbidden']);
  return false;
};
