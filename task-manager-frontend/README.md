# Task Manager Frontend

A modern, responsive task management application built with Angular 17, featuring user authentication, task management, and a clean Material Design interface.

## 🚀 Features

- **User Authentication**: Secure JWT-based authentication system
- **Task Management**: Full CRUD operations for tasks
- **Responsive Design**: Optimized for desktop and mobile devices
- **Material Design**: Angular Material components for consistent UI
- **Real-time Updates**: Dynamic task list updates
- **Server-Side Rendering (SSR)**: Enhanced performance and SEO
- **Progressive Web App**: PWA capabilities for offline usage

## 🛠️ Technology Stack

- **Framework**: Angular 17.3.x
- **UI Library**: Angular Material 17.3.x
- **Styling**: TailwindCSS 3.4.x + Custom SCSS
- **Authentication**: @auth0/angular-jwt 5.2.x
- **HTTP Client**: Angular HttpClient with RxJS
- **Build Tool**: Angular CLI 17.3.x
- **Testing**: Karma + Jasmine with Puppeteer
- **Server**: Express.js for SSR

## 📋 Prerequisites

- Node.js 18.x or higher
- npm 9.x or higher
- Angular CLI 17.x

## 🔧 Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd task-manager-frontend
```

2. **Install dependencies**
```bash
npm install
```

3. **Configure environment**
Update the environment configuration in `src/assets/env.js`:
```javascript
window.__env.apiUrl = 'http://localhost:8080/rest/api/v1'; // Your backend API URL
window.__env.token_name = 'access_token';
```

## 🚀 Running the Application

### Development Server
```bash
npm start
# or
ng serve
```
Navigate to `http://localhost:4200/` for the development server.

### Production Build
```bash
npm run build
```
Build artifacts will be stored in the `dist/` directory.

### Server-Side Rendering
```bash
npm run serve:ssr:task-manager-frontend
```
Serves the SSR version on `http://localhost:4000/`.

## 🧪 Testing

> 📖 For detailed testing documentation, see [TESTING.md](TESTING.md)

### Unit Tests
```bash
# Run tests in watch mode
npm run test

# Run tests once with coverage
npm run test:ci

# Run tests with detailed coverage report
npm run test:coverage
```

### Test Coverage
The project maintains high test coverage with the following thresholds:
- **Statements**: 80%
- **Branches**: 80% 
- **Functions**: 80%
- **Lines**: 80%

Coverage reports are generated in `coverage/task-manager-frontend/`

### CI Testing Script
For continuous integration environments:
```bash
./scripts/test-ci.sh
```

**CI/CD Features:**
- **Cross-platform testing**: Chrome headless for consistent results
- **Coverage reporting**: Automatic generation of coverage reports
- **Puppeteer integration**: Browser automation for reliable testing
- **Parallel test execution**: Optimized for CI/CD pipelines
- **Exit code handling**: Proper failure detection for automated workflows

## 📁 Project Structure

```
src/
├── app/
│   ├── _model/                 # Data models and DTOs
│   │   ├── dto.ts             # API response DTOs
│   │   ├── task.ts            # Task model
│   │   ├── user.ts            # User model
│   │   └── message.ts         # Notification message model
│   ├── _service/              # Angular services
│   │   ├── auth.service.ts    # Authentication service
│   │   ├── task.service.ts    # Task management service
│   │   ├── guard.service.ts   # Route guards
│   │   ├── notification.service.ts # Notification service
│   │   └── env.service.ts     # Environment configuration
│   ├── pages/                 # Page components
│   │   ├── login/             # Login page
│   │   ├── layout/            # Main layout wrapper
│   │   └── task/              # Task management pages
│   ├── shared/                # Shared components
│   │   ├── loader/            # Loading spinner
│   │   └── notification/      # Toast notifications
│   ├── modals/                # Modal dialogs
│   │   ├── confirm-dialog/    # Confirmation dialog
│   │   └── task-edition-dialog/ # Task edit/create modal
│   ├── util/                  # Utility functions
│   ├── app.config.ts          # Application configuration
│   ├── app.routes.ts          # Application routes
│   └── app.component.*        # Root component
├── assets/
│   └── env.js                 # Runtime environment config
├── styles.css                 # Global styles
└── custom-theme.scss          # Material Design theme
```

