# Task Manager

A complete task management application built with modern technologies, demonstrating a full-stack development workflow with backend API, containerization, and infrastructure automation.

## 📋 Project Overview

This project showcases a production-ready task management system with:

- **Backend API**: RESTful service built with Quarkus and Java 21
- **Authentication**: JWT-based security with RS256 encryption
- **Database**: MySQL 8.0 with Hibernate ORM
- **Containerization**: Multiple Docker deployment strategies
- **Development**: Complete development environment with Docker Compose

## 🏗️ Project Structure

```
task-manager/
├── task-manager-backend/     # Quarkus REST API
│   ├── src/                  # Source code
│   ├── docker/               # Dockerfile variants
│   ├── target/               # Build artifacts
│   └── pom.xml              # Maven configuration
├── dev/                      # Development environment
│   ├── docker-compose.yml   # Local services
│   ├── publicKey.pem        # JWT public key
│   ├── privateKey.pem       # JWT private key
│   └── mysql-data/          # Database persistence
├── scripts/                  # Utility scripts
│   └── setup-keys.sh        # JWT key generation
└── README.md                # This file
```

## 🛠️ Technology Stack

### Backend
- **Quarkus 3.26.2** - Supersonic Java framework
- **Java 21** - Latest LTS Java version
- **Maven 3.9.11** - Build and dependency management
- **MySQL 8.0** - Relational database
- **Hibernate ORM with Panache** - Object-relational mapping
- **SmallRye JWT** - JWT authentication
- **JAX-RS** - REST API framework
- **Bean Validation** - Data validation
- **JUnit 5** - Testing framework

### Infrastructure
- **Docker & Docker Compose** - Containerization
- **GraalVM Native Image** - High-performance native compilation
- **Swagger/OpenAPI** - API documentation

## 🚀 Quick Start Guide

### Prerequisites

Ensure you have the following installed:
- **Java 21** (confirmed: 21.0.8)
- **Maven 3.9.11** (confirmed: 3.9.11)
- **Docker & Docker Compose**
- **MySQL 8.0** (or use Docker)

### 1. Clone Repository

```bash
git clone <repository-url>
cd task-manager
```

### 2. Setup Development Environment

```bash
# Generate JWT keys
cd scripts
chmod +x setup-keys.sh
./setup-keys.sh
cd ..

# Start MySQL database
cd dev
docker-compose up -d bd
cd ..
```

### 3. Build and Run Backend

Navigate to the backend directory:

```bash
cd task-manager-backend
```

#### Development Mode (Hot Reload)
```bash
mvn quarkus:dev
```

#### Production JAR Build
```bash
mvn package

# Run the application
java -jar target/quarkus-app/quarkus-run.jar
```

#### Native Executable Build

For ultra-fast startup and minimal memory footprint:

```bash
# Using Docker for native compilation (recommended)
mvn package -Dnative -Dquarkus.native.container-build=true

# Run native executable
./target/task-manager-backend-1.0.0-SNAPSHOT-runner
```

**Note**: Native compilation requires either:
- GraalVM installed locally, OR
- Docker for container-based compilation (recommended)

### 4. Verify Installation

Once the application is running, verify the setup:

- **API Documentation**: http://localhost:8080/q/swagger-ui/
- **Health Check**: http://localhost:8080/q/health
- **OpenAPI Spec**: http://localhost:8080/q/openapi

## 🔧 Configuration

### Environment Variables

The application uses the following environment variables:

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `USER_BD` | MySQL username | `root` | No |
| `PASSWORD_BD` | MySQL password | `root` | No |
| `DATASOURCE_BD` | MySQL connection URL | `jdbc:mysql://localhost:3306/tmdb` | No |
| `JWT_PUBLIC_KEY_PATH` | JWT public key file path | `file:../dev/publicKey.pem` | Yes |
| `JWT_PRIVATE_KEY_PATH` | JWT private key file path | `file:../dev/privateKey.pem` | Yes |

### Database Setup

#### Option A: Docker (Recommended)
```bash
cd dev
docker-compose up -d bd
```

#### Option B: Local MySQL
```sql
CREATE DATABASE tmdb;
CREATE USER 'taskmanager'@'localhost' IDENTIFIED BY 'taskmanager123';
GRANT ALL PRIVILEGES ON tmdb.* TO 'taskmanager'@'localhost';
FLUSH PRIVILEGES;
```

## 📦 Docker Deployment

The project provides multiple Docker strategies optimized for different use cases:

### Build Docker Images

