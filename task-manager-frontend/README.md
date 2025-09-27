# 📋 Task Manager Frontend

Una aplicación web moderna para gestión de tareas construida con **Angular 17**, **Angular Material** y **Tailwind CSS**. Esta aplicación frontend se conecta con una API REST backend desarrollada en Quarkus para proporcionar un sistema completo de gestión de tareas con autenticación JWT.

## 🚀 Características Principales

- ✅ **Autenticación segura** con JWT tokens y guards de ruta
- ✅ **Gestión completa de tareas** (CRUD operations)
- ✅ **Dashboard con estadísticas** (total, pendientes, completadas)
- ✅ **Interfaz moderna** con Angular Material y Tailwind CSS
- ✅ **Responsive design** adaptable a todos los dispositivos
- ✅ **Server-Side Rendering (SSR)** configurado
- ✅ **Sistema de notificacione** Build para Producción
```bash
ng build --configuration production
```

### Server-Side Rendering
```bash
npm run build:ssr
npm run serve:ssr:task-manager-frontend
```
- ✅ **Modales interactivos** para creación y edición
- ✅ **Componentes standalone** de Angular 17
- ✅ **Tema personalizable** con paleta de colores consistente

## 🛠️ Stack Tecnológico

### Framework y Core
- **Angular 17.3.0** - Framework principal con standalone components
- **TypeScript 5.4.2** - Lenguaje de programación tipado
- **RxJS 7.8.0** - Programación reactiva

### UI/UX y Estilos
- **Angular Material 17.3.10** - Componentes de interfaz (Dialogs, CDK)
- **Tailwind CSS 3.4.17** - Framework de utilidades CSS
- **Angular Animations** - Animaciones fluidas
- **Font Awesome** - Iconografía
- **Custom CSS Variables** - Sistema de tokens de diseño

### Autenticación y HTTP
- **@auth0/angular-jwt 5.2.0** - Manejo de JWT tokens
- **HttpClient** - Comunicación con API REST
- **Route Guards** - Protección de rutas

### SSR y Servidor
- **Angular SSR 17.3.11** - Server-Side Rendering
- **Express 4.18.2** - Servidor Node.js para SSR

### Herramientas de Desarrollo
- **Angular CLI 17.3.11** - Herramienta de línea de comandos
- **Karma + Jasmine** - Framework de testing
- **PostCSS + Autoprefixer** - Procesamiento CSS

## 📁 Estructura del Proyecto

