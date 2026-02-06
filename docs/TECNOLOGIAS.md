# Tecnologías Utilizadas - EventosUy

## 📚 Stack Tecnológico Completo

Este documento detalla todas las tecnologías, frameworks, librerías y herramientas utilizadas en el desarrollo de EventosUy a través de sus 3 iteraciones.

---

## Lenguajes de Programación

### Java
- **Versión:** Java 17 (LTS)
- **Uso:** Backend completo en las 3 iteraciones
- **Características utilizadas:**
  - Streams API
  - Lambda expressions
  - Try-with-resources
  - Generics
  - Annotations
  - Reflection (JPA)

### HTML5
- **Uso:** Frontend en iteraciones 2 y 3
- **Características:**
  - Semántica (`<header>`, `<nav>`, `<section>`, `<article>`)
  - Formularios (`<form>`, `<input>`, `<select>`)
  - Multimedia básica

### CSS3
- **Uso:** Estilos en iteraciones 2 y 3
- **Características:**
  - Flexbox
  - Grid (limitado)
  - Media queries (responsive)
  - Transiciones y animaciones
  - Variables CSS

### JavaScript
- **Versión:** ES6+
- **Uso:** Interactividad frontend
- **Características:**
  - DOM manipulation
  - Event handling
  - AJAX/Fetch (limitado)
  - Form validation

### SQL
- **Uso:** Base de datos HSQLDB
- **Dialecto:** SQL estándar (ANSI SQL)

---

## Frameworks y Especificaciones Java EE/Jakarta EE

### Jakarta Servlets
- **Versión:** 6.0.0
- **Uso:** Controladores HTTP en iteraciones 2 y 3
- **Package:** `jakarta.servlet.*`
- **Características:**
  - `@WebServlet` annotations
  - `HttpServletRequest` / `HttpServletResponse`
  - Session management (`HttpSession`)
  - Request dispatching
  - Filtros (`Filter`)

**Ejemplo:**
```java
@WebServlet("/consultaEvento")
public class ConsultaEventoServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        // Lógica del servlet
    }
}
```

### Jakarta JSP (JavaServer Pages)
- **Versión:** 3.1.1
- **Uso:** Vistas dinámicas en iteraciones 2 y 3
- **Package:** `jakarta.servlet.jsp.*`
- **Características:**
  - Scriptlets `<% ... %>`
  - Expresiones `<%= ... %>`
  - Directivas `<%@ ... %>`
  - EL (Expression Language) `${...}`

### JSTL (Jakarta Standard Tag Library)
- **Versión:** 3.0.0
- **Uso:** Lógica en vistas JSP
- **Implementación:** GlassFish 3.0.1
- **Tags utilizados:**
  - `<c:forEach>` - Iteración
  - `<c:if>` / `<c:choose>` - Condicionales
  - `<c:out>` - Output seguro
  - `<c:set>` - Variables
  - `<c:url>` - URLs
  - `<fmt:formatDate>` - Formateo

**Ejemplo:**
```jsp
<c:forEach items="${eventos}" var="evento">
    <div class="evento">
        <h3><c:out value="${evento.nombre}"/></h3>
    </div>
</c:forEach>
```

### JAX-WS (Jakarta XML Web Services)
- **Versión:** 4.0.2
- **Implementación:** Metro (com.sun.xml.ws:jaxws-rt)
- **Uso:** Publicación y consumo de servicios SOAP en iteración 3
- **Annotations:**
  - `@WebService` - Definir servicio
  - `@WebMethod` - Exponer método
  - `@WebParam` - Parámetros
  - `@WebResult` - Resultado

**Servidor:**
```java
@WebService(serviceName = "PublicadorUsuario")
public class PublicadorUsuario {
    @WebMethod
    public DTUsuario obtenerUsuario(@WebParam(name = "nickname") String nickname) {
        // Implementación
    }
}
```

**Cliente:**
```java
PublicadorUsuarioService service = new PublicadorUsuarioService();
PublicadorUsuario port = service.getPublicadorUsuarioPort();
DTUsuario usuario = port.obtenerUsuario("user1");
```

