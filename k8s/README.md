# Kubernetes Configuration for Task Manager

This directory contains the Kubernetes manifests for deploying the Task Manager application infrastructure on a Kubernetes cluster.

## Overview

The Task Manager application uses a MySQL database as its data persistence layer. This configuration sets up the necessary Kubernetes resources to run MySQL in a containerized environment with proper persistence, security, and networking.

## Architecture

The deployment consists of the following components:

1. **Namespace**: Isolated environment for the application
2. **MySQL Database**: Containerized MySQL 8.0 instance
3. **Persistent Storage**: Persistent volume for database data
4. **Secrets Management**: Secure storage for database credentials

## Files Structure

```
k8s/
├── README.md                    # This documentation file
├── .env                         # Environment variables for secrets
├── 00.namespace.yaml           # Namespace definition
├── 01.mysql.yaml              # MySQL deployment, service, and PVC
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
├── 00.namespace.yaml           # Namespace definition
├── 01.mysql.yaml              # MySQL deployment, service, and PVC
└── kustomization.yaml          # Kustomize configuration (FIXED)
```

## Prerequisites

Before deploying this configuration, ensure you have:

- A running Kubernetes cluster
- `kubectl` configured to access your cluster
- `kustomize` installed (or use `kubectl apply -k`)
- Appropriate storage class available in your cluster

## Quick Start

### 1. Configuration Status ✅

**Good news!** The critical configuration issues have been resolved:
- ✅ Fixed file references in `kustomization.yaml`
- ✅ Removed references to non-existent files
- ✅ Configuration now passes validation (`kubectl apply --dry-run=client -k .`)

### 2. Deploy the Infrastructure

The configuration is now ready for deployment:

```bash
# Navigate to the k8s directory
cd k8s/

# Apply the configuration using kustomize
kubectl apply -k .

# Or apply individual files
kubectl apply -f 00.namespace.yaml
kubectl apply -f 01.mysql.yaml
```

### 3. Verify Deployment

```bash
# Check all resources in the test namespace
kubectl get all -n test

# Check persistent volume claims
kubectl get pvc -n test

# Check secrets
kubectl get secrets -n test

# Check pod logs
kubectl logs -n test deployment/mysql
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

## Monitoring and Troubleshooting

### Common Commands

```bash
# Check pod status
kubectl get pods -n test

# View pod logs
kubectl logs -n test -l app=mysql

# Describe deployment for detailed information
kubectl describe deployment mysql -n test

# Check persistent volume status
kubectl get pv
kubectl describe pvc mysql-pvc -n test

# Execute commands in the MySQL container
kubectl exec -it -n test deployment/mysql -- mysql -u root -p
```

### Health Checks

The MySQL deployment includes:

- **Liveness Probe**: Checks if the container is running (30s initial delay, 10s interval)
- **Readiness Probe**: Checks if the container is ready to serve traffic (5s initial delay, 5s interval)

Both probes use `mysqladmin ping` command.

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
6. **Application Integration**: Deploy the Task Manager backend and frontend components

## Support and Troubleshooting

For issues with this configuration:

1. Check pod logs: `kubectl logs -n test -l app=mysql`
2. Verify resource availability: `kubectl describe nodes`
3. Check storage class compatibility
4. Ensure proper RBAC permissions
5. Validate network connectivity between components

---

**Note**: This configuration is currently set up for development/testing environments. For production use, additional security, monitoring, and high-availability considerations should be implemented.