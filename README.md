  # Sistema de Inventario

Aplicación web para gestión de inventario desarrollada con Spring Boot y Thymeleaf.

## Especificaciones Técnicas

### Tecnologías Utilizadas
- **Java 17**
- **Spring Boot** 3.5.6
- **Spring Data JPA** - Persistencia de datos
- **Hibernate** - ORM
- **Thymeleaf** - Motor de plantillas
- **MySQL** - Base de datos relacional
- **Gradle** - Gestión de dependencias y construcción


## Requisitos Previos a la Ejecución

### Configuración de la Aplicación
- **Puerto del servidor**: 8080
- **Context Path**: `/`
- **Base de datos**: MySQL (sistema_inventario)
- **DDL Auto**: Update (Hibernate actualiza el esquema automáticamente)

## Configuración de la Base de Datos

1. Crear la base de datos en MySQL:

2. Configurar las credenciales en `src/main/resources/application.yml`:
   - **Usuario**: root
   - **Contraseña**: root
   - **Puerto**: 3306

## Instrucciones de Ejecución

### Usando Gradle Wrapper (Recomendado)

**Windows:**
```bash
.\gradlew bootRun
```

**Linux/Mac:**
```bash
./gradlew bootRun
```

## Acceso a la Aplicación

Una vez iniciada la aplicación, acceder a:
```
http://localhost:8080
```