```
src/
├── app/
│   ├── _model/                    # Modelos de datos TypeScript
│   │   ├── task.ts               # Modelo Task con User relation
│   │   ├── user.ts               # Modelo User
│   │   ├── dto.ts                # DTOs (API Response, Auth, Pageable, ConfirmDataDTO)
│   │   └── message.ts            # Modelo Message para notificaciones
│   ├── _service/                  # Servicios de la aplicación
│   │   ├── auth.service.ts       # Autenticación JWT (login/logout)
│   │   ├── task.service.ts       # CRUD operations de tareas + observables
│   │   ├── notification.service.ts # Sistema de notificaciones
│   │   ├── guard.service.ts      # AuthGuard para rutas protegidas
│   │   └── env.service.ts        # Configuración de entorno
│   ├── pages/                     # Páginas principales de la aplicación
│   │   ├── login/                # Página de autenticación
│   │   │   ├── login.component.ts # Lógica de login con validaciones
│   │   │   ├── login.component.html # Formulario reactivo de login
│   │   │   └── login.component.css
│   │   ├── layout/               # Layout principal con navegación
│   │   │   ├── layout.component.ts # Container con navbar y router-outlet
│   │   │   ├── layout.component.html # Header con botón "Nueva Tarea"
│   │   │   └── layout.component.css
│   │   ├── task/                 # Dashboard principal de tareas
│   │   │   ├── task.component.ts # Gestión completa de tareas con estadísticas y modales
│   │   │   ├── task.component.html # Dashboard con cards, estadísticas y lista de tareas
│   │   │   └── task.component.css
│   │   └── pages.routes.ts       # Rutas de páginas internas
│   ├── modals/                    # Componentes modales
│   │   ├── confirm-dialog/       # Modal de confirmación genérico configurable
│   │   │   ├── confirm-dialog.component.ts # Lógica del modal con ConfirmDataDTO
│   │   │   ├── confirm-dialog.component.html
│   │   │   └── confirm-dialog.component.css
│   │   └── task-edition-dialog/  # Modal CRUD de tareas con validaciones
│   │       ├── task-edition-dialog.component.ts # Formulario reactivo con manejo de errores
│   │       ├── task-edition-dialog.component.html
│   │       └── task-edition-dialog.component.css
│   ├── shared/                    # Componentes compartidos
│   │   ├── loader/               # Componente de loading spinner
│   │   │   ├── loader.component.ts
│   │   │   ├── loader.component.html # Spinner con overlay
│   │   │   └── loader.component.css
│   │   └── notification/         # Sistema de notificaciones toast
│   │       ├── notification.component.ts # Suscripción a mensajes
│   │       ├── notification.component.html
│   │       └── notification.component.css
│   ├── util/                      # Utilidades y helpers
│   │   ├── form.ts               # FormMethods para validaciones
│   │   └── util.ts               # UtilMethods para JWT y storage
│   ├── app.component.ts          # Componente raíz
│   ├── app.config.ts             # Configuración de la aplicación
│   ├── app.config.server.ts      # Configuración SSR
│   └── app.routes.ts             # Enrutamiento principal
├── assets/
│   └── env.js                    # Variables de entorno dinámicas
├── custom-theme.scss             # Tema personalizado Angular Material
├── styles.css                    # Estilos globales + Tailwind + CSS Variables
├── main.ts                       # Bootstrap de la aplicación
└── index.html                    # HTML base
```

## 🔧 Configuración del Entorno

### Prerrequisitos

- **Node.js** >= 16.x
- **npm** >= 8.x
- **Angular CLI** 17.x instalado globalmente

### Variables de Entorno

El archivo `src/assets/env.js` contiene la configuración dinámica del entorno:

```javascript
(function (window) {
  window.__env = window.__env || {};
  
  window.__env.production = false;
  window.__env.apiUrl = 'http://localhost:8080/rest/api/v1';
  window.__env.token_name = 'access_token';
  window.__env.domains = ['localhost:8080'];
})(this);
```

### Configuración del Backend
Asegúrate de que el backend (Quarkus) esté ejecutándose en:
- **Desarrollo**: `http://localhost:8080`
- **API Base**: `/rest/api/v1`

## 🚀 Instalación y Configuración

### 1. Clonar el repositorio
```bash
git clone [URL_DEL_REPOSITORIO]
cd task-manager-frontend
```

### 2. Instalar dependencias
```bash
npm install
```

### 3. Configurar variables de entorno
Edita `src/assets/env.js` con la URL de tu backend:
```javascript
window.__env.apiUrl = 'http://tu-backend-url:puerto/rest/api/v1';
window.__env.domains = ['tu-backend-url:puerto'];
```

### 4. Ejecutar en modo desarrollo
```bash
npm start
# o
ng serve
```
La aplicación estará disponible en `http://localhost:4200/`

### 5. Verificar conexión con backend
- ✅ Backend ejecutándose en puerto 8080
- ✅ CORS configurado para `localhost:4200`
- ✅ Endpoints disponibles: `/auth/login`, `/tasks`

## 🏗️ Scripts Disponibles

### Desarrollo
```bash
npm start                         # ng serve (puerto 4200)
ng serve                          # Servidor de desarrollo
ng build --watch                  # Build en modo watch
```

### Producción
```bash
npm run build                     # ng build --configuration production
ng build --configuration production # Build optimizado para producción
```

### Testing
```bash
npm test                          # ng test (Karma + Jasmine)
ng test                           # Ejecuta tests unitarios
ng test --code-coverage           # Tests con reporte de cobertura
```

