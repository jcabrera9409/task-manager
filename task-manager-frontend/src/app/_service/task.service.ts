import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from 'express';
import { EnvService } from './env.service';
import { Subject } from 'rxjs';
import { Task } from '../_model/task';
import { APIResponseDTO } from '../_model/dto';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private objectChange: Subject<Task[]> = new Subject<Task[]>();
  private messageChange: Subject<string> = new Subject<string>();

  private url: string = `${this.envService.getApiUrl}/tasks`;

  constructor(
    private http: HttpClient,
    private envService: EnvService
  ) { }

  getAll() {
    return this.http.get<APIResponseDTO<Task[]>>(this.url);
  }

  getById(id: number) {
    return this.http.get<APIResponseDTO<Task>>(`${this.url}/${id}`);
  }

  create(task: Task) {
    return this.http.post<APIResponseDTO<Task>>(this.url, task);
  }

  update(task: Task) {
    return this.http.put<APIResponseDTO<Task>>(`${this.url}/${task.id}`, task);
  }

  delete(id: number) {
    return this.http.delete<APIResponseDTO<void>>(`${this.url}/${id}`);
  }

  getObjectChange() {
    return this.objectChange.asObservable();
  }

  setObjectChange(object: Task[]) {
    this.objectChange.next(object);
  }

  getMessageChange() {
    return this.messageChange.asObservable();
  }

  setMessageChange(message: string) {
    this.messageChange.next(message);
  }
}
