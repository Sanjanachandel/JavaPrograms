import { Component, inject, signal, OnInit, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RechargeApiService } from '../../core/services/recharge-api.service';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';
import { RechargeResponse } from '../../shared/models/models';

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {
  private rechargeSvc = inject(RechargeApiService);
  private auth        = inject(AuthService);
  private toast       = inject(ToastService);

  loading      = signal(true);
  recharges    = signal<RechargeResponse[]>([]);
  activeFilter = signal<string>('ALL');
  
  // Pagination
  currentPage = signal(0);
  totalPages  = signal(0);
  pageSize    = 10;

  filters = [
    { label: 'All',     value: 'ALL'     },
    { label: 'Success', value: 'SUCCESS' },
    { label: 'Failed',  value: 'FAILED'  },
  ];

  filtered = computed(() => {
    const f = this.activeFilter();
    if (f === 'ALL') return this.recharges();
    return this.recharges().filter(r => r.status === f);
  });

  totalShown   = computed(() => this.filtered().reduce((s, r) => s + Number(r.amount), 0).toFixed(0));
  successShown = computed(() => this.filtered().filter(r => r.status === 'SUCCESS').length);

  ngOnInit(): void {
    this.loadHistory();
  }

  loadHistory(page: number = 0): void {
    const uid = this.auth.getUserId();
    if (!uid) { this.loading.set(false); return; }
    
    this.loading.set(true);
    this.rechargeSvc.getRechargesByUserId(uid, page, this.pageSize).subscribe({
      next: (res) => { 
        const transformed = res.content.map(r => ({
          ...r,
          paymentMethod: r.paymentMethod || 'UPI',
          createdAt: this.ensureIst(r.createdAt)
        }));
        this.recharges.set(transformed); 
        this.totalPages.set(res.totalPages);
        this.currentPage.set(res.number);
        this.loading.set(false); 
      },
      error: ()    => { 
        this.toast.error('Failed to load history.'); 
        this.loading.set(false); 
      }
    });
  }

  nextPage() {
    if (this.currentPage() < this.totalPages() - 1) {
      this.loadHistory(this.currentPage() + 1);
    }
  }

  prevPage() {
    if (this.currentPage() > 0) {
      this.loadHistory(this.currentPage() - 1);
    }
  }

  private ensureIst(dateStr: string): string {
    if (!dateStr) return '';
    // If it doesn't have a timezone, assume it's UTC from the server
    const isoStr = (dateStr.includes('Z') || dateStr.includes('+')) ? dateStr : dateStr.replace(' ', 'T') + 'Z';
    const date = new Date(isoStr);
    // Add 5.5 hours explicitly if we want to be absolute, 
    // but Date object already converts UTC to local.
    // If it's still wrong, the server time might be saved as "Local" 5:05 AM in a UTC container.
    return date.toISOString();
  }
}