## 🔐 Authentication

The application uses JWT-based authentication with the following features:

- **Token Storage**: Secure session storage
- **Route Guards**: Protected routes with authentication checks
- **Auto-logout**: Automatic session expiration handling
- **JWT Integration**: @auth0/angular-jwt for token management

## 🎨 UI/UX Design

### Material Design Theme
Custom Material Design theme with:
- Primary color: `#3B82F6` (Blue 500)
- Secondary color: `#1E40AF` (Blue 800)
- Success: `#10B981` (Emerald 500)
- Warning: `#F59E0B` (Amber 500)
- Danger: `#EF4444` (Red 500)

### TailwindCSS Integration
Utility-first CSS framework configured with:
- Responsive design utilities
- Custom color palette
- Component styling support

## 📱 Progressive Web App (PWA)

The application includes PWA capabilities:
- Offline functionality
- App-like experience
- Installation prompts
- Service worker integration

## 🌐 Server-Side Rendering (SSR)

Angular Universal SSR implementation:
- Improved SEO performance
- Faster initial page loads
- Enhanced user experience
- Express.js server integration

## 🔄 State Management

The application uses Angular services for state management:
- **AuthService**: User authentication state
- **TaskService**: Task data and operations
- **NotificationService**: UI notifications
- **RxJS Subjects**: Reactive state updates

## 📊 Performance Optimizations

- **Lazy Loading**: Route-based code splitting
- **OnPush Change Detection**: Optimized component updates
- **Tree Shaking**: Unused code elimination
- **Bundle Optimization**: Webpack optimization strategies

## 🔧 Build Configuration

### Angular Configuration
- **Target**: ES2022
- **Module**: ES2022
- **Strict Mode**: Enabled for type safety
- **Source Maps**: Available in development

### Build Budgets
- **Initial Bundle**: Max 1MB
- **Component Styles**: Max 4KB warning threshold

## 🚀 Deployment

### Docker Containerization

The project includes a production-ready Dockerfile for containerized deployment using nginx.

#### Building Docker Image

1. **Build the Angular application**
```bash
npm run build
```

2. **Build Docker image**
```bash
docker build -f docker/Dockerfile -t task-manager-frontend .
```

3. **Run the container**
```bash
# Run on port 80 (production)
docker run -p 80:80 task-manager-frontend

# Run on custom port
docker run -p 3000:80 task-manager-frontend
```

#### Docker Features

- **Base Image**: `nginx:1.27-alpine` - Lightweight and secure
- **Security Hardening**: 
  - Non-root user (`webuser`) for enhanced security
  - Proper file permissions and ownership
  - Minimal attack surface with Alpine Linux
- **Process Management**: 
  - `dumb-init` for proper signal handling and zombie reaping
  - Graceful shutdown support
- **Performance**: 
  - Optimized nginx configuration
  - Static file serving with caching headers
  - Small image size (~50MB)

#### Docker Image Structure
```dockerfile
FROM nginx:1.27-alpine

# Install process manager
RUN apk add --no-cache dumb-init

# Create non-root user
RUN addgroup -S webgroup && \
    adduser -S webuser -G webgroup

# Copy built Angular app
COPY --chown=webuser:webgroup dist/task-manager-frontend/browser/ /usr/share/nginx/html/

# Set proper permissions
RUN chown -R webuser:webgroup /var/cache/nginx && \
    chown -R webuser:webgroup /var/log/nginx && \
    chown -R webuser:webgroup /etc/nginx/conf.d && \
    touch /var/run/nginx.pid && \
    chown webuser:webgroup /var/run/nginx.pid

USER webuser
EXPOSE 80

ENTRYPOINT ["dumb-init", "--"]
CMD ["nginx", "-g", "daemon off;"]
```

### Environment Configuration
Update `src/assets/env.js` for different environments:

**Development**:
```javascript
window.__env.production = false;
window.__env.apiUrl = 'http://localhost:8080/rest/api/v1';
```

