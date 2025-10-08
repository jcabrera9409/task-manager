# Kubernetes Configuration for Task Manager

This directory contains the Kubernetes manifests for deploying the Task Manager application infrastructure on a Kubernetes cluster.

## Overview

The Task Manager application uses a MySQL database as its data persistence layer. This configuration sets up the necessary Kubernetes resources to run MySQL in a containerized environment with proper persistence, security, and networking.

## Architecture

The deployment consists of the following components:

1. **Namespace**: Isolated environment for the application
2. **MySQL Database**: Containerized MySQL 8.0 instance
3. **Backend API**: Task Manager backend service with JWT authentication
4. **Persistent Storage**: Persistent volume for database data
5. **Secrets Management**: Secure storage for database credentials
6. **JWT Keys**: ConfigMap for JWT public and private keys

## Files Structure

```
k8s/
├── README.md                    # This documentation file
├── .env                         # Environment variables for secrets
├── 00.namespace.yaml           # Namespace definition
├── 01.mysql.yaml              # MySQL deployment, service, and PVC
├── 02.backend.yaml             # Backend API deployment, service, and JWT keys
└── kustomization.yaml          # Kustomize configuration
```

## Issues Analysis and Status

### ✅ **Resolved Issues**

1. **Missing MySQL File Reference**
   - **Issue**: `kustomization.yaml` referenced `02.mysql.yaml` but the actual file is `01.mysql.yaml`
   - **Status**: ✅ **FIXED** - Updated reference to point to the correct file
   - **Validation**: `kubectl apply --dry-run=client -k .` now works successfully

2. **Missing ConfigMap File**
   - **Issue**: `kustomization.yaml` referenced `01.configmaps.yaml` which doesn't exist
   - **Status**: ✅ **FIXED** - Removed the non-existent reference
   - **Impact**: Kustomize build now completes successfully

### ⚠️ **Remaining Security Concerns**

1. **Plain Text Secrets**
   - **Issue**: Database credentials are stored in plain text in `.env` file
   - **Impact**: Security vulnerability if repository is exposed
   - **Recommendation**: Use external secret management (Azure Key Vault, AWS Secrets Manager, etc.)

2. **Weak Default Passwords**
   - **Issue**: Simple passwords (`root`, `user`) used for database access
   - **Impact**: Security vulnerability
   - **Recommendation**: Use strong, randomly generated passwords

### 💡 Configuration Improvements

1. **Storage Class Dependency**
   - **Issue**: Hard-coded `managed-csi` storage class (AKS specific)
   - **Impact**: Not portable to other cloud providers
   - **Recommendation**: Make storage class configurable or use default

2. **Resource Requests/Limits**
   - **Current**: Basic resource allocation defined
   - **Recommendation**: Review and adjust based on actual workload requirements

## Validation Results

### ✅ **Configuration Validation**

The Kubernetes configuration has been tested and validated:

```bash
# Test command used for validation:
kubectl apply --dry-run=client -k .

# Results:
namespace/test created (dry run)
secret/mysql-secret-m8db92ck82 created (dry run)
service/mysql-service created (dry run)
persistentvolumeclaim/mysql-pvc created (dry run)
deployment.apps/mysql created (dry run)
```

**Status**: ✅ **All resources are properly configured and ready for deployment**

### 📁 **Current File Structure**

```
k8s/
├── README.md                    # This documentation file
├── .env                         # Environment variables for secrets
├── .env.example                 # Example environment file
├── .gitignore                   # Git ignore rules
├── 00.namespace.yaml           # Namespace definition
├── 01.mysql.yaml              # MySQL deployment, service, and PVC
├── 02.backend.yaml             # Backend API deployment, service, and JWT keys
├── kustomization.yaml          # Kustomize configuration (FIXED)
├── deploy.sh                   # 🚀 Main deployment script (Opción 1)
├── cleanup.sh                  # 🧹 Cleanup script
├── setup-jwt-keys.sh           # Alternative: Setup JWT keys (Opción 4)
└── generate-jwt-configmap.sh   # Alternative: Generate ConfigMap (Opción 2)
```

## Prerequisites

Before deploying this configuration, ensure you have:

- A running Kubernetes cluster
- `kubectl` configured to access your cluster
- `kustomize` installed (or use `kubectl apply -k`)
- Appropriate storage class available in your cluster

## Quick Start (Opción 1: Kustomize con rutas relativas)

### ✅ **Configuración Actual: LISTA PARA USAR**

La configuración está optimizada para usar Kustomize con rutas relativas a las claves JWT.

### 🚀 **Despliegue Simple**