### Server-Side Rendering
```bash
npm run serve:ssr:task-manager-frontend  # Ejecuta aplicación SSR
```

## 🔐 Sistema de Autenticación

### Flujo Implementado

1. **Login Form**: Formulario reactivo con validaciones
2. **JWT Storage**: Tokens almacenados en `sessionStorage`
3. **AuthGuard**: Protección automática de rutas `/task`
4. **Auto-logout**: Redirección automática en caso de token expirado

### Componentes de Autenticación

```typescript
// LoginComponent - Formulario reactivo
loginForm = new FormGroup({
  email: new FormControl('', [Validators.required, Validators.email]),
  password: new FormControl('', [Validators.required, Validators.minLength(6)])
});

// AuthService - Gestión de JWT
login(email: string, password: string) {
  return this.http.post<APIResponseDTO<AuthenticationResponseDTO>>(`${this.url}/login`, user);
}

// AuthGuard - Protección de rutas  
canActivate(): boolean {
  return this.authService.isLogged();
}
```

### Manejo de Errores
- ✅ Validación de formularios en tiempo real
- ✅ Mensajes de error específicos de la API
- ✅ Manejo de tokens expirados
- ✅ Redirección automática a login

## 🎭 Sistema de Modales

### Modal de Confirmación Genérico

El sistema incluye un componente de confirmación reutilizable configurado mediante `ConfirmDataDTO`:

```typescript
// ConfirmDataDTO - Configuración del modal
interface ConfirmDataDTO {
  title: string;        // Título del modal
  message: string;      // Mensaje de confirmación
  confirmText: string;  // Texto del botón de confirmación
  cancelText: string;   // Texto del botón de cancelación
}

// Ejemplo de uso en componente
openCompleteDialog(task: Task) {
  const confirmData: ConfirmDataDTO = {
    title: 'Confirmar Completado',
    message: '¿Estás seguro de que deseas marcar esta tarea como completada?',
    confirmText: 'Completar',
    cancelText: 'Cancelar'
  };
  
  const dialogRef = this.dialog.open(ConfirmDialogComponent, {
    data: confirmData
  });

  dialogRef.afterClosed().subscribe(result => {
    if (result) {
      this.markAsCompleted(task);
    }
  });
}
```

### Modal de Edición de Tareas

Modal inteligente que detecta si es creación o edición:

```typescript
// TaskEditionDialogComponent - Auto-configuración
constructor(@Inject(MAT_DIALOG_DATA) public data: Task | null) {
  this.form = new FormGroup({
    title: new FormControl(data?.title || '', Validators.required),
    description: new FormControl(data?.description || '', Validators.required)
  });

  // Configuración automática del título
  this.dialogTitle = data && data.id ? 'Editar Tarea' : 'Nueva Tarea';
}
```

## 📊 Gestión de Tareas

### Funcionalidades del Dashboard

#### Estadísticas en Tiempo Real
- **Total de tareas**: Contador automático
- **Tareas pendientes**: Filtro por `completed: false`
- **Tareas completadas**: Filtro por `completed: true`

#### Operaciones CRUD
- ✅ **Crear**: Modal con formulario reactivo y validaciones en tiempo real
- ✅ **Listar**: Vista de cards con información completa y estadísticas actualizadas
- ✅ **Editar**: Modal pre-poblado con formulario reactivo (solo tareas pendientes)
- ✅ **Completar**: Acción directa con modal de confirmación personalizable
- ✅ **Eliminar**: Modal de confirmación genérico con mensajes customizables

### Modelo de Datos Actual

```typescript
export class Task {
  id: number;
  title: string;
  description: string;
  completed: boolean;
  createdAt: string;
  updatedAt: string;
  user: User;
}

export class User {
  name: string;
  email: string;
  password: string;
  active: boolean;
}
```

### API Endpoints Integrados

