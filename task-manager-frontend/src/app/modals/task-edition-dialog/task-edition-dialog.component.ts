import { Component, Inject, Renderer2 } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { LoaderComponent } from '../../shared/loader/loader.component';
import { Task } from '../../_model/task';
import { TaskService } from '../../_service/task.service';
import { NotificationService } from '../../_service/notification.service';
import { FormMethods } from '../../util/form';
import { Message } from '../../_model/message';
import { catchError, EMPTY, finalize, switchMap } from 'rxjs';

@Component({
  selector: 'app-task-edition-dialog',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, LoaderComponent],
  templateUrl: './task-edition-dialog.component.html',
  styleUrl: './task-edition-dialog.component.css'
})
export class TaskEditionDialogComponent {

  form: FormGroup;
  dialogTitle: string = 'Nueva Tarea';
  isLoading: boolean = false;

  constructor(
    private dialogRef: MatDialogRef<TaskEditionDialogComponent>,
    private taskService: TaskService,
    private notificationService: NotificationService,
    private renderer: Renderer2,
    @Inject(MAT_DIALOG_DATA) public data: Task | null
  ) {
    this.form = new FormGroup({
      title: new FormControl(data?.title || '', Validators.required),
      description: new FormControl(data?.description || '', Validators.required)
    });

    if (data && data.id) {
      this.dialogTitle = 'Editar Tarea';
    }

    FormMethods.addSubscribesForm(this.form, this.renderer);
  }

  onSubmit(): void {
    if (this.form.invalid) {
      FormMethods.validateForm(this.form, this.renderer);
      this.notificationService.setMessageChange(
        Message.error('Por favor, complete todos los campos obligatorios.')
      );
      return;
    }

    this.isLoading = true;

    let taskData:Task = new Task();
    taskData.id = this.data?.id || null;
    taskData.title = this.form.get('title')?.value;
    taskData.description = this.form.get('description')?.value;
    taskData.completed = false;
    
    console.log('Task Data:', taskData);

    const operation = (this.data) ?
      this.taskService.update(taskData) :
      this.taskService.create(taskData);

    operation
      .pipe(
        catchError(error => {
          this.notificationService.setMessageChange(
            Message.error('Ocurrió un error al procesar la tarea. Intente nuevamente.', error)
          )
          return EMPTY;
        }),
        switchMap(() => this.taskService.getAll()),
        catchError(error => {
          this.notificationService.setMessageChange(
            Message.error('Ocurrió un error al obtener las tareas. Intente nuevamente.', error)
          )
          return EMPTY;
        }),
        finalize(() => {
          this.isLoading = false;
        })
      )
      .subscribe((response) => {
        if (response.success) {
          this.taskService.setObjectChange(response.data);
          this.notificationService.setMessageChange(
            Message.success('Tarea creada correctamente.')
          );
          this.dialogRef.close();
        } else {
          this.notificationService.setMessageChange(
            Message.error('Ocurrió un error al crear la tarea. Intente nuevamente.', response)
          );
        }
      })
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
