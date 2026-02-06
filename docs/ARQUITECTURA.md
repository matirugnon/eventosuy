# Arquitectura de EventosUy

## 📐 Visión General

EventosUy es un sistema de gestión de eventos desarrollado en **3 iteraciones evolutivas**, cada una implementando una arquitectura diferente y progresivamente más compleja.

---

## Iteración 1: Arquitectura Monolítica de Escritorio

### Diagrama de Arquitectura

```
┌─────────────────────────────────────────────────────────┐
│                  Aplicación de Escritorio               │
│                                                         │
│  ┌──────────────────────────────────────────────────┐  │
│  │         Capa de Presentación (Swing)             │  │
│  │  - MainFrame                                     │  │
│  │  - JInternalFrames (Consultas, Altas)           │  │
│  └────────────────┬─────────────────────────────────┘  │
│                   │                                     │
│  ┌────────────────▼─────────────────────────────────┐  │
│  │            Capa de Lógica                        │  │
│  │  - Controladores (Casos de Uso)                 │  │
│  │  - Manejadores (ManejadorUsuario, etc.)         │  │
│  └────────────────┬─────────────────────────────────┘  │
│                   │                                     │
│  ┌────────────────▼─────────────────────────────────┐  │
│  │          Modelo de Dominio                       │  │
│  │  - Usuario, Evento, Edicion, Registro           │  │
│  │  - Organizador, Asistente                       │  │
│  └──────────────────────────────────────────────────┘  │
│                                                         │
│              (Sin Persistencia - Memoria)               │
└─────────────────────────────────────────────────────────┘
```

### Características

- **Patrón:** MVC (Model-View-Controller) monolítico
- **Presentación:** Java Swing con JGoodies Forms
- **Lógica:** Clases Java POJO
- **Persistencia:** En memoria (colecciones HashMap/ArrayList)
- **Ciclo de vida:** Aplicación standalone ejecutable

### Componentes Principales

| Componente | Descripción |
|------------|-------------|
| **Main.java** | Punto de entrada, inicializa MainFrame |
| **MainFrame** | Ventana principal con menú y escritorio MDI |
| **JInternalFrames** | Formularios para cada caso de uso |
| **Controladores** | Implementan lógica de casos de uso |
| **Manejadores** | Singleton para gestionar colecciones de entidades |
| **Modelo** | Clases de dominio (Usuario, Evento, etc.) |

### Flujo de Datos

```
Usuario → GUI (Swing) → Controlador → Manejador → Modelo (en memoria)
                           ↑                           ↓
                           └───────────────────────────┘
```

---

## Iteración 2: Arquitectura Web MVC

### Diagrama de Arquitectura

```
┌────────────────────────────────────────────────────────────┐
│                      Cliente (Navegador)                   │
│                    HTML + CSS + JavaScript                 │
└────────────────────┬───────────────────────────────────────┘
                     │ HTTP Request/Response
                     ▼
┌─────────────────────────────────────────────────────────────┐
│                   Servidor Web (Tomcat)                     │
│                                                             │
│  ┌──────────────────────────────────────────────────────┐  │
│  │             Capa de Presentación (JSP)               │  │
│  │  - index.html, *.jsp                                │  │
│  │  - Vistas por rol: asistente/, organizador/          │  │
│  └──────────────────┬───────────────────────────────────┘  │
│                     │                                       │
│  ┌──────────────────▼───────────────────────────────────┐  │
│  │         Capa de Control (Servlets)                   │  │
│  │  - LoginServlet, SignupServlet                      │  │
│  │  - ConsultaEventoServlet, etc.                      │  │
│  │  - Filtros (ContadorVisitasFilter)                 │  │
│  └──────────────────┬───────────────────────────────────┘  │
│                     │                                       │
│  ┌──────────────────▼───────────────────────────────────┐  │
│  │            Capa de Lógica                            │  │
│  │  - DTOs (EventoDTO, UsuarioDTO)                     │  │
│  │  - Validaciones                                     │  │
│  └──────────────────┬───────────────────────────────────┘  │
│                     │                                       │
│  ┌──────────────────▼───────────────────────────────────┐  │
│  │          Modelo de Dominio                           │  │
│  │  - Usuario, Evento, Edicion (similares a Iter. 1)   │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                             │
│              (Datos en memoria + sesiones HTTP)             │
└─────────────────────────────────────────────────────────────┘
```

### Características

