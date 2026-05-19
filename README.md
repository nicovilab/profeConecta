# ProfeConecta

Aplicación de escritorio para conectar profesores particulares con estudiantes. Permite publicar y buscar anuncios de clases por materia y ubicación, gestionar reservas de horarios, chatear entre usuarios y consultar valoraciones.

## Tecnologías

| Capa | Tecnologías |
|---|---|
| Lenguaje | Java 24 |
| Interfaz | Java Swing |
| Base de datos | MariaDB, JDBC |
| Seguridad | BCrypt |
| Reportes | JasperReports, JFreeChart, iText PDF |
| Utilidades | Lombok, LGoodDatePicker |
| Build | Maven |

## Características

- **Registro y autenticación** con contraseñas cifradas mediante BCrypt.
- **Perfil de usuario** editable con foto, descripción, teléfono y ubicación (comunidad autónoma, provincia y municipio).
- **Publicación de anuncios** de clases con título, materia, precio por hora y descripción. El profesor puede crear, editar y eliminar sus propios anuncios.
- **Búsqueda de anuncios** filtrable por materia y localización.
- **Sistema de reservas**: los profesores publican franjas horarias disponibles en un calendario, y los estudiantes las reservan directamente desde la aplicación.
- **Chat en tiempo real** entre usuarios, con soporte para envío de archivos adjuntos.
- **Valoraciones**: los estudiantes pueden puntuar y comentar a los profesores tras una sesión.
- **Panel de reportes** con estadísticas y gráficas generadas con JFreeChart, exportables a PDF mediante iText.
- **Arquitectura MVC** con separación clara entre modelo, vista (Swing) y controladores.

## Requisitos previos

- **Java 24** (o superior)
- **Docker** y **Docker Compose** (para levantar la base de datos MariaDB)
- **Maven** (o usar el wrapper incluido `mvnw`)

## Instalación

1. Clonar el repositorio:

```bash
git clone https://github.com/nicovilab/profeConecta.git
cd profeConecta
```

2. Levantar MariaDB con Docker:

```bash
docker compose up -d
```

3. Compilar y ejecutar la aplicación:

```bash
mvn package
java -jar target/ProfeConecta-Final.jar
```

## Estructura del proyecto

```
src/main/java/com/nicovilab/profeconecta/
├── controller/     # Lógica de eventos y flujo de pantallas (MVC)
├── model/          # Entidades del dominio (Usuario, Anuncio, Reserva, Chat...)
├── service/        # Capa de negocio y acceso a datos (JDBC)
├── view/           # Componentes Swing y paneles de la UI
└── utils/          # Utilidades compartidas
```

## Variables de conexión

La configuración de la base de datos se encuentra en `DatabaseService.java`. Para modificar la conexión, actualiza los siguientes valores:

| Parámetro | Valor por defecto |
|---|---|
| URL | `jdbc:mariadb://127.0.0.1:3306/some-mariadb` |
| Usuario | `root` |
| Contraseña | `my-secret-pw` |
