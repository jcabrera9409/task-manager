# Testing Documentation

## Test Configuration

This project uses **Karma** and **Jasmine** for unit testing. The configuration is optimized to provide coverage reports and execution in different environments.

## Testing Commands

```bash
# Run all tests in watch mode
npm run test

# Run all tests with watch
npm run test:watch

# Run tests once with coverage (ideal for CI)
npm run test:ci

# Run tests with coverage report
npm run test:coverage
```

## Test Structure

```
src/
├── app/
│   ├── _service/
│   │   ├── auth.service.spec.ts
│   │   ├── task.service.spec.ts
│   │   └── guard.service.spec.ts
│   ├── pages/
│   │   └── login/
│   │       └── login.component.spec.ts
│   ├── util/
│   │   └── util.spec.ts
│   └── test-setup.ts
├── karma.conf.js
└── tsconfig.spec.json
```

## Code Coverage

Tests are configured to generate coverage reports with the following thresholds:
- **Statements**: 80%
- **Branches**: 80%
- **Functions**: 80%
- **Lines**: 80%

Reports are generated in: `coverage/task-manager-frontend/`

## Implemented Test Types

### 1. **Services**

#### AuthService (`auth.service.spec.ts`)
- ✅ Login with valid credentials
- ✅ Authentication error handling
- ✅ Login state verification
- ✅ Logout process
- ✅ SessionStorage cleanup

#### TaskService (`task.service.spec.ts`)
- ✅ CRUD operations (Create, Read, Update, Delete)
- ✅ Mark tasks as completed
- ✅ Observables for state changes
- ✅ HTTP response handling

#### GuardService (`guard.service.spec.ts`)
- ✅ Authenticated route protection
- ✅ Expired token verification
- ✅ Redirection on non-authentication

### 2. **Components**

#### LoginComponent (`login.component.spec.ts`)
- ✅ Form validation
- ✅ Valid form submission
- ✅ Login error handling
- ✅ Loading states
- ✅ Navigation after successful login

### 3. **Utilities**

#### UtilMethods (`util.spec.ts`)
- ✅ JWT token management
- ✅ Token expiration verification
- ✅ Token field extraction
- ✅ Period to string conversion
- ✅ SessionStorage handling
- ✅ Singleton pattern

## Mocking and Test Utilities

### TestUtils Class
The `TestUtils` class in `test-setup.ts` provides common utilities:

```typescript
// Create sessionStorage mock
TestUtils.createMockSessionStorage()

// Browser environment mock
TestUtils.mockBrowserEnvironment()

// Create test JWT tokens
TestUtils.createMockJWTToken({ username: 'test', email: 'test@test.com' })
```

### Common Spies and Mocks

```typescript
// Service mocks
const authServiceSpy = jasmine.createSpyObj('AuthService', ['login', 'logout']);

// Router mock
const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

// HttpClient mock
HttpClientTestingModule // Used in all service tests
```

## Implemented Best Practices

### 1. **Arrange, Act, Assert Pattern**
```typescript
it('should login successfully', () => {
  // Arrange
  const mockResponse = { success: true, data: { access_token: 'token' } };
  authServiceSpy.login.and.returnValue(of(mockResponse));
  
  // Act
  component.onSubmit();
  
  // Assert
  expect(routerSpy.navigate).toHaveBeenCalledWith(['task']);
});
```

### 2. **Test Isolation**
- Each test is independent
- Mocks are reset in `beforeEach`
- No dependencies between tests

### 3. **Complete Coverage**
- Happy paths and edge cases
- Error handling
- Loading states
- Form validations

### 4. **Async Testing**
```typescript
// For Observables
service.getData().subscribe(data => {
  expect(data).toBeTruthy();
  done(); // For async tests
});

// For HTTP request testing
const req = httpMock.expectOne('/api/endpoint');
expect(req.request.method).toBe('GET');
req.flush(mockData);
```

## CI/CD Configuration

For continuous integration, use the command:
```bash
npm run test:ci
```

This command:
- Runs all tests once
- Generates coverage reports
- Uses Chrome Headless
- Requires no graphical interface
- Fails if coverage < 80%

## Coverage Reports

Reports include:
- **HTML Report**: `coverage/task-manager-frontend/index.html`
- **LCOV**: For CI/CD tools
- **Coverage**: For SonarQube
- **Text Summary**: In console

## Recommended VS Code Extensions

- **Angular Language Service**
- **Karma Test Explorer**
- **Coverage Gutters**
- **Angular Snippets**

## Troubleshooting

### Common Issues

1. **Tests fail due to sessionStorage**
   - Solution: Use `TestUtils.mockBrowserEnvironment()`

2. **JWT Helper doesn't work in tests**
   - Solution: Complete mock with `jasmine.createSpyObj`

3. **HTTP requests don't complete**
   - Solution: Use `HttpTestingController.verify()` in `afterEach`

4. **Components don't create**
   - Solution: Verify imports in `TestBed.configureTestingModule`

### Test Debugging

```typescript
// Enable debug mode
fit('should debug this test', () => { // 'fit' runs only this test
  console.log('Debug info:', component);
  // test code
});
```