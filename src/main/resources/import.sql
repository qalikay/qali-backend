-- ============================================================
-- QaliKay - Datos seed (se ejecuta automaticamente por Hibernate
-- cuando ddl-auto = create-drop / create).
--
-- Passwords (ya hasheadas con BCrypt):
--   admin   / admin123
--   cliente / cliente123
--   cliente2 / cliente123
--   experto / 
--   experto2 / experto123
-- ============================================================


-- ============================================================
-- ROLES
-- ============================================================
INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_CLIENTE');
INSERT INTO roles (id, name) VALUES (3, 'ROLE_EXPERTO');


-- ============================================================
-- USERS  (bcrypt $2b$10$... funciona con Spring Security)
-- ============================================================
INSERT INTO users (id, username, password) VALUES
    (1, 'admin',    '$2b$10$9WEwQa3jFYXrWnjsAy4OvOF81RCL8wZCydBUzR4S6kf5cJeLCwjUS');
INSERT INTO users (id, username, password) VALUES
    (2, 'cliente',  '$2b$10$CGRMGA4i3rHC0vkya4D60ucJVRz5FhOQ2iJg17.DgPurS9k.NsIEW');
INSERT INTO users (id, username, password) VALUES
    (3, 'cliente2', '$2b$10$CGRMGA4i3rHC0vkya4D60ucJVRz5FhOQ2iJg17.DgPurS9k.NsIEW');
INSERT INTO users (id, username, password) VALUES
    (4, 'experto',  '$2b$10$oYxD2MY1nIR5GGeOPiNuDu8fZosQNj0aeXL25m4UUJqkxcRRdVih2');
INSERT INTO users (id, username, password) VALUES
    (5, 'experto2', '$2b$10$oYxD2MY1nIR5GGeOPiNuDu8fZosQNj0aeXL25m4UUJqkxcRRdVih2');


-- ============================================================
-- USER_ROLES  (tabla intermedia de la relacion ManyToMany)
-- ============================================================
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);  -- admin    -> ROLE_ADMIN
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);  -- cliente  -> ROLE_CLIENTE
INSERT INTO user_roles (user_id, role_id) VALUES (3, 2);  -- cliente2 -> ROLE_CLIENTE
INSERT INTO user_roles (user_id, role_id) VALUES (4, 3);  -- experto  -> ROLE_EXPERTO
INSERT INTO user_roles (user_id, role_id) VALUES (5, 3);  -- experto2 -> ROLE_EXPERTO


-- ============================================================
-- ESPECIALIDADES
-- ============================================================
INSERT INTO especialidades (id, nombre, descripcion) VALUES
    (1, 'Medicina natural andina',  'Uso tradicional de plantas medicinales y saberes ancestrales de los Andes.');
INSERT INTO especialidades (id, nombre, descripcion) VALUES
    (2, 'Herbolaria amazonica',     'Conocimiento de plantas medicinales de la selva amazonica peruana.');
INSERT INTO especialidades (id, nombre, descripcion) VALUES
    (3, 'Nutricion ancestral',      'Alimentacion saludable basada en superalimentos andinos como quinua, kiwicha y maca.');
INSERT INTO especialidades (id, nombre, descripcion) VALUES
    (4, 'Aromaterapia natural',     'Uso terapeutico de aceites esenciales de plantas nativas.');


-- ============================================================
-- CATEGORIAS
-- ============================================================
INSERT INTO categorias (id, nombre, descripcion) VALUES
    (1, 'Infusiones', 'Preparaciones naturales en agua caliente para consumo diario.');
INSERT INTO categorias (id, nombre, descripcion) VALUES
    (2, 'Pomadas',    'Productos topicos elaborados con insumos naturales para uso externo.');
INSERT INTO categorias (id, nombre, descripcion) VALUES
    (3, 'Jarabes',    'Preparaciones liquidas dulces para malestares de garganta y respiratorios.');
INSERT INTO categorias (id, nombre, descripcion) VALUES
    (4, 'Tinturas',   'Extractos alcoholicos concentrados de plantas medicinales.');
INSERT INTO categorias (id, nombre, descripcion) VALUES
    (5, 'Aceites',    'Aceites esenciales y macerados de plantas medicinales.');
