# 🎯 GUÍA RÁPIDA - Ejecución del Proyecto

## ✅ Cambios Realizados
Tu webserver ahora llama al **PublicadorControlador** via SOAP en lugar de llamar directamente a la lógica.

---

## 🚀 EJECUTAR EN 3 PASOS

### 1️⃣ Iniciar Servidor Central
```powershell
.\1-iniciar-servidor-central.bat
```
**Espera ver:** "Publicando Servidor Central en: http://localhost:9128/publicador"

---

### 2️⃣ Compilar Web Server (EN OTRA TERMINAL)
```powershell
.\2-compilar-webserver.bat
```
**Espera ver:** "✅ Compilación exitosa"

---

### 3️⃣ Desplegar en Tomcat
```powershell
# Copiar el WAR
copy servidor-web\target\web.war C:\ruta\a\tomcat\webapps\

# Iniciar Tomcat (si no está corriendo)
C:\ruta\a\tomcat\bin\startup.bat
```

---

## 🧪 PRUEBAS

### Test 1: Conexión SOAP
```powershell
.\3-test-soap.bat
```
O abre: http://localhost:8080/web/test-soap

**Debes ver:**
- ✅ Servidor SOAP disponible
- ✅ Respuesta: "Hola desde el Servidor Central"

---

### Test 2: Alta de Evento
1. Login como organizador
2. Ir a "Alta de Evento"
3. Crear un evento de prueba
4. **Resultado:** Mensaje de éxito

**Lo que pasa:** 
```
WebServer → SOAP → ServidorCentral → Base de Datos
```

---

### Test 3: Alta de Edición
1. Login como organizador
2. Ir a "Alta de Edición"
3. Seleccionar un evento
4. Crear una edición
5. **Resultado:** Mensaje de éxito

---

## ⚠️ IMPORTANTE

### El ServidorCentral DEBE estar corriendo
Antes de iniciar el webserver, el ServidorCentral debe estar ejecutándose.

### Para listas dinámicas (eventos/categorías reales)
Después de iniciar el ServidorCentral por primera vez:
```powershell
cd servidor-web
mvn jaxws:wsimport
mvn package
# Redesplegar WAR
```

---

## 📚 Documentación Completa

- **RESUMEN-CAMBIOS.md** - Explicación detallada de todos los cambios
- **INSTRUCCIONES-WEBSERVER-SOAP.md** - Instrucciones técnicas completas

---

## 🐛 Problemas Comunes

**Error: "No se pudo conectar con el servidor SOAP"**
→ Verifica que el ServidorCentral esté corriendo en el puerto 9128

**No aparecen eventos en el formulario**
→ Regenera el cliente SOAP: `mvn jaxws:wsimport` (con ServidorCentral corriendo)

---

¡Listo! 🎉
