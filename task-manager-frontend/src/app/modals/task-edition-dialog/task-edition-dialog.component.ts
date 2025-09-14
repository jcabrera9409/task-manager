import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-task-edition-dialog',
  standalone: true,
  imports: [],
  templateUrl: './task-edition-dialog.component.html',
  styleUrl: './task-edition-dialog.component.css'
})
export class TaskEditionDialogComponent {

  constructor(
    private dialogRef: MatDialogRef<TaskEditionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  onCancel(): void {
    this.dialogRef.close();
  }
}
