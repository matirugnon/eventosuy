# Configuración para Acceso Móvil

## Permitir acceso desde dispositivos móviles

### 1. Obtener la IP de tu computadora

En PowerShell:
```powershell
ipconfig
```

Anota tu **IPv4 Address** (ejemplo: `192.168.1.105`)

---

### 2. Verificar el Firewall de Windows

El firewall de Windows puede bloquear las conexiones. Para permitirlas:

#### Opción A: Abrir puerto 8080 en el Firewall

```powershell
# Ejecutar como Administrador
New-NetFirewallRule -DisplayName "Tomcat 8080" -Direction Inbound -LocalPort 8080 -Protocol TCP -Action Allow
```

#### Opción B: Mediante la interfaz gráfica

1. Abre **Panel de Control** → **Sistema y seguridad** → **Firewall de Windows Defender**
2. Click en **Configuración avanzada**
3. Click en **Reglas de entrada** → **Nueva regla**
4. Selecciona **Puerto** → Siguiente
5. Selecciona **TCP** y escribe **8080** → Siguiente
6. Selecciona **Permitir la conexión** → Siguiente
7. Marca todas las opciones (Dominio, Privado, Público) → Siguiente
8. Dale un nombre como "Tomcat Mobile" → Finalizar

---

### 3. Asegúrate de que tu teléfono esté en la misma red WiFi

Tu computadora y tu teléfono deben estar conectados a la **misma red WiFi**.

---

### 4. Acceder desde el teléfono

En el navegador de tu teléfono móvil, ingresa:

```
http://TU_IP:8080/mobile
```

Por ejemplo, si tu IP es `192.168.1.105`:
```
http://192.168.1.105:8080/mobile
```

---

## Solución de problemas

### No puedo acceder desde el teléfono

1. **Verifica la conexión:**
   - Asegúrate de que ambos dispositivos estén en la misma red WiFi
   - Prueba hacer ping desde PowerShell:
     ```powershell
     ping 192.168.1.105
     ```

2. **Verifica que Tomcat esté corriendo:**
   - Desde la computadora, accede a `http://localhost:8080/mobile`
   - Si funciona localmente, el problema es la red/firewall

3. **Verifica el firewall:**
   - Desactiva temporalmente el firewall para probar:
     ```powershell
     # Ejecutar como Administrador
     Set-NetFirewallProfile -Profile Domain,Public,Private -Enabled False
     ```
   - Si funciona, vuelve a activarlo y crea la regla específica del paso 2
     ```powershell
     Set-NetFirewallProfile -Profile Domain,Public,Private -Enabled True
     ```

4. **Prueba con la IP en la computadora:**
   - En lugar de `localhost`, usa tu IP también en la PC:
     ```
     http://192.168.1.105:8080/mobile
     ```
   - Si no funciona, hay un problema de configuración de Tomcat

---

## Configuración alternativa: Cambiar puerto de Tomcat

Si el puerto 8080 está bloqueado, puedes cambiar Tomcat a otro puerto (ej: 9090):

1. Edita: `TOMCAT_HOME/conf/server.xml`
2. Busca la línea:
   ```xml
   <Connector port="8080" protocol="HTTP/1.1"
   ```
3. Cámbiala a:
   ```xml
   <Connector port="9090" protocol="HTTP/1.1"
   ```
4. Reinicia Tomcat
5. Accede desde el móvil: `http://TU_IP:9090/mobile`

---

## Método alternativo: Usando ngrok (Túnel público)

Si tienes problemas con la red local, puedes usar **ngrok** para crear un túnel público:

### 1. Instala ngrok
```powershell
# Con winget (Windows 11)
winget install ngrok

# O descarga desde: https://ngrok.com/download
```

### 2. Inicia el túnel
```powershell
ngrok http 8080
```

### 3. Usa la URL pública
ngrok te dará una URL pública como:
```
https://abc123.ngrok.io
```

Accede desde tu móvil:
```
https://abc123.ngrok.io/mobile
```

⚠️ **Nota:** La URL de ngrok cambia cada vez que lo reinicias (a menos que tengas cuenta premium).

---

## Verificar que el diseño sea responsive

Una vez que accedas desde el móvil, verifica que el diseño se adapte correctamente. Si no es así, necesitarás implementar Bootstrap (como mencionamos antes) para mejorar la experiencia móvil.

---

## Resumen rápido

1. ✅ Obtén tu IP: `ipconfig`
2. ✅ Abre puerto 8080 en el firewall
3. ✅ Conecta ambos dispositivos a la misma WiFi
4. ✅ Accede desde el móvil: `http://TU_IP:8080/mobile`
