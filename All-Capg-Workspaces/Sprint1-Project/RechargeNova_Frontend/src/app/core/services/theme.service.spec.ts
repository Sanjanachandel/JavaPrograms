import { TestBed } from '@angular/core/testing';
import { ThemeService } from './theme.service';

describe('ThemeService', () => {
  let service: ThemeService;

  beforeEach(() => {
    localStorage.clear();
    // Mock matchMedia for JSDOM
    Object.defineProperty(window, 'matchMedia', {
      writable: true,
      value: vi.fn().mockImplementation(query => ({
        matches: false,
        media: query,
        onchange: null,
        addListener: vi.fn(),
        removeListener: vi.fn(),
        addEventListener: vi.fn(),
        removeEventListener: vi.fn(),
        dispatchEvent: vi.fn(),
      })),
    });

    // Reset body classes between tests
    document.body.classList.remove('light-theme', 'dark-theme');
    TestBed.configureTestingModule({});
    service = TestBed.inject(ThemeService);
  });

  afterEach(() => {
    localStorage.clear();
    document.body.classList.remove('light-theme', 'dark-theme');
    TestBed.resetTestingModule();
  });

  // ─── Creation ───────────────────────────────────────────────────────────
  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // ─── Initial theme from localStorage ────────────────────────────────────
  it('should load saved "dark" theme from localStorage', () => {
    localStorage.setItem('rechargenova-theme', 'dark');
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({});
    const freshService = TestBed.inject(ThemeService);
    expect(freshService.theme()).toBe('dark');
  });

  it('should load saved "light" theme from localStorage', () => {
    localStorage.setItem('rechargenova-theme', 'light');
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({});
    const freshService = TestBed.inject(ThemeService);
    expect(freshService.theme()).toBe('light');
  });

  // ─── toggleTheme ────────────────────────────────────────────────────────
  it('should toggle from light to dark', () => {
    service.theme.set('light');
    service.toggleTheme();
    expect(service.theme()).toBe('dark');
  });

  it('should toggle from dark to light', () => {
    service.theme.set('dark');
    service.toggleTheme();
    expect(service.theme()).toBe('light');
  });

  it('should toggle twice and return to original theme', () => {
    const original = service.theme();
    service.toggleTheme();
    service.toggleTheme();
    expect(service.theme()).toBe(original);
  });

  // ─── Persistence ────────────────────────────────────────────────────────
  it('should persist theme choice to localStorage after toggle', () => {
    service.theme.set('light');
    service.toggleTheme();
    TestBed.flushEffects();
    expect(localStorage.getItem('rechargenova-theme')).toBe('dark');
  });

  it('should persist "light" theme to localStorage after toggle to light', () => {
    service.theme.set('dark');
    service.toggleTheme();
    TestBed.flushEffects();
    expect(localStorage.getItem('rechargenova-theme')).toBe('light');
  });

  it('should use dark if matchMedia matches light:false', () => {
    localStorage.clear();
    Object.defineProperty(window, 'matchMedia', {
      writable: true,
      value: vi.fn().mockImplementation(query => ({ matches: false }))
    });
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({});
    const freshService = TestBed.inject(ThemeService);
    expect(freshService.theme()).toBe('dark');
  });

  it('should use light if matchMedia matches light:true', () => {
    localStorage.clear();
    Object.defineProperty(window, 'matchMedia', {
      writable: true,
      value: vi.fn().mockImplementation(query => ({ matches: true }))
    });
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({});
    const freshService = TestBed.inject(ThemeService);
    expect(freshService.theme()).toBe('light');
  });
});

