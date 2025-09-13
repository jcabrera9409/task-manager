# Task Manager Backend

Task management system developed with Quarkus, providing a complete REST API for user and task management with JWT authentication.

## 📋 Project Description

This is the backend of a task management application that allows users to register, authenticate, and manage their tasks securely. The project implements clean architecture with well-defined layers and uses development best practices.

### Key Features

- ✅ **JWT Authentication** - Secure authentication system with tokens
- ✅ **Task CRUD** - Complete operations for task management
- ✅ **User CRUD** - User management with data validation
- ✅ **Security** - Password encryption with BCrypt
- ✅ **Validation** - Data validation with Bean Validation
- ✅ **REST API** - Well-documented RESTful endpoints
- ✅ **Database** - MySQL integration using Hibernate ORM
- ✅ **API Documentation** - Integrated Swagger/OpenAPI
- ✅ **Testing** - Unit and integration tests

## 🛠️ Technologies Used

### Core Framework
- **Quarkus 3.26.2** - Supersonic and subatomic Java framework
- **Java 21** - Java LTS version

### Persistence
- **Hibernate ORM with Panache** - Simplified ORM
- **MySQL 8.0** - Relational database
- **JDBC Driver MySQL** - Database connector

### Security
- **SmallRye JWT** - JWT implementation for Quarkus
- **BCrypt** - Password encryption
- **Jakarta Security** - Enterprise security model

### API & Documentation
- **JAX-RS (Quarkus REST)** - REST API
- **Jackson** - JSON serialization/deserialization
- **SmallRye OpenAPI** - Automatic API documentation
- **Swagger UI** - Documentation interface

### Testing
- **JUnit 5** - Testing framework
- **REST Assured** - REST API testing
- **Mockito** - Mocking framework
- **AssertJ** - Fluent assertions

### Validation & Logging
- **Hibernate Validator** - Data validation
- **JBoss Logging** - Logging system

## 🏗️ Project Architecture

```
src/main/java/com/taskmanager/
├── configuration/     # Security configuration and JWT filters
├── controller/        # REST controllers (AuthController, TaskController)
├── dto/              # Data Transfer Objects
├── model/            # JPA entities (User, Task, Token)
├── repository/       # Panache repositories
├── service/          # Services and interfaces
│   ├── impl/         # Service implementations
│   ├── IAuthService.java
│   ├── ITaskService.java
│   └── IJwtService.java
└── utils/            # Utilities (JwtUtils)
```

### Data Models

#### User
- Unique ID
- Name, email, and password
- One-to-Many relationship with tasks
- Email and length validations
- Creation/update timestamps

#### Task
- Unique ID, title, description
- Status: PENDING, IN_PROGRESS, COMPLETED
- Creation, update, and due dates
- Many-to-One relationship with user
- Length validations

#### Token
- JWT token management (revoked/valid)

## 🚀 Configuration and Installation Guide

### Prerequisites

- **Java 21 or higher** (tested with Java 21.0.8)
- **Maven 3.8+** (tested with Maven 3.9.11)
- **MySQL 8.0**
- **Docker** (optional, for database)

### 1. Clone Repository

```bash
git clone <repository-url>
cd task-manager/task-manager-backend
```

### 2. Configure Database

#### Option A: Use Docker (Recommended)
```bash
# Go to development directory
cd ../dev

# Start MySQL with Docker Compose
docker-compose up -d bd
```

#### Option B: Local MySQL
Create database `tmdb` in local MySQL

```sql
CREATE DATABASE tmdb;
CREATE USER 'taskmanager'@'localhost' IDENTIFIED BY 'taskmanager123';
GRANT ALL PRIVILEGES ON tmdb.* TO 'taskmanager'@'localhost';
FLUSH PRIVILEGES;
```

If using custom credentials, configure variables:
```bash
export USER_BD=taskmanager
export PASSWORD_BD=taskmanager123
export DATASOURCE_BD=jdbc:mysql://localhost:3306/tmdb
```

### 3. Configure Environment Variables

Create `.env` file or configure system variables:

```bash
# Database
export USER_BD=root                    # MySQL user
export PASSWORD_BD=root                # MySQL password
export DATASOURCE_BD=jdbc:mysql://localhost:3306/tmdb  # Complete connection URL

# JWT Keys (use absolute paths)
export JWT_PUBLIC_KEY_PATH=file:/Users/surrender/Proyectos/task-manager/dev/publicKey.pem
export JWT_PRIVATE_KEY_PATH=file:/Users/surrender/Proyectos/task-manager/dev/privateKey.pem
```

