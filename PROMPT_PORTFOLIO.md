# PROMPT PARA IA - DOCUMENTACIÓN DE PROYECTO PORTFOLIO

Genera una descripción completa y profesional del siguiente proyecto para incluir en mi portfolio web. Debe ser atractiva visualmente, destacar habilidades técnicas y mostrar la complejidad del proyecto.

---

## INFORMACIÓN DEL PROYECTO

**Nombre:** EventosUy - Sistema de Gestión de Eventos

**Contexto:** Proyecto académico desarrollado en la asignatura "Taller de Programación" de la Facultad de Ingeniería - Universidad de la República (UdelaR), Uruguay.

**Equipo:** 5 desarrolladores

**Duración:** 3 iteraciones (aproximadamente 6 meses)

**Objetivo:** Desarrollar un sistema completo de gestión de eventos que evolucione desde una aplicación desktop monolítica hasta una arquitectura distribuida basada en servicios web.

---

## DESCRIPCIÓN GENERAL

EventosUy es un sistema integral para la gestión de eventos que permite:
- Registro y gestión de usuarios (Asistentes y Organizadores)
- Creación y administración de eventos con múltiples ediciones
- Sistema de registro a ediciones con tipos de registro personalizables
- Gestión de patrocinios e instituciones
- Generación de constancias de asistencia en PDF
- Persistencia de datos con base de datos embebida
- Arquitectura distribuida con web services SOAP

---

## EVOLUCIÓN DEL PROYECTO (3 ITERACIONES)

### ITERACIÓN 1: Aplicación de Escritorio (Desktop)
**Tecnologías:**
- Java 17
- Java Swing (GUI)
- JGoodies Forms (Layout Manager)

**Arquitectura:**
- Patrón MVC (Model-View-Controller) monolítico
- Aplicación standalone ejecutable
- Sin persistencia (datos en memoria)

**Componentes:**
- **Vista:** JFrame principal con JInternalFrames (MDI - Multiple Document Interface)
- **Controlador:** Factory pattern para gestión de casos de uso
- **Modelo:** Clases de dominio (Usuario, Evento, Edición, Registro, etc.)
- **Manejadores:** Singleton pattern para gestión de entidades en colecciones (HashMap)

**Características:**
- Interfaz gráfica completa con formularios para todos los casos de uso
- Herencia (Usuario → Asistente/Organizador)
- Manejo de excepciones personalizadas
- Validaciones de negocio
- Gestión de imágenes de perfil

---

### ITERACIÓN 2: Aplicación Web
**Tecnologías:**
- Java 17
- Jakarta Servlets 6.0
- Jakarta JSP 3.1
- JSTL 3.0 (Jakarta Standard Tag Library)
- HTML5
- CSS3
- JavaScript (ES6+)
- Apache Tomcat 10.1

**Arquitectura:**
- Patrón MVC Web (Model-View-Controller)
- Aplicación web desplegada en servidor Tomcat
- Gestión de sesiones HTTP

**Componentes:**
- **Frontend:** HTML5, CSS3, JavaScript con diseño responsive
- **Backend:** Servlets como controladores HTTP
- **Vistas:** JSP con JSTL para renderizado dinámico
- **Filtros:** Interceptores de peticiones HTTP (ej: ContadorVisitasFilter)
- **DTOs:** Data Transfer Objects para comunicación entre capas
- **Sesiones:** HttpSession para gestión de estado de usuario

**Características:**
- Vistas diferenciadas por roles (Visitante, Asistente, Organizador)
- Navegación con sidebar
- Sistema de autenticación y autorización
- Formularios con validación cliente y servidor
- Diseño responsive con CSS Flexbox

**Servlets principales:**
- LoginServlet, SignupServlet, LogoutServlet
- ConsultaEventoServlet, ConsultaEdicionServlet
- RegistroAEdicionServlet, MisRegistrosServlet
- PerfilUsuarioServlet, ModificarUsuarioServlet
- Y más de 15 servlets adicionales

---

### ITERACIÓN 3: Arquitectura Distribuida con Web Services SOAP
**Tecnologías Backend:**
- Java 17
- JAX-WS 4.0.2 (Jakarta XML Web Services)
- JPA 3.1.0 (Jakarta Persistence API)
- EclipseLink 4.0.2 (Implementación JPA)
- HSQLDB 2.7.2 (Base de datos embebida)
- Apache Maven 3.x (Gestión de dependencias)
- iText 7/8 (Generación de PDFs)