```typescript
// TaskService endpoints implementados
GET    /tasks              # Obtener todas las tareas del usuario
GET    /tasks/{id}         # Obtener tarea por ID
POST   /tasks              # Crear nueva tarea
PUT    /tasks              # Actualizar tarea existente
PUT    /tasks/{id}/complete # Marcar tarea como completada
DELETE /tasks/{id}         # Eliminar tarea

// Ejemplo de implementación del método markAsCompleted
markAsCompleted(id: number) {
  return this.http.put<APIResponseDTO<void>>(`${this.url}/${id}/complete`, {});
}
```

### Gestión de Estado
- **Observables**: Comunicación reactiva entre componentes con Subject pattern
- **Subject Pattern**: Actualización automática de listas y estadísticas
- **Error Handling**: Manejo robusto con operadores RxJS (catchError, switchMap, finalize)
- **Reactive Forms**: Validación en tiempo real con FormControl y FormGroup
- **Modal System**: Sistema unificado de modales con DTOs para configuración

## 🛡️ Manejo Avanzado de Errores

### Patrón de Error Handling con RxJS

El sistema implementa un manejo robusto de errores usando operadores RxJS:

```typescript
// Ejemplo: Marcar tarea como completada con manejo de errores
private markAsCompleted(task: Task) {
  this.isLoading = true;
  
  this.taskService.markAsCompleted(task.id)
    .pipe(
      catchError(error => {
        this.notificationService.setMessageChange(
          Message.error('Ocurrió un error al procesar la solicitud.', error)
        );
        return EMPTY; // Evita continuar la cadena en caso de error
      }),
      switchMap(() => this.taskService.getAll()), // Actualizar lista tras éxito
      catchError(error => {
        this.notificationService.setMessageChange(
          Message.error('Ocurrió un error al listar las tareas.', error)
        );
        return EMPTY;
      }),
      finalize(() => {
        this.isLoading = false; // Siempre se ejecuta
      })
    )
    .subscribe((response) => {
      if (response.success) {
        this.taskService.setObjectChange(response.data);
        this.notificationService.setMessageChange(
          Message.success('Tarea marcada como completada.')
        );
      } else {
        this.notificationService.setMessageChange(
          Message.error('Error al marcar la tarea como completada.', response.message)
        );
      }
    });
}
```

### Características del Error Handling
- ✅ **Múltiples niveles**: Error en operación individual y en actualización de lista
- ✅ **Mensajes contextuales**: Errores específicos para cada operación
- ✅ **Continuidad de flujo**: `switchMap` para encadenar operaciones
- ✅ **Limpieza garantizada**: `finalize` para reset de estado de loading
- ✅ **Fallback graceful**: `EMPTY` para evitar propagación de errores
- ✅ **Estado de autenticación**: Manejo específico de errores 401

## 🎨 Sistema de Estilos

### Arquitectura de Estilos

#### 1. Tailwind CSS
- **Configuración**: `tailwind.config.js` con colores personalizados
- **Utilidades**: Responsive design, espaciado, tipografía
- **Purging**: Optimización automática en producción

```javascript
// tailwind.config.js
theme: {
  extend: {
    colors: {
      primary: '#3B82F6',    // Azul principal
      secondary: '#1E40AF',  // Azul secundario
      success: '#10B981',    // Verde éxito
      warning: '#F59E0B',    // Amarillo advertencia
      danger: '#EF4444',     // Rojo peligro
    }
  }
}
```

#### 2. Angular Material Theme
- **Paleta**: Indigo como color principal
- **Componentes**: Dialogs, CDK para overlays
- **Theming**: Configurado en `custom-theme.scss`

#### 3. CSS Variables Globales
- **Design tokens**: Más de 50 variables CSS personalizadas
- **Colores**: Paleta completa con todas las variaciones
- **Espaciado**: Sistema consistente de spacing
- **Animaciones**: Keyframes para transiciones suaves

### Componentes Visuales

#### Layout Principal
- **Header**: Navegación con botón "Nueva Tarea"
- **Dashboard**: Grid responsivo con estadísticas
- **Task Cards**: Diseño limpio con acciones contextuales

