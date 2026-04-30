import { Component, inject, computed } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './core/services/auth.service';
import { SidebarComponent } from './shared/components/sidebar/sidebar.component';
import { ToastComponent } from './shared/components/toast/toast.component';
import { FooterComponent } from './shared/components/footer/footer.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, SidebarComponent, ToastComponent, FooterComponent],
  template: `
    <!-- Global background orbs -->
    <div class="bg-orb bg-orb-1"></div>
    <div class="bg-orb bg-orb-2"></div>

    <!-- Toast notifications -->
    <app-toast></app-toast>

    <!-- Layout: sidebar + main -->
    @if (isLoggedIn()) {
      <div class="app-layout">
        <app-sidebar></app-sidebar>
        <main class="main-content page-enter">
          <router-outlet></router-outlet>
        </main>
      </div>
    } @else {
      <router-outlet></router-outlet>
    }

    <app-footer></app-footer>
  `
})
export class App {
  auth      = inject(AuthService);
  isLoggedIn = computed(() => this.auth.isLoggedIn());
}
