# Guía de Instalación y Ejecución - EventosUy

## 📋 Tabla de Contenidos

1. [Prerrequisitos](#prerrequisitos)
2. [Instalación de Herramientas](#instalación-de-herramientas)
3. [Configuración del Entorno](#configuración-del-entorno)
4. [Ejecución de Iteración 1](#ejecución-de-iteración-1)
5. [Ejecución de Iteración 2](#ejecución-de-iteración-2)
6. [Ejecución de Iteración 3](#ejecución-de-iteración-3)
7. [Troubleshooting](#troubleshooting)

---

## Prerrequisitos

### Software Necesario

- **JDK 17** o superior
- **Apache Maven 3.6+**
- **Apache Tomcat 10.1**
- **Git** (para clonar el repositorio)

### Sistema Operativo

- Windows 10/11
- Linux (Ubuntu 20.04+)
- macOS (10.15+)

---

## Instalación de Herramientas

### 1. Instalar Java JDK 17

**Windows:**
1. Descargar de [Oracle](https://www.oracle.com/java/technologies/downloads/#java17) o [AdoptOpenJDK](https://adoptium.net/)
2. Ejecutar el instalador
3. Configurar variable de entorno:
   ```cmd
   setx JAVA_HOME "C:\Program Files\Java\jdk-17"
   setx PATH "%PATH%;%JAVA_HOME%\bin"
   ```

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install openjdk-17-jdk
java -version
```

**macOS:**
```bash
brew install openjdk@17
echo 'export PATH="/usr/local/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

### 2. Instalar Apache Maven

**Windows:**
1. Descargar de [Maven Download](https://maven.apache.org/download.cgi)
2. Extraer a `C:\Program Files\Apache\maven`
3. Configurar variables:
   ```cmd
   setx MAVEN_HOME "C:\Program Files\Apache\maven"
   setx PATH "%PATH%;%MAVEN_HOME%\bin"
   ```

**Linux:**
```bash
sudo apt install maven
mvn -version
```

**macOS:**
```bash
brew install maven
mvn -version
```

### 3. Instalar Apache Tomcat 10.1

**Windows:**
1. Descargar de [Tomcat 10](https://tomcat.apache.org/download-10.cgi)
2. Extraer a `C:\Program Files\Apache\Tomcat-10.1`
3. Configurar variable:
   ```cmd
   setx CATALINA_HOME "C:\Program Files\Apache\Tomcat-10.1"
   ```

**Linux:**
```bash
cd /opt
sudo wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.xx/bin/apache-tomcat-10.1.xx.tar.gz
sudo tar -xvzf apache-tomcat-10.1.xx.tar.gz
sudo mv apache-tomcat-10.1.xx tomcat
export CATALINA_HOME=/opt/tomcat
```

**macOS:**
```bash
brew install tomcat@10
```

---

## Configuración del Entorno

### Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/eventosuy.git
cd eventosuy
```

### Verificar Instalación

```bash
java -version
mvn -version
echo $CATALINA_HOME  # Linux/Mac
echo %CATALINA_HOME%  # Windows
```

---

## Ejecución de Iteración 1

### Aplicación de Escritorio (Java Swing)

**Opción 1: Usando el script de compilación**

```bash
cd iteracion-1-logica-escritorio
# Windows:
compile.bat

# Linux/Mac: adaptar o compilar manualmente
```

**Opción 2: Compilación manual**

```bash
cd iteracion-1-logica-escritorio

# Compilar
javac -d bin -cp "lib/*" src/**/*.java

# Ejecutar
java -cp "bin:lib/*" presentacion.Main  # Linux/Mac
java -cp "bin;lib/*" presentacion.Main  # Windows
```

### Características de la Iteración 1

- Interfaz gráfica con Java Swing
- No requiere servidor
- Datos en memoria (no persisten al cerrar)

---

## Ejecución de Iteración 2

### Aplicación Web (HTML/CSS/JS + Servlets/JSP)

La iteración 2 contiene archivos HTML estáticos que pueden desplegarse en cualquier servidor web.

**Opción 1: Servidor HTTP simple (Python)**

```bash
cd iteracion-2-web-servlets-jsp
python -m http.server 8000
# Acceder a http://localhost:8000
```

**Opción 2: Desplegar en Tomcat**

1. Copiar la carpeta completa a `CATALINA_HOME/webapps/eventosuy-v2/`
2. Iniciar Tomcat:
   ```bash
   # Windows:
   %CATALINA_HOME%\bin\startup.bat
   
   # Linux/Mac:
   $CATALINA_HOME/bin/startup.sh
   ```
3. Acceder a: http://localhost:8080/eventosuy-v2/

### Características de la Iteración 2

- Frontend HTML/CSS/JavaScript
- Vistas separadas por rol: asistente, organizador, visitante
- Navegación con sidebar

---

## Ejecución de Iteración 3

### Sistema Completo con Web Services SOAP

Esta es la iteración más compleja, requiere levantar 3 componentes en orden específico.

### Paso 1: Iniciar el Servidor Central

El Servidor Central expone los servicios SOAP y gestiona la lógica de negocio.

**Compilar:**
```bash
cd iteracion-3-webservices-soap/servidor-central
mvn clean package
```

**Ejecutar (Modo Consola):**
```bash
java -jar target/servidor.jar
```

**Ejecutar (Modo GUI - recomendado):**
```bash
# Windows:
run-gui.bat

# PowerShell:
.\run-gui.ps1

# Linux/Mac:
chmod +x run-gui.sh
./run-gui.sh
```

**Verificar que los servicios SOAP estén activos:**

Abrir en navegador:
- http://localhost:9115/publicador?wsdl
- http://localhost:9115/publicadorUsuario?wsdl
- http://localhost:9115/publicadorRegistro?wsdl
- http://localhost:9115/publicadorCargaDatos?wsdl

Deberías ver el WSDL XML de cada servicio.

### Paso 2: Cargar Datos de Prueba (Opcional)

Con el Servidor Central corriendo, ejecutar:

```bash
# Desde la GUI: usar el menú "Cargar Datos"
# O hacer una petición HTTP:
curl http://localhost:9115/publicadorCargaDatos?wsdl
```

### Paso 3: Compilar y Desplegar Servidor Web

**Compilar con generación de clientes SOAP:**
```bash
cd iteracion-3-webservices-soap/servidor-web
mvn clean package -Dwsimport=true
```

Si el Servidor Central no está corriendo, puedes compilar sin generar clientes:
```bash
mvn clean package
```

**Desplegar en Tomcat:**

```bash
# Windows:
copy target\web.war %CATALINA_HOME%\webapps\

# Linux/Mac:
cp target/web.war $CATALINA_HOME/webapps/
```

**Iniciar Tomcat:**
```bash
# Windows:
%CATALINA_HOME%\bin\startup.bat

# Linux/Mac:
$CATALINA_HOME/bin/startup.sh
```

**Acceder a la aplicación:**
http://localhost:8080/web/

### Paso 4: Compilar y Desplegar Cliente Móvil (Opcional)

**Compilar:**
```bash
cd iteracion-3-webservices-soap/mobile-client
mvn clean package
```

**Desplegar:**
```bash
# Windows:
copy target\mobile.war %CATALINA_HOME%\webapps\

# Linux/Mac:
cp target/mobile.war $CATALINA_HOME/webapps/
```

**Acceder:**
http://localhost:8080/mobile/

---

## Configuración Avanzada

### Cambiar Puertos SOAP

Editar `servidor-central/src/main/java/publicadores/Publicador*.java`:

```java
String address = "http://0.0.0.0:9115/publicador"; // Cambiar puerto aquí
```

### Configurar Endpoints en Servidor Web

Editar `servidor-web/pom.xml`:

```xml
<properties>
    <publicadorControlador.wsdl>http://localhost:9115/publicador?wsdl</publicadorControlador.wsdl>
    <!-- Cambiar host/puerto si el Servidor Central está en otra máquina -->
</properties>
```

### Configurar Firewall (Windows)

Ejecutar como Administrador:
```cmd
scripts-deployment\abrir-puertos-firewall.bat
```

Esto abre los puertos:
- 9115 (SOAP)
- 8080 (Tomcat)

---

## Scripts de Deployment

### `compilar.bat` / `compilar.sh`

Compila automáticamente los 3 componentes de la Iteración 3 con configuración de endpoints.

```bash
cd scripts-deployment
# Windows:
compilar.bat

# Linux/Mac:
chmod +x compilar.sh
./compilar.sh
```

### `sql.bat` / `sql.sh`

Abre el cliente SQL para inspeccionar la base de datos HSQLDB.

**IMPORTANTE:** Detener el Servidor Central antes de ejecutar (HSQLDB solo permite 1 conexión en modo file).

```bash
cd scripts-deployment
# Windows:
sql.bat

# Linux/Mac:
chmod +x sql.sh
./sql.sh
```

---

## Troubleshooting

### Error: "Address already in use" (Puerto 9115 ocupado)

```bash
# Windows:
netstat -ano | findstr :9115
taskkill /PID <PID> /F

# Linux/Mac:
lsof -i :9115
kill -9 <PID>
```

### Error: "Cannot find WSDL" al compilar servidor-web

**Solución:** Asegurarse de que el Servidor Central esté corriendo antes de ejecutar `mvn package -Dwsimport=true`

O compilar sin wsimport:
```bash
mvn clean package
```

### Error: "HSQLDB lock file" al acceder a la base de datos

**Solución:** Detener el Servidor Central y Tomcat antes de abrir el cliente SQL.

```bash
# Windows:
%CATALINA_HOME%\bin\shutdown.bat

# Linux/Mac:
$CATALINA_HOME/bin/shutdown.sh
```

Eliminar el archivo `edicionesArchivadas.lck` si persiste:
```bash
rm iteracion-3-webservices-soap/servidor-central/data/edicionesArchivadas.lck
```

### Error: "Class not found" en Tomcat

**Solución:** Verificar que se use JDK 17 y Tomcat 10.1 (compatible con Jakarta EE 9+).

### Tomcat no inicia

Verificar logs:
```bash
tail -f $CATALINA_HOME/logs/catalina.out  # Linux/Mac
type %CATALINA_HOME%\logs\catalina.out    # Windows
```

### Puerto 8080 ocupado

Cambiar puerto de Tomcat editando `CATALINA_HOME/conf/server.xml`:

```xml
<Connector port="8081" protocol="HTTP/1.1" ... />
```

---

## Orden de Inicio Recomendado (Iteración 3)

1. ✅ Iniciar **Servidor Central** (puerto 9115)
2. ✅ Verificar WSDLs disponibles en navegador
3. ✅ (Opcional) Cargar datos de prueba
4. ✅ Iniciar **Tomcat** (puerto 8080)
5. ✅ Desplegar **web.war**
6. ✅ Desplegar **mobile.war** (opcional)
7. ✅ Acceder a http://localhost:8080/web/

---

## Orden de Detención

1. ⛔ Detener **Tomcat**
2. ⛔ Detener **Servidor Central**

Esto previene errores de conexión y lock de base de datos.

---

## Recursos Adicionales

- **Documentación HSQLDB**: [docs/HSQLDB-INSPECCION.md](../docs/HSQLDB-INSPECCION.md)
- **Tomcat Documentation**: https://tomcat.apache.org/tomcat-10.1-doc/
- **Maven Central**: https://mvnrepository.com/
- **JAX-WS Tutorial**: https://javaee.github.io/metro-jax-ws/

---

## Soporte

Para problemas técnicos:
1. Revisar logs de Tomcat: `CATALINA_HOME/logs/`
2. Revisar salida del Servidor Central
3. Verificar configuración de puertos
4. Consultar esta guía de troubleshooting

---

**Última actualización:** Febrero 2026  
**Versión:** 1.0  
**Mantenido por:** Equipo EventosUy - Facultad de Ingeniería UdelaR
