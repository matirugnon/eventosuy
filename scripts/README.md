# Scripts de Despliegue - TPGR15

Este directorio contiene todos los scripts necesarios para compilar y desplegar el proyecto.

## Scripts Disponibles

### 1. `0-ejecutar-todo.bat`
Ejecuta todos los scripts en secuencia para un despliegue completo.

### 2. `1-iniciar-servidor-central.bat`
Compila e inicia el Servidor Central (backend).

### 3. `2-compilar-webserver.bat`
Compila el servidor web.

### 4. `4-desplegar-tomcat.bat`
Despliega la aplicación web en Tomcat.

### 5. `mobile-compilar.bat`
Compila el módulo mobile (aplicación web móvil).

### 6. `mobile-desplegar.bat`
Despliega la aplicación móvil en Tomcat.

### 7. `run-gui.bat`
Compila y ejecuta la interfaz gráfica del Servidor Central.

---

## Configuración de Rutas

**IMPORTANTE**: Antes de ejecutar los scripts, debes configurar las siguientes rutas según tu instalación:

### En `mobile-desplegar.bat`:

```batch
set TOMCAT_HOME=C:\Program Files\Apache Software Foundation\Tomcat 11.0
set JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.8.9-hotspot
```

### En `4-desplegar-tomcat.bat`:

```batch
set TOMCAT_HOME=C:\Program Files\Apache Software Foundation\Tomcat 11.0
```

### En `DescargaConstanciaServlet.java`:

La ruta del banner se encuentra en:
`mobile/src/main/java/servlets/DescargaConstanciaServlet.java`

```java
String bannerPath = "C:/Users/Mati/eclipse-workspace/tpgr15/tarea2/img/BANNER_BORDE.png";
```

---

## Despliegue para Acceso Móvil

Para acceder a la aplicación móvil desde tu celular en la red local:

1. **Compilar el Servidor Central**:
   ```
   scripts\1-iniciar-servidor-central.bat
   ```

2. **Compilar y desplegar la aplicación móvil**:
   ```
   scripts\mobile-compilar.bat
   scripts\mobile-desplegar.bat
   ```

3. **Abrir Tomcat** (si no está iniciado):
   - Ir a `C:\Program Files\Apache Software Foundation\Tomcat 11.0\bin`
   - Ejecutar `startup.bat`

4. **Acceder desde el celular**:
   - Conectar el celular a la misma red WiFi
   - Obtener la IP de tu PC: `ipconfig` (buscar IPv4)
   - Abrir navegador: `http://TU_IP:8080/mobile`

---

## Requisitos

- **Java JDK 21** o superior
- **Apache Maven 3.x**
- **Apache Tomcat 11.0**
- Variables de entorno configuradas: `JAVA_HOME`, `MAVEN_HOME`

---

## Solución de Problemas

### Error: "CATALINA_HOME no está definido"
Verifica que la ruta en el script `mobile-desplegar.bat` apunte correctamente a tu instalación de Tomcat.

### Error: "No se encuentra mvn"
Asegúrate de tener Maven instalado y agregado al PATH del sistema.

### Error: "java: package com.itextpdf does not exist"
Ejecuta `mobile-compilar.bat` nuevamente. Maven descargará automáticamente las dependencias de iText.

### La aplicación no carga en el celular
- Verifica que el firewall de Windows permita conexiones en el puerto 8080
- Confirma que estés en la misma red WiFi
- Verifica que Tomcat esté ejecutándose: http://localhost:8080

---

## Notas Adicionales

- Los scripts deben ejecutarse desde el directorio `scripts/` o ajustar las rutas relativas
- El Servidor Central debe estar ejecutándose antes de usar la aplicación móvil
- Las constancias en PDF requieren que el banner esté en la ruta configurada
