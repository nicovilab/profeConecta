CREATE TABLE USUARIO (
  id_usuario int PRIMARY KEY AUTO_INCREMENT,
  nombre varchar(255),
  apellidos varchar(255),
  email varchar(255) UNIQUE,
  contrasena varchar(255),
  telefono varchar(255),
  foto_perfil varchar(255),
  descripcion text,
  fecha_registro date,
  es_admin boolean
);

CREATE TABLE DIRECCION (
  id_direccion int PRIMARY KEY AUTO_INCREMENT,
  id_usuario int,
  provincia varchar(255),
  municipio varchar(255),
  codigo_postal varchar(255),
  direccion varchar(255),
  latitud varchar(12),
  longitud varchar(12),
  FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario)
);

CREATE TABLE MATERIA (
  id_materia int PRIMARY KEY AUTO_INCREMENT,
  nombre varchar(255),
  descripcion text
);

CREATE TABLE ANUNCIO (
  id_anuncio int PRIMARY KEY AUTO_INCREMENT,
  id_usuario int,
  id_materia int,
  titulo varchar(255),
  descripcion text,
  precio_hora decimal,
  fecha_publicacion date,
  activo boolean,
  FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario),
  FOREIGN KEY (id_materia) REFERENCES MATERIA (id_materia)
);

CREATE TABLE RESERVA (
  id_reserva int PRIMARY KEY AUTO_INCREMENT,
  usuario_profesor int,
  usuario_estudiante int,
  estado varchar(255),
  fecha_solicitud date,
  comentarios text,
  fecha date,
  hora_inicio time,
  hora_fin time,
  disponible boolean,
  FOREIGN KEY (usuario_profesor) REFERENCES USUARIO(id_usuario),
  FOREIGN KEY (usuario_estudiante) REFERENCES USUARIO(id_usuario)
);

CREATE TABLE CHAT (
  id_chat int PRIMARY KEY AUTO_INCREMENT,
  usuario_emisor int,
  usuario_receptor int,
  fecha_creacion date,
  contenido text,
  fecha_hora datetime,
  leido boolean,
  es_mio boolean,
  contenido_archivo binary,
  FOREIGN KEY (usuario_emisor) REFERENCES USUARIO (id_usuario),
  FOREIGN KEY (usuario_receptor) REFERENCES USUARIO (id_usuario)
);

CREATE TABLE VALORACION (
  id_valoracion int PRIMARY KEY AUTO_INCREMENT,
  usuario_valorado int,
  usuario_valorador int,
  puntuacion tinyint,
  comentario text,
  fecha date,
  FOREIGN KEY (usuario_valorado) REFERENCES USUARIO (id_usuario),
  FOREIGN KEY (usuario_valorador) REFERENCES USUARIO (id_usuario)
);

CREATE TABLE INFORME (
  id_informe int PRIMARY KEY AUTO_INCREMENT,
  id_usuario_admin int,
  tipo varchar(255),
  nombre varchar(255),
  fecha_generacion datetime,
  ruta_archivo varchar(255),
  FOREIGN KEY (id_usuario_admin) REFERENCES USUARIO (id_usuario)
);