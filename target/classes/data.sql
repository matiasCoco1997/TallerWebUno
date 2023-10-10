INSERT INTO Usuario(email, password, activo, fotoPerfil, nombre)
VALUES( 'paoloaleman86@gmail.com', '123', true, '/imagenes/imgsPerfiles/c2d016cc-efc4-42ef-a1d5-dfb1a93f5bfc.jpg', 'Paolo');
INSERT INTO Categoria(descripcion)
VALUES('Deportes'),('Politica'),('Programacion'),('Arte'),('Futbol'),('Juegos');
insert into Noticia(activa,categoria,descripcion,fechaDePublicacion,resumen,titulo)
VALUES(1, 'Programacion', 'Nueva versión de smartphone lanzada al mercado.', '2023-09-13', 'Resumen de la noticia tecnológica.', 'Lanzamiento de Smartphone XYZ'),
      (1, 'Deportes', 'El equipo local gana 2-1 en el partido de fútbol emocionante', '2023-09-13', 'Resumen del emocionante partido de fútbol entre dos equipos rivales.', 'Victoria del Equipo Local en un Partido Emocionante'),
      (0, 'Deportes', 'El equipo local gana 2-1 en el partido de fútbol emocionante', '2023-09-13', 'Resumen del emocionante partido de fútbol entre dos equipos rivales.', 'Victoria del Equipo Local en un Partido Emocionante');