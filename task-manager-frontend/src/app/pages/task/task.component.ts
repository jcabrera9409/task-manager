import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../modals/confirm-dialog/confirm-dialog.component';
import { TaskService } from '../../_service/task.service';
import { LoaderComponent } from '../../shared/loader/loader.component';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { catchError, EMPTY, finalize, switchMap } from 'rxjs';
import { NotificationService } from '../../_service/notification.service';
import { Message } from '../../_model/message';
import { Task } from '../../_model/task';
import { TaskEditionDialogComponent } from '../../modals/task-edition-dialog/task-edition-dialog.component';
import { ConfirmDataDTO } from '../../_model/dto';

@Component({
  selector: 'app-task',
  standalone: true,
  imports: [LoaderComponent, CommonModule, ReactiveFormsModule],
  templateUrl: './task.component.html',
  styleUrl: './task.component.css'
})
export class TaskComponent implements OnInit{

  isLoading: boolean = false;

  totalTasks: number = 0;
  pendingTasks: number = 0;
  completedTasks: number = 0;

  tasks: Task[] = [];

  constructor(
    private taskService: TaskService,
    private notificationService: NotificationService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.getAllTasks();

    this.taskService.getObjectChange().subscribe({
      next: (data) => {
        this.tasks = data || [];
        this.completeStatistics();
      }
    })
  }

  openCompleteDialog(task: Task) {
    const confirmData: ConfirmDataDTO = {
      title: 'Confirmar Completado',
      message: '¿Estás seguro de que deseas marcar esta tarea como completada?',
      confirmText: 'Completar',
      cancelText: 'Cancelar'
    };
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: confirmData
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.markAsCompleted(task);
      }
    }); 
  }


  openDeleteDialog(task: Task) {
    const confirmData: ConfirmDataDTO = {
      title: 'Confirmar Eliminación',
      message: '¿Estás seguro de que deseas eliminar esta tarea?',
      confirmText: 'Eliminar',
      cancelText: 'Cancelar'
    };
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: confirmData
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleteTask(task);
      }
    });
  }

  openEditDialog(task: Task) {
    this.dialog.open(TaskEditionDialogComponent, {
      data: task,
      panelClass: 'w-1/3',
    });
  }

  private completeStatistics() {
    this.completedTasks = 0;
    this.pendingTasks = 0;
    this.tasks.forEach(task => {
      if (task.completed) {
        this.completedTasks++;
      } else {
        this.pendingTasks++;
      }
    });
    this.totalTasks = this.tasks.length;
  }

  private markAsCompleted(task: Task) {
    this.isLoading = true;
    this.taskService.markAsCompleted(task.id)
      .pipe(
        catchError(error => {
          this.notificationService.setMessageChange(
            Message.error('Ocurrió un error al procesar la solicitud.', error)
          )
          return EMPTY;
        }),
        switchMap(() => this.taskService.getAll()),
        catchError(error => {
          this.notificationService.setMessageChange(
            Message.error('Ocurrió un error al listar las tareas.', error)
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
            Message.success('Tarea marcada como completada.')
          );
        } else {
          this.notificationService.setMessageChange(
            Message.error('Error al marcar la tarea como completada.', response.message)
          );
        }
      });
  }

  private deleteTask(task: Task) {
    this.isLoading = true;
    this.taskService.delete(task.id)
      .pipe(
        catchError(error => {
          this.notificationService.setMessageChange(
            Message.error('Ocurrió un error al procesar la solicitud.', error)
          )
          return EMPTY;
        }),
        switchMap(() => this.taskService.getAll()),
        catchError(error => {
          this.notificationService.setMessageChange(
            Message.error('Ocurrió un error al listar las tareas.', error)
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
            Message.success('Tarea eliminada correctamente.')
          );
        } else {
          this.notificationService.setMessageChange(
            Message.error('Error al eliminar la tarea.', response.message)
          );
        }
      });
  }

  private getAllTasks() {
    this.isLoading = true;
    this.taskService.getAll()
      .pipe(
        finalize(() => {
          this.isLoading = false;
        })
      )
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.taskService.setObjectChange(response.data);
          } else {
            this.taskService.setObjectChange(null);
            this.notificationService.setMessageChange(
              Message.error(response.message)
            )
          }
        },
        error: (error) => {
          console.error('Error al obtener tareas:', error);
          if (error.status === 401) {
            this.notificationService.setMessageChange(
              Message.error('Sesión expirada. Por favor, inicie sesión nuevamente.')
            );
          } else {
            this.notificationService.setMessageChange(
              Message.error('Error al cargar las tareas. Por favor, intente nuevamente.')
            );
          }
        }
      });
  }

}