- **Patrón:** MVC Web (Model-View-Controller)
- **Frontend:** HTML5, CSS3, JavaScript vanilla
- **Backend:** Jakarta Servlets 6.0, JSP 3.1, JSTL 3.0
- **Servidor:** Apache Tomcat 10.1
- **Persistencia:** Sesiones HTTP (HttpSession)
- **Deployment:** WAR (Web Application Archive)

### Flujo de Petición HTTP

```
1. Cliente envía petición HTTP
   ↓
2. Tomcat recibe en puerto 8080
   ↓
3. Filtros procesan request (ej: ContadorVisitasFilter)
   ↓
4. Servlet correspondiente (@WebServlet mapping)
   ↓
5. Servlet procesa lógica de negocio
   ↓
6. Servlet crea DTOs y los añade a request/session
   ↓
7. Forward a JSP
   ↓
8. JSP renderiza vista con JSTL
   ↓
9. Response HTML al cliente
```

### Componentes Principales

| Componente | Descripción |
|------------|-------------|
| **Servlets** | Controladores HTTP (doGet/doPost) |
| **JSP** | Vistas dinámicas con Java embebido |
| **JSTL** | Tag libraries para lógica en vistas |
| **DTOs** | Objetos de transferencia de datos |
| **Filtros** | Interceptores de peticiones |
| **HttpSession** | Almacenamiento de estado de usuario |

### Estructura de URLs

| URL | Servlet | Descripción |
|-----|---------|-------------|
| `/login` | LoginServlet | Autenticación |
| `/signup` | SignupServlet | Registro de usuario |
| `/consultaEvento` | ConsultaEventoServlet | Consulta de eventos |
| `/consultaEdicion` | ConsultaEdicionServlet | Consulta de ediciones |
| `/registroAedicion` | RegistroAEdicionServlet | Registro a edición |
| `/miPerfil` | PerfilUsuarioServlet | Perfil de usuario |

---

## Iteración 3: Arquitectura Distribuida con SOA

### Diagrama de Arquitectura Completa

```
┌──────────────────────────────────────────────────────────────────────┐
│                         Capa de Clientes                             │
│                                                                      │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐    │
│  │  Cliente Web    │  │  Cliente Móvil  │  │  Desktop GUI    │    │
│  │   (Tomcat)      │  │   (Tomcat)      │  │  (Java Swing)   │    │
│  │   web.war       │  │   mobile.war    │  │                 │    │
│  └────────┬────────┘  └────────┬────────┘  └────────┬────────┘    │
│           │                    │                     │              │
└───────────┼────────────────────┼─────────────────────┼──────────────┘
            │                    │                     │
            │    SOAP/HTTP       │      SOAP/HTTP      │  Invocación
            │    (JAX-WS)        │      (JAX-WS)       │  Directa
            ▼                    ▼                     ▼
┌──────────────────────────────────────────────────────────────────────┐
│                      Servidor Central (Backend)                      │
│                                                                      │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │              Publicadores SOAP (Web Services)                  │ │
│  │                                                                │ │
│  │  ┌─────────────────┐  ┌─────────────────┐                    │ │
│  │  │ Publicador      │  │ Publicador      │                    │ │
│  │  │ Controlador     │  │ Usuario         │  ...               │ │
│  │  │ :9115/publicador│  │ :9115/publicador│                    │ │
│  │  │                 │  │ Usuario         │                    │ │
│  │  └────────┬────────┘  └────────┬────────┘                    │ │
│  │           │ @WebService         │                            │ │
│  └───────────┼─────────────────────┼────────────────────────────┘ │
│              │                     │                              │
│  ┌───────────▼─────────────────────▼────────────────────────────┐ │
│  │                    Capa de Lógica                            │ │
│  │  ┌──────────────────────────────────────────────────────┐   │ │
│  │  │ Controladores (IControlador*, Factory)               │   │ │
│  │  └───────────────────┬──────────────────────────────────┘   │ │
│  │                      │                                       │ │
│  │  ┌───────────────────▼──────────────────────────────────┐   │ │
│  │  │ Manejadores (Singleton)                              │   │ │
│  │  │  - ManejadorUsuario                                  │   │ │
│  │  │  - ManejadorEventos                                  │   │ │
│  │  │  - ManejadorPersistencia                            │   │ │
│  │  └───────────────────┬──────────────────────────────────┘   │ │
│  └────────────────────────────────────────────────────────────┘ │
│                        │                                         │
│  ┌─────────────────────▼───────────────────────────────────────┐ │
│  │                 Modelo de Dominio (JPA)                     │ │
│  │  @Entity: Usuario, Evento, Edicion, Registro, etc.         │ │
│  └─────────────────────┬───────────────────────────────────────┘ │
│                        │ JPA/EclipseLink                         │
│  ┌─────────────────────▼───────────────────────────────────────┐ │
│  │              Capa de Persistencia (JPA)                     │ │
│  │  EntityManager, persistence.xml                             │ │
│  └─────────────────────┬───────────────────────────────────────┘ │
│                        │ JDBC                                     │
└────────────────────────┼──────────────────────────────────────────┘
                         ▼
                  ┌─────────────┐
                  │   HSQLDB    │
                  │  (Embedded) │
                  │  File-based │
                  └─────────────┘
```

