#  Proyecto Spring Boot + MySQL (Usuarios API)
## Nombre estudiante: Fabián Alejandro Bravo Olguín.
## Ingenieria de software Seccion 1
---

Este proyecto es una API REST simple construida con **Spring Boot** que se conecta a una base de datos **MySQL** en contenedores Docker.  
Permite realizar operaciones **CRUD** sobre la tabla `usuario`.

---

## Estructura del Proyecto

```
.
├── docker-compose.yml     # Orquestación de contenedores
├── db_data/               # Volumen persistente para la base de datos
├── tarea/                 # Código fuente de la app Spring Boot
│   ├── pom.xml
│   ├── src/
│   │   ├── main/java/IngS/tarea/
│   │   │   ├── TareaApplication.java
│   │   │   ├── controller/
│   │   │   │   └── Controller.java
│   │   │   ├── model/
│   │   │   │   ├── Usuario.java
│   │   │   │   ├── Mueble.java
│   │   │   │   ├── Variante.java
│   │   │   │   ├── Cotizacion.java
│   │   │   │   └── DetalleCotizacion.java
│   │   │   └── repository/
│   │   │       ├── UsuarioRep.java
│   │   │       ├── MuebleRep.java
│   │   │       ├── VarianteRep.java
│   │   │       ├── CotizacionRep.java
│   │   │       └── DetalleCotizacionRep.java
│   │   └── main/resources/application.properties
└── README.md
```

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

## Patrones de Diseño

### Patrones Implementados Actualmente

#### 1. **Repository Pattern**
- **Ubicación:** Paquete `repository/` (UsuarioRep, MuebleRep, VarianteRep, CotizacionRep, DetalleCotizacionRep)
- **Propósito:** Abstrae la lógica de acceso a datos, separando la lógica de negocio de la persistencia
- **Implementación:** Uso de interfaces que extienden `JpaRepository<T, ID>` de Spring Data JPA
- **Beneficio:** Permite cambiar la implementación de persistencia sin afectar la lógica de negocio

```java
public interface MuebleRep extends JpaRepository<Mueble, Long> {
    List<Mueble> findByEstado(String estado);
    List<Mueble> findByTipo(String tipo);
}
```

#### 2. **MVC (Model-View-Controller)**
- **Ubicación:** Arquitectura general del proyecto
- **Propósito:** Separación de responsabilidades entre capas
- **Implementación:**
  - **Model:** Entidades en el paquete `model/` (Usuario, Mueble, Variante, Cotizacion, DetalleCotizacion)
  - **Controller:** Clases con `@RestController` en el paquete `controller/`
  - **View:** Respuestas JSON (API REST)

#### 3. **DTO (Data Transfer Object) / Entity Pattern**
- **Ubicación:** Paquete `model/`
- **Propósito:** Las entidades JPA sirven como objetos de transferencia de datos
- **Implementación:** Clases anotadas con `@Entity` que mapean directamente a tablas de base de datos
- **Nota:** Se podría mejorar separando DTOs de Entidades para mayor flexibilidad

### Patrones Recomendados para Implementar

#### 1. **Strategy Pattern** ⭐ (Altamente Recomendado)
- **Propósito:** Calcular precios de muebles con diferentes estrategias de variantes
- **Aplicación:** Implementar diferentes estrategias de cálculo de precio según el tipo de variante
- **Ejemplo de Implementación:**

```java
// Interfaz Strategy
public interface PrecioStrategy {
    BigDecimal calcularPrecio(BigDecimal precioBase);
}

// Estrategia para variante normal
public class PrecioNormalStrategy implements PrecioStrategy {
    public BigDecimal calcularPrecio(BigDecimal precioBase) {
        return precioBase;
    }
}

// Estrategia para variante premium
public class PrecioPremiumStrategy implements PrecioStrategy {
    private BigDecimal incremento;

    public BigDecimal calcularPrecio(BigDecimal precioBase) {
        return precioBase.add(incremento);
    }
}
```

**Beneficio:** Permite agregar nuevas formas de calcular precios sin modificar el código existente (Open/Closed Principle)

#### 2. **Decorator Pattern** ⭐ (Altamente Recomendado)
- **Propósito:** Agregar variantes (barniz premium, cojines de seda, ruedas) a los muebles de forma dinámica
- **Aplicación:** "Decorar" un mueble base con diferentes variantes que modifican su precio
- **Ejemplo de Implementación:**

```java
// Interfaz base
public interface MuebleComponent {
    BigDecimal getPrecio();
    String getDescripcion();
}

// Componente concreto
public class MuebleBase implements MuebleComponent {
    private Mueble mueble;

    public BigDecimal getPrecio() {
        return mueble.getPrecioBase();
    }
}

// Decorador abstracto
public abstract class MuebleDecorator implements MuebleComponent {
    protected MuebleComponent mueble;
}

// Decoradores concretos
public class BarnizPremiumDecorator extends MuebleDecorator {
    public BigDecimal getPrecio() {
        return mueble.getPrecio().add(new BigDecimal("50.00"));
    }
}

public class CojinesSedaDecorator extends MuebleDecorator {
    public BigDecimal getPrecio() {
        return mueble.getPrecio().add(new BigDecimal("75.00"));
    }
}
```

**Beneficio:** Permite combinar múltiples variantes sin crear una explosión de subclases

