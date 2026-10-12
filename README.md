# EventPass — API de venta y validación de tickets

API REST en **Spring Boot 3 / Java 21** para la venta de entradas a eventos:
catálogo de eventos, gestión de usuarios, órdenes de compra con descuento de
aforo y emisión/validación de tickets por código QR.

El diseño sigue el diagrama *EventPass - Arquitectura Cloud Native*: cada
paquete del código representa un microservicio del diagrama.

| Paquete del código          | Microservicio del diagrama | Responsabilidad                        |
|-----------------------------|----------------------------|----------------------------------------|
| `com.eventpass.auth`        | MS Autenticación           | Usuarios y roles (COMPRADOR/STAFF/ADMIN) |
| `com.eventpass.eventos`     | MS Eventos                 | Catálogo y control de aforo            |
| `com.eventpass.ordenes`     | MS Órdenes                 | Aceptación inmediata de la compra      |
| `com.eventpass.tickets`     | MS Tickets                 | Emisión, consulta y validación QR      |

---

## 1. Clonar el repositorio

```bash
git clone https://github.com/benhidalgov/eva2java.git eventpass
cd eventpass
git checkout main        # rama estable de la release
```

## 2. Requisitos e instalación

Necesitas **JDK 21** y **Maven 3.9+**.

**JDK 21 (Windows / PowerShell):**

```powershell
winget install --id EclipseAdoptium.Temurin.21.JDK --exact
```

**Maven 3.9 (Windows):** no está en winget, se instala descomprimiendo el binario:

```powershell
Invoke-WebRequest https://archive.apache.org/dist/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.zip -OutFile $env:TEMP\maven.zip
Expand-Archive $env:TEMP\maven.zip "$env:LOCALAPPDATA\Programs" -Force
$m = "$env:LOCALAPPDATA\Programs\apache-maven-3.9.11\bin"
[Environment]::SetEnvironmentVariable("Path", "$([Environment]::GetEnvironmentVariable('Path','User')); $m", "User")
```

**Verificar la instalación** (abre una terminal nueva):

```bash
java -version    # debe indicar 21.x
mvn -v           # debe indicar 3.9.x
```

**En IntelliJ IDEA:** *File → Open* y selecciona el `pom.xml` (no la carpeta);
IDEA importa el proyecto Maven y descarga las dependencias solas.

## 3. Ejecutar

**Opción A — Windows (recomendada):** doble clic en [`arrancar.bat`](arrancar.bat).
Ejecuta `mvn clean install` y arranca la API en `http://localhost:8081`.

**Opción B — manual:**

```bash
mvn clean install          # compila, corre pruebas y genera el .jar
java -jar target/eventpass-1.0.0.jar
```

La API queda en `http://localhost:8080`.

> Si el puerto 8080 está ocupado (por ejemplo por Steam), arranca con
> `java -jar target/eventpass-1.0.0.jar --server.port=8081`.

Comprueba que quedó arriba:

```bash
curl http://localhost:8080/api/eventos
```

## 4. Base de datos (H2)

Configuración en [`src/main/resources/application.properties`](src/main/resources/application.properties):

```properties
spring.datasource.url=jdbc:h2:file:./data/eventpass
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```

- Los datos persisten en `./data/eventpass.mv.db` entre corridas.
- Consola de inspección: `http://localhost:8080/h2-console`
  - *JDBC URL*: `jdbc:h2:file:./data/eventpass`, usuario `sa`, sin contraseña.
- `src/main/resources/data.sql` carga usuarios y eventos de ejemplo en cada
  arranque (sentencias idempotentes, no duplican registros).

## 5. Endpoints

| Método | Ruta                                  | Descripción                        | Éxito |
|--------|---------------------------------------|------------------------------------|-------|
| GET    | `/api/eventos`                        | Lista eventos (filtro `?nombre=`)  | 200 |
| GET    | `/api/eventos/{id}`                   | Detalle de evento                  | 200 |
| POST   | `/api/eventos`                        | Crea evento                        | 201 |
| PUT    | `/api/eventos/{id}`                   | Actualiza evento                   | 200 |
| DELETE | `/api/eventos/{id}`                   | Elimina evento                     | 204 |
| GET    | `/api/usuarios`                       | Lista usuarios                     | 200 |
| GET    | `/api/usuarios/{id}`                  | Detalle de usuario                 | 200 |
| POST   | `/api/usuarios`                       | Crea usuario                       | 201 |
| PUT    | `/api/usuarios/{id}`                  | Actualiza usuario                  | 200 |
| DELETE | `/api/usuarios/{id}`                  | Elimina usuario                    | 204 |
| GET    | `/api/ordenes`                        | Lista órdenes                      | 200 |
| GET    | `/api/ordenes/{id}`                   | Detalle de orden                   | 200 |
| POST   | `/api/ordenes`                        | Compra entradas y emite tickets    | 201 |
| DELETE | `/api/ordenes/{id}`                   | Anula orden y devuelve el aforo    | 204 |
| GET    | `/api/tickets`                        | Lista tickets                      | 200 |
| GET    | `/api/tickets/{id}`                   | Detalle de ticket                  | 200 |
| POST   | `/api/tickets/validar/{codigoQr}`     | Valida ticket en puerta            | 200 |