INSERT INTO categorias (id, nombre, descripcion) VALUES
    (6, 'Mascarillas','Mezclas naturales para el cuidado de la piel.');


-- ============================================================
-- CLIENTES
-- ============================================================
INSERT INTO clientes (id, nombres, apellidos, telefono, user_id) VALUES
    (1, 'Maria', 'Quispe Flores', '999111222', 2);
INSERT INTO clientes (id, nombres, apellidos, telefono, user_id) VALUES
    (2, 'Jose',  'Mamani Cruz',   '999333444', 3);


-- ============================================================
-- EXPERTOS
-- ============================================================
INSERT INTO expertos (id, nombres, apellidos, telefono, trayectoria, anos_experiencia, especialidad_id, user_id) VALUES
    (1, 'Rosa',  'Huaman Ccahuana', '988111222',
        'Curandera andina con 20 anos preparando infusiones, pomadas y jarabes tradicionales en la sierra de Cusco.',
        20, 1, 4);
INSERT INTO expertos (id, nombres, apellidos, telefono, trayectoria, anos_experiencia, especialidad_id, user_id) VALUES
    (2, 'Pedro', 'Vargas Shipibo',  '988333444',
        'Maestro herbolario amazonico, formado en la tradicion shipibo-konibo del Ucayali.',
        15, 2, 5);


-- ============================================================
-- INSUMOS  (productos / materias primas vendidas en la plataforma)
-- ============================================================
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (1, 'Hojas de muna',     'Hojas secas de muna andina, ideales para infusiones digestivas.',
        8.50, 100, 'paquete 50g', 'HIERBA', NULL, 'DISPONIBLE', 1, 1);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (2, 'Hojas de coca',     'Hojas de coca seleccionadas para mate y mascado tradicional.',
        12.00, 80, 'paquete 100g', 'HIERBA', NULL, 'DISPONIBLE', 1, 1);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (3, 'Aceite de copaiba', 'Aceite amazonico antiinflamatorio extraido del arbol de copaiba.',
        45.00, 30, 'frasco 30ml', 'ACEITE', NULL, 'DISPONIBLE', 5, 2);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (4, 'Polvo de maca',     'Maca negra molida en polvo, energizante natural de los Andes.',
        25.00, 60, 'bolsa 250g', 'POLVO', NULL, 'DISPONIBLE', 1, 1);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (5, 'Extracto de una de gato', 'Extracto concentrado en alcohol, planta inmunomoduladora amazonica.',
        38.00, 25, 'frasco 50ml', 'EXTRACTO', NULL, 'DISPONIBLE', 4, 2);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (6, 'Sangre de grado',   'Latex amazonico cicatrizante de uso topico.',
        30.00, 0,  'frasco 30ml', 'EXTRACTO', NULL, 'AGOTADO',    4, 2);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (7, 'Eucalipto seco',    'Hojas secas de eucalipto para vaporizaciones e infusiones respiratorias.',
        6.00,  120,'paquete 100g', 'HIERBA', NULL, 'DISPONIBLE', 1, 1);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (8, 'Manzanilla',        'Flores secas de manzanilla, relajante y digestiva.',
        7.50,  90, 'paquete 50g', 'HIERBA', NULL, 'DISPONIBLE', 1, 1);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (9, 'Valeriana seca',    'Raiz seca de valeriana para infusiones relajantes y sueno reparador.',
        14.00, 45, 'paquete 50g', 'HIERBA', NULL, 'DISPONIBLE', 1, 1);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (10, 'Toronjil',         'Hojas de toronjil (melisa) para calmar nervios y mejorar la digestion.',
        9.00,  70, 'paquete 50g', 'HIERBA', NULL, 'DISPONIBLE', 1, 1);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (11, 'Quinua en hojuelas', 'Quinua organica de Cusco, superalimento andino rico en proteinas.',
        18.00, 55, 'bolsa 500g', 'POLVO', NULL, 'DISPONIBLE', 1, 1);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (12, 'Kiwicha en hojuelas', 'Kiwicha (amaranto) andina, ideal para desayunos nutritivos.',
        16.00, 40, 'bolsa 400g', 'POLVO', NULL, 'DISPONIBLE', 1, 1);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (13, 'Aceite de eucalipto', 'Aceite esencial de eucalipto para vaporizaciones y masajes.',
        22.00, 35, 'frasco 15ml', 'ACEITE', NULL, 'DISPONIBLE', 5, 1);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (14, 'Aceite de lavanda', 'Aceite esencial de lavanda, relajante y aromatico.',
        28.00, 28, 'frasco 15ml', 'ACEITE', NULL, 'DISPONIBLE', 5, 1);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (15, 'Tintura de propoleo', 'Extracto de propoleo andino, apoyo inmunologico natural.',
        32.00, 20, 'frasco 30ml', 'EXTRACTO', NULL, 'DISPONIBLE', 4, 2);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (16, 'Extracto de chancapiedra', 'Planta tradicional para salud renal y hepatica.',
        35.00, 18, 'frasco 50ml', 'EXTRACTO', NULL, 'DISPONIBLE', 4, 2);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (17, 'Cera de abeja natural', 'Cera de abeja pura para elaborar pomadas caseras.',
        15.00, 50, 'bloque 100g', 'OTRO', NULL, 'DISPONIBLE', 2, 1);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (18, 'Arcilla verde',      'Arcilla medicinal andina para mascarillas faciales detox.',
        12.00, 65, 'bolsa 200g', 'OTRO', NULL, 'DISPONIBLE', 6, 1);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (19, 'Paico seco',        'Hojas de paico, antiparasitario natural de uso tradicional.',
        5.50,  110,'paquete 50g', 'HIERBA', NULL, 'DISPONIBLE', 1, 1);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (20, 'Hoja de coca en polvo', 'Polvo fino de hoja de coca para preparaciones energeticas.',
        20.00, 15, 'bolsa 100g', 'POLVO', NULL, 'AGOTADO', 1, 1);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (21, 'Aceite de uña de gato', 'Aceite macerado de uña de gato, soporte articular.',
        42.00, 12, 'frasco 30ml', 'ACEITE', NULL, 'DISPONIBLE', 5, 2);
