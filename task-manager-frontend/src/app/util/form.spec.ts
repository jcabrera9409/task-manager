import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Renderer2 } from '@angular/core';
import { FormMethods } from './form';

describe('FormMethods', () => {
  let mockRenderer: jasmine.SpyObj<Renderer2>;
  let testForm: FormGroup;
  let mockElement: HTMLElement;

  beforeEach(() => {
    // Create spy for Renderer2
    mockRenderer = jasmine.createSpyObj('Renderer2', ['addClass', 'removeClass']);

    // Create test form
    testForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required, Validators.minLength(6)]),
      name: new FormControl('')
    });

    // Create mock DOM element
    mockElement = document.createElement('input');
    mockElement.setAttribute('formControlName', 'email');
    
    // Mock document.querySelector
    spyOn(document, 'querySelector').and.callFake((selector: string) => {
      if (selector.includes('email')) {
        return mockElement;
      }
      if (selector.includes('password')) {
        const passwordElement = document.createElement('input');
        passwordElement.setAttribute('formControlName', 'password');
        return passwordElement;
      }
      if (selector.includes('name')) {
        const nameElement = document.createElement('input');
        nameElement.setAttribute('formControlName', 'name');
        return nameElement;
      }
      return null;
    });
  });

  describe('addSubscribesForm', () => {
    it('should add subscriptions to all form controls', () => {
      // Spy on control methods to verify subscriptions are added
      const emailControl = testForm.get('email');
      const passwordControl = testForm.get('password');
      const nameControl = testForm.get('name');

      spyOn(emailControl!.statusChanges, 'subscribe').and.callThrough();
      spyOn(emailControl!.valueChanges, 'subscribe').and.callThrough();
      spyOn(passwordControl!.statusChanges, 'subscribe').and.callThrough();
      spyOn(passwordControl!.valueChanges, 'subscribe').and.callThrough();
      spyOn(nameControl!.statusChanges, 'subscribe').and.callThrough();
      spyOn(nameControl!.valueChanges, 'subscribe').and.callThrough();

      FormMethods.addSubscribesForm(testForm, mockRenderer);

      // Verify subscriptions were created for all controls
      expect(emailControl!.statusChanges.subscribe).toHaveBeenCalled();
      expect(emailControl!.valueChanges.subscribe).toHaveBeenCalled();
      expect(passwordControl!.statusChanges.subscribe).toHaveBeenCalled();
      expect(passwordControl!.valueChanges.subscribe).toHaveBeenCalled();
      expect(nameControl!.statusChanges.subscribe).toHaveBeenCalled();
      expect(nameControl!.valueChanges.subscribe).toHaveBeenCalled();
    });

    it('should update control classes when control value changes', () => {
      spyOn(FormMethods, 'updateControlClasses');
      
      FormMethods.addSubscribesForm(testForm, mockRenderer);

      // Trigger value change
      testForm.get('email')!.setValue('test@example.com');

      expect(FormMethods.updateControlClasses).toHaveBeenCalledWith(
        testForm, 
        'email', 
        mockRenderer
      );
    });

    it('should update control classes when control status changes', () => {
      spyOn(FormMethods, 'updateControlClasses');
      
      FormMethods.addSubscribesForm(testForm, mockRenderer);

      // Trigger status change by making the control invalid
      testForm.get('email')!.setValue('invalid-email');

      expect(FormMethods.updateControlClasses).toHaveBeenCalledWith(
        testForm, 
        'email', 
        mockRenderer
      );
    });

    it('should handle form with no controls gracefully', () => {
      const emptyForm = new FormGroup({});
      
      expect(() => {
        FormMethods.addSubscribesForm(emptyForm, mockRenderer);
      }).not.toThrow();
    });
  });

  describe('validateForm', () => {
    it('should call updateControlClasses for all form controls', () => {
      spyOn(FormMethods, 'updateControlClasses');

      FormMethods.validateForm(testForm, mockRenderer);

      expect(FormMethods.updateControlClasses).toHaveBeenCalledWith(testForm, 'email', mockRenderer);
      expect(FormMethods.updateControlClasses).toHaveBeenCalledWith(testForm, 'password', mockRenderer);
      expect(FormMethods.updateControlClasses).toHaveBeenCalledWith(testForm, 'name', mockRenderer);
      expect(FormMethods.updateControlClasses).toHaveBeenCalledTimes(3);
    });

    it('should handle form with invalid controls', () => {
      // Make controls invalid
      testForm.get('email')!.setValue('invalid-email');
      testForm.get('password')!.setValue('123'); // Too short
      
      spyOn(FormMethods, 'updateControlClasses');

      FormMethods.validateForm(testForm, mockRenderer);

      expect(FormMethods.updateControlClasses).toHaveBeenCalledTimes(3);
    });

    it('should handle form with valid controls', () => {
      // Make controls valid
      testForm.get('email')!.setValue('valid@example.com');
      testForm.get('password')!.setValue('validpassword123');
      testForm.get('name')!.setValue('John Doe');
      
      spyOn(FormMethods, 'updateControlClasses');

      FormMethods.validateForm(testForm, mockRenderer);

      expect(FormMethods.updateControlClasses).toHaveBeenCalledTimes(3);
    });

    it('should skip null controls', () => {
      const formWithNullControl = new FormGroup({
        validControl: new FormControl('test')
      });
      
      // Mock get method to return null for some controls
      spyOn(formWithNullControl, 'get').and.callFake((controlName: string) => {
        if (controlName === 'validControl') {
          return new FormControl('test');
        }
        return null;
      });

      spyOn(FormMethods, 'updateControlClasses');

      expect(() => {
        FormMethods.validateForm(formWithNullControl, mockRenderer);
      }).not.toThrow();
    });
  });

  describe('updateControlClasses', () => {
    it('should remove form-error class from valid control', () => {
      // Make control valid
      testForm.get('email')!.setValue('valid@example.com');

      FormMethods.updateControlClasses(testForm, 'email', mockRenderer);

      expect(mockRenderer.removeClass).toHaveBeenCalledWith(mockElement, 'form-error');
      expect(mockRenderer.addClass).not.toHaveBeenCalled();
    });

    it('should add form-error class to invalid control', () => {
      // Make control invalid
      testForm.get('email')!.setValue('invalid-email');

      FormMethods.updateControlClasses(testForm, 'email', mockRenderer);

      expect(mockRenderer.removeClass).toHaveBeenCalledWith(mockElement, 'form-error');
      expect(mockRenderer.addClass).toHaveBeenCalledWith(mockElement, 'form-error');
    });

    it('should handle control with multiple validation errors', () => {
      // Make control invalid with multiple errors (required and email)
      testForm.get('email')!.setValue('');

      FormMethods.updateControlClasses(testForm, 'email', mockRenderer);

      expect(mockRenderer.removeClass).toHaveBeenCalledWith(mockElement, 'form-error');
      expect(mockRenderer.addClass).toHaveBeenCalledWith(mockElement, 'form-error');
    });

    it('should handle non-existent control gracefully', () => {
      expect(() => {
        FormMethods.updateControlClasses(testForm, 'nonExistentControl', mockRenderer);
      }).not.toThrow();

      // Should not call renderer methods when control doesn't exist
      expect(mockRenderer.removeClass).not.toHaveBeenCalled();
      expect(mockRenderer.addClass).not.toHaveBeenCalled();
    });

    it('should handle missing DOM element gracefully', () => {
      // Mock querySelector to return null
      (document.querySelector as jasmine.Spy).and.returnValue(null);

      expect(() => {
        FormMethods.updateControlClasses(testForm, 'email', mockRenderer);
      }).not.toThrow();

      // Should not call renderer methods when element doesn't exist
      expect(mockRenderer.removeClass).not.toHaveBeenCalled();
      expect(mockRenderer.addClass).not.toHaveBeenCalled();
    });

    it('should handle control without corresponding DOM element', () => {
      // Control exists but no matching DOM element
      (document.querySelector as jasmine.Spy).and.returnValue(null);

      FormMethods.updateControlClasses(testForm, 'password', mockRenderer);

      // Should not call renderer methods
      expect(mockRenderer.removeClass).not.toHaveBeenCalled();
      expect(mockRenderer.addClass).not.toHaveBeenCalled();
    });

    it('should work with different control types', () => {
      const passwordElement = document.querySelector('[formControlName="password"]') as HTMLElement;
      
      // Test with password control (has minLength validator)
      testForm.get('password')!.setValue('123'); // Invalid - too short

      FormMethods.updateControlClasses(testForm, 'password', mockRenderer);

      expect(mockRenderer.removeClass).toHaveBeenCalledWith(passwordElement, 'form-error');
      expect(mockRenderer.addClass).toHaveBeenCalledWith(passwordElement, 'form-error');
    });

    it('should handle controls that become valid after being invalid', () => {
      // Start with invalid control
      testForm.get('email')!.setValue('invalid');
      FormMethods.updateControlClasses(testForm, 'email', mockRenderer);
      
      expect(mockRenderer.addClass).toHaveBeenCalledWith(mockElement, 'form-error');
      
      // Reset spy calls
      mockRenderer.removeClass.calls.reset();
      mockRenderer.addClass.calls.reset();

      // Make control valid
      testForm.get('email')!.setValue('valid@example.com');
      FormMethods.updateControlClasses(testForm, 'email', mockRenderer);

      expect(mockRenderer.removeClass).toHaveBeenCalledWith(mockElement, 'form-error');
      expect(mockRenderer.addClass).not.toHaveBeenCalled();
    });
  });
});