-- ============================================================
-- EventPass - Carga inicial de datos (seed)
-- Se ejecuta al arrancar la aplicación, después de que Hibernate
-- crea las tablas (spring.jpa.defer-datasource-initialization=true).
-- Cada sentencia es idempotente: puede correr en cada arranque sin
-- duplicar registros.
-- ============================================================

-- ------------------------------------------------------------
-- USUARIOS
-- ------------------------------------------------------------
INSERT INTO usuarios (nombre, email, password, rol)
SELECT 'Ana Contreras', 'ana@eventpass.cl', 'admin123', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'ana@eventpass.cl');

INSERT INTO usuarios (nombre, email, password, rol)
SELECT 'Ben Hidalgo', 'ben@eventpass.cl', 'compra123', 'COMPRADOR'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'ben@eventpass.cl');

INSERT INTO usuarios (nombre, email, password, rol)
SELECT 'Carla Sanhueza', 'carla@eventpass.cl', 'staff123', 'STAFF'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'carla@eventpass.cl');

-- ------------------------------------------------------------
-- EVENTOS (catálogo con aforo completo al inicio)
-- ------------------------------------------------------------
INSERT INTO eventos (nombre, descripcion, fecha, lugar, aforo_total, aforo_disponible, precio)
SELECT 'Festival Andes Sound',
       'Festival de música indie con tres escenarios.',
       TIMESTAMP '2026-05-01 20:00:00',
       'Parque Bicentenario', 500, 500, 25000
WHERE NOT EXISTS (SELECT 1 FROM eventos WHERE nombre = 'Festival Andes Sound');

INSERT INTO eventos (nombre, descripcion, fecha, lugar, aforo_total, aforo_disponible, precio)
SELECT 'Charla Cloud Native',
       'Sobre arquitectura de microservicios y serverless.',
       TIMESTAMP '2026-04-18 09:00:00',
       'Auditorio DuocUC', 120, 120, 0
WHERE NOT EXISTS (SELECT 1 FROM eventos WHERE nombre = 'Charla Cloud Native');

INSERT INTO eventos (nombre, descripcion, fecha, lugar, aforo_total, aforo_disponible, precio)
SELECT 'Obra Tormenta',
       'Función de teatro contemporáneo.',
       TIMESTAMP '2026-06-12 19:30:00',
       'Teatro Municipal', 80, 80, 15000
WHERE NOT EXISTS (SELECT 1 FROM eventos WHERE nombre = 'Obra Tormenta');
