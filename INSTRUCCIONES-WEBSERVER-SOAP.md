# Guía de Ejecución - Web Server con SOAP

## Cambios Realizados

Se ha modificado el servidor web para que llame al PublicadorControlador via SOAP en lugar de llamar directamente a la lógica.

### Archivos Modificados:
1. **servidor-web/src/main/java/utils/SoapClientHelper.java** (NUEVO)
   - Helper para obtener la conexión SOAP
   
2. **servidor-web/src/main/java/servlets/AltaEventoServlet.java**
   - Modificado para usar SOAP en vez de IControladorEvento
   
3. **servidor-web/src/main/java/servlets/AltaEdicionServlet.java**
   - Modificado para usar SOAP en vez de IControladorEvento

4. **servidor-web/src/main/java/servlets/TestSoapServlet.java** (NUEVO)
   - Servlet de prueba para verificar la conexión SOAP

5. **ServidorCentral/src/main/java/publicadores/PublicadorControlador.java**
   - Agregados métodos listarEventos() y listarCategorias()

## Pasos para Ejecutar

### 1. Compilar el Servidor Central
```powershell
cd ServidorCentral
mvn clean package
```

### 2. Iniciar el Servidor Central
```powershell
cd ServidorCentral\target
java -jar ServidorCentral-1.0-SNAPSHOT.jar
```

El servidor debería iniciarse en: http://localhost:9128/publicador

### 3. Regenerar el Cliente SOAP en el Servidor Web

Una vez que el Servidor Central esté corriendo, regenerar las clases cliente:

```powershell
cd servidor-web
mvn clean compile jaxws:wsimport
```

Esto generará las clases actualizadas en `servidor-web/src/main/java/soap/`

### 4. Compilar el Servidor Web
```powershell
cd servidor-web
mvn clean package
```

### 5. Desplegar el WAR

Copiar el archivo `servidor-web/target/web.war` a tu servidor de aplicaciones (Tomcat, etc.)

O si usas Tomcat standalone:
```powershell
copy servidor-web\target\web.war C:\ruta\a\tomcat\webapps\
```

### 6. Iniciar el servidor web y probar

Acceder a: http://localhost:8080/web/test-soap

Este servlet de prueba verificará:
- ✓ Conexión al servidor SOAP
- ✓ Método hola()
- ✓ Método obtenerEventos()
- ✓ Método listarEventos()
- ✓ Método listarCategorias()

## Pruebas Funcionales

### Test 1: Verificar conexión SOAP
- URL: http://localhost:8080/web/test-soap
- Debería mostrar todos los tests exitosos

### Test 2: Alta de Evento
1. Iniciar sesión como organizador
2. Ir a "Alta de Evento"
3. Llenar el formulario
4. El evento se creará via SOAP (PublicadorControlador.darAltaEvento)

### Test 3: Alta de Edición
1. Iniciar sesión como organizador
2. Ir a "Alta de Edición"
3. Seleccionar un evento
4. Llenar el formulario
5. La edición se creará via SOAP (PublicadorControlador.altaEdicionDeEvento)

## Notas Importantes

- **El Servidor Central DEBE estar corriendo** antes de iniciar el servidor web
- Si el Servidor Central se reinicia, el cliente SOAP se reconectará automáticamente
- Las imágenes aún no están implementadas via SOAP (pendiente)
- Solo eventos y ediciones usan SOAP por ahora, otros módulos seguirán

## Troubleshooting

### Error: "No se pudo conectar con el servidor SOAP"
- Verificar que el Servidor Central esté corriendo en http://localhost:9128/publicador
- Verificar el WSDL: http://localhost:9128/publicador?wsdl

### Error: "The method listarEventos() is undefined"
- Regenerar el cliente SOAP con: `mvn jaxws:wsimport`

### Error de compilación en servlets
- Verificar que las clases SOAP estén generadas en `servidor-web/src/main/java/soap/`