INSERT INTO insumos (id, nombre, descripcion, precio, stock, unidad, tipo, imagen_url, estado, categoria_id, experto_id) VALUES
    (22, 'Resina de brea',    'Resina amazonica para unguentos y remedios tradicionales.',
        18.00, 8,  'frasco 20g', 'OTRO', NULL, 'AGOTADO', 4, 2);


-- ============================================================
-- RECETAS  (estado: BORRADOR / PUBLICADA)
-- ============================================================
INSERT INTO recetas (id, titulo, descripcion, ingredientes, preparacion, advertencias,
                     minutos_preparacion, precio, imagen_url, estado, fecha_creacion,
                     categoria_id, experto_id) VALUES
    (1, 'Infusion de muna para la digestion',
        'Infusion clasica andina que alivia molestias estomacales y gases.',
        '1 cucharadita de hojas secas de muna, 1 taza de agua hirviendo, miel al gusto.',
        'Calentar el agua hasta hervir. Apagar el fuego y agregar las hojas de muna. Tapar y dejar reposar 5 minutos. Colar y endulzar con miel.',
        'No consumir mas de 3 tazas al dia. Evitar en embarazo.',
        10, 12.00, NULL, 'PUBLICADA', '2025-09-10', 1, 1);

INSERT INTO recetas (id, titulo, descripcion, ingredientes, preparacion, advertencias,
                     minutos_preparacion, precio, imagen_url, estado, fecha_creacion,
                     categoria_id, experto_id) VALUES
    (2, 'Jarabe de eucalipto y miel',
        'Jarabe natural para aliviar tos y congestion respiratoria.',
        '2 cucharadas de hojas de eucalipto, 1 taza de miel pura, 1/2 taza de agua, jugo de medio limon.',
        'Hervir las hojas de eucalipto en el agua por 10 minutos. Colar y mezclar con la miel y el limon. Guardar en frasco esterilizado.',
        'No dar a ninos menores de 1 ano. Conservar refrigerado.',
        25, 22.00, NULL, 'PUBLICADA', '2025-09-20', 3, 1);