```bash
# 1. Navegar al directorio k8s
cd k8s/

# 2. Verificar/crear archivo .env
cp .env.example .env
# Editar .env con credenciales seguras

# 3. Desplegar todo con un comando
./deploy.sh
# O manualmente: kubectl apply -k .
```

### 🧹 **Limpieza**

```bash
# Eliminar todo el despliegue
./cleanup.sh
```

### 3. Verify Deployment

```bash
# Check all resources in the test namespace
kubectl get all -n test

# Check persistent volume claims
kubectl get pvc -n test

# Check secrets and configmaps
kubectl get secrets,configmaps -n test

# Check pod logs for MySQL
kubectl logs -n test deployment/mysql

# Check pod logs for Backend
kubectl logs -n test deployment/task-manager-backend

# Port forward to access backend locally (optional)
kubectl port-forward -n test service/task-manager-backend-service 8080:8080
```

## Resource Details

### Namespace (00.namespace.yaml)

Creates an isolated namespace called `test` for the application resources.

### MySQL Deployment (01.mysql.yaml)

This file contains three Kubernetes resources:

#### 1. PersistentVolumeClaim
- **Name**: `mysql-pvc`
- **Storage**: 10Gi
- **Access Mode**: ReadWriteOnce
- **Storage Class**: managed-csi (AKS default)

#### 2. Deployment
- **Image**: mysql:8.0
- **Replicas**: 1
- **Port**: 3306
- **Environment Variables**: Loaded from secret
- **Volume Mount**: `/var/lib/mysql`
- **Resource Limits**: 
  - Memory: 512Mi-1Gi
  - CPU: 250m-500m
- **Health Checks**: Liveness and readiness probes

#### 3. Service
- **Name**: `mysql-service`
- **Type**: ClusterIP
- **Port**: 3306
- **Target Port**: 3306

### Secret Configuration

The MySQL deployment uses a Kubernetes secret generated from the `.env` file containing:

```
MYSQL_ROOT_PASSWORD=root
MYSQL_USER=user  
MYSQL_PASSWORD=user
MYSQL_DATABASE=tmdb
```

### Backend API Configuration (02.backend.yaml)

The Task Manager backend API is deployed with the following configuration:

#### 1. ConfigMap for JWT Keys
- **Name**: `jwt-keys`
- **Contains**: 
  - `privateKey.pem`: JWT private key for token signing
  - `publicKey.pem`: JWT public key for token verification
- **Mount Path**: `/app/keys` in the container

#### 2. Deployment
- **Image**: `jcabrera9409/task-manager-backend:latest`
- **Replicas**: 1
- **Port**: 8080
- **Environment Variables**:
  - `MYSQL_USER`, `MYSQL_PASSWORD`, `DATASOURCE_DATABASE`: From MySQL secret
  - `DATASOURCE_HOST`: `mysql-service` (service discovery)
  - `DATASOURCE_PORT`: `3306`
  - `JWT_PUBLIC_KEY_PATH`: `/app/keys/publicKey.pem`
  - `JWT_PRIVATE_KEY_PATH`: `/app/keys/privateKey.pem`
- **Volume Mounts**: JWT keys mounted from ConfigMap
- **Resource Limits**: 
  - Memory: 256Mi-512Mi
  - CPU: 250m-500m
- **Health Checks**: 
  - Liveness: `/q/health/live` (Quarkus health endpoint)
  - Readiness: `/q/health/ready` (Quarkus health endpoint)

#### 3. Service
- **Name**: `task-manager-backend-service`
- **Type**: ClusterIP
- **Port**: 8080
- **Target Port**: 8080

### JWT Keys Configuration

**⚠️ Importante**: Las claves JWT NO están hardcodeadas en los archivos YAML por seguridad.

Tienes varias opciones para configurar las claves JWT:

#### Opción 1: Kustomize con rutas relativas (Recomendado)
```bash
# Las claves se referencian desde ../dev/ en kustomization.yaml
kubectl apply -k .
```

#### Opción 2: Generar ConfigMap por separado
```bash
# Ejecutar el script para generar el ConfigMap
./generate-jwt-configmap.sh

# Aplicar el ConfigMap generado
kubectl apply -f jwt-configmap.yaml

# Luego aplicar el resto (sin kustomize)
kubectl apply -f 00.namespace.yaml
kubectl apply -f 01.mysql.yaml  
kubectl apply -f 02.backend.yaml
```

#### Opción 3: Crear ConfigMap manualmente
```bash
kubectl create configmap jwt-keys \
  --from-file=privateKey.pem=../dev/privateKey.pem \
  --from-file=publicKey.pem=../dev/publicKey.pem \
  -n test
```

