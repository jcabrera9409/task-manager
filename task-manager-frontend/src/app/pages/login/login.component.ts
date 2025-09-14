import { Component, Renderer2 } from '@angular/core';
import { NotificationService } from '../../_service/notification.service';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { FormMethods } from '../../util/form';
import { Message } from '../../_model/message';
import { CommonModule } from '@angular/common';
import { LoaderComponent } from '../../shared/loader/loader.component';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, LoaderComponent, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  isLoading: boolean = false;
  loginForm: FormGroup;

  constructor(
    private notificationService: NotificationService,
    private renderer: Renderer2
  ) {
    this.loginForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required, Validators.minLength(6)])
    });

    FormMethods.addSubscribesForm(this.loginForm, this.renderer);
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      FormMethods.validateForm(this.loginForm, this.renderer);
      this.notificationService.setMessageChange(
        Message.error('Por favor, complete todos los campos requeridos.')
      )
      return;
    }
  }
}
