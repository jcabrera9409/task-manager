import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { TaskEditionDialogComponent } from '../../modals/task-edition-dialog/task-edition-dialog.component';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.css'
})
export class LayoutComponent {

  constructor(
    private dialog: MatDialog
  ) { }


  openDialog() {
    this.dialog.open(TaskEditionDialogComponent, {
      data: null,
      panelClass: 'w-1/3',
    });
  }
}
