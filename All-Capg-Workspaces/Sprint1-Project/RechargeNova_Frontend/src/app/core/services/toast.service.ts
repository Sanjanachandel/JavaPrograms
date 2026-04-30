import { Injectable, signal } from '@angular/core';
import { Toast, ToastType } from '../../shared/models/models';

@Injectable({ providedIn: 'root' })
export class ToastService {
  readonly toasts = signal<Toast[]>([]);

  show(type: ToastType, message: string, durationMs = 3000): void {
    const id = `${Date.now()}-${Math.random().toString(36).substring(2, 9)}`;
    this.toasts.update(list => [...list, { id, type, message }]);
    setTimeout(() => this.remove(id), durationMs);
  }

  remove(id: string): void {
    this.toasts.update(list => list.filter(t => t.id !== id));
  }

  success(message: string) { this.show('success', message); }
  error(message: string)   { this.show('error', message); }
  info(message: string)    { this.show('info', message); }
}
