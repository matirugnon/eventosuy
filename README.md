# EventosUy - Sistema de Gestión de Eventos

[![Facultad de Ingeniería - UdelaR](https://img.shields.io/badge/Facultad%20de%20Ingenier%C3%ADa-UdelaR-blue)](https://www.fing.edu.uy/)
[![Taller de Programación](https://img.shields.io/badge/Asignatura-Taller%20de%20Programaci%C3%B3n-green)]()
[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Tomcat](https://img.shields.io/badge/Tomcat-10.1-yellow)](https://tomcat.apache.org/)

## 📋 Descripción del Proyecto

**EventosUy** es un sistema completo de gestión de eventos desarrollado como proyecto académico en el marco de la asignatura **Taller de Programación** de la **Facultad de Ingeniería - Universidad de la República (UdelaR)**.

El proyecto fue desarrollado por un equipo de **5 estudiantes** a lo largo de **3 iteraciones**, evolucionando desde una aplicación de escritorio hasta un sistema distribuido con arquitectura de servicios web.

### 🎯 Características Principales

- **Gestión de Usuarios**: Alta, consulta y modificación de usuarios (Asistentes y Organizadores)
- **Gestión de Eventos**: Creación y consulta de eventos con múltiples ediciones
- **Gestión de Ediciones**: Administración de ediciones de eventos con tipos de registro personalizables
- **Sistema de Registros**: Registro de asistentes a ediciones de eventos
- **Gestión de Patrocinios**: Sistema de patrocinios e instituciones
- **Generación de Constancias**: Descarga de constancias de asistencia en formato PDF
- **Archivado de Ediciones**: Persistencia de ediciones archivadas en base de datos HSQLDB

---

## 🏗️ Arquitectura del Proyecto

### Evolución por Iteraciones

#### **Iteración 1: Lógica y Estación de Trabajo** 
📁 `/iteracion-1-logica-escritorio`

- **Tecnologías**: Java Swing, Java SE
- **Arquitectura**: Aplicación de escritorio monolítica
- **Componentes**:
  - **Capa de Presentación**: Interfaz gráfica con Java Swing (JFrame, JInternalFrame)
  - **Capa de Lógica**: Clases de dominio (Usuario, Evento, Edición, Registro, etc.)
  - **Controladores**: Manejo de casos de uso
  - **Manejadores**: Gestión de entidades en memoria
  - **Excepciones**: Manejo de errores personalizados

**Funcionalidades implementadas**:
- Alta de Usuario
- Consulta de Usuario
- Alta de Evento
- Consulta de Evento
- Alta de Edición de Evento
- Consulta de Edición de Evento
- Alta de Tipo de Registro
- Consulta de Tipo de Registro
- Registro a Edición de Evento
- Alta de Institución y Patrocinio

---

#### **Iteración 2: Aplicación Web con Servlets y JSP**
📁 `/iteracion-2-web-servlets-jsp`

- **Tecnologías**: Java Servlets, JSP, JSTL, HTML5, CSS3
- **Servidor**: Apache Tomcat 10.1
- **Arquitectura**: Aplicación web MVC (Modelo-Vista-Controlador)
  
**Componentes**:
- **Frontend**:
  - HTML5 + CSS3 personalizado
  - JSP con JSTL para vistas dinámicas
  - JavaScript para interactividad
  - Diseño responsive con navegación por sidebar

- **Backend**:
  - **Servlets**: Controladores para cada caso de uso
    - `LoginServlet`: Autenticación de usuarios
    - `SignupServlet`: Registro de nuevos usuarios
    - `ConsultaEventoServlet`: Consulta de eventos
    - `ConsultaEdicionServlet`: Consulta de ediciones
    - `RegistroAEdicionServlet`: Registro a ediciones
    - `DescargaConstanciaServlet`: Generación de PDFs
    - `PerfilUsuarioServlet`: Gestión de perfiles
    - `ListarUsuariosServlet`: Listado de usuarios
    - Y más...
  
  - **Filtros**: `ContadorVisitasFilter` para tracking de visitas
  - **DTOs**: Objetos de transferencia de datos
  - **Sesiones**: Gestión de estado de usuario

**Patrones de diseño**:
- MVC (Model-View-Controller)
- Front Controller
- Session Management
- Data Transfer Object (DTO)

---

#### **Iteración 3: Web Services SOAP y Arquitectura Distribuida**
📁 `/iteracion-3-webservices-soap`

La iteración final implementa una **arquitectura distribuida basada en servicios web SOAP**, separando completamente el backend (lógica y persistencia) de los clientes (web y móvil).

**Tecnologías**: JAX-WS, SOAP, JPA/EclipseLink, HSQLDB, Maven

### 📦 Componentes de la Iteración 3

#### 1. **Servidor Central** (`servidor-central/`)
Aplicación Java standalone que expone servicios web SOAP y gestiona la lógica de negocio.

**Características**:
- **Publicadores SOAP**:
  - `PublicadorControlador`: Servicios generales
  - `PublicadorUsuario`: Operaciones de usuarios
  - `PublicadorRegistro`: Gestión de registros
  - `PublicadorCargaDatos`: Carga inicial de datos de prueba

- **Persistencia**:
  - JPA con EclipseLink
  - HSQLDB embebida (modo archivo)
  - Archivado de ediciones en base de datos separada

- **Lógica de Negocio**:
  - Manejadores: `ManejadorUsuario`, `ManejadorEventos`, `ManejadorPersistencia`
  - Controladores para casos de uso
  - Validaciones y excepciones personalizadas

- **Interfaz Gráfica Desktop** (opcional):
  - GUI Java Swing para administración
  - Punto de entrada: `Main.java` o `ServidorCentralMain.java`

**Endpoints SOAP** (por defecto):
```
http://localhost:9115/publicador
http://localhost:9115/publicadorUsuario
http://localhost:9115/publicadorRegistro
http://localhost:9115/publicadorCargaDatos
```

**Compilación y ejecución**:
```bash
cd iteracion-3-webservices-soap/servidor-central
mvn clean package
java -jar target/servidor.jar
```

O para iniciar con GUI:
```bash
run-gui.bat  # Windows
./run-gui.ps1  # PowerShell
```

---

#### 2. **Servidor Web** (`servidor-web/`)
Aplicación web Java EE desplegada en Tomcat, consume servicios SOAP del Servidor Central.

**Características**:
- **Arquitectura**: Cliente SOAP + Servlets + JSP
- **Consumo de Web Services**:
  - Clientes JAX-WS generados desde WSDLs
  - Comunicación HTTP con Servidor Central
  
- **Packaging**: WAR (Web Application Archive)
- **Deployment**: Tomcat 10.1

**Configuración**:
- Los endpoints SOAP se configuran en tiempo de compilación mediante propiedades Maven
- Archivo `pom.xml` incluye perfil para generar clientes SOAP vía `wsimport`

**Build y despliegue**:
```bash
cd iteracion-3-webservices-soap/servidor-web
mvn clean package
# Desplegar target/web.war en Tomcat
```

---

#### 3. **Cliente Móvil** (`mobile-client/`)
Aplicación web optimizada para dispositivos móviles, también consume servicios SOAP.

**Características**:
- WAR independiente para despliegue en Tomcat
- Interfaz adaptada a dispositivos móviles
- Consume los mismos servicios SOAP que el servidor web

**Build**:
```bash
cd iteracion-3-webservices-soap/mobile-client
mvn clean package
# Desplegar target/mobile.war en Tomcat
```

---

### 🔄 Flujo de Comunicación (Iteración 3)

```
┌─────────────────┐
│  Cliente Web    │
│  (Navegador)    │
└────────┬────────┘
         │ HTTP
         ▼
┌─────────────────────┐       SOAP/HTTP        ┌──────────────────┐
│  Servidor Web       │◄──────────────────────►│ Servidor Central │
│  (Tomcat + WAR)     │                        │  (Java Standalone)│
└─────────────────────┘                        └────────┬─────────┘
                                                        │
┌─────────────────────┐       SOAP/HTTP               │
│  Mobile Client      │◄──────────────────────────────┤
│  (Tomcat + WAR)     │                               │
└─────────────────────┘                               ▼
                                                ┌────────────┐
┌─────────────────────┐                        │   HSQLDB   │
│  Desktop GUI        │◄───────────────────────┤ (Embedded) │
│  (Java Swing)       │    Método directo      └────────────┘
└─────────────────────┘
```

---

## 🗂️ Estructura de Carpetas

```
eventosuy/
├── iteracion-1-logica-escritorio/     # Iteración 1: Aplicación de escritorio
│   ├── src/                           # Código fuente Java
│   │   ├── datoprueba/                # Datos de prueba
│   │   ├── excepciones/               # Excepciones personalizadas
│   │   ├── gui/                       # Interfaces gráficas Swing
│   │   ├── logica/                    # Clases de dominio
│   │   ├── presentacion/              # Capa de presentación
│   │   └── utils/                     # Utilidades
│   ├── lib/                           # Librerías externas
│   ├── compile.bat                    # Script de compilación
│   └── Readme                         # Documentación iteración 1
│
├── iteracion-2-web-servlets-jsp/     # Iteración 2: Aplicación web
│   ├── asistente/                     # Vistas para usuarios asistentes
│   ├── organizador/                   # Vistas para organizadores
│   ├── visitante/                     # Vistas públicas
│   ├── img/                           # Recursos de imagen
│   ├── src/                           # Fuentes (si aplica)
│   ├── index.html                     # Página principal
│   ├── styles.css                     # Estilos CSS
│   ├── sidebar.txt                    # Configuración sidebar
│   └── README.txt                     # Documentación iteración 2
│
├── iteracion-3-webservices-soap/     # Iteración 3: Web Services
│   ├── servidor-central/              # Backend con servicios SOAP
│   │   ├── src/main/java/
│   │   │   ├── publicadores/          # Publicadores SOAP
│   │   │   ├── logica/                # Lógica de negocio
│   │   │   │   ├── manejadores/       # Manejadores de entidades
│   │   │   │   └── Controladores/     # Controladores
│   │   │   ├── gui/internal/          # Componentes GUI
│   │   │   ├── presentacion/          # Capa presentación
│   │   │   ├── soap/                  # Clases generadas SOAP
│   │   │   └── utils/                 # Utilidades
│   │   ├── data/                      # Base de datos HSQLDB
│   │   ├── pom.xml                    # Configuración Maven
│   │   ├── run-gui.bat                # Script inicio GUI
│   │   └── run-gui.ps1                # Script PowerShell
│   │
│   ├── servidor-web/                  # Cliente web (WAR)
│   │   ├── src/main/java/
│   │   │   ├── servlets/              # Servlets
│   │   │   │   └── dto/               # DTOs
│   │   │   ├── filtros/               # Filtros
│   │   │   └── soap/                  # Clientes SOAP
│   │   ├── src/main/webapp/           # Recursos web
│   │   │   ├── WEB-INF/               # Configuración
│   │   │   ├── css/                   # Estilos
│   │   │   ├── js/                    # JavaScript
│   │   │   └── *.jsp                  # Vistas JSP
│   │   └── pom.xml                    # Configuración Maven
│   │
│   └── mobile-client/                 # Cliente móvil (WAR)
│       ├── src/main/java/
│       ├── src/main/webapp/
│       └── pom.xml
│
├── scripts-deployment/                # Scripts de compilación y despliegue
│   ├── compilar.bat                   # Compilación Windows
│   ├── compilar.sh                    # Compilación Linux/Mac
│   ├── sql.bat                        # Cliente SQL Windows
│   ├── sql.sh                         # Cliente SQL Linux/Mac
│   ├── abrir-puertos-firewall.bat     # Configuración firewall
│   └── data/                          # Datos de configuración
│
├── docs/                              # Documentación
│   └── HSQLDB-INSPECCION.md          # Guía inspección base de datos
│
├── .gitignore                         # Exclusiones Git
└── README.md                          # Este archivo
```

---

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 17**
- **Jakarta EE** (Servlets 6.0, JSP 3.1, JSTL 3.0)
- **JAX-WS 4.0** (Web Services SOAP)
- **JPA 3.1** con EclipseLink (ORM)
- **Maven 3.x** (Gestión de dependencias)

### Base de Datos
- **HSQLDB 2.7.2** (Embedded database)

### Servidor de Aplicaciones
- **Apache Tomcat 10.1**

### Frontend
- **HTML5**
- **CSS3**
- **JavaScript**
- **Java Swing** (Desktop GUI)

### Librerías Adicionales
- **iText 7/8** (Generación de PDFs)
- **JGoodies Forms** (Layout Swing)

---

## 🚀 Instalación y Ejecución

### Prerrequisitos

- **JDK 17** o superior
- **Apache Maven 3.6+**
- **Apache Tomcat 10.1** (para aplicaciones web)
- **Git** (para clonar el repositorio)

### Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/eventosuy.git
cd eventosuy
```

### Ejecución por Iteraciones

#### **Iteración 1: Aplicación de Escritorio**

```bash
cd iteracion-1-logica-escritorio
# Windows:
compile.bat
# Linux/Mac: compilar manualmente o adaptar scripts
```

#### **Iteración 2: Aplicación Web Standalone**

Esta iteración requiere un servidor web básico. Los archivos HTML/CSS/JS pueden servirse con cualquier servidor HTTP estático o integrarse en Tomcat.

#### **Iteración 3: Sistema Completo con Web Services**

**1. Iniciar el Servidor Central:**

```bash
cd iteracion-3-webservices-soap/servidor-central
mvn clean package
java -jar target/servidor.jar
```

O con GUI:
```bash
./run-gui.bat  # Windows
```

Verificar que los servicios SOAP estén disponibles:
- http://localhost:9115/publicador?wsdl
- http://localhost:9115/publicadorUsuario?wsdl
- http://localhost:9115/publicadorRegistro?wsdl
- http://localhost:9115/publicadorCargaDatos?wsdl

**2. Compilar y desplegar Servidor Web:**

```bash
cd iteracion-3-webservices-soap/servidor-web
mvn clean package -Dwsimport=true
# Copiar target/web.war a TOMCAT_HOME/webapps/
```

Acceder en: http://localhost:8080/web/

**3. Compilar y desplegar Cliente Móvil:**

```bash
cd iteracion-3-webservices-soap/mobile-client
mvn clean package
# Copiar target/mobile.war a TOMCAT_HOME/webapps/
```

Acceder en: http://localhost:8080/mobile/

---

## 📊 Base de Datos

### HSQLDB

El proyecto utiliza **HSQLDB** en modo embebido (file-based) para:
- Persistencia de ediciones archivadas
- Almacenamiento de datos del sistema

**Ubicación**: `iteracion-3-webservices-soap/servidor-central/data/`

**Archivos**:
- `edicionesArchivadas.properties` - Configuración
- `edicionesArchivadas.script` - Scripts SQL
- `edicionesArchivadas.lck` - Lock file (se genera en ejecución)

### Inspección de la Base de Datos

Ver documentación detallada en: [docs/HSQLDB-INSPECCION.md](docs/HSQLDB-INSPECCION.md)

**Inicio rápido**:
```bash
# Detener servidor central y Tomcat primero
java -cp ~/.m2/repository/org/hsqldb/hsqldb/2.7.2/hsqldb-2.7.2.jar \
  org.hsqldb.util.DatabaseManagerSwing
```

Configurar:
- Type: `HSQL Database Engine Standalone`
- URL: `jdbc:hsqldb:file:/ruta/a/data/edicionesArchivadas`
- User: `SA`
- Password: (vacío)

---

## 📝 Scripts de Deployment

La carpeta `scripts-deployment/` contiene scripts para automatizar la compilación y despliegue:

### `compilar.bat` / `compilar.sh`
Compila los 3 componentes de la iteración 3 (Servidor Central, Servidor Web, Mobile) con configuración de endpoints SOAP.

### `sql.bat` / `sql.sh`
Lanza el cliente SQL para inspeccionar la base de datos HSQLDB.

### `abrir-puertos-firewall.bat`
Configura el firewall de Windows para permitir conexiones a los puertos necesarios (9115 para SOAP, 8080 para Tomcat).

---

## 👥 Equipo de Desarrollo

Este proyecto fue desarrollado por un equipo de **5 estudiantes** como parte de la asignatura **Taller de Programación** de la Facultad de Ingeniería, UdelaR.

**Roles y responsabilidades**:
- Diseño de arquitectura compartido
- Implementación por iteraciones colaborativa
- Integración de componentes
- Testing y debugging conjunto

---

## 📚 Casos de Uso Implementados

### Gestión de Usuarios
- ✅ Alta de Usuario
- ✅ Consulta de Usuario
- ✅ Modificar Datos de Usuario
- ✅ Listar Usuarios
- ✅ Autenticación (Login/Logout)

### Gestión de Eventos
- ✅ Alta de Evento
- ✅ Consulta de Evento
- ✅ Listar Eventos
- ✅ Eventos más visitados

### Gestión de Ediciones
- ✅ Alta de Edición de Evento
- ✅ Consulta de Edición de Evento
- ✅ Archivar Edición
- ✅ Ediciones Organizadas (por usuario)

### Gestión de Registros
- ✅ Alta de Tipo de Registro
- ✅ Consulta de Tipo de Registro
- ✅ Registro a Edición de Evento
- ✅ Consulta de Registro
- ✅ Mis Registros (por usuario)
- ✅ Descargar Constancia (PDF)

### Gestión de Patrocinios
- ✅ Alta de Institución
- ✅ Alta de Patrocinio
- ✅ Consulta de Patrocinio

### Administración
- ✅ Carga de Datos de Prueba
- ✅ Inspección de Base de Datos

---

## 🔐 Seguridad

- **Autenticación basada en sesiones** (HttpSession)
- **Validación de entrada** en servlets y servicios
- **Filtrado de acceso** según rol (Asistente/Organizador)
- **Gestión de sesiones** con timeout

---

## 📈 Evolución del Proyecto

### Iteración 1 ➜ Iteración 2
- Migración de Swing a Web (Servlets + JSP)
- Implementación de arquitectura MVC
- Introducción de sesiones HTTP
- Desarrollo de frontend HTML/CSS

### Iteración 2 ➜ Iteración 3
- Separación de capas: Cliente-Servidor
- Implementación de Web Services SOAP
- Introducción de JPA para persistencia
- Cliente móvil independiente
- Scripts de deployment automatizados

---

## 🤝 Contribuciones

Este es un proyecto académico finalizado. No se aceptan contribuciones externas, pero puedes usarlo como referencia educativa.

---

## 📜 Licencia

Este proyecto fue desarrollado con fines académicos en el contexto de la asignatura **Taller de Programación** de la **Facultad de Ingeniería, UdelaR**.

Todos los derechos reservados a los autores del proyecto.

---

## 📞 Contacto

Para consultas sobre el proyecto:
- **Institución**: Facultad de Ingeniería - Universidad de la República
- **Asignatura**: Taller de Programación
- **Página Web**: https://www.fing.edu.uy/

---

## 🙏 Agradecimientos

- **Facultad de Ingeniería - UdelaR** por el marco académico
- **Docentes de Taller de Programación** por la guía y supervisión
- **Equipo de desarrollo** por el esfuerzo colaborativo

---

**Desarrollado con ❤️ en Montevideo, Uruguay**
