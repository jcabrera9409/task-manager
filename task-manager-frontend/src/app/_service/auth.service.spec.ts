import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import { EnvService } from './env.service';
import { UtilMethods } from '../util/util';
import { APIResponseDTO, AuthenticationResponseDTO } from '../_model/dto';
import { User } from '../_model/user';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  let routerSpy: jasmine.SpyObj<Router>;
  let envServiceSpy: jasmine.SpyObj<EnvService>;
  let utilMethodsSpy: jasmine.SpyObj<UtilMethods>;

  beforeEach(() => {
    const routerSpyObj = jasmine.createSpyObj('Router', ['navigate']);
    const envServiceSpyObj = jasmine.createSpyObj('EnvService', [], {
      getApiUrl: 'http://localhost:8080/api'
    });

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        AuthService,
        { provide: Router, useValue: routerSpyObj },
        { provide: EnvService, useValue: envServiceSpyObj }
      ]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    routerSpy = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    envServiceSpy = TestBed.inject(EnvService) as jasmine.SpyObj<EnvService>;

    // Mock UtilMethods singleton
    utilMethodsSpy = jasmine.createSpyObj('UtilMethods', ['getJwtToken']);
    spyOn(UtilMethods, 'getInstance').and.returnValue(utilMethodsSpy);
  });

  afterEach(() => {
    httpMock.verify();
    // Clear sessionStorage after each test
    if (typeof window !== 'undefined' && typeof sessionStorage !== 'undefined') {
      sessionStorage.clear();
    }
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('login', () => {
    it('should send POST request to login endpoint with user credentials', () => {
      const email = 'test@example.com';
      const password = 'password123';
      const mockResponse: APIResponseDTO<AuthenticationResponseDTO> = {
        success: true,
        message: 'Login successful',
        statusCode: 200,
        timestamp: new Date().toISOString(),
        data: {
          access_token: 'mock-access-token',
          refresh_token: 'mock-refresh-token'
        }
      };

      service.login(email, password).subscribe(response => {
        expect(response).toEqual(mockResponse);
        expect(response.success).toBe(true);
        expect(response.data?.access_token).toBe('mock-access-token');
      });

      const req = httpMock.expectOne('http://localhost:8080/api/auth/login');
      expect(req.request.method).toBe('POST');
      expect(req.request.body.email).toBe(email);
      expect(req.request.body.password).toBe(password);
      req.flush(mockResponse);
    });

    it('should create User object with provided credentials', () => {
      const email = 'test@example.com';
      const password = 'password123';

      service.login(email, password).subscribe();

      const req = httpMock.expectOne('http://localhost:8080/api/auth/login');
      const requestBody = req.request.body;
      expect(requestBody).toBeInstanceOf(User);
      expect(requestBody.email).toBe(email);
      expect(requestBody.password).toBe(password);
      req.flush({});
    });
  });

  describe('isLogged', () => {
    it('should return true when JWT token exists', () => {
      utilMethodsSpy.getJwtToken.and.returnValue('mock-jwt-token');

      const result = service.isLogged();

      expect(result).toBe(true);
      expect(utilMethodsSpy.getJwtToken).toHaveBeenCalled();
    });

    it('should return false when JWT token is null', () => {
      utilMethodsSpy.getJwtToken.and.returnValue(null);

      const result = service.isLogged();

      expect(result).toBe(false);
      expect(utilMethodsSpy.getJwtToken).toHaveBeenCalled();
    });

    it('should return false when JWT token is undefined', () => {
      utilMethodsSpy.getJwtToken.and.returnValue(undefined);

      const result = service.isLogged();

      expect(result).toBe(false);
      expect(utilMethodsSpy.getJwtToken).toHaveBeenCalled();
    });
  });

  describe('logout', () => {
    it('should send GET request to logout endpoint and navigate to login', () => {
      service.logout();

      const req = httpMock.expectOne('http://localhost:8080/api/auth/logout');
      expect(req.request.method).toBe('GET');
      
      req.flush({});

      expect(routerSpy.navigate).toHaveBeenCalledWith(['login']);
    });

    it('should clear sessionStorage after logout request completes', () => {
      // Mock sessionStorage
      const mockSessionStorage = {
        clear: jasmine.createSpy('clear')
      };
      Object.defineProperty(window, 'sessionStorage', {
        value: mockSessionStorage,
        writable: true
      });

      service.logout();

      const req = httpMock.expectOne('http://localhost:8080/api/auth/logout');
      req.flush({});

      expect(mockSessionStorage.clear).toHaveBeenCalled();
    });

    it('should navigate to login page even if logout request fails', () => {
      service.logout();

      const req = httpMock.expectOne('http://localhost:8080/api/auth/logout');
      req.error(new ErrorEvent('Network error'));

      expect(routerSpy.navigate).toHaveBeenCalledWith(['login']);
    });
  });
});