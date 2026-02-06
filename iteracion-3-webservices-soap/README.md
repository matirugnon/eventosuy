# Iteración 3 - Web Services SOAP y Arquitectura Distribuida

## 📋 Descripción

La **Iteración 3** representa la evolución final de EventosUy hacia una **arquitectura distribuida basada en servicios web SOAP**. Esta iteración separa completamente el backend (Servidor Central con lógica y persistencia) de los clientes (aplicaciones web y móvil) que consumen servicios remotos.

---

## 🎯 Objetivos

- Implementar arquitectura SOA (Service-Oriented Architecture)
- Publicar servicios web SOAP con JAX-WS
- Separar completamente frontend y backend
- Implementar persistencia real con JPA y HSQLDB
- Crear clientes web y móvil que consuman servicios SOAP
- Desarrollar scripts de deployment automatizados

---

## 🏗️ Arquitectura Distribuida

```
┌────────────────────────────────────────────────────────┐
│                  CAPA DE CLIENTES                      │
├────────────────────────────────────────────────────────┤
│                                                        │
│  ┌──────────────┐  ┌──────────────┐  ┌─────────────┐ │
│  │ Cliente Web  │  │Cliente Móvil │  │Desktop GUI  │ │
│  │  (Tomcat)    │  │  (Tomcat)    │  │(Java Swing) │ │
│  │  :8080/web   │  │:8080/mobile  │  │  Standalone │ │
│  └──────┬───────┘  └──────┬───────┘  └──────┬──────┘ │
│         │                 │                  │        │
└─────────┼─────────────────┼──────────────────┼────────┘
          │                 │                  │
          │   SOAP/HTTP     │   SOAP/HTTP      │ Directo
          │   JAX-WS        │   JAX-WS         │
          │                 │                  │
┌─────────▼─────────────────▼──────────────────▼────────┐
│              SERVIDOR CENTRAL (Backend)               │
├───────────────────────────────────────────────────────┤
│                                                       │
│  ┌─────────────────────────────────────────────────┐ │
│  │         PUBLICADORES SOAP (:9115)               │ │
│  │  ┌──────────────────┐  ┌──────────────────┐    │ │
│  │  │ Publicador       │  │ Publicador       │    │ │
│  │  │ Controlador      │  │ Usuario          │ .. │ │
│  │  │ /publicador      │  │ /publicadorUsuario   │ │
│  │  └────────┬─────────┘  └────────┬─────────┘    │ │
│  └───────────┼────────────────────┼────────────────┘ │
│              │                    │                  │
│  ┌───────────▼────────────────────▼────────────────┐ │
│  │             CAPA DE LÓGICA                      │ │
│  │  - Controladores (IControlador*)                │ │
│  │  - Factory Pattern                              │ │
│  └───────────┬─────────────────────────────────────┘ │
│              │                                        │
│  ┌───────────▼─────────────────────────────────────┐ │
│  │      MANEJADORES (Singleton)                    │ │
│  │  - ManejadorUsuario                             │ │
│  │  - ManejadorEventos                             │ │
│  │  - ManejadorPersistencia (JPA)                  │ │
│  └───────────┬─────────────────────────────────────┘ │
│              │                                        │
│  ┌───────────▼─────────────────────────────────────┐ │
│  │       MODELO DE DOMINIO (JPA)                   │ │
│  │  @Entity: Usuario, Evento, Edicion, etc.        │ │
│  └───────────┬─────────────────────────────────────┘ │
│              │ JPA/EclipseLink                       │
│  ┌───────────▼─────────────────────────────────────┐ │
│  │        PERSISTENCIA (EntityManager)             │ │
│  │  persistence.xml                                │ │
│  └───────────┬─────────────────────────────────────┘ │
│              │ JDBC                                   │
└──────────────┼────────────────────────────────────────┘
               │
        ┌──────▼──────┐
        │   HSQLDB    │
        │ (Embedded)  │
        │ File-based  │
        └─────────────┘
```

---

## 📦 Componentes

### 1. Servidor Central (`servidor-central/`)

**Backend standalone con servicios SOAP y persistencia JPA**