**Production**:
```javascript
window.__env.production = true;
window.__env.apiUrl = 'https://your-api-domain.com/rest/api/v1';
```

### Production Deployment

#### Full Stack with Docker Compose

Create a `docker-compose.yml` for full deployment:

```yaml
version: '3.8'
services:
  frontend:
    image: task-manager-frontend:latest
    ports:
      - "80:80"
    environment:
      - NGINX_PORT=80
    depends_on:
      - backend
    restart: unless-stopped
    
  backend:
    image: task-manager-backend:latest
    ports:
      - "8080:8080"
    environment:
      USER_BD: ${DB_USER}
      PASSWORD_BD: ${DB_PASSWORD}
      DATASOURCE_BD: jdbc:mysql://database:3306/tmdb
    depends_on:
      - database
      
  database:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_ROOT_PASSWORD}
      MYSQL_DATABASE: tmdb
    volumes:
      - mysql_data:/var/lib/mysql
    ports:
      - "3306:3306"

volumes:
  mysql_data:
```

#### Cloud Deployment Options

**AWS ECS/Fargate:**
- Lightweight nginx container perfect for Fargate
- Quick startup times and low resource usage
- Ideal for auto-scaling scenarios

**Google Cloud Run:**
- Serverless deployment with pay-per-request model
- Container-optimized with minimal resource requirements
- Automatic HTTPS and global CDN

**Azure Container Instances:**
- Simple container deployment
- Cost-effective for smaller workloads
- Easy integration with Azure services

#### Production Best Practices

1. **Environment Variables**
   - Set `window.__env.production = true`
   - Configure proper API URLs
   - Enable error tracking

2. **Security Headers**
   - Add custom nginx configuration for security headers
   - Implement proper CORS settings
   - Use HTTPS in production

3. **Performance Optimization**
   - Enable gzip compression in nginx
   - Set appropriate cache headers
   - Use CDN for static assets

4. **Monitoring**
   - Implement health checks
   - Monitor nginx access and error logs
   - Set up alerting for container failures

### Build Commands
```bash
# Development build
ng build

# Production build
ng build --configuration production

# SSR build
ng build --ssr
```

## 🤝 Development Guidelines

### Code Standards
- Follow Angular style guide
- Use TypeScript strict mode
- Implement proper error handling
- Write comprehensive unit tests

### Component Structure
- Use OnPush change detection
- Implement proper lifecycle hooks
- Follow reactive programming patterns
- Use Angular Material components

### Service Architecture
- Injectable services with proper DI
- Observable-based data flow
- Error handling with operators
- Proper memory management

## 📄 API Integration

The frontend integrates with a REST API backend:

**Base URL**: Configurable via `env.js`
**Authentication**: JWT token in headers
**Endpoints**:
- `POST /auth/login` - User authentication
- `GET /tasks` - Retrieve tasks
- `POST /tasks` - Create task
- `PUT /tasks/{id}` - Update task
- `DELETE /tasks/{id}` - Delete task

## 🐛 Troubleshooting

### Common Issues

**Chrome binary not found in tests**:
```bash
npm install puppeteer
```

**Port already in use**:
```bash
ng serve --port 4201
```

**Build memory issues**:
```bash
ng build --max-old-space-size=8192
```

**Docker build fails**:
```bash
# Ensure Angular build completed successfully
npm run build
ls -la dist/task-manager-frontend/browser/

# Build with verbose output
docker build -f docker/Dockerfile -t task-manager-frontend . --no-cache
```

**Docker container not starting**:
```bash
# Check container logs
docker logs <container-id>

# Run with interactive shell for debugging
docker run -it --entrypoint sh task-manager-frontend
```

**Nginx permission issues**:
```bash
# Verify file ownership in container
docker run -it --entrypoint sh task-manager-frontend
ls -la /usr/share/nginx/html/
```

## 📚 Additional Resources

- [Angular Documentation](https://angular.io/docs)
- [Angular Material](https://material.angular.io/)
- [TailwindCSS](https://tailwindcss.com/)
- [RxJS Documentation](https://rxjs.dev/)

## 👥 Contributors

This project is part of a full-stack task management system.

## 📜 License

This project is licensed under the terms specified in the LICENSE file.