**Note**: Variables have default values, so they are optional in development:
- `USER_BD` → default: `root`
- `PASSWORD_BD` → default: `root`  
- `DATASOURCE_BD` → default: `jdbc:mysql://localhost:3306/tmdb`

### 4. Generate JWT Keys

```bash
# Go to scripts directory
cd ../scripts

# Execute key generation script
chmod +x setup-keys.sh
./setup-keys.sh
```

### 5. Run Application

#### Development Mode
```bash
mvn quarkus:dev
```

#### Production Mode
```bash
mvn package
java -jar target/quarkus-app/quarkus-run.jar
```

## 📊 API Endpoints

### Authentication (`/rest/api/v1/auth`)

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|---------------|
| POST | `/register` | User registration | No |
| POST | `/login` | User login | No |
| POST | `/logout` | User logout | Yes |

### Tasks (`/rest/api/v1/tasks`)

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|---------------|
| GET | `/` | List all user tasks | Yes |
| GET | `/{id}` | Get task by ID | Yes |
| POST | `/` | Create new task | Yes |
| PUT | `/{id}` | Update task | Yes |
| DELETE | `/{id}` | Delete task | Yes |
| PATCH | `/{id}/status` | Change task status | Yes |

### API Documentation

- **Swagger UI**: http://localhost:8080/q/swagger-ui/
- **OpenAPI Spec**: http://localhost:8080/q/openapi

## 🧪 Testing

### Run Tests

```bash
# All tests
mvn test

# Specific tests
mvn test -Dtest=AuthServiceImplTest
mvn test -Dtest=TaskServiceImplTest
```

### Test Coverage

The project includes:
- Unit tests for services
- Controller integration tests
- Dependency mocking
- Error case validation

## 🔧 Advanced Configuration

### Configuration Profiles

#### Development (`dev`)
- SQL logs enabled
- Permissive CORS
- Local JWT keys
- Relaxed validation

#### Testing (`test`)
- In-memory database
- Test JWT keys
- Minimal logs

#### Production (`prod`)
- Error-only logs
- Restrictive CORS
- JWT keys from environment variables
- Strict validations

### Production Environment Variables

```bash
# Required in production
JWT_PUBLIC_KEY_PATH=file:/path/to/production/publicKey.pem
JWT_PRIVATE_KEY_PATH=file:/path/to/production/privateKey.pem

# Database (optional if using default values)
USER_BD=production_user
PASSWORD_BD=secure_password
DATASOURCE_BD=jdbc:mysql://production_host:3306/production_db
```

**Available Environment Variables:**

| Variable | Description | Default Value | Required |
|----------|-------------|---------------|----------|
| `USER_BD` | MySQL database user | `root` | No |
| `PASSWORD_BD` | MySQL database password | `root` | No |
| `DATASOURCE_BD` | Complete MySQL connection URL | `jdbc:mysql://localhost:3306/tmdb` | No |
| `JWT_PUBLIC_KEY_PATH` | Path to JWT public key file | `/dev/publicKey.pem` | Yes (prod) |
| `JWT_PRIVATE_KEY_PATH` | Path to JWT private key file | `/dev/privateKey.pem` | Yes (prod) |

## 📦 Packaging and Deployment

### Standard JAR
```bash
mvn package
java -jar target/quarkus-app/quarkus-run.jar
```

### Uber JAR
```bash
mvn package -Dquarkus.package.jar.type=uber-jar
java -jar target/*-runner.jar
```

### Native Executable

**System Requirements for Native Compilation:**
- **Maven 3.9.11+** (confirmed working version)
- **Java 21** (confirmed working with 21.0.8)
- **Docker** (for container-based compilation)
- **GraalVM** (optional, for local compilation)

```bash
# Container-based compilation (recommended)
mvn package -Dnative -Dquarkus.native.container-build=true

# Local GraalVM compilation (requires GraalVM installed)
mvn package -Dnative

# Execute native binary
./target/task-manager-backend-1.0.0-SNAPSHOT-runner
```

**Note**: Container-based compilation (`-Dquarkus.native.container-build=true`) is recommended as it:
- Works without local GraalVM installation
- Ensures consistent build environment
- Avoids platform-specific compilation issues

### Docker Images

**Before building Docker images, compile the native executable:**

```bash
# Container-based native compilation (recommended)
mvn package -Dnative -Dquarkus.native.container-build=true
```

**Then build the Docker images:**