INSERT INTO recetas (id, titulo, descripcion, ingredientes, preparacion, advertencias,
                     minutos_preparacion, precio, imagen_url, estado, fecha_creacion,
                     categoria_id, experto_id) VALUES
    (3, 'Pomada de copaiba para articulaciones',
        'Pomada amazonica para aliviar dolores musculares y articulares.',
        '20ml de aceite de copaiba, 30g de cera de abeja, 50ml de aceite de coco.',
        'Derretir la cera con el aceite de coco a fuego lento. Retirar y agregar el aceite de copaiba. Mezclar bien y verter en envase. Dejar enfriar.',
        'Solo uso topico. No aplicar en heridas abiertas.',
        20, 35.00, NULL, 'PUBLICADA', '2025-10-01', 2, 2);

INSERT INTO recetas (id, titulo, descripcion, ingredientes, preparacion, advertencias,
                     minutos_preparacion, precio, imagen_url, estado, fecha_creacion,
                     categoria_id, experto_id) VALUES
    (4, 'Mate de coca energizante',
        'Bebida tradicional andina para combatir el cansancio y el soroche.',
        '1 cucharadita de hojas de coca, 1 taza de agua caliente, panela o miel al gusto.',
        'Calentar el agua sin que hierva. Verter sobre las hojas de coca y dejar reposar 3 minutos. Endulzar.',
        'Evitar antes de dormir por su efecto estimulante.',
        5, 10.00, NULL, 'PUBLICADA', '2025-10-15', 1, 1);

INSERT INTO recetas (id, titulo, descripcion, ingredientes, preparacion, advertencias,
                     minutos_preparacion, precio, imagen_url, estado, fecha_creacion,
                     categoria_id, experto_id) VALUES
    (5, 'Tintura de una de gato',
        'Tintura amazonica usada como inmunomodulador.',
        '50g de corteza de una de gato, 250ml de alcohol etilico de 70 grados.',
        'Macerar la corteza en el alcohol durante 21 dias en un frasco oscuro. Agitar diariamente. Colar y envasar.',
        'Consultar al medico si recibe tratamiento inmunosupresor.',
        30, 40.00, NULL, 'PUBLICADA', '2025-10-25', 4, 2);

INSERT INTO recetas (id, titulo, descripcion, ingredientes, preparacion, advertencias,
                     minutos_preparacion, precio, imagen_url, estado, fecha_creacion,
                     categoria_id, experto_id) VALUES
    (6, 'Mascarilla de maca y miel',
        'Mascarilla nutritiva para piel cansada o seca.',
        '1 cucharada de polvo de maca, 1 cucharada de miel, 1 cucharadita de aceite de oliva.',
        'Mezclar todos los ingredientes hasta obtener una pasta homogenea. Aplicar sobre rostro limpio por 15 minutos y enjuagar con agua tibia.',
        'Probar antes en el antebrazo si tiene piel sensible.',
        5, 18.00, NULL, 'PUBLICADA', '2025-11-02', 6, 1);

INSERT INTO recetas (id, titulo, descripcion, ingredientes, preparacion, advertencias,
                     minutos_preparacion, precio, imagen_url, estado, fecha_creacion,
                     categoria_id, experto_id) VALUES
    (7, 'Infusion de manzanilla relajante',
        'Infusion clasica para relajar el sistema nervioso y favorecer el sueno.',
        '1 cucharadita de flores de manzanilla, 1 taza de agua hirviendo.',
        'Agregar las flores al agua caliente y dejar reposar 5 minutos. Colar y beber preferentemente antes de dormir.',
        'Sin contraindicaciones conocidas.',
        7, 9.00, NULL, 'PUBLICADA', '2025-11-08', 1, 1);

INSERT INTO recetas (id, titulo, descripcion, ingredientes, preparacion, advertencias,
                     minutos_preparacion, precio, imagen_url, estado, fecha_creacion,
                     categoria_id, experto_id) VALUES
    (8, 'Pomada de sangre de grado',
        'Pomada cicatrizante para heridas leves y picaduras.',
        '10ml de sangre de grado, 20g de cera de abeja, 40ml de aceite de coco.',
        'Derretir la cera con el aceite a bano maria. Retirar del fuego y agregar la sangre de grado. Mezclar y envasar.',
        'No usar en alergicos al latex.',
        15, 28.00, NULL, 'BORRADOR', '2025-11-12', 2, 2);