### JPA (Jakarta Persistence API)
- **Versión:** 3.1.0
- **Uso:** ORM en iteración 3
- **Package:** `jakarta.persistence.*`
- **Annotations:**
  - `@Entity` - Entidad JPA
  - `@Id` - Clave primaria
  - `@GeneratedValue` - Auto-generación
  - `@OneToMany` / `@ManyToOne` - Relaciones
  - `@Temporal` - Fechas
  - `@Transient` - Excluir campos

**Ejemplo:**
```java
@Entity
public class Usuario {
    @Id
    private String nickname;
    
    private String nombre;
    
    @OneToMany(mappedBy = "usuario")
    private List<Registro> registros;
}
```

---

## Implementaciones y Librerías

### EclipseLink
- **Versión:** 4.0.2
- **Uso:** Implementación JPA
- **Características:**
  - Proveedor JPA
  - DDL generation
  - Lazy loading
  - Caching
  - Logging

### HSQLDB (HyperSQL Database)
- **Versión:** 2.7.2
- **Modo:** File-based embedded
- **Uso:** Persistencia en iteración 3
- **Características:**
  - Base de datos embebida
  - SQL estándar
  - Transacciones ACID
  - Índices
  - Triggers

**Configuración:**
```xml
<property name="jakarta.persistence.jdbc.url" 
          value="jdbc:hsqldb:file:./data/edicionesArchivadas"/>
<property name="jakarta.persistence.jdbc.driver" 
          value="org.hsqldb.jdbcDriver"/>
```

### iText
- **Versión:** 7.2.5 (servidor-web), 8.0.3 (servidor-central)
- **Uso:** Generación de PDFs (constancias de asistencia)
- **Package:** `com.itextpdf.*`

**Ejemplo:**
```java
PdfWriter writer = new PdfWriter(outputStream);
PdfDocument pdf = new PdfDocument(writer);
Document document = new Document(pdf);
document.add(new Paragraph("Constancia de Asistencia"));
document.close();
```

### JGoodies Forms
- **Versión:** 1.9.0
- **Uso:** Layout manager para Swing (iteración 1)
- **Package:** `com.jgoodies.forms.*`

### JGoodies Common
- **Versión:** 1.8.1
- **Uso:** Utilidades complementarias para Swing

---

## Servidores y Contenedores

### Apache Tomcat
- **Versión:** 10.1.x
- **Uso:** Servidor de aplicaciones para iteraciones 2 y 3
- **Características:**
  - Servlet Container
  - JSP Engine
  - HTTP Server
  - Manager App
  - Connector (HTTP/1.1)

**Configuración típica:**
- Puerto HTTP: 8080
- Puerto Shutdown: 8005
- Puerto AJP: 8009

### Endpoint (JAX-WS)
- **Uso:** Publicador de servicios SOAP standalone
- **Puerto:** 9115
- **Características:**
  - Lightweight HTTP server
  - Embedded en Servidor Central

**Publicación:**
```java
String address = "http://0.0.0.0:9115/publicador";
Endpoint.publish(address, new PublicadorControlador());
```

---

## Herramientas de Build

### Apache Maven
- **Versión:** 3.6+
- **Uso:** Gestión de dependencias y build en iteración 3
- **Archivos:** `pom.xml`
- **Plugins utilizados:**

#### maven-compiler-plugin
```xml
<maven.compiler.source>17</maven.compiler.source>
<maven.compiler.target>17</maven.compiler.target>
```

#### maven-war-plugin
- **Versión:** 3.4.0
- **Uso:** Empaquetado WAR para servidor-web y mobile

#### maven-assembly-plugin
- **Versión:** 3.6.0
- **Uso:** JAR ejecutable con dependencias para servidor-central
```xml
<finalName>servidor</finalName>
<descriptorRef>jar-with-dependencies</descriptorRef>
```

#### jaxws-maven-plugin
- **Versión:** 4.0.2
- **Uso:** Generación de clientes SOAP desde WSDL
```bash
mvn jaxws:wsimport -Dwsimport=true
```

