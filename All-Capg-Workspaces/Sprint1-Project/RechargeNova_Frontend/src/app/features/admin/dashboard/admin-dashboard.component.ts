import { Component, inject, signal, OnInit, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { UserApiService } from '../../../core/services/user-api.service';
import { RechargeApiService } from '../../../core/services/recharge-api.service';
import { OperatorApiService } from '../../../core/services/operator-api.service';
import { UserResponse, RechargeResponse, OperatorDto } from '../../../shared/models/models';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  private userApi = inject(UserApiService);
  private rechargeApi = inject(RechargeApiService);
  private operatorApi = inject(OperatorApiService);

  users     = signal<UserResponse[]>([]);
  recharges = signal<RechargeResponse[]>([]);
  operators = signal<OperatorDto[]>([]);
  
  // Dashboard Stats
  userCount = signal(0);
  rechargeCount = signal(0);
  revenue = signal(0);
  operatorCount = signal(0);

  totalVolume = computed(() => {
    return this.recharges()
      .filter(r => r.status === 'SUCCESS')
      .reduce((sum, r) => sum + r.amount, 0);
  });

  newUsersToday = computed(() => {
    const today = new Date().toISOString().split('T')[0];
    return this.users().filter(u => u.createdAt?.startsWith(today)).length;
  });

  recentRecharges = computed(() => {
    return this.recharges().slice(0, 4);
  });

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    // Stats
    this.userApi.getUserCount().subscribe(c => this.userCount.set(c));
    this.rechargeApi.getRechargeCount().subscribe(c => this.rechargeCount.set(c));
    this.rechargeApi.getTotalRevenue().subscribe(r => this.revenue.set(r));
    this.operatorApi.getOperatorCount().subscribe(c => this.operatorCount.set(c));

    // Recent lists (first 10)
    this.userApi.getAllUsers(0, 10).subscribe(res => this.users.set(res.content));
    this.rechargeApi.getAllRecharges(0, 10).subscribe(res => this.recharges.set(res.content));
    this.operatorApi.getAllOperators().subscribe(data => this.operators.set(data));
  }
}