-- ============================================================
-- CONSULTAS  (cliente <-> experto, mensajeria)
-- ============================================================
INSERT INTO consultas (id, asunto, estado, fecha_creacion, cliente_id, experto_id) VALUES
    (1, 'Dudas sobre dosis de muna para mi hijo',
        'ABIERTA', '2025-11-10 10:30:00', 1, 1);

INSERT INTO consultas (id, asunto, estado, fecha_creacion, cliente_id, experto_id) VALUES
    (2, 'Pomada de copaiba para dolor de rodilla',
        'ABIERTA', '2025-11-11 14:20:00', 1, 2);

INSERT INTO consultas (id, asunto, estado, fecha_creacion, cliente_id, experto_id) VALUES
    (3, 'Recomendacion de planta para insomnio',
        'CERRADA', '2025-11-05 09:00:00', 2, 1);


-- ============================================================
-- MENSAJES (de cada consulta)
-- ============================================================
INSERT INTO mensajes (id, contenido, remitente, fecha_envio, consulta_id) VALUES
    (1, 'Hola, quisiera saber si puedo darle infusion de muna a mi hijo de 8 anos para los gases.',
        'CLIENTE', '2025-11-10 10:30:00', 1);
INSERT INTO mensajes (id, contenido, remitente, fecha_envio, consulta_id) VALUES
    (2, 'Hola Maria, si puedes darle media taza tibia, una vez al dia despues del almuerzo. No mas de 3 dias seguidos.',
        'EXPERTO', '2025-11-10 11:15:00', 1);
INSERT INTO mensajes (id, contenido, remitente, fecha_envio, consulta_id) VALUES
    (3, 'Muchas gracias, lo intentare hoy mismo.',
        'CLIENTE', '2025-11-10 11:40:00', 1);

INSERT INTO mensajes (id, contenido, remitente, fecha_envio, consulta_id) VALUES
    (4, 'Buenas tardes Pedro, mi mama tiene dolor en la rodilla derecha. La pomada de copaiba le serviria?',
        'CLIENTE', '2025-11-11 14:20:00', 2);
INSERT INTO mensajes (id, contenido, remitente, fecha_envio, consulta_id) VALUES
    (5, 'Hola Maria, claro. Aplicarla en masaje suave 2 veces al dia por 7 dias. Si no mejora, deberia ir al medico.',
        'EXPERTO', '2025-11-11 15:00:00', 2);

INSERT INTO mensajes (id, contenido, remitente, fecha_envio, consulta_id) VALUES
    (6, 'Buenos dias, me cuesta dormir hace varios dias. Que planta me recomienda?',
        'CLIENTE', '2025-11-05 09:00:00', 3);
INSERT INTO mensajes (id, contenido, remitente, fecha_envio, consulta_id) VALUES
    (7, 'Hola Jose, te recomiendo infusion de manzanilla antes de dormir. Tambien puedes probar valeriana si persiste.',
        'EXPERTO', '2025-11-05 09:45:00', 3);
INSERT INTO mensajes (id, contenido, remitente, fecha_envio, consulta_id) VALUES
    (8, 'Perfecto, lo probare esta noche. Gracias!',
        'CLIENTE', '2025-11-05 10:00:00', 3);


-- ============================================================
-- ORDENES (compras)
-- ============================================================
INSERT INTO ordenes (id, fecha, total, estado, metodo_pago, cliente_id) VALUES
    (1, '2025-11-08 16:00:00', 44.00, 'PAGADA',    'YAPE', 1);
INSERT INTO ordenes (id, fecha, total, estado, metodo_pago, cliente_id) VALUES
    (2, '2025-11-09 11:30:00', 35.00, 'PAGADA',    'TARJETA', 2);
INSERT INTO ordenes (id, fecha, total, estado, metodo_pago, cliente_id) VALUES
    (3, '2025-11-11 19:15:00', 30.00, 'PENDIENTE', 'EFECTIVO', 1);


-- ============================================================
-- DETALLE_ORDEN
-- ============================================================
-- Orden 1: 2 infusiones de muna + 1 paquete de hojas de muna
INSERT INTO detalle_orden (id, tipo_item, item_id, descripcion, cantidad, precio_unitario, subtotal, orden_id) VALUES
    (1, 'RECETA', 1, 'Infusion de muna para la digestion', 2, 12.00, 24.00, 1);
