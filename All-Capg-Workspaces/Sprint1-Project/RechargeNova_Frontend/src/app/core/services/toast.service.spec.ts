import { TestBed } from '@angular/core/testing';
import { ToastService } from './toast.service';

describe('ToastService', () => {
  let service: ToastService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ToastService);
  });

  afterEach(() => {
    TestBed.resetTestingModule();
  });

  // ─── Creation ───────────────────────────────────────────────────────────
  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should start with an empty toasts array', () => {
    expect(service.toasts()).toEqual([]);
  });

  // ─── show ───────────────────────────────────────────────────────────────
  it('should add a toast when show() is called', () => {
    service.show('success', 'Recharge successful!');
    expect(service.toasts().length).toBe(1);
    expect(service.toasts()[0].message).toBe('Recharge successful!');
    expect(service.toasts()[0].type).toBe('success');
  });

  it('should assign a unique string id to each toast', () => {
    service.show('info', 'First message');
    service.show('info', 'Second message');
    const ids = service.toasts().map(t => t.id);
    expect(ids[0]).not.toBe(ids[1]);
  });

  it('should stack multiple toasts', () => {
    service.show('success', 'Msg 1');
    service.show('error', 'Msg 2');
    service.show('info', 'Msg 3');
    expect(service.toasts().length).toBe(3);
  });

  // ─── remove ─────────────────────────────────────────────────────────────
  it('should remove a toast by id', () => {
    service.show('success', 'To be removed');
    const id = service.toasts()[0].id;
    service.remove(id);
    expect(service.toasts().length).toBe(0);
  });

  it('should only remove the toast with matching id', () => {
    service.show('success', 'Keep me');
    service.show('error', 'Remove me');
    const removeId = service.toasts()[1].id;
    service.remove(removeId);
    expect(service.toasts().length).toBe(1);
    expect(service.toasts()[0].message).toBe('Keep me');
  });

  it('should not throw when removing a non-existent id', () => {
    service.show('info', 'Some toast');
    expect(() => service.remove('non-existent-id')).not.toThrow();
    expect(service.toasts().length).toBe(1);
  });

  // ─── Shortcut helpers ───────────────────────────────────────────────────
  it('should add a success toast via success()', () => {
    service.success('Great!');
    expect(service.toasts()[0].type).toBe('success');
    expect(service.toasts()[0].message).toBe('Great!');
  });

  it('should add an error toast via error()', () => {
    service.error('Something went wrong');
    expect(service.toasts()[0].type).toBe('error');
    expect(service.toasts()[0].message).toBe('Something went wrong');
  });

  it('should add an info toast via info()', () => {
    service.info('Please wait...');
    expect(service.toasts()[0].type).toBe('info');
    expect(service.toasts()[0].message).toBe('Please wait...');
  });

  // ─── Auto-remove after duration ─────────────────────────────────────────
  it('should auto-remove toast after the specified duration', async () => {
    service.show('success', 'Flash message', 50);
    expect(service.toasts().length).toBe(1);
    await new Promise(resolve => setTimeout(resolve, 100));
    expect(service.toasts().length).toBe(0);
  });
});