#### 3. **Builder Pattern**
- **Propósito:** Construir objetos complejos como Cotizaciones con múltiples detalles
- **Aplicación:** Facilitar la creación de cotizaciones con múltiples muebles, variantes y cantidades
- **Ejemplo de Implementación:**

```java
public class CotizacionBuilder {
    private Cotizacion cotizacion;

    public CotizacionBuilder() {
        this.cotizacion = new Cotizacion();
    }

    public CotizacionBuilder agregarMueble(Mueble mueble, Variante variante, Integer cantidad) {
        DetalleCotizacion detalle = new DetalleCotizacion(mueble, variante, cantidad);
        cotizacion.agregarDetalle(detalle);
        return this;
    }

    public Cotizacion build() {
        cotizacion.calcularTotal();
        return cotizacion;
    }
}

// Uso:
Cotizacion cotizacion = new CotizacionBuilder()
    .agregarMueble(silla, barnizPremium, 4)
    .agregarMueble(mesa, null, 1)
    .build();
```

**Beneficio:** Hace más legible y mantenible la creación de cotizaciones complejas

#### 4. **Factory Pattern**
- **Propósito:** Crear diferentes tipos de muebles (sillas, sillones, mesas, estantes, cajones)
- **Aplicación:** Centralizar la lógica de creación de muebles según su tipo
- **Ejemplo de Implementación:**

```java
public class MuebleFactory {
    public static Mueble crearMueble(String tipo, Map<String, Object> atributos) {
        return switch (tipo.toLowerCase()) {
            case "silla" -> crearSilla(atributos);
            case "sillon" -> crearSillon(atributos);
            case "mesa" -> crearMesa(atributos);
            case "estante" -> crearEstante(atributos);
            case "cajon" -> crearCajon(atributos);
            default -> throw new IllegalArgumentException("Tipo de mueble no válido");
        };
    }

    private static Mueble crearSilla(Map<String, Object> attr) {
        Mueble silla = new Mueble();
        silla.setTipo("silla");
        // Configuración específica de sillas
        return silla;
    }
}
```

**Beneficio:** Encapsula la lógica de creación y facilita agregar nuevos tipos de muebles

#### 5. **Service Layer Pattern** (Muy Recomendado)
- **Propósito:** Separar la lógica de negocio del controlador
- **Aplicación:** Crear servicios como `MuebleService`, `CotizacionService`, `VentaService`
- **Ejemplo de Implementación:**

```java
@Service
public class VentaService {
    @Autowired
    private CotizacionRep cotizacionRepository;

    @Autowired
    private MuebleRep muebleRepository;

    public Cotizacion confirmarVenta(Long cotizacionId) throws StockInsuficienteException {
        Cotizacion cotizacion = cotizacionRepository.findById(cotizacionId)
            .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));

        // Verificar stock
        for (DetalleCotizacion detalle : cotizacion.getDetalles()) {
            Mueble mueble = detalle.getMueble();
            if (mueble.getStock() < detalle.getCantidad()) {
                throw new StockInsuficienteException(
                    "Stock insuficiente para: " + mueble.getNombreMueble()
                );
            }
        }

        // Decrementar stock
        for (DetalleCotizacion detalle : cotizacion.getDetalles()) {
            Mueble mueble = detalle.getMueble();
            mueble.setStock(mueble.getStock() - detalle.getCantidad());
            muebleRepository.save(mueble);
        }

        cotizacion.setEstado("confirmada");
        return cotizacionRepository.save(cotizacion);
    }
}
```

**Beneficio:** Mantiene los controladores delgados y facilita el testing

#### 6. **Observer Pattern** (Opcional)
- **Propósito:** Notificar cuando el stock de un mueble baja de cierto nivel
- **Aplicación:** Implementar un sistema de alertas de stock bajo
- **Beneficio:** Desacopla la lógica de notificación de la lógica de ventas

#### 7. **Singleton Pattern**
- **Propósito:** Asegurar una única instancia de servicios críticos
- **Aplicación:** Spring ya implementa este patrón por defecto con los beans `@Service`, `@Repository`, `@Controller`
- **Nota:** Ya está implementado implícitamente por Spring

### Resumen de Patrones para la Evaluación

**Mínimo requerido:** 2 patrones de diseño

**Recomendación de implementación:**

1. **Strategy Pattern** - Para el cálculo de precios con variantes (Requisito 4 del PDF)
2. **Service Layer Pattern** - Para la gestión de ventas y validación de stock (Requisito 5 del PDF)

**Alternativa:**

1. **Decorator Pattern** - Para agregar variantes a muebles (Requisito 4 del PDF)
2. **Service Layer Pattern** - Para la gestión de cotizaciones y ventas (Requisito 5 del PDF)

Ambas opciones cumplen con los requisitos y demuestran comprensión de patrones de diseño aplicados a problemas reales.

---

## Notas

- La base de datos se crea automáticamente (`pruebadb`).  
- Hibernate crea y actualiza la tabla `usuario`.  
- La tabla se crea vacia asi que para verificar deberá primero crear un usuario.
- Los datos se guardan en el volumen `db_data` para persistencia.  
- Puedes modificar las credenciales en `docker-compose.yml` y `application.properties`.
- Si llega a no funcionar correctamente la primera vez, pruebe parar los contenedores y levantarlos otra vez (Tambien pruebe borrando el db_data si es que se creo y no funciono):D

---

 Proyecto listo para levantar y usar.  
