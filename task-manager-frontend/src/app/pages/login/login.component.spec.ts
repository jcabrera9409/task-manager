import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { Renderer2 } from '@angular/core';
import { of, throwError } from 'rxjs';

import { LoginComponent } from './login.component';
import { AuthService } from '../../_service/auth.service';
import { NotificationService } from '../../_service/notification.service';
import { UtilMethods } from '../../util/util';
import { FormMethods } from '../../util/form';
import { APIResponseDTO, AuthenticationResponseDTO } from '../../_model/dto';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let notificationServiceSpy: jasmine.SpyObj<NotificationService>;
  let routerSpy: jasmine.SpyObj<Router>;
  let utilMethodsSpy: jasmine.SpyObj<UtilMethods>;
  let rendererSpy: jasmine.SpyObj<Renderer2>;

  beforeEach(async () => {
    const authServiceSpyObj = jasmine.createSpyObj('AuthService', ['login']);
    const notificationServiceSpyObj = jasmine.createSpyObj('NotificationService', ['setMessageChange']);
    const routerSpyObj = jasmine.createSpyObj('Router', ['navigate']);
    const rendererSpyObj = jasmine.createSpyObj('Renderer2', ['addClass', 'removeClass']);

    // Mock UtilMethods singleton
    utilMethodsSpy = jasmine.createSpyObj('UtilMethods', ['setJwtToken']);
    spyOn(UtilMethods, 'getInstance').and.returnValue(utilMethodsSpy);

    // Mock FormMethods static methods
    spyOn(FormMethods, 'addSubscribesForm');
    spyOn(FormMethods, 'validateForm');

    await TestBed.configureTestingModule({
      imports: [LoginComponent, ReactiveFormsModule],
      providers: [
        { provide: AuthService, useValue: authServiceSpyObj },
        { provide: NotificationService, useValue: notificationServiceSpyObj },
        { provide: Router, useValue: routerSpyObj },
        { provide: Renderer2, useValue: rendererSpyObj }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authServiceSpy = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    notificationServiceSpy = TestBed.inject(NotificationService) as jasmine.SpyObj<NotificationService>;
    routerSpy = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    rendererSpy = TestBed.inject(Renderer2) as jasmine.SpyObj<Renderer2>;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with proper validators', () => {
    expect(component.loginForm).toBeDefined();
    expect(component.loginForm.get('email')?.hasError('required')).toBe(true);
    expect(component.loginForm.get('password')?.hasError('required')).toBe(true);
  });

  it('should call FormMethods.addSubscribesForm on initialization', () => {
    expect(FormMethods.addSubscribesForm).toHaveBeenCalledWith(jasmine.any(FormGroup), jasmine.any(Object));
  });

  describe('Form Validation', () => {
    it('should validate email format', () => {
      const emailControl = component.loginForm.get('email');
      
      emailControl?.setValue('invalid-email');
      expect(emailControl?.hasError('email')).toBe(true);

      emailControl?.setValue('valid@email.com');
      expect(emailControl?.hasError('email')).toBe(false);
    });

    it('should validate password minimum length', () => {
      const passwordControl = component.loginForm.get('password');
      
      passwordControl?.setValue('12345');
      expect(passwordControl?.hasError('minlength')).toBe(true);

      passwordControl?.setValue('123456');
      expect(passwordControl?.hasError('minlength')).toBe(false);
    });
  });

  describe('onSubmit', () => {
    it('should not submit if form is invalid', () => {
      component.loginForm.patchValue({
        email: '',
        password: ''
      });

      component.onSubmit();

      expect(FormMethods.validateForm).toHaveBeenCalledWith(jasmine.any(FormGroup), jasmine.any(Object));
      expect(notificationServiceSpy.setMessageChange).toHaveBeenCalledWith(
        jasmine.objectContaining({
          status: 'ERROR',
          message: 'Por favor, complete todos los campos requeridos.'
        })
      );
      expect(authServiceSpy.login).not.toHaveBeenCalled();
    });

    it('should submit valid form and handle successful login', () => {
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

      component.loginForm.patchValue({
        email: 'test@example.com',
        password: 'password123'
      });

      authServiceSpy.login.and.returnValue(of(mockResponse));

      component.onSubmit();

      expect(component.isLoading).toBe(false); // Should be false after finalize
      expect(authServiceSpy.login).toHaveBeenCalledWith('test@example.com', 'password123');
      expect(utilMethodsSpy.setJwtToken).toHaveBeenCalledWith('mock-access-token');
      expect(routerSpy.navigate).toHaveBeenCalledWith(['task']);
    });

    it('should handle unsuccessful login response', () => {
      const mockResponse: APIResponseDTO<AuthenticationResponseDTO> = {
        success: false,
        message: 'Invalid credentials',
        statusCode: 401,
        timestamp: new Date().toISOString(),
        data: {} as AuthenticationResponseDTO
      };

      component.loginForm.patchValue({
        email: 'test@example.com',
        password: 'wrongpassword'
      });

      authServiceSpy.login.and.returnValue(of(mockResponse));

      component.onSubmit();

      expect(notificationServiceSpy.setMessageChange).toHaveBeenCalledWith(
        jasmine.objectContaining({
          status: 'ERROR',
          message: 'Invalid credentials'
        })
      );
      expect(utilMethodsSpy.setJwtToken).not.toHaveBeenCalled();
      expect(routerSpy.navigate).not.toHaveBeenCalled();
    });

    it('should handle login error with error message', () => {
      const errorResponse = {
        error: {
          success: false,
          message: 'Account is locked'
        }
      };

      component.loginForm.patchValue({
        email: 'test@example.com',
        password: 'password123'
      });

      authServiceSpy.login.and.returnValue(throwError(() => errorResponse));

      component.onSubmit();

      expect(notificationServiceSpy.setMessageChange).toHaveBeenCalledWith(
        jasmine.objectContaining({
          status: 'ERROR',
          message: 'Account is locked'
        })
      );
    });

    it('should handle generic login error', () => {
      const genericError = new Error('Network error');

      component.loginForm.patchValue({
        email: 'test@example.com',
        password: 'password123'
      });

      authServiceSpy.login.and.returnValue(throwError(() => genericError));

      component.onSubmit();

      expect(notificationServiceSpy.setMessageChange).toHaveBeenCalledWith(
        jasmine.objectContaining({
          status: 'ERROR',
          message: 'Error al iniciar sesión. Por favor, inténtelo de nuevo.'
        })
      );
    });

    it('should set isLoading to true during request', () => {
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

      component.loginForm.patchValue({
        email: 'test@example.com',
        password: 'password123'
      });

      authServiceSpy.login.and.returnValue(of(mockResponse));

      // Check that isLoading is set to true at the start
      expect(component.isLoading).toBe(false);
      component.onSubmit();
      expect(component.isLoading).toBe(false); // Should be false after completion
    });
  });
});