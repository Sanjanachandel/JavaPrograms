import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserApiService } from '../../../core/services/user-api.service';
import { UserResponse } from '../../../shared/models/models';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-admin-users',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-users.component.html',
  styleUrls: ['./admin-users.component.css']
})
export class AdminUsersComponent implements OnInit {
  private userApi = inject(UserApiService);
  private toast   = inject(ToastService);
  users = signal<UserResponse[]>([]);
  
  // Pagination
  currentPage = signal(0);
  totalPages  = signal(0);
  pageSize    = 10;

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers(page: number = 0) {
    this.userApi.getAllUsers(page, this.pageSize).subscribe({
      next: (res) => {
        this.users.set(res.content);
        this.totalPages.set(res.totalPages);
        this.currentPage.set(res.number);
      },
      error: () => this.toast.error('Failed to load users')
    });
  }

  nextPage() {
    if (this.currentPage() < this.totalPages() - 1) {
      this.loadUsers(this.currentPage() + 1);
    }
  }

  prevPage() {
    if (this.currentPage() > 0) {
      this.loadUsers(this.currentPage() - 1);
    }
  }
}
