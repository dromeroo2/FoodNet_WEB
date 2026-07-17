-- 1. Borrar datos de las tablas hijas (las que dependen de otras)
DELETE FROM MENSAJES;
DELETE FROM COMENTARIOS;
DELETE FROM LIKES;
DELETE FROM SEGUIDORES;

-- 2. Borrar datos de las tablas intermedias/padres
DELETE FROM RECETAS;
DELETE FROM USUARIOS;

-- 3. Reiniciar los contadores de AUTO_INCREMENT a 1
ALTER TABLE USUARIOS ALTER COLUMN ID_USUARIO RESTART WITH 1;
ALTER TABLE RECETAS ALTER COLUMN ID_RECETA RESTART WITH 1;
ALTER TABLE MENSAJES ALTER COLUMN ID_MENSAJE RESTART WITH 1;
ALTER TABLE COMENTARIOS ALTER COLUMN ID_COMENTARIO RESTART WITH 1;

-- 4. Insertar USUARIOS
INSERT INTO USUARIOS (USERNAME, EMAIL, PASSWORD, FOTO_PERFIL, DESCRIPCION, ROL) VALUES
('admin', 'admin@foodnet.com', '123', 'https://placehold.co/150x150/FF5733/ffffff?text=Admin', 'Administrador principal del sitio. Amante de la alta cocina.', 'admin'),
('maria_cocina', 'maria@email.com', '123', 'https://placehold.co/150x150/33FF57/ffffff?text=Maria', 'Cocinera aficionada, especialista en postres.', 'usuario'),
('juan_parrilla', 'juan@email.com', '123', 'https://placehold.co/150x150/3357FF/ffffff?text=Juan', 'Fanático de los asados y la comida al aire libre.', 'usuario'),
('laura_veggie', 'laura@email.com', '123', 'https://placehold.co/150x150/FF33A8/ffffff?text=Laura', 'Recetas vegetarianas y saludables fáciles de hacer.', 'usuario'),
('carlos_spicy', 'carlos@email.com', '123', 'https://placehold.co/150x150/FFC300/ffffff?text=Carlos', 'Si no pica, no sabe. Buscando el picante perfecto.', 'usuario'),
('ana_tradicional', 'ana@email.com', '123', 'https://placehold.co/150x150/C70039/ffffff?text=Ana', 'Rescatando las recetas de la abuela. Cocina tradicional.', 'usuario');


-- 5. Insertar RECETAS
INSERT INTO RECETAS (ID_USUARIO, TITULO, DESCRIPCION, IMAGEN_RUTA, FECHA_PUBLICACION) VALUES
(2, 'Tarta de Manzana Casera', 'Una receta clásica y deliciosa.', 'https://placehold.co/600x400/e6be8a/ffffff?text=Tarta', '2023-10-01 10:30:00'),
(3, 'Costillas BBQ al Horno', 'Costillas de cerdo tiernas.', 'https://placehold.co/600x400/8a0000/ffffff?text=Costillas', '2023-10-05 14:15:00'),
(4, 'Curry de Garbanzos Vegano', 'Un plato lleno de sabor.', 'https://placehold.co/600x400/008a2e/ffffff?text=Curry', '2023-10-10 20:00:00'),
(5, 'Tacos Gobernador Picantes', 'Tacos de camarón con habanero.', 'https://placehold.co/600x400/d97700/ffffff?text=Tacos', '2023-10-12 12:45:00'),
(6, 'Lentejas Estofadas', 'El guiso tradicional español.', 'https://placehold.co/600x400/5c2b00/ffffff?text=Lentejas', '2023-10-15 13:00:00'),
(2, 'Brownie de Chocolate', 'Húmedo por dentro y crujiente fuera.', 'https://placehold.co/600x400/3b1f00/ffffff?text=Brownie', '2023-10-18 16:20:00');

-- 6. Insertar SEGUIDORES
INSERT INTO SEGUIDORES (ID_SEGUIDOR, ID_SEGUIDO, FECHA_SEGUIMIENTO) VALUES
(2, 3, '2023-10-02 09:00:00'), -- Maria sigue a Juan
(2, 4, '2023-10-02 09:05:00'), -- Maria sigue a Laura
(3, 5, '2023-10-06 11:00:00'), -- Juan sigue a Carlos
(4, 2, '2023-10-11 08:30:00'), -- Laura sigue a Maria
(5, 3, '2023-10-13 15:00:00'), -- Carlos sigue a Juan
(6, 2, '2023-10-16 10:00:00'), -- Ana sigue a Maria
(1, 2, '2023-10-20 10:00:00'); -- Admin sigue a Maria


-- 7. Insertar LIKES
INSERT INTO LIKES (ID_USUARIO, ID_RECETA) VALUES
(3, 1), -- Juan le gusta Tarta de Manzana (Receta 1)
(4, 1), -- Laura le gusta Tarta de Manzana (Receta 1)
(2, 2), -- Maria le gusta Costillas BBQ (Receta 2)
(5, 2), -- Carlos le gusta Costillas BBQ (Receta 2)
(2, 3), -- Maria le gusta Curry Vegano (Receta 3)
(3, 4), -- Juan le gusta Tacos Picantes (Receta 4)
(1, 5), -- Admin le gusta Lentejas (Receta 5)
(6, 1); -- Ana le gusta Tarta de Manzana (Receta 1)


-- 8. Insertar COMENTARIOS
INSERT INTO COMENTARIOS (ID_USUARIO, ID_RECETA, TEXTO, FECHA_COMENTARIO) VALUES
(3, 1, '¡Se ve deliciosa María! Intentaré hacerla este fin de semana.', '2023-10-01 12:00:00'),
(4, 1, '¿Se puede sustituir la harina normal por harina de almendras?', '2023-10-01 13:30:00'),
(2, 3, 'Laura, este curry me salvó la cena de ayer. ¡Buenísimo!', '2023-10-11 21:00:00'),
(5, 3, 'Le añadí un poco de cayena para darle más fuerza. Quedó excelente.', '2023-10-12 10:00:00'),
(6, 5, 'Justo como las hacía mi madre. Gracias por compartir.', '2023-10-15 18:45:00'),
(1, 2, 'Gran receta Juan. La foto de portada es excelente.', '2023-10-06 09:00:00');


-- 9. Insertar MENSAJES
INSERT INTO MENSAJES (ID_EMISOR, ID_RECEPTOR, TEXTO, FECHA_ENVIO) VALUES
(2, 4, 'Hola Laura, ¿tienes alguna otra receta vegana para cumpleaños?', '2023-10-03 10:00:00'),
(4, 2, '¡Hola María! Sí, tengo una tarta de chocolate vegana increíble. La subiré pronto.', '2023-10-03 10:15:00'),
(3, 5, 'Carlos, probé tus tacos. ¡Casi muero del picante pero estaban buenísimos!', '2023-10-13 20:30:00'),
(5, 3, 'Jajaja, te lo advertí Juan. ¡Me alegro que te gustaran!', '2023-10-13 20:35:00'),
(1, 2, 'María, felicidades por ser la usuaria más activa de la semana.', '2023-10-25 09:00:00'),
(6, 1, 'Hola Admin, tengo problemas para subir mi foto de perfil.', '2023-10-26 11:00:00');
