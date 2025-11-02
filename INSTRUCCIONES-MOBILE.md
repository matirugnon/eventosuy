# Instrucciones para ejecutar la Aplicación Mobile

## Requisitos previos

1. **Java 17** instalado
2. **Maven** configurado
3. **Apache Tomcat 10.1** instalado
4. **Servidor Central** corriendo en `http://localhost:9128`

---

## Pasos para ejecutar

### 1. Iniciar el Servidor Central

Primero, debes tener el Servidor Central corriendo (expone los servicios SOAP):

```bash
# Opción A: Ejecutar el script
1-iniciar-servidor-central.bat

# Opción B: Manualmente desde ServidorCentral
cd ServidorCentral
mvn clean install
mvn exec:java
```

**Verifica que esté corriendo:** Abre en el navegador:
- http://localhost:9128/publicador?wsdl
- http://localhost:9128/publicador-usuario?wsdl
- http://localhost:9128/publicador-registro?wsdl

---

### 2. Compilar la aplicación Mobile

```bash
mobile-compilar.bat
```

Este script:
- Regenera los clientes SOAP desde los WSDL
- Compila el proyecto Maven
- Genera el archivo WAR en `mobile/target/web.war`

---

### 3. Desplegar en Tomcat

```bash
mobile-desplegar.bat
```

Este script:
- Detiene Tomcat si está corriendo
- Limpia despliegues anteriores
- Copia el WAR a Tomcat
- Inicia Tomcat

**Nota:** Ajusta la ruta de Tomcat en `mobile-desplegar.bat` si es necesaria.

---

### 4. Acceder a la aplicación

Abre tu navegador en:

```
http://localhost:8080/mobile
```

---

## Método alternativo: Maven + Jetty

Si no quieres usar Tomcat, puedes usar Jetty embebido:

### 1. Agregar plugin Jetty al pom.xml de mobile

```xml
<plugin>
    <groupId>org.eclipse.jetty</groupId>
    <artifactId>jetty-maven-plugin</artifactId>
    <version>11.0.15</version>
    <configuration>
        <httpConnector>
            <port>9090</port>
        </httpConnector>
        <webApp>
            <contextPath>/mobile</contextPath>
        </webApp>
    </configuration>
</plugin>
```

### 2. Ejecutar con Maven

```bash
cd mobile
mvn clean jaxws:wsimport jetty:run
```

La aplicación estará disponible en:
```
http://localhost:9090/mobile
```

---

## Solución de problemas

### Error al generar clases SOAP
**Problema:** `mvn jaxws:wsimport` falla

**Solución:**
- Verifica que el Servidor Central esté corriendo
- Verifica que los WSDL sean accesibles:
  - http://localhost:9128/publicador?wsdl
  - http://localhost:9128/publicador-usuario?wsdl
  - http://localhost:9128/publicador-registro?wsdl

### Puerto 8080 ocupado
**Problema:** Tomcat no puede iniciar porque el puerto está ocupado

**Solución:**
```powershell
# Ver qué proceso usa el puerto 8080
netstat -ano | findstr :8080

# Matar el proceso (reemplaza PID con el número que aparece)
taskkill /PID <PID> /F
```

### Errores de compilación
**Problema:** Maven no puede compilar

**Solución:**
```bash
cd mobile
mvn clean
mvn dependency:resolve
mvn compile
```

---

## Estructura de la aplicación Mobile

```
mobile/
├── src/
│   └── main/
│       ├── java/
│       │   ├── servlets/        # Servlets (LoginServlet, etc.)
│       │   ├── logica/          # Lógica de negocio
│       │   ├── soap/            # Clientes SOAP generados
│       │   └── utils/           # Utilidades
│       └── webapp/
│           ├── css/             # Estilos
│           ├── img/             # Imágenes
│           ├── index.jsp        # Página de inicio
│           └── WEB-INF/
│               ├── views/       # Vistas JSP
│               └── web.xml      # Configuración web
└── pom.xml                      # Configuración Maven
```

---

## Usuarios de prueba

Una vez que la aplicación esté corriendo, puedes iniciar sesión con:

### Asistentes:
- **Usuario:** `atorres` | **Contraseña:** `25atorres`
- **Usuario:** `jgomez` | **Contraseña:** `25jgomez`
- **Usuario:** `mrodriguez` | **Contraseña:** `25mrodriguez`

### Organizadores (NO deberían poder acceder desde mobile):
- **Usuario:** `techcorp` | **Contraseña:** `25techcorp`

---

## Próximos pasos

1. Implementar **Registro de Asistencia** (funcionalidad faltante)
2. Agregar **Bootstrap** para responsive design
3. Mejorar la UX mobile
