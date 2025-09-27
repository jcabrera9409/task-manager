import { Routes } from '@angular/router';
import { TaskComponent } from './task/task.component';
import { AuthGuard } from '../_service/guard.service';

export const pagesRoutes: Routes = [
    { path: '', component: TaskComponent, canActivate: [AuthGuard] },
];