---

## Protocolos y Estándares

### HTTP/1.1
- **Uso:** Comunicación cliente-servidor
- **Métodos:** GET, POST
- **Status codes:** 200, 302, 404, 500

### SOAP (Simple Object Access Protocol)
- **Versión:** 1.2
- **Uso:** Comunicación entre clientes y Servidor Central
- **Transporte:** HTTP
- **Serialización:** XML

**Estructura de mensaje:**
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <ns:obtenerUsuario xmlns:ns="http://publicadores/">
      <nickname>user1</nickname>
    </ns:obtenerUsuario>
  </soap:Body>
</soap:Envelope>
```

### WSDL (Web Services Description Language)
- **Versión:** 1.1
- **Uso:** Descripción de servicios SOAP
- **URL ejemplo:** http://localhost:9115/publicador?wsdl

### XSD (XML Schema Definition)
- **Uso:** Definición de tipos de datos en WSDL

### JDBC (Java Database Connectivity)
- **Versión:** Incluida en JDK
- **Uso:** Conexión a HSQLDB

---

## Testing

### JUnit Jupiter
- **Versión:** 5.10.2
- **Uso:** Testing unitario en servidor-central
- **Package:** `org.junit.jupiter.*`
- **Scope:** test

```java
@Test
void testCrearUsuario() {
    Usuario u = new Usuario("test", "Test User");
    assertNotNull(u);
    assertEquals("test", u.getNickname());
}
```

---

## IDEs y Herramientas de Desarrollo

### Eclipse IDE
- Evidenciado por archivos `.project`, `.classpath`
- Configuración en `.settings/`

### Visual Studio Code
- Configuración en `.vscode/`

### Git
- Control de versiones
- Archivo `.gitignore` presente

---

## Dependencias por Módulo

### Servidor Central (servidor-central)

```xml
<dependencies>
    <!-- Web Services SOAP -->
    <dependency>
        <groupId>com.sun.xml.ws</groupId>
        <artifactId>jaxws-rt</artifactId>
        <version>4.0.2</version>
    </dependency>
    
    <!-- JPA -->
    <dependency>
        <groupId>jakarta.persistence</groupId>
        <artifactId>jakarta.persistence-api</artifactId>
        <version>3.1.0</version>
    </dependency>
    
    <!-- EclipseLink (JPA Provider) -->
    <dependency>
        <groupId>org.eclipse.persistence</groupId>
        <artifactId>eclipselink</artifactId>
        <version>4.0.2</version>
    </dependency>
    
    <!-- HSQLDB -->
    <dependency>
        <groupId>org.hsqldb</groupId>
        <artifactId>hsqldb</artifactId>
        <version>2.7.2</version>
    </dependency>
    
    <!-- iText PDF -->
    <dependency>
        <groupId>com.itextpdf</groupId>
        <artifactId>itext7-core</artifactId>
        <version>8.0.3</version>
        <type>pom</type>
    </dependency>
    
    <!-- Servlet API (utils) -->
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>6.0.0</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- JUnit -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Servidor Web (servidor-web)

```xml
<dependencies>
    <!-- Servlets -->
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>6.0.0</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- JSP -->
    <dependency>
        <groupId>jakarta.servlet.jsp</groupId>
        <artifactId>jakarta.servlet.jsp-api</artifactId>
        <version>3.1.1</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- JSTL API -->
    <dependency>
        <groupId>jakarta.servlet.jsp.jstl</groupId>
        <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
        <version>3.0.0</version>
    </dependency>
    
    <!-- JSTL Implementation -->
    <dependency>
        <groupId>org.glassfish.web</groupId>
        <artifactId>jakarta.servlet.jsp.jstl</artifactId>
        <version>3.0.1</version>
    </dependency>
    
    <!-- JAX-WS (cliente SOAP) -->
    <dependency>
        <groupId>com.sun.xml.ws</groupId>
        <artifactId>jaxws-rt</artifactId>
        <version>4.0.2</version>
    </dependency>
    
    <!-- iText PDF -->
    <dependency>
        <groupId>com.itextpdf</groupId>
        <artifactId>itext7-core</artifactId>
        <version>7.2.5</version>
        <type>pom</type>
    </dependency>
</dependencies>
```

