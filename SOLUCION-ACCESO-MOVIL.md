# SOLUCIÓN: Acceso desde Dispositivo Móvil

## Problema identificado

El Servidor Central está configurado para escuchar solo en `localhost` (127.0.0.1), lo que significa que SOLO la computadora puede acceder a él. Tu teléfono móvil no puede conectarse.

## Solución

### 1. Reiniciar el Servidor Central con la nueva configuración

He creado un archivo de configuración que permite conexiones desde la red local.

**Detén el Servidor Central** (si está corriendo) presionando `Ctrl+C` en su terminal.

Luego, **reinicia el Servidor Central**:

```powershell
.\1-iniciar-servidor-central.bat
```

Deberías ver que ahora escucha en `0.0.0.0:9128` en lugar de `localhost:9128`.

### 2. Abrir puerto en el Firewall

Abre PowerShell **como Administrador** y ejecuta:

```powershell
# Puerto 9128 para el Servidor Central
New-NetFirewallRule -DisplayName "Servidor Central SOAP 9128" -Direction Inbound -LocalPort 9128 -Protocol TCP -Action Allow

# Puerto 8080 para Tomcat
New-NetFirewallRule -DisplayName "Tomcat 8080" -Direction Inbound -LocalPort 8080 -Protocol TCP -Action Allow
```

### 3. Verificar que Tomcat esté corriendo

```powershell
netstat -ano | findstr :8080
```

Si no aparece nada, inicia Tomcat nuevamente ejecutando el despliegue.

### 4. Acceder desde tu teléfono móvil

**Tu IP es:** `192.168.1.3`

En el navegador de tu teléfono (asegúrate de estar en la misma WiFi), abre:

```
http://192.168.1.3:8080/mobile
```

---

## Verificación de que todo funciona

### Desde tu computadora, verifica que los servicios están accesibles:

1. **Servidor Central:**
   ```
   http://192.168.1.3:9128/publicador?wsdl
   ```

2. **Aplicación Mobile:**
   ```
   http://192.168.1.3:8080/mobile
   ```

Si ambos funcionan desde tu computadora usando la IP (no localhost), entonces también funcionarán desde tu teléfono.

---

## Si aún se congela

### Problema posible: La aplicación mobile aún usa localhost

Si la aplicación mobile se cargó antes de reiniciar el Servidor Central, puede estar intentando conectarse a `localhost:9128` en lugar de usar la IP correcta.

**Solución:**

1. Detén Tomcat:
   ```powershell
   C:\apache-tomcat-11.0.4\bin\shutdown.bat
   ```

2. Limpia el despliegue:
   ```powershell
   Remove-Item "C:\apache-tomcat-11.0.4\webapps\mobile*" -Force -Recurse
   ```

3. Recompila la aplicación mobile:
   ```powershell
   cd mobile
   mvn clean package
   ```

4. Despliega nuevamente:
   ```powershell
   Copy-Item ".\target\web.war" "C:\apache-tomcat-11.0.4\webapps\mobile.war" -Force
   C:\apache-tomcat-11.0.4\bin\startup.bat
   ```

---

## Resumen de pasos:

1. ✅ **Archivo de configuración creado** en: `C:\Users\bruno\.eventosUy\servidor-central.properties`
2. ⏳ **Reiniciar Servidor Central** (presiona Ctrl+C y ejecuta el bat nuevamente)
3. ⏳ **Abrir puertos en Firewall** (9128 y 8080)
4. ⏳ **Acceder desde móvil** a: `http://192.168.1.3:8080/mobile`

---

## Usuarios para probar desde el móvil:

- **Usuario:** `atorres` | **Contraseña:** `25atorres`
- **Usuario:** `jgomez` | **Contraseña:** `25jgomez`

---

## Nota sobre el congelamiento

Si la página se "congela" (muestra una pantalla en blanco que carga indefinidamente), probablemente significa que:

1. La aplicación mobile está intentando conectarse al Servidor Central
2. Pero no puede alcanzarlo porque:
   - El Servidor Central está escuchando solo en localhost
   - O el firewall está bloqueando la conexión
   - O el Servidor Central no está corriendo

**Solución:** Sigue los pasos anteriores para reiniciar todo con la nueva configuración.
