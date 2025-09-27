import { TestBed } from '@angular/core/testing';
import { AuthGuard } from './guard.service';
import { AuthService } from './auth.service';
import { UtilMethods } from '../util/util';

describe('AuthGuard', () => {
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let utilMethodsSpy: jasmine.SpyObj<UtilMethods>;

  beforeEach(() => {
    const authServiceSpyObj = jasmine.createSpyObj('AuthService', ['isLogged', 'logout']);
    utilMethodsSpy = jasmine.createSpyObj('UtilMethods', ['isTokenExpired']);
    spyOn(UtilMethods, 'getInstance').and.returnValue(utilMethodsSpy);

    TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: authServiceSpyObj }
      ]
    });

    authServiceSpy = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
  });

  it('should return true when user is logged and token is not expired', () => {
    authServiceSpy.isLogged.and.returnValue(true);
    utilMethodsSpy.isTokenExpired.and.returnValue(false);

    const result = TestBed.runInInjectionContext(() => AuthGuard());

    expect(result).toBe(true);
    expect(authServiceSpy.isLogged).toHaveBeenCalled();
    expect(utilMethodsSpy.isTokenExpired).toHaveBeenCalled();
    expect(authServiceSpy.logout).not.toHaveBeenCalled();
  });

  it('should return false and logout when user is not logged', () => {
    authServiceSpy.isLogged.and.returnValue(false);

    const result = TestBed.runInInjectionContext(() => AuthGuard());

    expect(result).toBe(false);
    expect(authServiceSpy.isLogged).toHaveBeenCalled();
    expect(authServiceSpy.logout).toHaveBeenCalled();
    expect(utilMethodsSpy.isTokenExpired).not.toHaveBeenCalled();
  });

  it('should return false and logout when user is logged but token is expired', () => {
    authServiceSpy.isLogged.and.returnValue(true);
    utilMethodsSpy.isTokenExpired.and.returnValue(true);

    const result = TestBed.runInInjectionContext(() => AuthGuard());

    expect(result).toBe(false);
    expect(authServiceSpy.isLogged).toHaveBeenCalled();
    expect(utilMethodsSpy.isTokenExpired).toHaveBeenCalled();
    expect(authServiceSpy.logout).toHaveBeenCalled();
  });

  it('should handle edge case when isLogged throws error', () => {
    authServiceSpy.isLogged.and.throwError('Auth service error');

    expect(() => {
      TestBed.runInInjectionContext(() => AuthGuard());
    }).toThrow();
  });
});