INSERT INTO detalle_orden (id, tipo_item, item_id, descripcion, cantidad, precio_unitario, subtotal, orden_id) VALUES
    (2, 'INSUMO', 1, 'Hojas de muna (50g)',                 1,  8.50,  8.50, 1);
INSERT INTO detalle_orden (id, tipo_item, item_id, descripcion, cantidad, precio_unitario, subtotal, orden_id) VALUES
    (3, 'INSUMO', 8, 'Manzanilla (50g)',                    1,  7.50,  7.50, 1);

-- Orden 2: 1 pomada de copaiba
INSERT INTO detalle_orden (id, tipo_item, item_id, descripcion, cantidad, precio_unitario, subtotal, orden_id) VALUES
    (4, 'RECETA', 3, 'Pomada de copaiba para articulaciones', 1, 35.00, 35.00, 2);

-- Orden 3: 1 jarabe de eucalipto + 1 mascarilla
INSERT INTO detalle_orden (id, tipo_item, item_id, descripcion, cantidad, precio_unitario, subtotal, orden_id) VALUES
    (5, 'RECETA', 2, 'Jarabe de eucalipto y miel', 1, 22.00, 22.00, 3);
INSERT INTO detalle_orden (id, tipo_item, item_id, descripcion, cantidad, precio_unitario, subtotal, orden_id) VALUES
    (6, 'RECETA', 6, 'Mascarilla de maca y miel',  1,  8.00,  8.00, 3);


-- ============================================================
-- RESENAS
-- ============================================================
INSERT INTO resenas (id, calificacion, comentario, tipo_item, item_id, fecha_creacion, cliente_id) VALUES
    (1, 5, 'Excelente infusion, me ayudo muchisimo con la digestion despues del almuerzo.',
        'RECETA', 1, '2025-11-08 20:00:00', 1);
INSERT INTO resenas (id, calificacion, comentario, tipo_item, item_id, fecha_creacion, cliente_id) VALUES
    (2, 4, 'La pomada huele bien y alivia, aunque el efecto dura unas pocas horas.',
        'RECETA', 3, '2025-11-09 21:30:00', 2);
INSERT INTO resenas (id, calificacion, comentario, tipo_item, item_id, fecha_creacion, cliente_id) VALUES
    (3, 5, 'Las hojas de muna llegaron muy frescas, super recomendado.',
        'INSUMO', 1, '2025-11-09 09:00:00', 1);
INSERT INTO resenas (id, calificacion, comentario, tipo_item, item_id, fecha_creacion, cliente_id) VALUES
    (4, 5, 'La maestra Rosa atiende con mucha paciencia y conocimiento.',
        'EXPERTO', 1, '2025-11-10 19:00:00', 1);
INSERT INTO resenas (id, calificacion, comentario, tipo_item, item_id, fecha_creacion, cliente_id) VALUES
    (5, 4, 'El jarabe es efectivo aunque un poco dulce para mi gusto.',
        'RECETA', 2, '2025-11-12 08:30:00', 2);


-- ============================================================
-- AJUSTE DE SECUENCIAS (H2)
-- ============================================================
ALTER TABLE roles ALTER COLUMN id RESTART WITH 4;
ALTER TABLE users ALTER COLUMN id RESTART WITH 6;
ALTER TABLE especialidades ALTER COLUMN id RESTART WITH 5;
ALTER TABLE categorias ALTER COLUMN id RESTART WITH 7;
ALTER TABLE clientes ALTER COLUMN id RESTART WITH 3;
ALTER TABLE expertos ALTER COLUMN id RESTART WITH 3;
ALTER TABLE insumos ALTER COLUMN id RESTART WITH 23;
ALTER TABLE recetas ALTER COLUMN id RESTART WITH 9;
ALTER TABLE consultas ALTER COLUMN id RESTART WITH 4;
ALTER TABLE mensajes ALTER COLUMN id RESTART WITH 9;
ALTER TABLE ordenes ALTER COLUMN id RESTART WITH 4;
ALTER TABLE detalle_orden ALTER COLUMN id RESTART WITH 7;
ALTER TABLE resenas ALTER COLUMN id RESTART WITH 6;
