import { Message } from './message';

describe('Message', () => {
  
  describe('constructor', () => {
    it('should create message with status and message', () => {
      const message = new Message('SUCCESS', 'Test message');
      
      expect(message.status).toBe('SUCCESS');
      expect(message.message).toBe('Test message');
      expect(message.error).toBeUndefined();
    });

    it('should create message with status, message and error', () => {
      const error = { code: 500, detail: 'Server error' };
      const message = new Message('ERROR', 'Test error message', error);
      
      expect(message.status).toBe('ERROR');
      expect(message.message).toBe('Test error message');
      expect(message.error).toBe(error);
    });
  });

  describe('static error', () => {
    it('should create error message without error object', () => {
      const message = Message.error('Error occurred');
      
      expect(message.status).toBe('ERROR');
      expect(message.message).toBe('Error occurred');
      expect(message.error).toBeUndefined();
    });

    it('should create error message with error object', () => {
      const error = { code: 404, detail: 'Not found' };
      const message = Message.error('Resource not found', error);
      
      expect(message.status).toBe('ERROR');
      expect(message.message).toBe('Resource not found');
      expect(message.error).toBe(error);
    });
  });

  describe('static success', () => {
    it('should create success message', () => {
      const message = Message.success('Operation completed successfully');
      
      expect(message.status).toBe('SUCCESS');
      expect(message.message).toBe('Operation completed successfully');
      expect(message.error).toBeUndefined();
    });

    it('should create success message with empty string', () => {
      const message = Message.success('');
      
      expect(message.status).toBe('SUCCESS');
      expect(message.message).toBe('');
      expect(message.error).toBeUndefined();
    });
  });

  describe('static info', () => {
    it('should create info message', () => {
      const message = Message.info('This is an informational message');
      
      expect(message.status).toBe('INFO');
      expect(message.message).toBe('This is an informational message');
      expect(message.error).toBeUndefined();
    });

    it('should create info message with special characters', () => {
      const message = Message.info('Info: 100% completed! ✓');
      
      expect(message.status).toBe('INFO');
      expect(message.message).toBe('Info: 100% completed! ✓');
      expect(message.error).toBeUndefined();
    });
  });

  describe('static warning', () => {
    it('should create warning message', () => {
      const message = Message.warning('This is a warning message');
      
      expect(message.status).toBe('WARNING');
      expect(message.message).toBe('This is a warning message');
      expect(message.error).toBeUndefined();
    });

    it('should create warning message with long text', () => {
      const longWarning = 'This is a very long warning message that might contain multiple sentences and detailed information about the warning condition.';
      const message = Message.warning(longWarning);
      
      expect(message.status).toBe('WARNING');
      expect(message.message).toBe(longWarning);
      expect(message.error).toBeUndefined();
    });
  });

  describe('integration tests', () => {
    it('should create different message types with consistent structure', () => {
      const errorMsg = Message.error('Error test');
      const successMsg = Message.success('Success test');
      const infoMsg = Message.info('Info test');
      const warningMsg = Message.warning('Warning test');

      // All should have the expected properties and correct status
      expect(errorMsg.status).toBe('ERROR');
      expect(errorMsg.message).toBe('Error test');
      
      expect(successMsg.status).toBe('SUCCESS');
      expect(successMsg.message).toBe('Success test');
      
      expect(infoMsg.status).toBe('INFO');
      expect(infoMsg.message).toBe('Info test');
      
      expect(warningMsg.status).toBe('WARNING');
      expect(warningMsg.message).toBe('Warning test');
    });
  });
});