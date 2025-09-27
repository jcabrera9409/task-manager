// Test setup and utilities
import 'zone.js';
import 'zone.js/testing';
import { getTestBed } from '@angular/core/testing';
import {
  BrowserDynamicTestingModule,
  platformBrowserDynamicTesting
} from '@angular/platform-browser-dynamic/testing';

// Initialize the Angular testing environment
getTestBed().initTestEnvironment(
  BrowserDynamicTestingModule,
  platformBrowserDynamicTesting()
);

// Prevent any real HTTP calls during testing
if (typeof window !== 'undefined') {
  const originalFetch = window.fetch;
  window.fetch = jasmine.createSpy('fetch').and.rejectWith(new Error('HTTP calls are not allowed in tests. Use HttpTestingController instead.'));
}

// Global test utilities and mocks
export class TestUtils {
  static createMockSessionStorage(): Storage {
    const storage: { [key: string]: string } = {};
    
    return {
      getItem: jasmine.createSpy('getItem').and.callFake((key: string) => storage[key] || null),
      setItem: jasmine.createSpy('setItem').and.callFake((key: string, value: string) => storage[key] = value),
      removeItem: jasmine.createSpy('removeItem').and.callFake((key: string) => delete storage[key]),
      clear: jasmine.createSpy('clear').and.callFake(() => {
        for (const key in storage) {
          delete storage[key];
        }
      }),
      length: 0,
      key: () => null
    };
  }

  static mockBrowserEnvironment() {
    Object.defineProperty(window, 'sessionStorage', {
      value: TestUtils.createMockSessionStorage(),
      writable: true
    });
  }

  static createMockJWTToken(payload: any = {}): string {
    const header = btoa(JSON.stringify({ alg: 'HS256', typ: 'JWT' }));
    const payloadStr = btoa(JSON.stringify(payload));
    const signature = 'mock_signature';
    return `${header}.${payloadStr}.${signature}`;
  }
}

// Global beforeEach for all tests
beforeEach(() => {
  TestUtils.mockBrowserEnvironment();
});

// Global cleanup to prevent HTTP calls during test teardown
afterEach(() => {
  // Prevent any lingering HTTP calls during test cleanup
  if (typeof window !== 'undefined') {
    // Clear any pending timeouts/intervals that might trigger HTTP calls
    const highestTimeoutId = setTimeout(function(){}, 0);
    clearTimeout(highestTimeoutId);
    for (let i = 0; i < 1000; i++) {
      clearTimeout(i);
      clearInterval(i);
    }
  }
});