### Errores

Toda la API responde con el mismo cuerpo `ApiError`:

```json
{ "status": 404, "message": "Evento no encontrado con id 999",
  "path": "/api/eventos/999", "fieldErrors": {}, "timestamp": "..." }
```

| Código | Cuándo ocurre                                                        |
|--------|----------------------------------------------------------------------|
| 400    | Validación de `@Valid` (`fieldErrors` por campo), id no numérico      |
| 404    | Recurso inexistente (evento, usuario, orden o código de ticket)       |
| 409    | Aforo insuficiente, email duplicado, ticket ya validado               |
| 500    | Error no previsto                                                     |

El manejo centralizado está en
[`GlobalExceptionHandler`](src/main/java/com/eventpass/common/exception/GlobalExceptionHandler.java).

## 6. Estructura en capas

```
src/main/java/com/eventpass/
├── EventPassApplication.java
├── common/exception/     ApiError, GlobalExceptionHandler, excepciones
├── auth/                 controller/ service/ repository/ model/ dto/
├── eventos/              controller/ service/ repository/ model/ dto/
├── ordenes/              controller/ service/ repository/ model/ dto/
└── tickets/              controller/ service/ repository/ model/ dto/
```

Cada módulo aplica inyección de dependencias por constructor y separa
**controller → service → repository → model**.

### Relaciones JPA (OneToMany / ManyToOne)

```
Usuario 1 ──── N Orden          (auth.model.Usuario.ordenes)
Evento  1 ──── N Orden          (eventos.model.Evento.ordenes)
Orden   1 ──── N Ticket         (ordenes.model.Orden.tickets)
```

### Control de aforo

El descuento se hace con una única sentencia JPQL condicionada, atómica:

```java
@Modifying
@Query("update Evento e set e.aforoDisponible = e.aforoDisponible - :cantidad "
     + "where e.id = :id and e.aforoDisponible >= :cantidad")
int descontarAforo(Long id, int cantidad);
```

Si devuelve `0` filas, no queda aforo y la API responde `409`. Es el punto
crítico de concurrencia señalado en el diagrama.

## 7. Pruebas en Postman

Colección: [`postman/EventPass.postman_collection.json`](postman/EventPass.postman_collection.json)

Importar en Postman → correr la colección completa. Son **27 requests y 52
assertions** automáticas de código HTTP, mensajes y datos. La colección es
idempotente: se puede correr cuantas veces se quiera sin reiniciar la API:

- **Casos de éxito**: CRUD completo de eventos y usuarios, creación de orden
  (verifica que emite un ticket por entrada), validación de ticket.
- **Casos de error**: 400 (validación e id no numérico), 404 (recurso
  inexistente) y 409 (aforo insuficiente, email duplicado, ticket revalidado).

## 8. Compilar y empaquetar (Maven)

```bash
mvn clean        # limpia target/
mvn install      # compila, prueba e instala en el repositorio local
mvn package      # genera el .jar ejecutable
java -jar target/eventpass-1.0.0.jar
```

El `pom.xml` está documentado sección por sección (dependencias, propiedades
y plugins).

## 9. Flujo de Git

```
main ──●──────────────●  (release final)
        \            /
develop ─●────●──────●
          \  /      /
feature/*  ●  ●  ●
```

- `main`: historia estable, solo recibe merges de `develop`.
- `develop`: integración del trabajo en curso.
- `feature/eventos-crud`, `feature/ordenes-tickets`, `feature/script-arranque`:
  trabajo aislado que se integra con `--no-ff`.
- Commits con mensaje tipo `tipo(ámbito): descripción`.

### Cómo verificar el flujo

```bash
git log --oneline --graph --all   # historial con merges
git branch                        # main, develop y las feature/
```
