# Guion de video — EventPass (3:30 min, 2 personas)

**Reparto**
- **A (Desarrollo):** terminal, IntelliJ, base de datos.
- **B (Demo):** Postman, navegador, endpoints y errores.

**Duración total:** 3 min 30 s. Sin cortes largos: si algo tarda, se edita.

---

## Timeline

| Tiempo | En pantalla | Quién | Qué dice |
|--------|-------------|-------|----------|
| 0:00 | Cara de ambos o el repo en GitHub | **A** | "Hola, somos [A] y [B]. Les mostramos **EventPass**, una API de venta de tickets en Spring Boot 3 y Java 21." |
| 0:10 | Terminal: `git branch` y `git log --oneline --graph --all` | **B** | "Usamos flujo Git: `main`, `develop` y ramas `feature/`, con merges y commits descriptivos." |
| 0:25 | Terminal: `git clone …` → `mvn clean install` | **A** | "Clonamos el repo y Maven descarga las dependencias. Ahí está **BUILD SUCCESS** y el `.jar` generado en `target/`." |
| 0:50 | Terminal: `java -jar target\eventpass-1.0.0.jar --server.port=8081` | **A** | "Arrancamos la API." |
| 1:05 | Navegador: `/` y luego `/api/eventos` | **B** | "La raíz lista los endpoints y `/api/eventos` devuelve el catálogo con aforo y precio." |
| 1:25 | IntelliJ: árbol `eventos → controller/service/repository/model/dto`, luego `EventoService` | **A** | "Arquitectura en capas por módulo y dependencias inyectadas por constructor: el service recibe su repository." |
| 1:50 | `pom.xml` (dependencia h2) y `application.properties` (datasource) | **B** | "La conexión a la BD está en el properties y el driver H2 en el pom." |
| 2:10 | Postman → **Run collection** → resumen | **B** | "Colección de pruebas: **27 requests, 52 assertions, cero fallos**." |
| 2:35 | Postman: crear evento (201), inexistente (404), aforo insuficiente (409) | **B** | "Validamos éxito y error: 201 al crear, 404 si no existe, 409 si no queda aforo, 400 si el JSON es inválido." |
| 2:55 | Postman: comprar 3 entradas → 201 con tickets | **A** | "Compramos entradas: devuelve un ticket con código QR por entrada." |
| 3:10 | H2 console: `SELECT id, aforo_total, aforo_disponible FROM eventos WHERE id = 1;` | **A** | "El aforo bajó de 500 a 497." |
| 3:20 | Ctrl+C, vuelve a arrancar, mismo SELECT | **A** | "Reiniciamos y el dato sigue igual: **persistencia real**." |
| 3:28 | Código: `@ManyToOne` en `Orden`, `@OneToMany` en `Usuario` | **B** | "Relaciones JPA: un usuario muchas órdenes, una orden muchos tickets." |
| 3:32 | GitHub con las ramas + README | **A y B** | **A:** "Todo en `github.com/benhidalgov/eva2java`." **B:** "Con el README cualquiera lo clona, instala y ejecuta." |

---

## Antes de grabar (checklist)

- [ ] `data/` borrado (BD limpia: 3 eventos y 3 usuarios de ejemplo).
- [ ] API arrancada con `arrancar.bat` (puerto **8081**).
- [ ] Postman: variable `baseUrl` = `http://localhost:8081` y **Save**.
- [ ] Colección importada; correrla una vez de prueba → 0 fallos.
- [ ] IntelliJ con `EventoService.java` y `Orden.java` abiertos en pestañas.
- [ ] Fuente de terminal e IDE a 16 px o más.
- [ ] Steam cerrado (ocupa el 8080).

## Grabación

- Win + G (Game Bar) u OBS; grabar pantalla + audio.
- **A** maneja teclado; **B** habla y apunta con el mouse. Cuando cambien de pantalla, que el que habla diga qué está buscando.
- Nada de datos personales en pantalla (el email del seed es ficticio).

## Si algo falla en vivo

- Postman en rojo / "Could not send request" → la API no está en el puerto del `baseUrl`.
- `409` en "Crear usuario" → el usuario ya existe: borra `data/` y reinicia.
- `Connection refused` al validar tickets → no se corrió la carpeta **3. Órdenes** antes que **4. Tickets**.