**Tecnologías Frontend:**
- Jakarta Servlets 6.0
- Jakarta JSP 3.1
- JSTL 3.0
- HTML5, CSS3, JavaScript
- Apache Tomcat 10.1

**Arquitectura:**
- **SOA (Service-Oriented Architecture)**: Arquitectura orientada a servicios
- **Cliente-Servidor distribuido**: Backend standalone + múltiples clientes web
- **SOAP sobre HTTP**: Comunicación basada en estándares XML
- **Persistencia con JPA**: ORM para mapeo objeto-relacional

**Componentes de la Arquitectura:**

1. **Servidor Central (Backend Standalone):**
   - Aplicación Java standalone que expone servicios web SOAP
   - Puerto: 9115
   - **Publicadores SOAP:**
     - PublicadorControlador (servicios generales)
     - PublicadorUsuario (gestión de usuarios)
     - PublicadorRegistro (gestión de registros)
     - PublicadorCargaDatos (carga inicial de datos)
   - **Capa de Lógica:**
     - Controladores (implementan casos de uso)
     - Factory pattern
   - **Manejadores (Singleton):**
     - ManejadorUsuario
     - ManejadorEventos
     - ManejadorPersistencia (JPA)
   - **Persistencia JPA:**
     - EntityManager para gestión de entidades
     - HSQLDB en modo file-based
     - Archivado de ediciones en base de datos
   - **GUI Desktop opcional:** Java Swing para administración

2. **Servidor Web (Cliente SOAP + Web App):**
   - Aplicación web empaquetada como WAR
   - Desplegada en Tomcat (puerto 8080)
   - Consume servicios SOAP del Servidor Central
   - Clientes JAX-WS generados desde WSDL
   - Servlets como capa de presentación web
   - JSP para vistas dinámicas

3. **Cliente Móvil (Web App optimizada):**
   - Aplicación web WAR separada
   - Optimizada para dispositivos móviles
   - También consume servicios SOAP

**Flujo de Comunicación SOAP:**
```
Cliente Web (Navegador)
    ↓ HTTP
Servidor Web (Tomcat:8080)
    ↓ SOAP/HTTP (XML)
Servidor Central (Java Standalone:9115)
    ↓ JPA
Base de Datos HSQLDB (Embedded)
```

**Endpoints SOAP:**
- http://localhost:9115/publicador
- http://localhost:9115/publicadorUsuario
- http://localhost:9115/publicadorRegistro
- http://localhost:9115/publicadorCargaDatos

**Persistencia JPA:**
- Entidades JPA con anotaciones (@Entity, @Id, @OneToMany, etc.)
- persistence.xml para configuración
- EclipseLink como proveedor JPA
- HSQLDB en modo file-based (jdbc:hsqldb:file)
- DDL generation automático
- Transacciones ACID

---

## PATRONES DE DISEÑO UTILIZADOS

### Patrones Arquitectónicos:
- **MVC (Model-View-Controller):** Separación de responsabilidades en todas las iteraciones
- **SOA (Service-Oriented Architecture):** Arquitectura basada en servicios (Iteración 3)
- **Cliente-Servidor:** Comunicación distribuida (Iteración 3)
- **Layered Architecture:** Capas bien definidas (Presentación, Lógica, Persistencia)

### Patrones de Diseño:
- **Singleton:** Manejadores de entidades (ManejadorUsuario, ManejadorEventos, etc.)
- **Factory:** Creación de controladores e instancias de lógica
- **DAO (Data Access Object):** Acceso a datos con EntityManager
- **DTO (Data Transfer Object):** Transferencia de datos entre capas y servicios SOAP
- **Repository:** ManejadorPersistencia para abstracción de persistencia
- **Proxy:** Clientes JAX-WS actúan como proxies remotos
- **Front Controller:** Servlets como puntos de entrada HTTP
- **Session Façade:** HttpSession para gestión de estado
- **Façade:** Publicadores SOAP exponen interfaz simplificada

---

## STACK TECNOLÓGICO COMPLETO

### Lenguajes:
- Java 17 (LTS)
- HTML5
- CSS3
- JavaScript (ES6+)
- SQL (HSQLDB dialect)
- XML (SOAP, WSDL, persistence.xml)

### Frameworks y APIs:
- Jakarta EE 9/10 (Servlets 6.0, JSP 3.1, JPA 3.1)
- JAX-WS 4.0.2 (Web Services SOAP)
- JSTL 3.0.0
- JPA 3.1.0
- Java Swing

