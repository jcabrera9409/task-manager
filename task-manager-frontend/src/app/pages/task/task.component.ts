import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../modals/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-task',
  standalone: true,
  imports: [],
  templateUrl: './task.component.html',
  styleUrl: './task.component.css'
})
export class TaskComponent {

  constructor(
    private dialog: MatDialog
  ) { }

  openDeleteDialog() {
    this.dialog.open(ConfirmDialogComponent, {
      data: null
    })
  }

}