#### Sistema de Notificaciones
- **Toast notifications**: Posicionadas top-right
- **Tipos**: Success, Error, Warning con colores específicos
- **Animaciones**: Slide-in desde la derecha

## 🔧 Configuración de Build

### Configuraciones Angular

#### Development
```json
{
  "optimization": false,
  "extractLicenses": false,
  "sourceMap": true
}
```

#### Production
```json
{
  "outputHashing": "all",
  "budgets": [
    {
      "type": "initial",
      "maximumWarning": "500kb",
      "maximumError": "1mb"
    },
    {
      "type": "anyComponentStyle",
      "maximumWarning": "2kb",
      "maximumError": "4kb"
    }
  ]
}
```

### Assets y Scripts
```json
"assets": [
  "src/favicon.ico",
  "src/assets"
],
"styles": [
  "src/styles.css",
  "src/custom-theme.scss"
],
"scripts": [
  "src/assets/env.js"
]
```

## 🧪 Testing

### Configuración Actual
- **Framework**: Jasmine + Karma
- **Browser**: Chrome Headless
- **Coverage**: Incluido en configuración
- **Archivos**: `src/**/*.spec.ts`

### Ejecutar Tests
```bash
# Tests en modo watch
ng test

# Tests con cobertura
ng test --code-coverage

# Tests una sola vez
ng test --watch=false
```

## 🚀 Deployment y SSR

### Build para Producción
```bash
# Build estándar
ng build --configuration production

# Build con SSR
npm run build:ssr
```

### Server-Side Rendering
El proyecto incluye configuración completa para SSR:
- **server.ts**: Express server configurado
- **app.config.server.ts**: Configuración específica de servidor
- **Hydration**: Cliente-servidor sincronizado

```bash
# Ejecutar SSR
npm run serve:ssr:task-manager-frontend
```

## 🛡️ Seguridad

### Medidas Implementadas

#### Autenticación
- **JWT Tokens** con expiración automática
- **AuthGuard** para protección de rutas
- **Token Storage** en sessionStorage (se limpia al cerrar pestaña)

#### Validaciones
- **Frontend**: Validaciones reactivas con Angular Forms
- **Backend**: Validaciones adicionales en API
- **Sanitización**: Automática de Angular para XSS

#### Headers y CORS
```typescript
// Dominios permitidos para JWT
allowedDomains: envService.getDomains,
disallowedRoutes: [
  `${envService.getApiUrl}/auth/login`,
  `${envService.getApiUrl}/auth/register`
]
```

## 📚 Guías de Desarrollo

### Agregar Nuevo Componente
```bash
# Standalone component
ng generate component pages/nueva-pagina --standalone

# Con módulo (legacy)
ng generate component pages/nueva-pagina
```

### Agregar Nuevo Servicio
```bash
ng generate service _service/nuevo-servicio
```

### Agregar Nueva Ruta
1. Crear el componente
2. Agregar en `app.routes.ts` o `pages.routes.ts`:
```typescript
{ path: 'nueva-ruta', component: NuevoComponent, canActivate: [AuthGuard] }
```

### Patrones de Código

#### Standalone Components (Angular 17)
```typescript
@Component({
  selector: 'app-ejemplo',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './ejemplo.component.html'
})
export class EjemploComponent { }
```

#### Manejo de Observables
```typescript
// Patrón de finalización
this.taskService.getAll()
  .pipe(
    finalize(() => this.isLoading = false)
  )
  .subscribe({
    next: (response) => { /* éxito */ },
    error: (error) => { /* error */ }
  });
```

#### Formularios Reactivos
```typescript
// Patrón de validación
loginForm = new FormGroup({
  email: new FormControl('', [Validators.required, Validators.email]),
  password: new FormControl('', [Validators.required, Validators.minLength(6)])
});
```

## 🤝 Integración con Backend

### API Endpoints Consumidos

#### Autenticación
```
POST /auth/login
- Body: { email: string, password: string }
- Response: { success: boolean, data: { access_token: string, refresh_token: string } }

GET /auth/logout  
- Headers: Authorization: Bearer {token}
- Response: { success: boolean }
```