```
servidor-central/
├── src/main/java/
│   ├── publicadores/              # Publicadores SOAP
│   │   ├── PublicadorControlador.java
│   │   ├── PublicadorUsuario.java
│   │   ├── PublicadorRegistro.java
│   │   └── PublicadorCargaDatos.java
│   │
│   ├── logica/                    # Lógica de negocio
│   │   ├── Controladores/         # Casos de uso
│   │   ├── manejadores/           # Singletons
│   │   │   ├── ManejadorUsuario.java
│   │   │   ├── ManejadorEventos.java
│   │   │   └── ManejadorPersistencia.java
│   │   ├── DatatypesYEnum/        # DTOs y Enums
│   │   ├── Usuario.java           # @Entity
│   │   ├── Evento.java            # @Entity
│   │   ├── Edicion.java           # @Entity
│   │   └── ...
│   │
│   ├── presentacion/              # GUI Desktop (opcional)
│   │   ├── Main.java
│   │   └── MainFrame.java
│   │
│   ├── gui/internal/              # JInternalFrames
│   │
│   ├── soap/                      # Clases generadas SOAP
│   │   ├── DTUsuario.java
│   │   ├── DTEvento.java
│   │   └── ...
│   │
│   ├── utils/                     # Utilidades
│   │
│   ├── excepciones/               # Excepciones personalizadas
│   │
│   └── ServidorCentralMain.java  # Main para servicios SOAP
│
├── src/main/resources/
│   └── META-INF/
│       └── persistence.xml        # Configuración JPA
│
├── src/test/                      # Tests JUnit
│
├── data/                          # Base de datos HSQLDB
│   ├── edicionesArchivadas.properties
│   ├── edicionesArchivadas.script
│   └── edicionesArchivadas.lck (runtime)
│
├── target/
│   └── servidor.jar               # JAR ejecutable
│
├── pom.xml                        # Configuración Maven
├── run-gui.bat                    # Iniciar con GUI (Windows)
└── run-gui.ps1                    # Iniciar con GUI (PowerShell)
```

#### Publicadores SOAP

**PublicadorControlador.java:**
```java
@WebService(serviceName = "PublicadorControlador")
public class PublicadorControlador {
    
    @WebMethod
    public StringArray listarEventos() {
        IControladorEvento ctrl = Factory.getInstance().getIControladorEvento();
        Collection<String> eventos = ctrl.listarEventos();
        
        StringArray result = new StringArray();
        result.getItem().addAll(eventos);
        return result;
    }
    
    @WebMethod
    public DTEvento obtenerEvento(@WebParam(name = "nombre") String nombre) 
            throws EventoNoExisteException_Exception {
        IControladorEvento ctrl = Factory.getInstance().getIControladorEvento();
        return ctrl.obtenerDtEvento(nombre);
    }
    
    // Más métodos...
    
    public static void main(String[] args) {
        String address = "http://0.0.0.0:9115/publicador";
        Endpoint.publish(address, new PublicadorControlador());
        System.out.println("Publicador Controlador publicado en: " + address);
    }
}
```

**PublicadorUsuario.java:**
```java
@WebService(serviceName = "PublicadorUsuario")
public class PublicadorUsuario {
    
    @WebMethod
    public DTUsuario obtenerUsuario(@WebParam(name = "nickname") String nickname) 
            throws UsuarioNoExisteException_Exception {
        IControladorUsuario ctrl = Factory.getInstance().getIControladorUsuario();
        return ctrl.obtenerDtUsuario(nickname);
    }
    
    @WebMethod
    public void altaUsuario(@WebParam(name = "usuario") DTUsuario usuario) 
            throws UsuarioRepetidoException_Exception {
        IControladorUsuario ctrl = Factory.getInstance().getIControladorUsuario();
        ctrl.altaUsuario(usuario);
    }
    
    // Más métodos...
}
```

**Endpoints disponibles:**
- `http://localhost:9115/publicador` - Servicios generales
- `http://localhost:9115/publicadorUsuario` - Gestión de usuarios
- `http://localhost:9115/publicadorRegistro` - Gestión de registros
- `http://localhost:9115/publicadorCargaDatos` - Carga inicial de datos

#### Persistencia JPA

