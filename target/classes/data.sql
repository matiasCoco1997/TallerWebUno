/*INSERT INTO Usuario(email, password, activo, fotoPerfil, nombre)
VALUES( 'paleman86@gmail.com', '123', true, '/imagenes/imgsPerfiles/c2d016cc-efc4-42ef-a1d5-dfb1a93f5bfc.jpg', 'Paolo');*/


INSERT INTO `sunn`.`noticia` (
    `idNoticia`,
    `activa`,
    `categoria`,
    `descripcion`,
    `fechaDePublicacion`,
    `resumen`,
    `rutaDeAudioPodcast`,
    `rutaDeimagen`,
    `titulo`,
    `altDeImagen`,
    `usuario_idUsuario`
) VALUES     (1, 1, 'Deportes', 'Descripción de la noticia 1', '2023-10-11', 'Resumen 1', 'audio1.mp3', 'imagen1.jpg', 'Noticia 1', 'alt1', 1),
    (2, 1, 'Tecnología', 'Descripción de la noticia 2', '2023-10-12', 'Resumen 2', 'audio2.mp3', 'imagen2.jpg', 'Noticia 2', 'alt2', 1),
    (3, 1, 'Entretenimiento', 'Descripción de la noticia 3', '2023-10-13', 'Resumen 3', 'audio3.mp3', 'imagen3.jpg', 'Noticia 3', 'alt3', 1),
	(4, 1, 'Deportes', 'Descripción de la noticia 4', '2023-10-11', 'Resumen 4', 'audio1.mp3', 'imagen1.jpg', 'Noticia 1', 'alt4', 1),
    (5, 1, 'Tecnología', 'Descripción de la noticia 5', '2023-10-12', 'Resumen 5', 'audio2.mp3', 'imagen2.jpg', 'Noticia 2', 'alt5', 1),
    (6, 1, 'Entretenimiento', 'Descripción de la noticia 6', '2023-10-13', 'Resumen 6', 'audio3.mp3', 'imagen3.jpg', 'Noticia 3', 'alt6', 1);