#### Tareas
```
GET /tasks
- Headers: Authorization: Bearer {token}
- Response: { success: boolean, data: Task[] }

POST /tasks
- Headers: Authorization: Bearer {token}
- Body: { title: string, description: string }

PUT /tasks
- Headers: Authorization: Bearer {token}
- Body: { id: number, title: string, description: string, completed: boolean }

PUT /tasks/{id}/complete
- Headers: Authorization: Bearer {token}
- Response: { success: boolean, message: string }

DELETE /tasks/{id}
- Headers: Authorization: Bearer {token}
```

### Estructura de Respuesta API
```typescript
interface APIResponseDTO<T> {
  success: boolean;
  message: string;
  data: T;
  statusCode: number;
  timestamp: string;
}
```

## 🔍 Troubleshooting

### Problemas Comunes

#### Error CORS
```bash
# Verificar que el backend permita origen localhost:4200
# En Quarkus: quarkus.http.cors.origins=http://localhost:4200
```

#### Token Expirado
```bash
# El sistema redirige automáticamente al login
# Verificar en Network tab si el endpoint retorna 401
```

#### Build Errors
```bash
# Limpiar caché de Angular
ng cache clean

# Limpiar node_modules
rm -rf node_modules package-lock.json
npm install
```

#### Estilos no aplicados
```bash
# Verificar que Tailwind esté compilando
npm run build -- --watch

# Verificar importación en styles.css
@tailwind base;
@tailwind components; 
@tailwind utilities;
```

### Debug Mode
```typescript
// Habilitar logs en desarrollo
console.log('Task loaded:', tasks); // Solo en desarrollo
```

## 📄 Licencia

Este proyecto está bajo la licencia especificada en el archivo LICENSE del repositorio principal.

## 👥 Contribución

### Workflow de Desarrollo
1. Fork del proyecto
2. Crear rama para feature (`git checkout -b feature/nueva-funcionalidad`)
3. Seguir convenciones de código Angular
4. Commit con mensajes descriptivos
5. Push y crear Pull Request

### Convenciones de Commit
```
feat: nueva funcionalidad
fix: corrección de bug
docs: actualización de documentación  
style: cambios de formato (no afectan lógica)
refactor: refactorización de código
test: agregar o modificar tests
build: cambios en build/deploy
```

### Code Style
- **TypeScript**: Strict mode habilitado
- **Linting**: ESLint configurado
- **Formatting**: Prettier recomendado
- **Naming**: camelCase para variables, PascalCase para componentes

---

**Desarrollado con ❤️ usando Angular 17 + Tailwind CSS + Angular Material**

*Última actualización: Septiembre 2025*

## 🔐 Autenticación

La aplicación utiliza JWT tokens para autenticación:

1. **Login**: Los usuarios se autentican con email/password
2. **Token Storage**: Los tokens se almacenan en `sessionStorage`
3. **Route Guards**: Las rutas protegidas requieren autenticación
4. **Auto-refresh**: Los tokens se envían automáticamente en las peticiones

### Flujo de Autenticación
```typescript
// Login
authService.login(email, password)
  .subscribe(response => {
    sessionStorage.setItem('access_token', response.data.token);
    router.navigate(['/task']);
  });

// Logout
authService.logout(); // Limpia el token y redirige
```

## 📊 Gestión de Tareas

### Funcionalidades
- ✅ Crear nuevas tareas
- ✅ Listar todas las tareas del usuario
- ✅ Editar tareas existentes
- ✅ Marcar tareas como completadas/pendientes
- ✅ Eliminar tareas
- ✅ Filtrado y búsqueda

### Modelo de Datos
```typescript
export class Task {
  id: number;
  title: string;
  description: string;
  completed: boolean;
  createdAt: string;
  updatedAt: string;
  user: User;
}
```

## 🎨 Personalización de Temas

### Tailwind CSS
La aplicación utiliza una paleta de colores personalizada definida en `tailwind.config.js`:

