import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TaskService } from './task.service';
import { EnvService } from './env.service';
import { Task } from '../_model/task';
import { User } from '../_model/user';
import { APIResponseDTO } from '../_model/dto';

describe('TaskService', () => {
  let service: TaskService;
  let httpMock: HttpTestingController;
  let envServiceSpy: jasmine.SpyObj<EnvService>;

  const mockApiUrl = 'http://localhost:8080/api';
  
  const mockUser: User = {
    name: 'Test User',
    email: 'test@example.com',
    password: 'password',
    active: true
  };

  const mockTask: Task = {
    id: 1,
    title: 'Test Task',
    description: 'Test Description',
    completed: false,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
    user: mockUser
  };

  beforeEach(() => {
    const envServiceSpyObj = jasmine.createSpyObj('EnvService', [], {
      getApiUrl: mockApiUrl
    });

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        TaskService,
        { provide: EnvService, useValue: envServiceSpyObj }
      ]
    });

    service = TestBed.inject(TaskService);
    httpMock = TestBed.inject(HttpTestingController);
    envServiceSpy = TestBed.inject(EnvService) as jasmine.SpyObj<EnvService>;
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getAll', () => {
    it('should retrieve all tasks', () => {
      const mockTasks: Task[] = [mockTask, { ...mockTask, id: 2, title: 'Task 2' }];
      const mockResponse: APIResponseDTO<Task[]> = {
        success: true,
        message: 'Tasks retrieved successfully',
        statusCode: 200,
        timestamp: new Date().toISOString(),
        data: mockTasks
      };

      service.getAll().subscribe(response => {
        expect(response).toEqual(mockResponse);
        expect(response.data.length).toBe(2);
        expect(response.data[0].title).toBe('Test Task');
      });

      const req = httpMock.expectOne(`${mockApiUrl}/tasks`);
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });
  });

  describe('getById', () => {
    it('should retrieve task by id', () => {
      const taskId = 1;
      const mockResponse: APIResponseDTO<Task> = {
        success: true,
        message: 'Task retrieved successfully',
        statusCode: 200,
        timestamp: new Date().toISOString(),
        data: mockTask
      };

      service.getById(taskId).subscribe(response => {
        expect(response).toEqual(mockResponse);
        expect(response.data.id).toBe(taskId);
        expect(response.data.title).toBe('Test Task');
      });

      const req = httpMock.expectOne(`${mockApiUrl}/tasks/${taskId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });
  });

  describe('create', () => {
    it('should create a new task', () => {
      const newTask: Partial<Task> = {
        title: 'New Task',
        description: 'New Description',
        completed: false
      };
      const createdTask: Task = { 
        ...mockTask,
        id: 3, 
        title: 'New Task',
        description: 'New Description',
        createdAt: new Date().toISOString(), 
        updatedAt: new Date().toISOString() 
      };
      const mockResponse: APIResponseDTO<Task> = {
        success: true,
        message: 'Task created successfully',
        statusCode: 201,
        timestamp: new Date().toISOString(),
        data: createdTask
      };

      service.create(newTask as Task).subscribe(response => {
        expect(response).toEqual(mockResponse);
        expect(response.data.id).toBe(3);
        expect(response.data.title).toBe('New Task');
      });

      const req = httpMock.expectOne(`${mockApiUrl}/tasks`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(newTask);
      req.flush(mockResponse);
    });
  });

  describe('update', () => {
    it('should update an existing task', () => {
      const updatedTask: Task = { ...mockTask, title: 'Updated Task' };
      const mockResponse: APIResponseDTO<Task> = {
        success: true,
        message: 'Task updated successfully',
        statusCode: 200,
        timestamp: new Date().toISOString(),
        data: updatedTask
      };

      service.update(updatedTask).subscribe(response => {
        expect(response).toEqual(mockResponse);
        expect(response.data.title).toBe('Updated Task');
      });

      const req = httpMock.expectOne(`${mockApiUrl}/tasks`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(updatedTask);
      req.flush(mockResponse);
    });
  });

  describe('delete', () => {
    it('should delete a task by id', () => {
      const taskId = 1;
      const mockResponse: APIResponseDTO<void> = {
        success: true,
        message: 'Task deleted successfully',
        statusCode: 200,
        timestamp: new Date().toISOString(),
        data: undefined as any
      };

      service.delete(taskId).subscribe(response => {
        expect(response).toEqual(mockResponse);
        expect(response.success).toBe(true);
      });

      const req = httpMock.expectOne(`${mockApiUrl}/tasks/${taskId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush(mockResponse);
    });
  });

  describe('markAsCompleted', () => {
    it('should mark a task as completed', () => {
      const taskId = 1;
      const mockResponse: APIResponseDTO<void> = {
        success: true,
        message: 'Task marked as completed',
        statusCode: 200,
        timestamp: new Date().toISOString(),
        data: undefined as any
      };

      service.markAsCompleted(taskId).subscribe(response => {
        expect(response).toEqual(mockResponse);
        expect(response.success).toBe(true);
      });

      const req = httpMock.expectOne(`${mockApiUrl}/tasks/${taskId}/complete`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual({});
      req.flush(mockResponse);
    });
  });

  describe('Subject observables', () => {
    it('should emit tasks through objectChange observable', (done) => {
      const testTasks: Task[] = [mockTask];

      service.getObjectChange().subscribe(tasks => {
        expect(tasks).toEqual(testTasks);
        expect(tasks.length).toBe(1);
        done();
      });

      service.setObjectChange(testTasks);
    });

    it('should emit messages through messageChange observable', (done) => {
      const testMessage = 'Test message';

      service.getMessageChange().subscribe(message => {
        expect(message).toBe(testMessage);
        done();
      });

      service.setMessageChange(testMessage);
    });

    it('should handle multiple subscribers for objectChange', () => {
      const testTasks: Task[] = [mockTask];
      let subscriber1Called = false;
      let subscriber2Called = false;

      service.getObjectChange().subscribe(() => {
        subscriber1Called = true;
      });

      service.getObjectChange().subscribe(() => {
        subscriber2Called = true;
      });

      service.setObjectChange(testTasks);

      expect(subscriber1Called).toBe(true);
      expect(subscriber2Called).toBe(true);
    });
  });
});