#### Opción 4: Enlaces simbólicos locales
```bash
# Ejecutar script de configuración
./setup-jwt-keys.sh

# Modificar kustomization.yaml para usar archivos locales
# Luego aplicar
kubectl apply -k .
```

Las claves se montan en el contenedor en:
- **Private Key**: `/app/keys/privateKey.pem`
- **Public Key**: `/app/keys/publicKey.pem`

**Security Note**: En producción, considera usar Kubernetes Secrets en lugar de ConfigMap para las claves JWT.

## Monitoring and Troubleshooting

### Common Commands

```bash
# Check pod status
kubectl get pods -n test

# View pod logs for MySQL
kubectl logs -n test -l app=mysql

# View pod logs for Backend
kubectl logs -n test -l app=task-manager-backend

# Describe deployments for detailed information
kubectl describe deployment mysql -n test
kubectl describe deployment task-manager-backend -n test

# Check persistent volume status
kubectl get pv
kubectl describe pvc mysql-pvc -n test

# Check services
kubectl get services -n test

# Execute commands in the MySQL container
kubectl exec -it -n test deployment/mysql -- mysql -u root -p

# Test backend health endpoints (requires port-forward)
kubectl port-forward -n test service/task-manager-backend-service 8080:8080
# Then in another terminal: curl http://localhost:8080/q/health
```

### Health Checks

**MySQL Deployment:**
- **Liveness Probe**: Checks if the container is running (30s initial delay, 10s interval)
- **Readiness Probe**: Checks if the container is ready to serve traffic (5s initial delay, 5s interval)
- **Command**: `mysqladmin ping`

**Backend API Deployment:**
- **Liveness Probe**: Checks if the application is running (30s initial delay, 10s interval)
- **Readiness Probe**: Checks if the application is ready to serve traffic (10s initial delay, 5s interval)
- **Endpoints**: 
  - Liveness: `GET /q/health/live`
  - Readiness: `GET /q/health/ready`

## Scaling and Performance

### Vertical Scaling

To adjust resource allocation:

1. Edit the `resources` section in `01.mysql.yaml`
2. Apply the changes: `kubectl apply -f 01.mysql.yaml`
3. The deployment will perform a rolling update

### Storage Scaling

To increase storage:

1. Edit the storage request in the PVC
2. Note: Not all storage classes support volume expansion
3. Check your cloud provider's documentation for storage expansion capabilities

## Security Best Practices

### Implemented

- ✅ Resource limits and requests defined
- ✅ Health checks configured
- ✅ Non-root user for MySQL container
- ✅ Secrets used for sensitive data

### Recommended Improvements

- 🔄 Use external secret management
- 🔄 Enable pod security policies/standards
- 🔄 Implement network policies
- 🔄 Use strong, randomly generated passwords
- 🔄 Regular security updates and image scanning

## Migration and Backup

### Data Persistence

Data is persisted using Kubernetes PersistentVolume. Ensure your storage class supports:
- Backup and restore capabilities
- Cross-zone replication (for HA setups)

### Backup Strategy

Consider implementing:
- Regular database dumps using `mysqldump`
- Volume-level backups via cloud provider tools
- Point-in-time recovery capabilities

## Integration with Application

This MySQL instance is configured to work with the Task Manager application. The connection details are:

- **Host**: `mysql-service.test.svc.cluster.local`
- **Port**: `3306`
- **Database**: `tmdb`
- **Username/Password**: Stored in Kubernetes secrets

## Next Steps

### ✅ **Completed**
1. **Fix Configuration Issues**: ✅ **DONE** - All critical issues resolved
2. **Validation**: ✅ **DONE** - Configuration passes Kubernetes validation

### 🔄 **Recommended Next Actions**
1. **Security Hardening**: Implement stronger password policies and external secret management
2. **Deploy to Cluster**: The configuration is ready for deployment to your Kubernetes cluster
3. **Monitoring**: Add monitoring and alerting for the database
4. **Backup Strategy**: Implement automated backup solutions
5. **High Availability**: Consider multi-replica setup for production
6. **Application Integration**: ✅ **COMPLETED** - Deploy the Task Manager backend component

## Support and Troubleshooting

For issues with this configuration:

1. Check pod logs: `kubectl logs -n test -l app=mysql`
2. Verify resource availability: `kubectl describe nodes`
3. Check storage class compatibility
4. Ensure proper RBAC permissions
5. Validate network connectivity between components

---

**Note**: This configuration is currently set up for development/testing environments. For production use, additional security, monitoring, and high-availability considerations should be implemented.