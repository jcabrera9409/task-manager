import { UtilMethods } from './util';
import { EnvService } from '../_service/env.service';
import { JwtHelperService } from '@auth0/angular-jwt';

describe('UtilMethods', () => {
  let utilMethods: UtilMethods;
  let mockSessionStorage: { [key: string]: string };

  beforeEach(() => {
    // Mock sessionStorage
    mockSessionStorage = {};
    Object.defineProperty(window, 'sessionStorage', {
      value: {
        getItem: jasmine.createSpy('getItem').and.callFake((key: string) => mockSessionStorage[key] || null),
        setItem: jasmine.createSpy('setItem').and.callFake((key: string, value: string) => mockSessionStorage[key] = value),
        removeItem: jasmine.createSpy('removeItem').and.callFake((key: string) => delete mockSessionStorage[key]),
        clear: jasmine.createSpy('clear').and.callFake(() => mockSessionStorage = {})
      },
      writable: true
    });

    // Reset singleton
    (UtilMethods as any).utilMethods = undefined;
    (UtilMethods as any).envService = undefined;
    
    utilMethods = UtilMethods.getInstance();
  });

  afterEach(() => {
    // Clean up
    mockSessionStorage = {};
  });

  describe('Singleton Pattern', () => {
    it('should return the same instance', () => {
      const instance1 = UtilMethods.getInstance();
      const instance2 = UtilMethods.getInstance();
      
      expect(instance1).toBe(instance2);
    });
  });

  describe('convertPeriodoToString', () => {
    it('should convert January period correctly', () => {
      const result = utilMethods.convertPeriodoToString('2023-01');
      expect(result).toBe('Enero 2023');
    });

    it('should convert December period correctly', () => {
      const result = utilMethods.convertPeriodoToString('2023-12');
      expect(result).toBe('Diciembre 2023');
    });

    it('should convert all months correctly', () => {
      const months = [
        { period: '2023-01', expected: 'Enero 2023' },
        { period: '2023-02', expected: 'Febrero 2023' },
        { period: '2023-03', expected: 'Marzo 2023' },
        { period: '2023-04', expected: 'Abril 2023' },
        { period: '2023-05', expected: 'Mayo 2023' },
        { period: '2023-06', expected: 'Junio 2023' },
        { period: '2023-07', expected: 'Julio 2023' },
        { period: '2023-08', expected: 'Agosto 2023' },
        { period: '2023-09', expected: 'Septiembre 2023' },
        { period: '2023-10', expected: 'Octubre 2023' },
        { period: '2023-11', expected: 'Noviembre 2023' },
        { period: '2023-12', expected: 'Diciembre 2023' }
      ];

      months.forEach(({ period, expected }) => {
        expect(utilMethods.convertPeriodoToString(period)).toBe(expected);
      });
    });
  });

  describe('JWT Token Management', () => {
    const mockToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwidXNlcm5hbWUiOiJqb2huZG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';
    const tokenName = 'authToken';

    beforeEach(() => {
      // Mock EnvService to return token name
      const mockEnvService = { getTokenName: tokenName };
      (UtilMethods as any).envService = mockEnvService;
    });

    describe('setJwtToken', () => {
      it('should store JWT token in sessionStorage', () => {
        utilMethods.setJwtToken(mockToken);
        
        expect(window.sessionStorage.setItem).toHaveBeenCalledWith(tokenName, mockToken);
        expect(mockSessionStorage[tokenName]).toBe(mockToken);
      });
    });

    describe('getJwtToken', () => {
      it('should retrieve JWT token from sessionStorage', () => {
        mockSessionStorage[tokenName] = mockToken;
        
        const result = utilMethods.getJwtToken();
        
        expect(result).toBe(mockToken);
        expect(window.sessionStorage.getItem).toHaveBeenCalledWith(tokenName);
      });

      it('should return null when token does not exist', () => {
        const result = utilMethods.getJwtToken();
        
        expect(result).toBeNull();
      });
    });

    describe('removeJwtToken', () => {
      it('should remove JWT token from sessionStorage', () => {
        mockSessionStorage[tokenName] = mockToken;
        
        utilMethods.removeJwtToken();
        
        expect(window.sessionStorage.removeItem).toHaveBeenCalledWith(tokenName);
        expect(mockSessionStorage[tokenName]).toBeUndefined();
      });
    });

    describe('isTokenExpired', () => {
      it('should return true for expired token', () => {
        spyOn(utilMethods, 'getHelper').and.returnValue({
          isTokenExpired: jasmine.createSpy('isTokenExpired').and.returnValue(true)
        } as any);
        spyOn(utilMethods, 'getJwtToken').and.returnValue(mockToken);

        const result = utilMethods.isTokenExpired();

        expect(result).toBe(true);
      });

      it('should return false for valid token', () => {
        spyOn(utilMethods, 'getHelper').and.returnValue({
          isTokenExpired: jasmine.createSpy('isTokenExpired').and.returnValue(false)
        } as any);
        spyOn(utilMethods, 'getJwtToken').and.returnValue(mockToken);

        const result = utilMethods.isTokenExpired();

        expect(result).toBe(false);
      });
    });

    describe('getFieldJwtToken', () => {
      it('should return field value from decoded token', () => {
        const mockDecodedToken = { username: 'johndoe', email: 'john@example.com' };
        spyOn(utilMethods as any, 'getDecodedJwtToken').and.returnValue(mockDecodedToken);

        const result = utilMethods.getFieldJwtToken('username');

        expect(result).toBe('johndoe');
      });

      it('should return empty string for non-existent field', () => {
        const mockDecodedToken = { username: 'johndoe' };
        spyOn(utilMethods as any, 'getDecodedJwtToken').and.returnValue(mockDecodedToken);

        const result = utilMethods.getFieldJwtToken('nonexistent');

        expect(result).toBe('');
      });

      it('should return null when token cannot be decoded', () => {
        spyOn(utilMethods as any, 'getDecodedJwtToken').and.returnValue(null);

        const result = utilMethods.getFieldJwtToken('username');

        expect(result).toBeNull();
      });
    });

    describe('getUsernameFieldJwtToken', () => {
      it('should return username from token', () => {
        spyOn(utilMethods, 'getFieldJwtToken').and.returnValue('johndoe');

        const result = utilMethods.getUsernameFieldJwtToken();

        expect(result).toBe('johndoe');
        expect(utilMethods.getFieldJwtToken).toHaveBeenCalledWith('username');
      });

      it('should return empty string when username is null', () => {
        spyOn(utilMethods, 'getFieldJwtToken').and.returnValue(null);

        const result = utilMethods.getUsernameFieldJwtToken();

        expect(result).toBe('');
      });
    });
  });

  describe('getHelper', () => {
    it('should return JwtHelperService instance', () => {
      const helper = utilMethods.getHelper();
      
      expect(helper).toBeInstanceOf(JwtHelperService);
    });
  });

  describe('extractJwtPayload (private method testing through public methods)', () => {
    it('should handle invalid token format', () => {
      spyOn(utilMethods, 'getJwtToken').and.returnValue('invalid.token');

      const result = utilMethods.getFieldJwtToken('username');

      expect(result).toBeNull();
    });

    it('should handle null token', () => {
      spyOn(utilMethods, 'getJwtToken').and.returnValue(null);

      const result = utilMethods.getFieldJwtToken('username');

      expect(result).toBeNull();
    });

    it('should handle token decoding error', () => {
      spyOn(utilMethods, 'getJwtToken').and.returnValue('invalid.jwt.token');
      spyOn(utilMethods, 'getHelper').and.returnValue({
        decodeToken: jasmine.createSpy('decodeToken').and.throwError('Invalid token')
      } as any);

      const result = utilMethods.getFieldJwtToken('username');

      expect(result).toBeNull();
    });
  });
});