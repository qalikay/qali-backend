# QaliKay Backend

Backend de la plataforma **QaliKay** (medicina natural andina), desarrollado en Spring Boot siguiendo el estilo aprendido en clase (`security_202520` / `backend_proveedores`).

## Stack

- **Spring Boot 3.3.2** + Java 21
- **Spring Data JPA** + **H2** (en memoria, modo desarrollo)
- **Spring Security** + **JWT** (jjwt 0.11.5)
- **ModelMapper** 3.0.0 (entidad ↔ DTO)
- **Springdoc OpenAPI / Swagger UI** 2.0.3
- **Lombok**

## Arquitectura (estilo clase)

```
com.qalikay.backend
├── QaliBackendApplication.java
├── config/
│   ├── ModelMapperConfig.java
│   ├── OpenApiConfig.java
│   └── DataInitializer.java
├── entities/                  ← Categoria, Especialidad, Cliente, Experto,
│                                Receta, Insumo, Consulta, Mensaje,
│                                Orden, DetalleOrden, Resena
├── repositories/              ← XxxRepositorio.java (interfaces JpaRepository)
├── services/                  ← XxxService.java (interfaces)
├── serviceimpl/               ← XxxServiceImpl.java (implementaciones)
├── dtos/                      ← POJOs con Lombok
├── controllers/               ← REST controllers por entidad
└── security/
    ├── config/SecurityConfig.java
    ├── controllers/AuthController.java
    ├── dtos/                  ← AuthRequestDTO, AuthResponseDTO, RegistroXxxDTO
    ├── entities/              ← User, Role
    ├── filters/JwtRequestFilter.java
    ├── repositories/          ← UserRepository, RoleRepository
    ├── services/              ← UserService, CustomUserDetailsService
    └── util/JwtUtil.java
```

## Cómo correr

```bash
./mvnw spring-boot:run
```

- API:        `http://localhost:8080/api/...`
- Swagger:    `http://localhost:8080/swagger-ui/index.html`
- H2 Console: `http://localhost:8080/h2-console`  (jdbc url: `jdbc:h2:mem:qalikay_db`, user `sa`, sin contraseña)

## Usuarios de prueba (cargados por `DataInitializer`)

| Username | Password    | Roles          |
| -------- | ----------- | -------------- |
| admin    | admin123    | ROLE_ADMIN     |
| cliente  | cliente123  | ROLE_CLIENTE   |
| experto  | experto123  | ROLE_EXPERTO   |

## Endpoints principales

### Autenticación (públicos)

| Método | URL                       | Body                                        |
| ------ | ------------------------- | ------------------------------------------- |
| POST   | `/api/authenticate`       | `{ username, password }` → `{ jwt, roles }` |
| POST   | `/api/registro/cliente`   | `{ username, password, nombres, apellidos, telefono }` |
| POST   | `/api/registro/experto`   | `{ username, password, nombres, apellidos, telefono, especialidadId, trayectoria, anosExperiencia }` |

Una vez logueado, enviar el JWT en el header: `Authorization: Bearer <token>`.

### Catálogos (lectura pública)

- `GET /api/categorias`
- `GET /api/categorias/{id}`
- `GET /api/especialidades`
- `GET /api/especialidades/{id}`
- (POST/PUT/DELETE `/api/categoria` y `/api/especialidad` requieren ROLE_ADMIN)

### Recetas

| Método | URL                                       | Acceso       |
| ------ | ----------------------------------------- | ------------ |
| GET    | `/api/recetas?categoriaId=&q=`            | Público (solo PUBLICADA) |
| GET    | `/api/recetas/{id}`                       | Público (solo PUBLICADA) |
| GET    | `/api/experto/recetas`                    | ROLE_EXPERTO (sus recetas, todos los estados) |
| GET    | `/api/experto/recetas/{id}`               | ROLE_EXPERTO |
| POST   | `/api/experto/recetas`                    | ROLE_EXPERTO |
| PUT    | `/api/experto/recetas/{id}`               | ROLE_EXPERTO |
| POST   | `/api/experto/recetas/{id}/publicar`      | ROLE_EXPERTO |
| POST   | `/api/experto/recetas/{id}/archivar`      | ROLE_EXPERTO |
| DELETE | `/api/experto/recetas/{id}`               | ROLE_EXPERTO |

### Insumos / Productos

| Método | URL                                | Acceso       |
| ------ | ---------------------------------- | ------------ |
| GET    | `/api/insumos?categoriaId=&tipo=&q=` | Público    |
| GET    | `/api/insumos/{id}`                | Público      |
| GET    | `/api/experto/insumos`             | ROLE_EXPERTO |
| POST   | `/api/experto/insumos`             | ROLE_EXPERTO |
| PUT    | `/api/experto/insumos/{id}`        | ROLE_EXPERTO |
| DELETE | `/api/experto/insumos/{id}`        | ROLE_EXPERTO |

### Cliente / Experto

- `GET  /api/cliente/me`        ROLE_CLIENTE
- `PUT  /api/cliente/me`        ROLE_CLIENTE
- `GET  /api/experto/me`        ROLE_EXPERTO
- `PUT  /api/experto/me`        ROLE_EXPERTO
- `GET  /api/expertos?especialidadId=`  Público

### Consultas

- `GET  /api/cliente/consultas`         ROLE_CLIENTE
- `GET  /api/experto/consultas`         ROLE_EXPERTO
- `GET  /api/consultas/{id}`            Autenticado
- `POST /api/consultas`                 ROLE_CLIENTE — `{ asunto, expertoId, mensajeInicial }`
- `POST /api/consultas/{id}/mensajes`   Autenticado — `{ contenido }`
- `POST /api/consultas/{id}/cerrar`     Autenticado

### Órdenes (compras)

- `POST /api/ordenes`              ROLE_CLIENTE — `{ metodoPago, detalles: [{ tipoItem, itemId, cantidad }] }`
- `GET  /api/cliente/ordenes`      ROLE_CLIENTE
- `GET  /api/ordenes/{id}`         Autenticado
- `POST /api/ordenes/{id}/estado`  ROLE_ADMIN  — `{ estado }`

### Reseñas

- `GET  /api/resenas?tipoItem=RECETA&itemId=1`   Público
- `GET  /api/cliente/resenas`                    ROLE_CLIENTE
- `POST /api/resenas`                            ROLE_CLIENTE — `{ calificacion, comentario, tipoItem, itemId }`
- `DELETE /api/resenas/{id}`                     ROLE_CLIENTE

## Notas para el frontend

El backend cambió de `/api/v1/...` con DTOs en inglés a `/api/...` con DTOs en español, alineado al estilo de clase. El frontend Angular debe actualizar:

1. `apiUrl` en `environment.development.ts` → `http://localhost:8080/api` (sin `/v1`).
2. Login: `POST /api/authenticate` con `{ username, password }` (antes `email`).
3. Registro: `POST /api/registro/cliente` o `POST /api/registro/experto` (cambiaron las rutas y campos).
4. Headers: el token se envía como `Authorization: Bearer <jwt>`.
5. Modelos: cambiar nombres de campos (e.g. `firstName` → `nombres`, `categoryId` → `categoriaId`, `status` → `estado`, `title` → `titulo`, etc.).