```bash
cd task-manager-backend

# Ultra-optimized micro image (~20-50MB)
docker build -f docker/Dockerfile.native-micro -t task-manager-micro .

# Standard native image (~50-100MB)
docker build -f docker/Dockerfile.native -t task-manager-native .

# JVM image (development, ~200MB)
docker build -f docker/Dockerfile.jvm -t task-manager-jvm .

# Legacy JAR image
docker build -f docker/Dockerfile.legacy-jar -t task-manager-legacy .
```

### Full Stack Deployment

```bash
# Start complete stack (database + backend)
cd dev
docker-compose up -d
```

This will start:
- MySQL database on port 3306
- Backend API on port 8080

## 🧪 Testing

### Run Tests

```bash
cd task-manager-backend

# All tests
mvn test

# Specific test class
mvn test -Dtest=AuthServiceImplTest

# Integration tests
mvn verify
```

### Test Coverage

The project includes comprehensive tests:
- Unit tests for services and repositories
- Integration tests for REST endpoints
- Security and authentication tests
- Database interaction tests

## 📊 API Documentation

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/rest/api/v1/auth/register` | User registration |
| POST | `/rest/api/v1/auth/login` | User authentication |
| POST | `/rest/api/v1/auth/logout` | User logout |

### Task Management Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/rest/api/v1/tasks` | List user tasks | Yes |
| GET | `/rest/api/v1/tasks/{id}` | Get task by ID | Yes |
| POST | `/rest/api/v1/tasks` | Create new task | Yes |
| PUT | `/rest/api/v1/tasks/{id}` | Update task | Yes |
| DELETE | `/rest/api/v1/tasks/{id}` | Delete task | Yes |
| PATCH | `/rest/api/v1/tasks/{id}/status` | Update task status | Yes |

### Interactive Documentation

- **Swagger UI**: http://localhost:8080/q/swagger-ui/
- **OpenAPI JSON**: http://localhost:8080/q/openapi

## 🔒 Security Features

- **JWT Authentication**: RS256 algorithm with RSA key pairs
- **Password Encryption**: BCrypt hashing
- **Input Validation**: Bean Validation annotations
- **CORS Configuration**: Configurable cross-origin policies
- **Token Blacklisting**: Revoked token management

## 📈 Performance & Optimization

### Startup Times & Memory Usage

| Build Type | Startup Time | Memory Usage | Image Size |
|------------|--------------|--------------|------------|
| JVM Mode | ~3-5 seconds | ~100-200MB | ~200MB |
| Native Mode | ~0.1 seconds | ~20-50MB | ~50MB |
| Micro Image | ~0.05 seconds | ~15-30MB | ~20MB |

### Native Compilation Benefits

- **99% faster startup**: From seconds to milliseconds
- **75% less memory**: Minimal runtime footprint
- **Smaller containers**: Optimized for cloud deployment
- **Instant scaling**: Perfect for serverless environments

## 🐛 Troubleshooting

### Common Issues

1. **Maven command not found**
   ```bash
   # Install Maven
   brew install maven  # macOS
   # or download from https://maven.apache.org/
   ```

2. **Database connection failed**
   ```bash
   # Check MySQL is running
   docker-compose up -d bd
   
   # Verify environment variables
   echo $USER_BD $PASSWORD_BD $DATASOURCE_BD
   ```

3. **JWT errors**
   ```bash
   # Regenerate keys
   cd scripts && ./setup-keys.sh
   
   # Check file paths are absolute
   export JWT_PUBLIC_KEY_PATH=file:/absolute/path/to/publicKey.pem
   ```

4. **Native compilation issues**
   ```bash
   # Use container build (recommended)
   mvn package -Dnative -Dquarkus.native.container-build=true
   ```

### Development vs Production

| Feature | Development | Production |
|---------|-------------|------------|
| Hot Reload | ✅ Enabled | ❌ Disabled |
| SQL Logging | ✅ Enabled | ❌ Disabled |
| CORS | ✅ Permissive | ⚠️ Restrictive |
| Validation | ⚠️ Relaxed | ✅ Strict |

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

### Development Workflow

```bash
# Start development environment
cd dev && docker-compose up -d bd

# Run in development mode
cd task-manager-backend && mvn quarkus:dev

# Run tests
mvn test

# Build native image
mvn package -Dnative -Dquarkus.native.container-build=true
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support & Resources

- **Quarkus Documentation**: https://quarkus.io/guides/
- **Java 21 Features**: https://openjdk.org/projects/jdk/21/
- **Maven Documentation**: https://maven.apache.org/guides/
- **Docker Best Practices**: https://docs.docker.com/develop/best-practices/

---

**Built with ❤️ using Quarkus, Java 21, and Maven 3.9.11**