### Implementaciones:
- EclipseLink 4.0.2 (JPA Provider)
- Metro/JAX-WS RT 4.0.2 (SOAP Implementation)
- GlassFish JSTL 3.0.1

### Base de Datos:
- HSQLDB 2.7.2 (Embedded, File-based)

### Servidor de Aplicaciones:
- Apache Tomcat 10.1

### Build Tools:
- Apache Maven 3.x
- Maven plugins: compiler, war, assembly, jaxws

### Librerías Adicionales:
- iText 7/8 (Generación de PDFs)
- JGoodies Forms/Common (Swing Layout)
- JUnit 5 (Testing)

---

## CASOS DE USO IMPLEMENTADOS

### Gestión de Usuarios:
- Alta de Usuario (Asistente/Organizador)
- Consulta de Usuario
- Modificar Datos de Usuario
- Listar Usuarios
- Autenticación (Login/Logout)

### Gestión de Eventos:
- Alta de Evento
- Consulta de Evento
- Listar Eventos por Categoría
- Eventos Más Visitados (contador)

### Gestión de Ediciones:
- Alta de Edición de Evento
- Consulta de Edición de Evento
- Archivar Edición (con persistencia JPA)
- Consultar Ediciones Archivadas
- Ediciones Organizadas (por usuario)

### Gestión de Registros:
- Alta de Tipo de Registro
- Consulta de Tipo de Registro
- Registro a Edición de Evento
- Consulta de Registro
- Mis Registros (por usuario)
- Descargar Constancia de Asistencia (PDF)
- Consulta de Registros de Edición (para organizadores)

### Gestión de Patrocinios:
- Alta de Institución
- Alta de Patrocinio
- Consulta de Patrocinio

### Administración:
- Carga de Datos de Prueba
- Inspección de Base de Datos

---

## MODELO DE DOMINIO

### Entidades Principales:
- **Usuario (abstracta):**
  - Subclases: Asistente, Organizador
  - Atributos: nickname, nombre, apellido, email, fechaNacimiento, imagen
  
- **Evento:**
  - nombre, fechaInicio, fechaFin, descripción
  - Relaciones: List<Edicion>, List<Categoria>
  
- **Edicion:**
  - nombre, fechaInicio, fechaFin, lugarFisico, estado (Enum)
  - Relaciones: Evento, Organizador, List<TipoDeRegistro>, List<Registro>, List<Patrocinio>
  
- **Registro:**
  - fechaRegistro, costoRegistro
  - Relaciones: Asistente, Edicion, TipoDeRegistro
  
- **TipoDeRegistro:**
  - nombre, descripción, fechas de vigencia, descuento, costo
  
- **Patrocinio:**
  - fecha, costo
  - Relaciones: Institucion, Edicion
  
- **Institucion:**
  - nombre, descripción, url
  
- **Categoria (Enum):**
  - DEPORTE, CHARLA, ESPECTACULO, COMIDA, INNOVACION

### Relaciones:
- Usuario 1:N Registro
- Evento 1:N Edicion
- Edicion 1:N Registro
- Edicion 1:N TipoDeRegistro
- Edicion N:M Institucion (through Patrocinio)
- Evento N:M Categoria

---

## CARACTERÍSTICAS TÉCNICAS DESTACADAS

### Persistencia:
- JPA con EclipseLink
- HSQLDB embebida (mode: file)
- Mapeo objeto-relacional con anotaciones
- Gestión de transacciones
- DDL generation automático
- Consultas JPQL

### Web Services SOAP:
- Publicación de servicios con @WebService
- Generación automática de WSDL
- Clientes generados con wsimport (Maven plugin)
- Serialización XML automática
- Manejo de excepciones SOAP

### Seguridad:
- Autenticación basada en sesiones HTTP
- Filtros para control de acceso
- Validación de entrada en cliente y servidor
- Protección de vistas por rol

### Generación de PDFs:
- iText para constancias de asistencia
- Generación dinámica con datos del usuario
- Descarga directa desde servlet

### Scripts de Deployment:
- compilar.bat/sh: Compilación automatizada de los 3 módulos
- sql.bat/sh: Cliente SQL para HSQLDB
- abrir-puertos-firewall.bat: Configuración de firewall
- run-gui.bat/ps1: Inicio del servidor con GUI

---

## ESTRUCTURA DEL PROYECTO