**persistence.xml:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="3.0" xmlns="https://jakarta.ee/xml/ns/persistence">
    
    <persistence-unit name="EdicionesArchivadas">
        <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
        
        <class>logica.EdicionArchivada</class>
        
        <properties>
            <!-- HSQLDB File-based -->
            <property name="jakarta.persistence.jdbc.url" 
                      value="jdbc:hsqldb:file:./data/edicionesArchivadas"/>
            <property name="jakarta.persistence.jdbc.user" value="SA"/>
            <property name="jakarta.persistence.jdbc.password" value=""/>
            <property name="jakarta.persistence.jdbc.driver" 
                      value="org.hsqldb.jdbcDriver"/>
            
            <!-- DDL Generation -->
            <property name="eclipselink.ddl-generation" 
                      value="create-or-extend-tables"/>
            <property name="eclipselink.ddl-generation.output-mode" 
                      value="database"/>
            
            <!-- Logging -->
            <property name="eclipselink.logging.level" value="FINE"/>
            <property name="eclipselink.logging.parameters" value="true"/>
        </properties>
    </persistence-unit>
    
</persistence>
```

**ManejadorPersistencia.java:**
```java
public class ManejadorPersistencia {
    private static ManejadorPersistencia instancia = null;
    private EntityManagerFactory emf;
    
    private ManejadorPersistencia() {
        emf = Persistence.createEntityManagerFactory("EdicionesArchivadas");
    }
    
    public static ManejadorPersistencia getInstance() {
        if (instancia == null) {
            instancia = new ManejadorPersistencia();
        }
        return instancia;
    }
    
    public void archivarEdicion(EdicionArchivada edicion) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(edicion);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    public List<EdicionArchivada> obtenerEdicionesArchivadas() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT e FROM EdicionArchivada e", 
                                 EdicionArchivada.class).getResultList();
        } finally {
            em.close();
        }
    }
}
```

**Entidad JPA:**
```java
@Entity
@Table(name = "EDICIONES_ARCHIVADAS")
public class EdicionArchivada {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombreEdicion;
    private String nombreEvento;
    
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    
    @Lob
    private String descripcion;
    
    // Getters y setters
}
```

#### Compilación y Ejecución

**Compilar:**
```bash
cd servidor-central
mvn clean package
```

**Ejecutar (consola):**
```bash
java -jar target/servidor.jar
```

**Ejecutar (con GUI):**
```bash
# Windows:
run-gui.bat

# PowerShell:
.\run-gui.ps1

# Linux/Mac:
java -cp target/servidor.jar presentacion.Main
```

---

### 2. Servidor Web (`servidor-web/`)

**Cliente SOAP + Servlets + JSP desplegado en Tomcat**

```
servidor-web/
├── src/main/java/
│   ├── servlets/                  # Servlets HTTP
│   │   ├── LoginServlet.java
│   │   ├── ConsultaEventoServlet.java
│   │   ├── RegistroAEdicionServlet.java
│   │   └── ...
│   │
│   ├── servlets/dto/              # DTOs locales
│   │   ├── EventoDTO.java
│   │   └── UsuarioDTO.java
│   │
│   ├── filtros/                   # Filtros HTTP
│   │   └── ContadorVisitasFilter.java
│   │
│   └── soap/                      # Clases generadas de WSDL
│       ├── PublicadorControladorService.java
│       ├── PublicadorControlador.java (port)
│       ├── DTEvento.java
│       └── ...
│
├── src/main/webapp/
│   ├── WEB-INF/
│   │   ├── web.xml                # Descriptor (opcional)
│   │   └── jsp/                   # JSPs privadas
│   │
│   ├── css/
│   │   └── styles.css
│   │
│   ├── js/
│   │   └── main.js
│   │
│   ├── img/
│   │
│   ├── index.jsp                  # Página principal
│   ├── consultaEvento.jsp
│   ├── login.jsp
│   └── ...
│
├── target/
│   └── web.war                    # WAR deployable
│
└── pom.xml                        # Configuración Maven
```

#### Consumo de Servicios SOAP

**En Servlet:**
```java
@WebServlet("/consultaEvento")
public class ConsultaEventoServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nombreEvento = request.getParameter("evento");
        
        try {
            // Crear cliente SOAP
            PublicadorControladorService service = new PublicadorControladorService();
            PublicadorControlador port = service.getPublicadorControladorPort();
            
            // Invocar servicio remoto
            DTEvento evento = port.obtenerEvento(nombreEvento);
            
            // Pasar datos a JSP
            request.setAttribute("evento", evento);
            request.getRequestDispatcher("/consultaEvento.jsp")
                   .forward(request, response);
                   
        } catch (EventoNoExisteException_Exception e) {
            request.setAttribute("error", "Evento no encontrado");
            request.getRequestDispatcher("/error.jsp")
                   .forward(request, response);
        }
    }
}
```

#### Generación de Clientes SOAP

**pom.xml con perfil wsimport:**
```xml
<profiles>
    <profile>
        <id>wsimport</id>
        <activation>
            <property>
                <name>wsimport</name>
                <value>true</value>
            </property>
        </activation>
        <build>
            <plugins>
                <plugin>
                    <groupId>com.sun.xml.ws</groupId>
                    <artifactId>jaxws-maven-plugin</artifactId>
                    <version>4.0.2</version>
                    <executions>
                        <execution>
                            <id>wsimport-publicador</id>
                            <goals>
                                <goal>wsimport</goal>
                            </goals>
                            <configuration>
                                <wsdlUrls>
                                    <wsdlUrl>http://localhost:9115/publicador?wsdl</wsdlUrl>
                                </wsdlUrls>
                                <packageName>soap</packageName>
                                <keep>true</keep>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
    </profile>
