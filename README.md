# Proyecto Spring Boot + MySQL - Sistema de Mueblería

## Nombre estudiante: Fabián Alejandro Bravo Olguín

## Ingeniería de Software - Sección 1

## Evaluación 2 - Mueblería "Los Muebles Hermanos S.A"

---

## Resumen del Proyecto

Este proyecto es una **API REST completa** construida con **Spring Boot** que gestiona un sistema de mueblería, cumpliendo todos los requisitos de la Evaluación 2 de Ingeniería de Software.

### ✅ Funcionalidades Implementadas

1. **Gestión de Catálogo de Muebles (CRUD)**
   - Crear, listar, actualizar y desactivar muebles
   - Atributos completos: ID, nombre, tipo, precio base, stock, estado, tamaño, material

2. **Sistema de Variantes de Precio**
   - Variantes que modifican el precio (barniz premium, cojines de seda, ruedas)
   - Variante "normal" sin costo adicional
   - Cálculo automático de precios con variantes

3. **Gestión de Cotizaciones y Ventas**
   - Crear cotizaciones con múltiples muebles
   - Selección de variantes y cantidades
   - Confirmación de ventas con decrementación automática de stock
   - Validación de stock insuficiente con mensajes de error

### 🛠️ Stack Tecnológico

- **Spring Boot 3**
- **Spring Data JPA**
- **MySQL 8**
- **JUnit 5** (Testing)
- **Docker + Docker Compose**
- **Maven**
- **Java 21**

---

## Requisitos Previos

- [Docker](https://www.docker.com/) y [Docker Compose](https://docs.docker.com/compose/)
- Maven (solo si quieres compilar sin Docker)
- JDK 21 (recomendado)

---

## Cómo levantar el proyecto

2. **Levantar los contenedores en la carpeta donde se encuentra el docker-compose.yaml:**

   ```bash
   docker compose up --build
   ```

   Esto levantará:
   - **springapp** en [http://localhost:8080](http://localhost:8080)  
   - **MySQL** en el puerto `3306`
   - **PhpMyAdmin** en el puerto `8081`

4. **Detener los contenedores:**

   ```bash
   docker compose down
   ```

---

## Base de Datos

El contenedor de **MySQL** se levanta con la siguiente configuración:

- **Database:** `pruebadb`  
- **Usuario:** `root`
- **Root Password:** `1234`  

El volumen de datos está en la carpeta `./db_data` para persistencia. Esta se crea una vez iniciado los contenedores y la trabla usuuarios se encuentra vacia.

### Creación Automática de Tablas

Spring Boot está configurado con Hibernate (`spring.jpa.hibernate.ddl-auto=update`) para que la tabla `usuarios` se cree automáticamente a partir de la clase `Usuario`.

---

## Endpoints Disponibles

No se asegura que funcione correctamente los comandos de curl en windows ya que estos fueron probados en Linux (Arch). Se recomienda la utilización de postman para windows (con el cual fue probado en este entorno). Igualmente se puede utilizar la herramienta o metodo que se quiera para realizar las peticiones a los endpoints tomando de ejemplo los comandos curl usados a continuación.

### 1. Probar conexión

```bash
curl http://localhost:8080/
```

### 2. Crear un usuario

```bash
curl -X POST http://localhost:8080/crear   -H "Content-Type: application/json"   -d '{
        "nombre": "Juan",
        "apellido": "Pérez",
        "numero": "987654321",
        "correo": "juan.perez@example.com",
        "direccion": "Av. Siempre Viva 742, Santiago"
      }'
```

### 3. Listar usuarios

```bash
curl http://localhost:8080/usuarios
```

### 4. Buscar por ID

```bash
curl http://localhost:8080/buscar/1
```

### 5. Actualizar usuario (ejemplo con id=1)

```bash
curl -X POST http://localhost:8080/actualizar/1   -H "Content-Type: application/json"   -d '{
        "numero": "123456789",
        "correo": "nuevo.mail@example.com",
        "direccion": "Nueva Direccion 123"
      }'
```

### 6. Eliminar usuario

```bash
curl http://localhost:8080/borrar/1
```

---

## Tecnologías Usadas

- **Spring Boot 3**
- **Spring Data JPA**
- **MySQL**
- **Docker + Docker Compose**
- **Maven**
- **Java 21**
- **phpMyAdmin**

---