```
eventosuy/
├── iteracion-1-logica-escritorio/    # Iteración 1: Desktop
├── iteracion-2-web-servlets-jsp/     # Iteración 2: Web
├── iteracion-3-webservices-soap/     # Iteración 3: SOAP + JPA
│   ├── servidor-central/             # Backend SOAP + JPA
│   ├── servidor-web/                 # Cliente web (WAR)
│   └── mobile-client/                # Cliente móvil (WAR)
├── docs/                             # Documentación técnica
├── scripts-deployment/               # Scripts de compilación
└── README.md
```

---

## COMPETENCIAS TÉCNICAS DEMOSTRADAS

### Programación:
- Java avanzado (OOP, Generics, Lambdas, Streams)
- Diseño orientado a objetos
- Patrones de diseño
- Manejo de excepciones
- Testing unitario (JUnit)

### Desarrollo Web:
- Servlets y JSP
- HTML5, CSS3, JavaScript
- AJAX/Fetch (limitado)
- Responsive design
- Gestión de sesiones HTTP

### Arquitectura de Software:
- Evolución de arquitectura monolítica a distribuida
- Separación de capas
- Diseño de APIs
- SOA (Service-Oriented Architecture)
- Cliente-Servidor

### Persistencia:
- JPA y ORM
- Diseño de base de datos
- Transacciones
- SQL

### Web Services:
- SOAP/XML
- WSDL
- JAX-WS
- Generación de clientes
- Comunicación distribuida

### Herramientas:
- Maven (gestión de dependencias, build)
- Git (control de versiones)
- Tomcat (deployment)
- IDEs (Eclipse, VS Code)

### Soft Skills:
- Trabajo en equipo (5 personas)
- Desarrollo iterativo
- Documentación técnica
- Resolución de problemas

---

## DESAFÍOS TÉCNICOS SUPERADOS

1. **Migración de arquitectura:** De monolítico a distribuido manteniendo compatibilidad
2. **Integración JPA:** Configuración de persistence.xml, manejo de EntityManager
3. **SOAP/XML:** Generación y consumo de servicios web
4. **Generación de clientes JAX-WS:** Integración de wsimport en Maven
5. **HSQLDB file-based:** Gestión de locks, configuración de conexiones
6. **Deployment multi-módulo:** Coordinación de servidor central + clientes web
7. **Sesiones HTTP:** Gestión de estado distribuido
8. **PDFs dinámicos:** Integración de iText para constancias

---

## RESULTADOS Y LOGROS

✅ Sistema completo funcional en 3 iteraciones  
✅ Más de 20 casos de uso implementados  
✅ Persistencia real con JPA + HSQLDB  
✅ Arquitectura distribuida con SOAP  
✅ 3 clientes diferentes (Desktop, Web, Mobile)  
✅ Documentación técnica completa  
✅ Scripts de deployment automatizados  
✅ Código limpio y bien estructurado  
✅ Patrones de diseño aplicados correctamente  
✅ Proyecto académico exitoso con calificación destacada  

---

## ENLACES

- **Repositorio GitHub:** [Incluir URL]
- **Documentación completa:** Ver carpeta /docs
- **Arquitectura detallada:** docs/ARQUITECTURA.md
- **Guía de instalación:** docs/INSTALACION.md
- **Stack tecnológico:** docs/TECNOLOGIAS.md

---

## INSTRUCCIONES PARA LA IA

Por favor, genera:

1. **Una descripción atractiva del proyecto** para la página principal de mi portfolio, destacando:
   - La evolución del proyecto (3 iteraciones)
   - Las tecnologías más relevantes (Java 17, Jakarta EE, SOAP, JPA, Tomcat)
   - La arquitectura distribuida
   - El contexto académico (UdelaR)

2. **Una sección técnica detallada** que incluya:
   - Stack tecnológico con iconos/badges si es posible
   - Diagrama de arquitectura (descripción para que yo pueda crearlo)
   - Patrones de diseño utilizados
   - Características destacadas

3. **Sección de capturas/demos** (descripción de qué mostrar):
   - GUI Desktop (Iteración 1)
   - Vista web (Iteración 2)
   - Diagrama de arquitectura SOAP (Iteración 3)
   - Ejemplo de WSDL
   - Base de datos

4. **Highlights/Logros** en formato bullet points atractivo

5. **Sección de "Lo que aprendí"** destacando competencias técnicas

El tono debe ser profesional pero accesible, destacando tanto la complejidad técnica como el valor del proyecto académico. Debe impresionar a reclutadores técnicos y demostrar conocimiento profundo de Java EE, arquitecturas distribuidas y desarrollo full-stack.

Formatea el contenido en Markdown o HTML según prefieras, listo para copiar-pegar en mi portfolio.
