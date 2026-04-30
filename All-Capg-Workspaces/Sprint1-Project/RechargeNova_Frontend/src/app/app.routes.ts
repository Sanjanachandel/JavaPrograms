import { Routes } from '@angular/router';
import { authGuard, guestGuard, adminGuard, userGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  // Landing Page as default
  { 
    path: '', 
    loadComponent: () => import('./features/landing/landing.component').then(m => m.LandingComponent),
    canActivate: [guestGuard]
  },

  {
    path: 'auth',
    canActivate: [guestGuard],
    loadChildren: () =>
      import('./features/auth/auth.routes').then(m => m.AUTH_ROUTES)
  },
  {
    path: 'dashboard',
    canActivate: [userGuard],
    loadComponent: () =>
      import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent)
  },
  {
    path: 'admin',
    canActivate: [adminGuard],
    children: [
      {
        path: '',
        loadComponent: () => import('./features/admin/dashboard/admin-dashboard.component').then(m => m.AdminDashboardComponent)
      },
      {
        path: 'users',
        loadComponent: () => import('./features/admin/users/admin-users.component').then(m => m.AdminUsersComponent)
      },
      {
        path: 'operators',
        loadComponent: () => import('./features/admin/operators/admin-operators.component').then(m => m.AdminOperatorsComponent)
      },
      {
        path: 'transactions',
        loadComponent: () => import('./features/admin/transactions/admin-transactions.component').then(m => m.AdminTransactionsComponent)
      }
    ]
  },
  {
    path: 'recharge',
    canActivate: [userGuard],
    loadComponent: () =>
      import('./features/recharge/recharge.component').then(m => m.RechargeComponent)
  },
  {
    path: 'history',
    canActivate: [userGuard],
    loadComponent: () =>
      import('./features/history/history.component').then(m => m.HistoryComponent)
  },
  {
    path: 'profile',
    canActivate: [userGuard],
    loadComponent: () =>
      import('./features/profile/profile.component').then(m => m.ProfileComponent)
  },
  
  // Error pages
  {
    path: 'forbidden',
    loadComponent: () => import('./features/errors/forbidden/forbidden.component').then(m => m.ForbiddenComponent)
  },
  {
    path: 'not-found',
    loadComponent: () => import('./features/errors/not-found/not-found.component').then(m => m.NotFoundComponent)
  },
  { path: '**', redirectTo: 'not-found' }
];