</profiles>
```

**Compilar con generación de clientes:**
```bash
cd servidor-web
mvn clean package -Dwsimport=true
```

**Desplegar:**
```bash
cp target/web.war $CATALINA_HOME/webapps/
```

**Acceder:**
```
http://localhost:8080/web/
```

---

### 3. Cliente Móvil (`mobile-client/`)

**Aplicación web optimizada para móviles, consume SOAP**

```
mobile-client/
├── src/main/java/
│   ├── servlets/                  # Servlets móvil
│   └── soap/                      # Clientes SOAP
│
├── src/main/webapp/
│   ├── WEB-INF/
│   ├── css/                       # Estilos mobile
│   ├── index.jsp
│   └── ...
│
├── target/
│   └── mobile.war
│
└── pom.xml
```

Similar al servidor-web pero con interfaz adaptada a dispositivos móviles.

---

## 🔄 Flujo de Comunicación SOAP

### Petición SOAP

```xml
POST http://localhost:9115/publicador HTTP/1.1
Content-Type: text/xml; charset=utf-8
SOAPAction: ""

<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope 
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:pub="http://publicadores/">
    <soapenv:Header/>
    <soapenv:Body>
        <pub:obtenerEvento>
            <nombre>Deporte2024</nombre>
        </pub:obtenerEvento>
    </soapenv:Body>
</soapenv:Envelope>
```

### Respuesta SOAP

```xml
HTTP/1.1 200 OK
Content-Type: text/xml; charset=utf-8

<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope 
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:pub="http://publicadores/">
    <soapenv:Body>
        <pub:obtenerEventoResponse>
            <return>
                <nombre>Deporte2024</nombre>
                <descripcion>Evento deportivo anual</descripcion>
                <fechaInicio>2024-06-01</fechaInicio>
                <fechaFin>2024-06-30</fechaFin>
                <categorias>DEPORTE</categorias>
            </return>
        </pub:obtenerEventoResponse>
    </soapenv:Body>
</soapenv:Envelope>
```

---

## 🔧 Configuración de Endpoints

### En tiempo de compilación (Maven)

```bash
mvn clean package \
  -DpublicadorControlador.wsdl=http://servidor:9115/publicador?wsdl \
  -DpublicadorUsuario.wsdl=http://servidor:9115/publicadorUsuario?wsdl
```

### En código (Publicadores)

```java
String address = "http://0.0.0.0:9115/publicador";
Endpoint endpoint = Endpoint.publish(address, new PublicadorControlador());
```

---

## 🗄️ Base de Datos HSQLDB

### Ubicación
```
servidor-central/data/
├── edicionesArchivadas.properties
├── edicionesArchivadas.script
└── edicionesArchivadas.lck (durante ejecución)
```

### Conexión
```
URL: jdbc:hsqldb:file:./data/edicionesArchivadas
User: SA
Password: (vacío)
```

### Inspección

**Detener servidor primero** (HSQLDB solo permite 1 conexión en modo file).

```bash
java -cp ~/.m2/repository/org/hsqldb/hsqldb/2.7.2/hsqldb-2.7.2.jar \
  org.hsqldb.util.DatabaseManagerSwing