### Características

- **Patrón:** SOA (Service-Oriented Architecture) + Cliente-Servidor
- **Protocolo:** SOAP sobre HTTP
- **Serialización:** XML (WSDL, XSD)
- **Framework WS:** JAX-WS 4.0
- **Persistencia:** JPA 3.1 con EclipseLink
- **Base de datos:** HSQLDB 2.7.2 (embedded, file-based)
- **Deployment:**
  - Servidor Central: JAR ejecutable standalone
  - Clientes Web/Móvil: WARs en Tomcat

### Publicadores SOAP

| Publicador | Puerto | Endpoint | Descripción |
|------------|--------|----------|-------------|
| **PublicadorControlador** | 9115 | `/publicador` | Servicios generales |
| **PublicadorUsuario** | 9115 | `/publicadorUsuario` | CRUD de usuarios |
| **PublicadorRegistro** | 9115 | `/publicadorRegistro` | Gestión de registros |
| **PublicadorCargaDatos** | 9115 | `/publicadorCargaDatos` | Carga datos prueba |

### Flujo de Comunicación SOAP

```
┌─────────────┐                                      ┌──────────────────┐
│ Cliente Web │                                      │ Servidor Central │
└──────┬──────┘                                      └────────┬─────────┘
       │                                                      │
       │  1. HTTP POST (SOAP Request XML)                    │
       │ ─────────────────────────────────────────────────► │
       │    <soapenv:Envelope>                               │
       │      <soapenv:Body>                                 │
       │        <obtenerUsuario>user1</obtenerUsuario>       │
       │      </soapenv:Body>                                │
       │    </soapenv:Envelope>                              │
       │                                                      │
       │                              2. Procesar en Publicador
       │                              3. Invocar Controlador │
       │                              4. Consultar JPA       │
       │                              5. Consultar HSQLDB    │
       │                                                      │
       │  6. HTTP Response (SOAP Response XML)               │
       │ ◄───────────────────────────────────────────────── │
       │    <soapenv:Envelope>                               │
       │      <soapenv:Body>                                 │
       │        <obtenerUsuarioResponse>                     │
       │          <DTUsuario>...</DTUsuario>                 │
       │        </obtenerUsuarioResponse>                    │
       │      </soapenv:Body>                                │
       │    </soapenv:Envelope>                              │
       │                                                      │
       │  7. Cliente deserializa XML a objetos Java          │
       │  8. Servlet procesa DTOs                            │
       │  9. Forward a JSP                                   │
       │                                                      │
```

### Capa de Persistencia (JPA)

```
┌────────────────────────────────────────────────────┐
│            persistence.xml                         │
│  <persistence-unit name="EventosUy">              │
│    <provider>EclipseLink</provider>               │
│    <class>logica.Usuario</class>                  │
│    <class>logica.Evento</class>                   │
│    ...                                            │
│  </persistence-unit>                              │
└────────────────┬───────────────────────────────────┘
                 │
                 ▼
┌────────────────────────────────────────────────────┐
│            EntityManager                           │
│  persist(), find(), merge(), remove()             │
└────────────────┬───────────────────────────────────┘
                 │
                 ▼
┌────────────────────────────────────────────────────┐
│         Entidades JPA (@Entity)                    │
│                                                    │
│  @Entity                                          │
│  public class Usuario {                           │
│      @Id                                          │
│      private String nickname;                     │
│      private String nombre;                       │
│      @OneToMany                                   │
│      private List<Registro> registros;            │
│      ...                                          │
│  }                                                │
└────────────────┬───────────────────────────────────┘
                 │ JDBC
                 ▼
          ┌─────────────┐
          │   HSQLDB    │
          │  ediciones  │
          │  Archivadas │
          └─────────────┘
```

### Configuración de Base de Datos