```javascript
theme: {
  extend: {
    colors: {
      primary: '#3B82F6',
      secondary: '#1E40AF',
      success: '#10B981',
      warning: '#F59E0B',
      danger: '#EF4444',
    }
  }
}
```

### Angular Material
El tema se define en `src/custom-theme.scss` usando la paleta Indigo como color principal.

## 🔧 Configuración de Build

### Configuraciones Disponibles
- **development**: Sin optimizaciones, con source maps
- **production**: Optimizado, minificado, con hashing

### Presupuestos de Bundle
```json
{
  "budgets": [
    {
      "type": "initial",
      "maximumWarning": "500kb",
      "maximumError": "1mb"
    },
    {
      "type": "anyComponentStyle", 
      "maximumWarning": "2kb",
      "maximumError": "4kb"
    }
  ]
}
```

## 🧪 Testing

### Configuración
- **Framework**: Jasmine + Karma
- **Configuración**: `tsconfig.spec.json`
- **Coverage**: Incluido en la configuración de Karma

### Ejecutar Tests
```bash
ng test                    # Modo interactivo
ng test --watch=false     # Ejecución única
ng test --code-coverage   # Con reporte de cobertura
```

## 🚀 Deploy

### Build para Producción
```bash
ng build --configuration production
```

### Server-Side Rendering
```bash
npm run build:ssr
npm run serve:ssr:task-manager-frontend
```

## 🛡️ Seguridad

### Medidas Implementadas
- **JWT Authentication** con expiración automática
- **Route Guards** para proteger rutas sensibles
- **HTTPS** recomendado en producción
- **Sanitización** automática de Angular para XSS
- **CSP Headers** recomendados para el servidor

### Dominios Permitidos
Los dominios permitidos para JWT se configuran en `src/assets/env.js`:

```javascript
window.__env.domains = ['tu-dominio.com', 'api.tu-dominio.com'];
```

## 📚 Guías de Desarrollo

### Agregar Nuevo Componente
```bash
ng generate component pages/nueva-pagina
```

### Agregar Nuevo Servicio
```bash
ng generate service _service/nuevo-servicio
```

### Agregar Nueva Ruta
1. Crear el componente
2. Agregar la ruta en `app.routes.ts` o `pages.routes.ts`
3. Configurar guard si es necesario

### Estructura de Commits
```
feat: nueva funcionalidad
fix: corrección de bug
docs: actualización de documentación
style: cambios de formato
refactor: refactorización de código
test: agregado de tests
```

## 🤝 Integración con Backend

### API Endpoints Utilizados
```typescript
// Autenticación
POST /auth/login      # Inicio de sesión
GET  /auth/logout     # Cierre de sesión

// Tareas
GET    /tasks         # Listar tareas
POST   /tasks         # Crear tarea
PUT    /tasks/:id     # Actualizar tarea
DELETE /tasks/:id     # Eliminar tarea
```

### Estructura de Respuesta API
```typescript
interface APIResponseDTO<T> {
  success: boolean;
  message: string;
  data: T;
}
```

## 📝 Notas de Desarrollo

- La aplicación utiliza **standalone components** de Angular
- **SSR** está configurado pero es opcional
- **Lazy loading** está preparado para futuras expansiones
- **PWA** capabilities pueden ser agregadas fácilmente
- **Internationalization (i18n)** está preparado para implementar

## 🔍 Troubleshooting

### Problemas Comunes

**Error de CORS**
```bash
# Verificar configuración del backend
# Asegurar que el dominio frontend esté en allowedOrigins
```

**Token expirado**
```bash
# El token se limpia automáticamente
# El usuario será redirigido al login
```

**Build errors**
```bash
# Limpiar caché y reinstalar
rm -rf node_modules package-lock.json
npm install
```

## 📄 Licencia

Este proyecto está bajo la licencia especificada en el archivo LICENSE del repositorio principal.

## 👥 Contribución

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'feat: nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear un Pull Request

---

**Desarrollado con ❤️ usando Angular 17 + Tailwind CSS + Angular Material**
