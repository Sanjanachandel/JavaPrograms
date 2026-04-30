import { Component, inject, signal, OnInit, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { RechargeApiService } from '../../core/services/recharge-api.service';
import { RechargeResponse, PaginatedResponse } from '../../shared/models/models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  auth         = inject(AuthService);
  rechargeSvc  = inject(RechargeApiService);

  loading     = signal(true);
  recharges   = signal<RechargeResponse[]>([]);

  recentRecharges = computed(() => this.recharges().slice(0, 5));
  totalRecharges  = computed(() => this.recharges().length);
  successCount    = computed(() => this.recharges().filter(r => r.status === 'SUCCESS').length);
  totalSpent      = computed(() => {
    return this.recharges()
      .filter(r => r.status === 'SUCCESS')
      .reduce((sum, r) => sum + Number(r.amount), 0)
      .toFixed(0);
  });
  lastMobile = computed(() => {
    const r = this.recharges()[0];
    return r ? r.mobileNumber : '—';
  });

  ngOnInit(): void {
    const uid = this.auth.getUserId();
    if (!uid) { this.loading.set(false); return; }
    this.rechargeSvc.getRechargesByUserId(uid).subscribe({
      next: (data: PaginatedResponse<RechargeResponse>) => { 
        // 1. Transform dates to ensure IST
        // 2. Sort newest first
        const transformed = data.content.map((r: RechargeResponse) => ({
          ...r,
          createdAt: this.ensureIst(r.createdAt)
        })).sort((a: RechargeResponse, b: RechargeResponse) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());

        this.recharges.set(transformed); 
        this.loading.set(false); 
      },
      error: ()    => { this.loading.set(false); }
    });
  }

  private ensureIst(dateStr: string): string {
    if (!dateStr) return '';
    const isoStr = (dateStr.includes('Z') || dateStr.includes('+')) ? dateStr : dateStr.replace(' ', 'T') + 'Z';
    return new Date(isoStr).toISOString();
  }

  greeting(): string {
    const h = new Date().getHours();
    if (h < 12) return 'morning';
    if (h < 17) return 'afternoon';
    return 'evening';
  }

  firstName(): string {
    return this.auth.currentUser()?.name?.split(' ')[0] ?? 'User';
  }

  statusBadge(status: string): string {
    return status === 'SUCCESS' ? 'badge-success' : 'badge-error';
  }
}