**persistence.xml:**
```xml
<persistence-unit name="EdicionesArchivadas">
    <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
    
    <class>logica.EdicionArchivada</class>
    
    <properties>
        <property name="jakarta.persistence.jdbc.url" 
                  value="jdbc:hsqldb:file:./data/edicionesArchivadas"/>
        <property name="jakarta.persistence.jdbc.user" value="SA"/>
        <property name="jakarta.persistence.jdbc.password" value=""/>
        <property name="jakarta.persistence.jdbc.driver" 
                  value="org.hsqldb.jdbcDriver"/>
        
        <property name="eclipselink.ddl-generation" value="create-or-extend-tables"/>
        <property name="eclipselink.logging.level" value="FINE"/>
    </properties>
</persistence-unit>
```

### Cliente Web (Consumidor SOAP)

**Generación de clientes JAX-WS:**

```bash
# Maven genera clases cliente desde WSDL
mvn jaxws:wsimport -Dwsdl.url=http://localhost:9115/publicador?wsdl
```

**Clases generadas:**
- `PublicadorControladorService` (ServiceFactory)
- `PublicadorControlador` (Port/Proxy)
- `DTUsuario`, `DTEvento`, etc. (DTOs)
- `ObjectFactory` (para crear instancias)

**Uso en Servlet:**
```java
@WebServlet("/consultaEvento")
public class ConsultaEventoServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // Crear cliente SOAP
        PublicadorControladorService service = new PublicadorControladorService();
        PublicadorControlador port = service.getPublicadorControladorPort();
        
        // Invocar servicio remoto
        StringArray eventos = port.listarEventos();
        
        // Procesar respuesta
        request.setAttribute("eventos", eventos.getItem());
        request.getRequestDispatcher("consultaEvento.jsp").forward(request, response);
    }
}
```

---

## Patrones de Diseño Utilizados

### Iteración 1
- **MVC**: Separación de presentación, lógica y modelo
- **Singleton**: Manejadores (ManejadorUsuario, ManejadorEventos)
- **Factory**: Creación de objetos de dominio

### Iteración 2
- **MVC Web**: Servlets (Controller), JSP (View), DTOs (Model)
- **Front Controller**: Servlets como puntos de entrada
- **DTO (Data Transfer Object)**: Para transferencia entre capas
- **Session Façade**: HttpSession para gestión de estado

### Iteración 3
- **SOA (Service-Oriented Architecture)**: Servicios SOAP independientes
- **Repository**: ManejadorPersistencia para JPA
- **DAO**: Acceso a datos con EntityManager
- **Proxy**: Clientes JAX-WS actúan como proxies remotos
- **Factory**: ObjectFactory para crear DTOs SOAP
- **Singleton**: Manejadores en Servidor Central
- **Façade**: Publicadores exponen interfaz simplificada

---

## Ventajas y Desventajas de Cada Arquitectura

| Aspecto | Iteración 1 | Iteración 2 | Iteración 3 |
|---------|-------------|-------------|-------------|
| **Complejidad** | Baja | Media | Alta |
| **Escalabilidad** | ❌ No | ⚠️ Limitada | ✅ Alta |
| **Mantenibilidad** | Media | Alta | Muy Alta |
| **Testabilidad** | Baja | Media | Alta |
| **Distribución** | ❌ No | ❌ No | ✅ Sí |
| **Persistencia** | Memoria | Sesiones | Base de datos |
| **Multiusuario** | ❌ No | ⚠️ Limitado | ✅ Sí |
| **Reutilización** | Baja | Media | Alta |

---

## Evolución Tecnológica

```
Iteración 1               Iteración 2              Iteración 3
───────────              ───────────              ───────────
Java SE                  Jakarta EE               Jakarta EE
Swing                    Servlets + JSP           Servlets + JSP + SOAP
HashMap                  HttpSession              JPA + HSQLDB
Standalone               Tomcat                   Tomcat + Standalone
Monolítico               Web MVC                  SOA Distribuido
```

---

## Conclusión

La arquitectura de EventosUy evolucionó desde una aplicación monolítica de escritorio hasta un sistema distribuido basado en servicios web, demostrando la progresión natural de un proyecto académico que abarca múltiples paradigmas de desarrollo de software:

1. **Iteración 1**: Fundamentos de POO y GUI
2. **Iteración 2**: Desarrollo web y arquitectura MVC
3. **Iteración 3**: Servicios distribuidos, persistencia y arquitectura empresarial

Esta progresión refleja la evolución real de sistemas empresariales modernos y las mejores prácticas de ingeniería de software.

---

**Documentación mantenida por:** Equipo EventosUy  
**Facultad de Ingeniería - UdelaR**  
**Asignatura:** Taller de Programación
