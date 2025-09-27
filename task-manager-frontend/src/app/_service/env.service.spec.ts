import { TestBed } from '@angular/core/testing';
import { PLATFORM_ID } from '@angular/core';
import { EnvService } from './env.service';

describe('EnvService', () => {
  let service: EnvService;

  beforeEach(() => {
    // Clear any existing window.__env
    if (typeof window !== 'undefined') {
      delete (window as any).__env;
    }
  });

  describe('with default values (no window.__env)', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        providers: [
          { provide: PLATFORM_ID, useValue: 'browser' }
        ]
      });
      service = TestBed.inject(EnvService);
    });

    it('should be created', () => {
      expect(service).toBeTruthy();
    });

    it('should return default production value (false)', () => {
      expect(service.isProduction).toBe(false);
    });

    it('should return default API URL', () => {
      expect(service.getApiUrl).toBe('http://localhost:8080/rest/api/v1');
    });

    it('should return default token name', () => {
      expect(service.getTokenName).toBe('access_token');
    });

    it('should return default domains', () => {
      expect(service.getDomains).toEqual(['localhost:8080']);
    });
  });

  describe('with custom window.__env values', () => {
    beforeEach(() => {
      // Set up custom environment
      (window as any).__env = {
        production: true,
        apiUrl: 'https://api.production.com/v1',
        token_name: 'jwt_token',
        domains: ['production.com', 'api.production.com']
      };

      TestBed.configureTestingModule({
        providers: [
          { provide: PLATFORM_ID, useValue: 'browser' }
        ]
      });
      service = TestBed.inject(EnvService);
    });

    it('should return custom production value (true)', () => {
      expect(service.isProduction).toBe(true);
    });

    it('should return custom API URL', () => {
      expect(service.getApiUrl).toBe('https://api.production.com/v1');
    });

    it('should return custom token name', () => {
      expect(service.getTokenName).toBe('jwt_token');
    });

    it('should return custom domains', () => {
      expect(service.getDomains).toEqual(['production.com', 'api.production.com']);
    });
  });

  describe('with partial window.__env values', () => {
    beforeEach(() => {
      // Set up partial environment (only some values)
      (window as any).__env = {
        production: true,
        apiUrl: 'https://staging.api.com/v1'
        // token_name and domains will use defaults
      };

      TestBed.configureTestingModule({
        providers: [
          { provide: PLATFORM_ID, useValue: 'browser' }
        ]
      });
      service = TestBed.inject(EnvService);
    });

    it('should use custom production value', () => {
      expect(service.isProduction).toBe(true);
    });

    it('should use custom API URL', () => {
      expect(service.getApiUrl).toBe('https://staging.api.com/v1');
    });

    it('should use default token name when not specified', () => {
      expect(service.getTokenName).toBe('access_token');
    });

    it('should use default domains when not specified', () => {
      expect(service.getDomains).toEqual(['localhost:8080']);
    });
  });

  describe('with empty window.__env object', () => {
    beforeEach(() => {
      // Set up empty environment object
      (window as any).__env = {};

      TestBed.configureTestingModule({
        providers: [
          { provide: PLATFORM_ID, useValue: 'browser' }
        ]
      });
      service = TestBed.inject(EnvService);
    });

    it('should use all default values', () => {
      expect(service.isProduction).toBe(false);
      expect(service.getApiUrl).toBe('http://localhost:8080/rest/api/v1');
      expect(service.getTokenName).toBe('access_token');
      expect(service.getDomains).toEqual(['localhost:8080']);
    });
  });

  describe('server-side rendering (non-browser environment)', () => {
    beforeEach(() => {
      TestBed.configureTestingModule({
        providers: [
          { provide: PLATFORM_ID, useValue: 'server' }
        ]
      });
      service = TestBed.inject(EnvService);
    });

    it('should use default values when not in browser', () => {
      expect(service.isProduction).toBe(false);
      expect(service.getApiUrl).toBe('http://localhost:8080/rest/api/v1');
      expect(service.getTokenName).toBe('access_token');
      expect(service.getDomains).toEqual(['localhost:8080']);
    });
  });

  describe('without PLATFORM_ID injection', () => {
    beforeEach(() => {
      // Set up window.__env for this test
      (window as any).__env = {
        production: true,
        apiUrl: 'https://test.com/api'
      };

      TestBed.configureTestingModule({
        // Don't provide PLATFORM_ID to test fallback logic
      });
      service = TestBed.inject(EnvService);
    });

    it('should still work with fallback window detection', () => {
      expect(service.isProduction).toBe(true);
      expect(service.getApiUrl).toBe('https://test.com/api');
    });
  });

  describe('edge cases', () => {
    it('should handle null values in window.__env', () => {
      (window as any).__env = {
        production: null,
        apiUrl: null,
        token_name: null,
        domains: null
      };

      TestBed.configureTestingModule({
        providers: [
          { provide: PLATFORM_ID, useValue: 'browser' }
        ]
      });
      service = TestBed.inject(EnvService);

      // Should use defaults when values are null
      expect(service.isProduction).toBe(false);
      expect(service.getApiUrl).toBe('http://localhost:8080/rest/api/v1');
      expect(service.getTokenName).toBe('access_token');
      expect(service.getDomains).toEqual(['localhost:8080']);
    });

    it('should handle undefined values in window.__env', () => {
      (window as any).__env = {
        production: undefined,
        apiUrl: undefined,
        token_name: undefined,
        domains: undefined
      };

      TestBed.configureTestingModule({
        providers: [
          { provide: PLATFORM_ID, useValue: 'browser' }
        ]
      });
      service = TestBed.inject(EnvService);

      // Should use defaults when values are undefined
      expect(service.isProduction).toBe(false);
      expect(service.getApiUrl).toBe('http://localhost:8080/rest/api/v1');
      expect(service.getTokenName).toBe('access_token');
      expect(service.getDomains).toEqual(['localhost:8080']);
    });

    it('should handle string "false" as truthy value for production', () => {
      (window as any).__env = {
        production: 'false' // String instead of boolean
      };

      TestBed.configureTestingModule({
        providers: [
          { provide: PLATFORM_ID, useValue: 'browser' }
        ]
      });
      service = TestBed.inject(EnvService);

      expect(service.isProduction).toBeTruthy(); // String 'false' is truthy in JS
    });

    it('should handle empty arrays and strings with fallback to defaults', () => {
      (window as any).__env = {
        apiUrl: '',
        token_name: '',
        domains: []
      };

      TestBed.configureTestingModule({
        providers: [
          { provide: PLATFORM_ID, useValue: 'browser' }
        ]
      });
      service = TestBed.inject(EnvService);

      // Empty strings are falsy, so defaults should be used
      expect(service.getApiUrl).toBe('http://localhost:8080/rest/api/v1');
      expect(service.getTokenName).toBe('access_token');
      // Empty array is truthy, so it should be preserved
      expect(service.getDomains).toEqual([]);
    });
  });

  describe('property getters immutability', () => {
    beforeEach(() => {
      (window as any).__env = {
        domains: ['domain1.com', 'domain2.com']
      };

      TestBed.configureTestingModule({
        providers: [
          { provide: PLATFORM_ID, useValue: 'browser' }
        ]
      });
      service = TestBed.inject(EnvService);
    });

    it('should return the same reference for multiple calls to getDomains', () => {
      const domains1 = service.getDomains;
      const domains2 = service.getDomains;
      
      expect(domains1).toBe(domains2); // Same reference
    });

    it('should return consistent values across multiple calls', () => {
      expect(service.isProduction).toBe(service.isProduction);
      expect(service.getApiUrl).toBe(service.getApiUrl);
      expect(service.getTokenName).toBe(service.getTokenName);
      expect(service.getDomains).toEqual(service.getDomains);
    });
  });
});