```

Configurar:
- Type: `HSQL Database Engine Standalone`
- URL: `jdbc:hsqldb:file:/ruta/a/data/edicionesArchivadas`
- User: `SA`
- Password: (vacío)

Ver [docs/HSQLDB-INSPECCION.md](../../docs/HSQLDB-INSPECCION.md) para más detalles.

---

## 🚀 Deployment Completo

### Orden de inicio

1. **Iniciar Servidor Central:**
   ```bash
   cd servidor-central
   java -jar target/servidor.jar
   ```
   
   Verificar: http://localhost:9115/publicador?wsdl

2. **Iniciar Tomcat:**
   ```bash
   $CATALINA_HOME/bin/startup.sh
   ```

3. **Desplegar WARs:**
   ```bash
   cp servidor-web/target/web.war $CATALINA_HOME/webapps/
   cp mobile-client/target/mobile.war $CATALINA_HOME/webapps/
   ```

4. **Acceder:**
   - Web: http://localhost:8080/web/
   - Mobile: http://localhost:8080/mobile/

### Scripts automatizados

Ver carpeta `scripts-deployment/` en la raíz del proyecto:
- `compilar.bat` / `compilar.sh` - Compila todo
- `abrir-puertos-firewall.bat` - Configura firewall (Windows)

---

## 📊 Casos de Uso

Todos los casos de uso de iteraciones anteriores, más:

✅ **Archivar Edición** (con persistencia JPA)  
✅ **Consultar Ediciones Archivadas**  
✅ **Generar Constancia PDF** (iText)  
✅ **Carga de Datos de Prueba** (vía SOAP)  
✅ **Eventos Más Visitados** (contador de visitas)  

---

## 🔐 Seguridad

- Autenticación en cada cliente (sesiones HTTP)
- SOAP sin autenticación (para simplificar)
- Validaciones en servidor y cliente
- Filtrado de acceso por rol

**Mejoras posibles:**
- WS-Security para SOAP
- JWT tokens
- HTTPS

---

## ⚙️ Tecnologías Utilizadas

| Tecnología | Versión | Uso |
|------------|---------|-----|
| Java | 17 | Lenguaje |
| JAX-WS | 4.0.2 | Web Services SOAP |
| JPA | 3.1.0 | Persistencia |
| EclipseLink | 4.0.2 | Implementación JPA |
| HSQLDB | 2.7.2 | Base de datos |
| Jakarta Servlets | 6.0.0 | Controladores web |
| Jakarta JSP | 3.1.1 | Vistas dinámicas |
| JSTL | 3.0.0 | Tag libraries |
| iText | 7/8 | Generación PDF |
| Tomcat | 10.1 | Servidor de aplicaciones |
| Maven | 3.x | Build y dependencias |

---

## 📈 Ventajas de esta Arquitectura

✅ **Separación de responsabilidades**  
✅ **Escalabilidad** (múltiples clientes, un backend)  
✅ **Reutilización** (servicios SOAP reutilizables)  
✅ **Persistencia real** (JPA + HSQLDB)  
✅ **Interoperabilidad** (SOAP estándar)  
✅ **Testabilidad** (servicios independientes)  

---

## 🐛 Troubleshooting

### Error: "Address already in use" (Puerto 9115)

```bash
# Encontrar proceso
lsof -i :9115  # Linux/Mac
netstat -ano | findstr :9115  # Windows

# Matar proceso
kill -9 <PID>
```

### Error: HSQLDB lock file

Detener servidor antes de inspeccionar DB:
```bash
rm servidor-central/data/edicionesArchivadas.lck
```

### Error: Cannot find WSDL

Asegurar que Servidor Central esté corriendo antes de compilar con `-Dwsimport=true`.

---

## 📚 Documentación Adicional

- [Arquitectura General](../../docs/ARQUITECTURA.md)
- [Guía de Instalación](../../docs/INSTALACION.md)
- [Tecnologías](../../docs/TECNOLOGIAS.md)
- [Inspección HSQLDB](../../docs/HSQLDB-INSPECCION.md)

---

**Desarrollado por:** Equipo EventosUy (5 integrantes)  
**Asignatura:** Taller de Programación  
**Institución:** Facultad de Ingeniería - Universidad de la República (UdelaR)  
**Año:** 2024-2026