```bash
# Ultra-optimized micro image (~20-50MB)
docker build -f docker/Dockerfile.native-micro -t task-manager-micro .

# Standard native image (~50-100MB)
docker build -f docker/Dockerfile.native -t task-manager-native .

# JVM image (development, ~200MB)
docker build -f docker/Dockerfile.jvm -t task-manager-jvm .

# Legacy JAR image
docker build -f docker/Dockerfile.legacy-jar -t task-manager-legacy .
```

**Run Docker containers:**

```bash
# Run micro image
docker run -i --rm -p 8080:8080 task-manager-micro

# Run with environment variables
docker run -i --rm -p 8080:8080 \
  -e USER_BD=root \
  -e PASSWORD_BD=root \
  -e DATASOURCE_BD=jdbc:mysql://host.docker.internal:3306/tmdb \
  task-manager-micro
```

## 🔒 Security

### JWT Authentication
- Tokens with configurable expiration time
- RS256 algorithm with RSA keys
- Automatic validation on protected endpoints
- Blacklist for revoked tokens

### Validations
- Input validation with Bean Validation
- Data sanitization
- Password encryption with BCrypt
- Configurable CORS security headers

## 🐛 Troubleshooting

### Common Problems

1. **Maven command not found**:
   ```bash
   # Check Maven installation
   mvn --version
   
   # Install Maven (macOS with Homebrew)
   brew install maven
   
   # Or download from https://maven.apache.org/
   ```

2. **Maven version compatibility**:
   ```bash
   # Verify Maven version (should be 3.8+ for Quarkus)
   mvn --version
   # Expected: Apache Maven 3.9.11 or higher
   
   # Java version compatibility
   java --version
   # Expected: Java 21.0.8 or higher
   ```

3. **Database connection error**: 
   - Verify MySQL is running
   - Check credentials in `USER_BD` and `PASSWORD_BD` variables
   - Verify connection URL in `DATASOURCE_BD`
   
4. **JWT error**: 
   - Verify paths in `JWT_PUBLIC_KEY_PATH` and `JWT_PRIVATE_KEY_PATH` are absolute
   - Verify key files exist
   - Run `setup-keys.sh` script if keys don't exist
   
5. **CORS error**: 
   - Verify allowed origins configuration
   - In development, use `http://localhost:3000`
   
6. **Validation error**: 
   - Verify sent data meets validations
   - Check logs for specific details

7. **Native compilation issues**:
   ```bash
   # Use container build to avoid local GraalVM requirements
   mvn package -Dnative -Dquarkus.native.container-build=true
   
   # Check Docker is running
   docker --version
   
   # Verify sufficient memory for compilation (8GB+ recommended)
   docker run --rm -it --memory=8g maven:3.9.11-eclipse-temurin-21 mvn --version
   ```

### Configuration Variables by Profile

```bash
# Development (dev)
%dev.quarkus.hibernate-orm.log.sql=true
%dev.quarkus.http.cors.origins=http://localhost:3000,http://127.0.0.1:3000

# Production (prod)  
%prod.quarkus.log.console.level=INFO
%prod.quarkus.http.cors.origins=https://task-manager.com
```

### Maven Configuration

This project requires Maven for building and running. Since the project doesn't include Maven Wrapper, you need Maven installed on your system.

| Aspect | System Maven (`mvn`) |
|--------|---------------------|
| **Version Required** | 3.9.11+ |
| **Setup** | Requires Maven installation |
| **Java Compatibility** | Java 21+ |
| **Installation** | `brew install maven` (macOS) |

**Verified compatibility:**
- ✅ Java 21.0.8 (Oracle Corporation)
- ✅ Maven 3.9.11
- ✅ macOS (tested on macOS 15.6.1 ARM64)

**Installation:**
```bash
# macOS with Homebrew
brew install maven

# Verify installation
mvn --version
```

### Useful Logs

```bash
# View SQL logs in development
%dev.quarkus.hibernate-orm.log.sql=true

# Log level
%dev.quarkus.log.console.level=DEBUG
```

## 🤝 Contributing

1. Fork the project
2. Create feature branch (`git checkout -b feature/new-feature`)
3. Commit changes (`git commit -am 'Add new feature'`)
4. Push to branch (`git push origin feature/new-feature`)
5. Create Pull Request

## 📄 License

This project is under MIT license. See `LICENSE` file for more details.

## 📞 Support

For more information about Quarkus, visit: <https://quarkus.io/>

---

**Developed with ❤️ using Quarkus Framework**
