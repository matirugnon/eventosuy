# Análisis de Uso de SOAP en servidor-web

## ⚠️ ESTADO ACTUAL: ARQUITECTURA HÍBRIDA

Actualmente el sistema tiene una arquitectura **HÍBRIDA** donde:
- **Algunos servlets** usan SOAP (llaman a ServidorCentral vía Web Service)
- **La mayoría de servlets** usan lógica directa (instancian controladores localmente)

---

## ✅ SERVLETS QUE SÍ USAN SOAP (Centralizados)

### 1. **AltaEventoServlet.java** ✅
- **Ruta:** `/altaEvento`
- **SOAP:** `SoapClientHelper.getPublicadorControlador()`
- **Método SOAP:** `publicador.darAltaEvento()`
- **Referencia:** `servidor-web/src/main/java/servlets/AltaEventoServlet.java:111`

### 2. **AltaEdicionServlet.java** ✅
- **Ruta:** `/altaEdicion`
- **SOAP:** `SoapClientHelper.getPublicadorControlador()`
- **Método SOAP:** `publicador.altaEdicionDeEvento()`
- **Referencia:** `servidor-web/src/main/java/servlets/AltaEdicionServlet.java:121`

### 3. **TestSoapServlet.java** ✅
- **Ruta:** `/test-soap`
- **SOAP:** `SoapClientHelper.getPublicadorControlador()`
- **Métodos SOAP:** `hola()`, `obtenerEventos()`
- **Referencia:** `servidor-web/src/main/java/servlets/TestSoapServlet.java`

---

## ❌ SERVLETS QUE NO USAN SOAP (Lógica Directa)

### Gestión de Usuarios
- **SignupServlet.java** ❌ - Usa `IControladorUsuario.getInstance()`
- **loginServlet.java** ❌ - Usa `IControladorUsuario.getInstance()`
- **LogoutServlet.java** ❌ - Manejo de sesiones
- **ModificarUsuarioServlet.java** ❌ - Usa `IControladorUsuario.getInstance()`
- **ListarUsuariosServlet.java** ❌ - Usa `IControladorUsuario.getInstance()`
- **PerfilUsuarioServlet.java** ❌ - Usa `IControladorUsuario.getInstance()`

### Gestión de Instituciones
- **AltaInstitucionServlet.java** ❌ - Usa `IControladorUsuario.getInstance()` y `IControladorEvento.getInstance()`

### Gestión de Patrocinios
- **AltaPatrocinioServlet.java** ❌ - Usa controladores directos
- **ConsultaPatrocinioServlet.java** ❌ - Usa `IControladorEvento.getInstance()` y `IControladorUsuario.getInstance()`

### Gestión de Registros
- **RegistroAEdicionServlet.java** ❌ - Usa controladores directos
- **ConsultaRegistroServlet.java** ❌ - Usa controladores directos
- **MisRegistrosServlet.java** ❌ - Usa controladores directos
- **RegistrosEdicionServlet.java** ❌ - Usa `IControladorEvento.getInstance()`

### Tipos de Registro
- **AltaTipoRegistroServlet.java** ❌ - Usa controladores directos
- **ConsultaTipoRegistroServlet.java** ❌ - Usa controladores directos

### Consultas
- **inicioServlet.java** ❌ - Usa `IControladorEvento.getInstance()`, `IControladorUsuario.getInstance()`, `IControladorRegistro.getInstance()`
- **ConsultaEventoServlet.java** ❌ - Usa controladores directos
- **ConsultaEdicionServlet.java** ❌ - Usa controladores directos
- **EdicionesOrganizadasServlet.java** ❌ - Usa controladores directos

### Utilidades
- **CargarDatosServlet.java** ❌ - Usa `Utils.cargarDatos()` que internamente usa controladores directos

---

## 🎯 RECOMENDACIONES PARA CENTRALIZAR

Para cumplir con el objetivo de **"centralizar todo en el ServidorCentral"**, necesitas:

### 1. **Agregar métodos SOAP a PublicadorControlador**

Debes agregar en `ServidorCentral/src/main/java/publicadores/PublicadorControlador.java`:

```java
// Usuarios
@WebMethod
public boolean darAltaAsistente(String nickname, String nombre, String apellido, String email, 
                                 DTFecha fechaNacimiento, String password);

@WebMethod
public boolean darAltaOrganizador(String nickname, String nombre, String apellido, String email, 
                                   DTFecha fechaNacimiento, String password, String biografia, String link);

@WebMethod
public DTUsuario iniciarSesion(String emailOrNickname, String password);

@WebMethod
public String[] listarUsuarios();

@WebMethod
public DTUsuario obtenerUsuario(String nickname);

// Registros
@WebMethod
public boolean registrarAEdicion(String nickAsistente, String evento, String edicion, DTFecha fechaRegistro);

@WebMethod
public DTRegistro[] obtenerRegistrosDeAsistente(String nickAsistente);

// Patrocinios
@WebMethod
public boolean darAltaPatrocinio(String evento, String edicion, String nickInstitucion, 
                                  String descripcion, String tipo, DTFecha fecha, double monto);

// Tipos de Registro
@WebMethod
public boolean darAltaTipoRegistro(String evento, String edicion, String nombre, 
                                    DTFecha fechaCierre, double costo);

// Y más...
```

### 2. **Modificar los servlets uno por uno**

Ejemplo para **SignupServlet.java**:

**ANTES (Lógica directa):**
```java
IControladorUsuario ctrl = IControladorUsuario.getInstance();
ctrl.darAltaAsistente(nickname, nombre, apellido, email, fechaNac, password);
```

**DESPUÉS (SOAP):**
```java
PublicadorControlador publicador = SoapClientHelper.getPublicadorControlador();
soap.DTFecha fechaSoap = new soap.DTFecha();
fechaSoap.setDia(dia);
fechaSoap.setMes(mes);
fechaSoap.setAnio(anio);
publicador.darAltaAsistente(nickname, nombre, apellido, email, fechaSoap, password);
```

### 3. **Regenerar el cliente SOAP**

Después de agregar métodos a PublicadorControlador:
```powershell
cd servidor-web
mvn jaxws:wsimport
```

Esto regenerará las clases en `servidor-web/src/main/java/soap/`

---

## 📋 CHECKLIST DE MIGRACIÓN

- [x] **AltaEventoServlet** - ✅ Usa SOAP
- [x] **AltaEdicionServlet** - ✅ Usa SOAP
- [ ] **SignupServlet** - ⚠️ Migrar a SOAP
- [ ] **loginServlet** - ⚠️ Migrar a SOAP
- [ ] **ModificarUsuarioServlet** - ⚠️ Migrar a SOAP
- [ ] **AltaInstitucionServlet** - ⚠️ Migrar a SOAP
- [ ] **AltaPatrocinioServlet** - ⚠️ Migrar a SOAP
- [ ] **RegistroAEdicionServlet** - ⚠️ Migrar a SOAP
- [ ] **AltaTipoRegistroServlet** - ⚠️ Migrar a SOAP
- [ ] **inicioServlet** - ⚠️ Migrar a SOAP (listarEventos, listarUsuarios)
- [ ] **Todos los servlets de Consulta** - ⚠️ Migrar a SOAP

---

## 🔧 PASOS PARA MIGRAR UN SERVLET

1. **Agregar método @WebMethod en PublicadorControlador.java**
2. **Reiniciar ServidorCentral** (`.\1-iniciar-servidor-central.bat`)
3. **Regenerar cliente SOAP** en servidor-web (`mvn jaxws:wsimport`)
4. **Modificar el servlet** para usar `SoapClientHelper` en lugar de `IControlador`
5. **Recompilar WAR** (`.\2-compilar-webserver.bat`)
6. **Redesplegar en Tomcat** (`.\4-desplegar-tomcat.bat`)
7. **Probar funcionalidad**

---

## 🚀 ARQUITECTURA OBJETIVO

```
┌─────────────────┐
│  Navegador Web  │
└────────┬────────┘
         │ HTTP
         ▼
┌─────────────────┐
│  Tomcat (8080)  │
│  servidor-web   │
│   (Servlets)    │
└────────┬────────┘
         │ SOAP/HTTP
         ▼
┌─────────────────┐
│ServidorCentral  │◄──── También se conectará
│    (9128)       │      la app móvil
│ PublicadorCtrl  │
│  (SOAP Server)  │
└────────┬────────┘
         │
         ▼
    [Lógica + BD]
```

**IMPORTANTE:** Con esta arquitectura:
- ✅ El webserver será un **cliente SOAP puro**
- ✅ La app móvil usará el **mismo SOAP server**
- ✅ Toda la lógica estará **centralizada en ServidorCentral**
- ✅ La base de datos HSQLDB solo existe en ServidorCentral

---

## 📝 NOTAS

- Actualmente tienes **lógica duplicada** (servidor-web tiene copia de las clases de logica/*)
- Esto fue necesario porque mezclaste SOAP con lógica directa
- Una vez que migres TODO a SOAP, podrás **eliminar** las carpetas `logica/` y `excepciones/` de servidor-web
- El scope `provided` en el pom.xml garantiza que el JAR de servidor-central NO se incluya en el WAR

