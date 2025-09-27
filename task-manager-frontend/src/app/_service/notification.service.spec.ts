import { TestBed } from '@angular/core/testing';
import { NotificationService } from './notification.service';
import { Message } from '../_model/message';
import { take } from 'rxjs/operators';

describe('NotificationService', () => {
  let service: NotificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NotificationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getMessageChange', () => {
    it('should return an observable', () => {
      const observable = service.getMessageChange();
      expect(observable).toBeDefined();
      expect(typeof observable.subscribe).toBe('function');
    });

    it('should emit messages when setMessageChange is called', (done: DoneFn) => {
      const testMessage = Message.success('Test message');

      service.getMessageChange().pipe(take(1)).subscribe({
        next: (message: Message) => {
          expect(message).toBe(testMessage);
          expect(message.status).toBe('SUCCESS');
          expect(message.message).toBe('Test message');
          done();
        }
      });

      service.setMessageChange(testMessage);
    });

    it('should emit multiple messages in sequence', (done: DoneFn) => {
      const messages = [
        Message.success('First message'),
        Message.error('Second message'),
        Message.info('Third message')
      ];
      let receivedCount = 0;

      service.getMessageChange().subscribe({
        next: (message: Message) => {
          expect(message).toBe(messages[receivedCount]);
          receivedCount++;
          
          if (receivedCount === messages.length) {
            done();
          }
        }
      });

      // Emit messages with slight delay to ensure sequence
      messages.forEach((msg, index) => {
        setTimeout(() => service.setMessageChange(msg), index * 10);
      });
    });
  });

  describe('setMessageChange', () => {
    it('should emit success message', (done: DoneFn) => {
      const successMessage = Message.success('Operation completed');

      service.getMessageChange().pipe(take(1)).subscribe({
        next: (message: Message) => {
          expect(message.status).toBe('SUCCESS');
          expect(message.message).toBe('Operation completed');
          expect(message.error).toBeUndefined();
          done();
        }
      });

      service.setMessageChange(successMessage);
    });

    it('should emit error message', (done: DoneFn) => {
      const errorObj = { code: 500, detail: 'Server error' };
      const errorMessage = Message.error('Something went wrong', errorObj);

      service.getMessageChange().pipe(take(1)).subscribe({
        next: (message: Message) => {
          expect(message.status).toBe('ERROR');
          expect(message.message).toBe('Something went wrong');
          expect(message.error).toBe(errorObj);
          done();
        }
      });

      service.setMessageChange(errorMessage);
    });

    it('should emit info message', (done: DoneFn) => {
      const infoMessage = Message.info('Information update');

      service.getMessageChange().pipe(take(1)).subscribe({
        next: (message: Message) => {
          expect(message.status).toBe('INFO');
          expect(message.message).toBe('Information update');
          expect(message.error).toBeUndefined();
          done();
        }
      });

      service.setMessageChange(infoMessage);
    });

    it('should emit warning message', (done: DoneFn) => {
      const warningMessage = Message.warning('Warning notification');

      service.getMessageChange().pipe(take(1)).subscribe({
        next: (message: Message) => {
          expect(message.status).toBe('WARNING');
          expect(message.message).toBe('Warning notification');
          expect(message.error).toBeUndefined();
          done();
        }
      });

      service.setMessageChange(warningMessage);
    });

    it('should handle empty message', (done: DoneFn) => {
      const emptyMessage = Message.info('');

      service.getMessageChange().pipe(take(1)).subscribe({
        next: (message: Message) => {
          expect(message.status).toBe('INFO');
          expect(message.message).toBe('');
          expect(message.error).toBeUndefined();
          done();
        }
      });

      service.setMessageChange(emptyMessage);
    });

    it('should handle message with null error', (done: DoneFn) => {
      const messageWithNullError = new Message('ERROR', 'Error message', null);

      service.getMessageChange().pipe(take(1)).subscribe({
        next: (message: Message) => {
          expect(message.status).toBe('ERROR');
          expect(message.message).toBe('Error message');
          expect(message.error).toBeNull();
          done();
        }
      });

      service.setMessageChange(messageWithNullError);
    });
  });

  describe('integration scenarios', () => {
    it('should handle multiple subscribers', (done: DoneFn) => {
      const testMessage = Message.success('Broadcast message');
      let subscriber1Received = false;
      let subscriber2Received = false;

      const checkComplete = () => {
        if (subscriber1Received && subscriber2Received) {
          done();
        }
      };

      service.getMessageChange().pipe(take(1)).subscribe({
        next: (message: Message) => {
          expect(message).toBe(testMessage);
          subscriber1Received = true;
          checkComplete();
        }
      });

      service.getMessageChange().pipe(take(1)).subscribe({
        next: (message: Message) => {
          expect(message).toBe(testMessage);
          subscriber2Received = true;
          checkComplete();
        }
      });

      service.setMessageChange(testMessage);
    });

    it('should work correctly with rapid successive messages', (done: DoneFn) => {
      const rapidMessages = [
        Message.info('Message 1'),
        Message.warning('Message 2'),
        Message.success('Message 3')
      ];
      const receivedMessages: Message[] = [];

      service.getMessageChange().subscribe({
        next: (message: Message) => {
          receivedMessages.push(message);
          
          if (receivedMessages.length === rapidMessages.length) {
            expect(receivedMessages).toEqual(rapidMessages);
            done();
          }
        }
      });

      // Send messages rapidly
      rapidMessages.forEach(msg => service.setMessageChange(msg));
    });
  });
});