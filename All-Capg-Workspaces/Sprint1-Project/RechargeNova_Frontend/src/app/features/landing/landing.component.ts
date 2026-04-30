import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { catchError, EMPTY } from 'rxjs';
import { ThemeService } from '../../core/services/theme.service';
import { AuthService } from '../../core/services/auth.service';
import { OperatorApiService } from '../../core/services/operator-api.service';
import { UserApiService } from '../../core/services/user-api.service';
import { RechargeApiService } from '../../core/services/recharge-api.service';


@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent implements OnInit {
  themeSvc = inject(ThemeService);
  features = [
    { icon: '⚡', title: 'Instant Recharge', desc: 'Recharge any Indian mobile number in under 2 seconds with real-time confirmation.', bg: 'rgba(124,58,237,0.15)' },
    { icon: '🔒', title: 'JWT Security', desc: 'All transactions secured with industry-standard JWT tokens and Spring Security.', bg: 'rgba(6,182,212,0.15)' },
    { icon: '📡', title: 'All Operators', desc: 'Supports Airtel, Jio, Vi, BSNL and all major Indian telecom operators.', bg: 'rgba(16,185,129,0.15)' },
    { icon: '💳', title: 'Multiple Payments', desc: 'Pay via UPI, Credit/Debit Card, Net Banking or Wallet — your choice.', bg: 'rgba(245,158,11,0.15)' },
    { icon: '📊', title: 'Recharge History', desc: 'Track all your past transactions in a beautiful, filterable timeline view.', bg: 'rgba(239,68,68,0.15)' },
    { icon: '🔔', title: 'Email Alerts', desc: 'Get instant email notifications on successful recharges via RabbitMQ events.', bg: 'rgba(168,85,247,0.15)' },
  ];

  steps = [
    { num: '01', icon: '📱', title: 'Enter Mobile', desc: 'Type your 10-digit Indian mobile number to get started.' },
    { num: '02', icon: '📡', title: 'Pick Operator', desc: 'Select your network — Jio, Airtel, Vi, BSNL or others.' },
    { num: '03', icon: '📋', title: 'Choose Plan', desc: 'Browse plans by category — All-in-One, Data, or Talktime.' },
    { num: '04', icon: '💰', title: 'Pay & Done!', desc: 'Select payment method, confirm, and receive instant confirmation.' },
  ];

  supportedOperators = signal<{ icon: string, name: string }[]>([]);

  getIconForOperator(name: string): string {
    const n = name.toLowerCase();
    if (n.includes('jio')) return '📱';
    if (n.includes('airtel')) return '📡';
    if (n.includes('vi') || n.includes('vodafone')) return '📶';
    if (n.includes('bsnl')) return '📠';
    if (n.includes('mtnl')) return '📞';
    if (n.includes('tata')) return '💫';
    if (n.includes('sun')) return '☀️';
    return '🔌';
  }

  private auth = inject(AuthService);
  private router = inject(Router);
  private operatorApi = inject(OperatorApiService);
  private userApi = inject(UserApiService);
  private rechargeApi = inject(RechargeApiService);

  operatorCount = signal<number>(0);
  planCount = signal<number>(0);
  userCount = signal<number>(0);
  rechargeCount = signal<number>(0);

  ngOnInit() {
    // 1. Fetch lightweight counts
    this.operatorApi.getOperatorCount().pipe(
      catchError(err => { console.warn('[Landing] Failed to fetch operator count:', err.status, err.message); return EMPTY; })
    ).subscribe(c => this.operatorCount.set(c));

    this.operatorApi.getPlanCount().pipe(
      catchError(err => { console.warn('[Landing] Failed to fetch plan count:', err.status, err.message); return EMPTY; })
    ).subscribe(c => this.planCount.set(c));

    this.userApi.getUserCount().pipe(
      catchError(err => { console.warn('[Landing] Failed to fetch user count:', err.status, err.message); return EMPTY; })
    ).subscribe(c => this.userCount.set(c));

    this.rechargeApi.getRechargeCount().pipe(
      catchError(err => { console.warn('[Landing] Failed to fetch recharge count:', err.status, err.message); return EMPTY; })
    ).subscribe(c => this.rechargeCount.set(c));

    // 2. Fetch operators specifically for the "Supported Networks" grid
    this.operatorApi.getAllOperators().pipe(
      catchError(err => { console.warn('[Landing] Failed to fetch operators list:', err.status, err.message); return EMPTY; })
    ).subscribe(ops => {
      const dynamicOps = ops.map(op => ({
        icon: this.getIconForOperator(op.name),
        name: op.name
      }));

      const uniqueOps = Array.from(new Set(dynamicOps.map(a => a.name)))
        .map(name => dynamicOps.find(a => a.name === name)!)
        .slice(0, 8);

      this.supportedOperators.set(uniqueOps);
    });
  }

  onLogoClick() {
    if (this.auth.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
    } else {
      this.router.navigate(['/']);
    }
  }
}