### Mobile Client (mobile)

```xml
<dependencies>
    <!-- Servlet API -->
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>6.0.0</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- JSP API -->
    <dependency>
        <groupId>jakarta.servlet.jsp</groupId>
        <artifactId>jakarta.servlet.jsp-api</artifactId>
        <version>3.1.0</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- JAX-WS runtime -->
    <dependency>
        <groupId>com.sun.xml.ws</groupId>
        <artifactId>jaxws-rt</artifactId>
        <version>4.0.2</version>
    </dependency>
</dependencies>
```

---

## Versionado de Dependencias

### Java Platform
- **Java SE:** 17 (LTS - Long Term Support)
- **Jakarta EE:** 9.1 / 10 (transición de javax.* a jakarta.*)

### Compatibilidad
- Tomcat 10.1 → Jakarta EE 9.1+
- Java 17 → Compatible con todas las librerías

---

## Configuración de Encoding

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

---

## Resumen por Iteración

### Iteración 1
- Java SE 17
- Java Swing
- JGoodies Forms/Common
- Ninguna dependencia externa adicional

### Iteración 2
- Java SE 17
- Jakarta Servlets 6.0
- Jakarta JSP 3.1
- JSTL 3.0
- HTML5, CSS3, JavaScript
- Apache Tomcat 10.1

### Iteración 3
- Todo de Iteración 2, más:
- JAX-WS 4.0
- JPA 3.1
- EclipseLink 4.0
- HSQLDB 2.7.2
- iText 7/8
- Apache Maven 3.x
- JUnit 5

---

## Stack Completo (Diagrama)

```
┌─────────────────────────────────────────────────┐
│              Frontend Layer                      │
├─────────────────────────────────────────────────┤
│  HTML5 │ CSS3 │ JavaScript │ JSP │ JSTL         │
└─────────────────────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────┐
│            Web/Servlet Layer                     │
├─────────────────────────────────────────────────┤
│  Jakarta Servlets 6.0 │ Apache Tomcat 10.1      │
└─────────────────────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────┐
│           Service Layer (SOAP)                   │
├─────────────────────────────────────────────────┤
│  JAX-WS 4.0 │ SOAP 1.2 │ WSDL │ HTTP            │
└─────────────────────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────┐
│            Business Logic Layer                  │
├─────────────────────────────────────────────────┤
│  Java 17 │ POJOs │ Controladores │ Manejadores  │
└─────────────────────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────┐
│           Persistence Layer (JPA)                │
├─────────────────────────────────────────────────┤
│  JPA 3.1 │ EclipseLink 4.0 │ JDBC               │
└─────────────────────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────┐
│              Database Layer                      │
├─────────────────────────────────────────────────┤
│  HSQLDB 2.7.2 (Embedded, File-based)            │
└─────────────────────────────────────────────────┘
```

---

## Librerías Adicionales

### Utilidades
- **Java Time API** (java.time.*) - Manejo de fechas
- **Java Collections** (java.util.*) - Estructuras de datos
- **Java Streams** - Procesamiento funcional
- **Java I/O / NIO** - Entrada/salida

### Logging (implícito)
- System.out / System.err (básico)
- EclipseLink logging (JPA)

---

## Conclusión

El proyecto EventosUy utiliza un stack tecnológico moderno y profesional, basado en:

✅ **Java 17** - Versión LTS moderna  
✅ **Jakarta EE 9/10** - Estándares empresariales  
✅ **Maven** - Gestión profesional de dependencias  
✅ **SOAP/JAX-WS** - Interoperabilidad de servicios  
✅ **JPA** - ORM estándar de la industria  
✅ **Tomcat** - Servidor robusto y ampliamente usado  

Este stack es representativo de sistemas empresariales Java modernos y demuestra competencia en tecnologías relevantes para el mercado laboral.

---

**Mantenido por:** Equipo EventosUy  
**Facultad de Ingeniería - UdelaR**
