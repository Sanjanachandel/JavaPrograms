import { Component, inject, signal, OnInit, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RechargeApiService } from '../../../core/services/recharge-api.service';
import { RechargeResponse } from '../../../shared/models/models';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-admin-transactions',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-transactions.component.html',
  styleUrls: ['./admin-transactions.component.css']
})
export class AdminTransactionsComponent implements OnInit {
  private rechargeApi = inject(RechargeApiService);
  private toast       = inject(ToastService);

  transactions = signal<RechargeResponse[]>([]);
  loading = signal(false);
  searchQuery = signal('');
  
  // Pagination
  currentPage = signal(0);
  totalPages  = signal(0);
  pageSize    = 10;

  filteredTransactions = computed(() => {
    const term = this.searchQuery().toLowerCase();
    if (!term) return this.transactions();
    return this.transactions().filter(t => {
      const userIdStr = `user #${t.userId}`;
      return t.mobileNumber.includes(term) || 
             t.userId.toString().includes(term) ||
             userIdStr.includes(term) ||
             t.status.toLowerCase().includes(term) ||
             t.amount.toString().includes(term)
    });
  });

  ngOnInit() {
    this.loadTransactions();
  }

  loadTransactions(page: number = 0) {
    this.loading.set(true);
    this.rechargeApi.getAllRecharges(page, this.pageSize).subscribe({
      next: (res) => {
        const transformed = res.content.map(r => ({
          ...r,
          paymentMethod: r.paymentMethod || 'UPI',
          createdAt: this.ensureIst(r.createdAt)
        }));
        this.transactions.set(transformed);
        this.totalPages.set(res.totalPages);
        this.currentPage.set(res.number);
        this.loading.set(false);
      },
      error: () => {
        this.toast.error('Failed to load transactions');
        this.loading.set(false);
      }
    });
  }

  nextPage() {
    if (this.currentPage() < this.totalPages() - 1) {
      this.loadTransactions(this.currentPage() + 1);
    }
  }

  prevPage() {
    if (this.currentPage() > 0) {
      this.loadTransactions(this.currentPage() - 1);
    }
  }

  private ensureIst(dateStr: string): string {
    if (!dateStr) return '';
    const isoStr = (dateStr.includes('Z') || dateStr.includes('+')) ? dateStr : dateStr.replace(' ', 'T') + 'Z';
    return new Date(isoStr).toISOString();
  }
}
