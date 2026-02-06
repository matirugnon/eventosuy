# Iteración 2 - Aplicación Web con Servlets y JSP

## 📋 Descripción

La **Iteración 2** transforma EventosUy de una aplicación de escritorio a una **aplicación web** utilizando tecnologías Jakarta EE (Servlets y JSP), HTML5, CSS3 y JavaScript. Esta iteración implementa una arquitectura web MVC completa accesible desde navegadores.

---

## 🎯 Objetivos

- Migrar la aplicación desktop a arquitectura web
- Implementar frontend con HTML/CSS/JavaScript
- Desarrollar backend con Servlets y JSP
- Crear vistas diferenciadas por roles (Visitante, Asistente, Organizador)
- Implementar sistema de sesiones y autenticación

---

## 🏗️ Arquitectura Web MVC

```
┌───────────────────────────────────────────────┐
│          Cliente (Navegador)                  │
│      HTML + CSS + JavaScript                  │
└────────────────┬──────────────────────────────┘
                 │ HTTP
                 ▼
┌────────────────────────────────────────────────┐
│         Servidor Tomcat (Puerto 8080)          │
│                                                │
│  ┌──────────────────────────────────────────┐ │
│  │         Vista (JSP + HTML)               │ │
│  │  - index.html                            │ │
│  │  - asistente/*.html                      │ │
│  │  - organizador/*.html                    │ │
│  │  - visitante/*.html                      │ │
│  └──────────────┬───────────────────────────┘ │
│                 │                              │
│  ┌──────────────▼───────────────────────────┐ │
│  │      Controlador (Servlets)              │ │
│  │  - LoginServlet                          │ │
│  │  - ConsultaEventoServlet                 │ │
│  │  - RegistroAEdicionServlet               │ │
│  │  - etc.                                  │ │
│  └──────────────┬───────────────────────────┘ │
│                 │                              │
│  ┌──────────────▼───────────────────────────┐ │
│  │        Modelo (Lógica)                   │ │
│  │  - DTOs                                  │ │
│  │  - Validaciones                          │ │
│  │  - Sesiones (HttpSession)                │ │
│  └──────────────────────────────────────────┘ │
└────────────────────────────────────────────────┘
```

---

## 📂 Estructura de Carpetas

```
iteracion-2-web-servlets-jsp/
├── asistente/                      # Vistas para usuarios Asistente
│   ├── consultaEdicionAsistenteEv*.html
│   ├── consultaEventoAsistente*.html
│   ├── consultaPatrocinioAsistente.html
│   ├── consultaPerfil.html
│   ├── consultaRegistro*.html
│   ├── consultaTipodeRegistroAsistente*.html
│   ├── deporteAsistente.html
│   ├── innovacionAsistente.html
│   ├── listarUsuariosAsistente.html
│   ├── modificarusuario-atorres.html
│   ├── perfil-*.html
│   ├── principalAsistente.html
│   ├── registroAedicion.html
│   ├── registroaEdicionTR*.html
│   └── ...
│
├── organizador/                    # Vistas para usuarios Organizador
│   ├── consultaEdicionOrganizadorEv*.html
│   ├── consultaEventoOrganizador*.html
│   ├── edicionesOrganizadas*.html
│   ├── principalOrganizador.html
│   ├── registrosEdicion*.html
│   └── ...
│
├── visitante/                      # Vistas públicas (sin autenticación)
│   ├── consultaEdicionVisitanteEv*.html
│   ├── consultaEventoVisitante*.html
│   ├── innovacionVisitante.html
│   ├── listarUsuariosVisitante.html
│   ├── login.html
│   ├── principalVisitante.html
│   ├── signup.html
│   └── ...
│
├── img/                           # Recursos de imagen
│   ├── default.jpg
│   ├── eventos/
│   └── usuarios/
│
├── src/                           # Fuentes adicionales (si aplica)
│
├── index.html                     # Página principal
├── styles.css                     # Estilos CSS globales
├── sidebar.txt                    # Configuración de sidebar
└── README.txt                     # Documentación original
```

---

## 🎨 Frontend

### HTML5

**Estructura semántica:**
```html
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>EventosUy - Consulta de Evento</title>
    <link rel="stylesheet" href="../styles.css">
</head>
<body>
    <header>
        <nav>
            <!-- Menú de navegación -->
        </nav>
    </header>
    
    <aside class="sidebar">
        <!-- Navegación lateral -->
    </aside>
    
    <main class="content">
        <!-- Contenido principal -->
    </main>
    
    <footer>
        <!-- Pie de página -->
    </footer>
</body>
</html>
```

### CSS3

**Características utilizadas:**

```css
/* Variables CSS */
:root {
    --color-primary: #007bff;
    --color-secondary: #6c757d;
    --spacing: 1rem;
}

/* Flexbox para layout */
.container {
    display: flex;
    flex-direction: row;
}

/* Sidebar fijo */
.sidebar {
    position: fixed;
    left: 0;
    top: 0;
    height: 100vh;
    width: 250px;
}

/* Responsive */
@media (max-width: 768px) {
    .sidebar {
        width: 100%;
        position: relative;
    }
}

/* Transiciones */
.btn:hover {
    transition: background-color 0.3s ease;
}
```

**Archivo styles.css:**
- Estilos globales
- Layout de sidebar
- Estilos de formularios
- Botones y navegación
- Cards para eventos/ediciones
- Tipografía

### JavaScript

**Funcionalidades:**
```javascript
// Validación de formularios
function validarFormulario(form) {
    const nombre = form.nombre.value;
    if (!nombre || nombre.trim() === '') {
        alert('El nombre es obligatorio');
        return false;
    }
    return true;
}

// Navegación
function irAPagina(url) {
    window.location.href = url;
}

// Confirmaciones
function confirmarAccion() {
    return confirm('¿Está seguro de realizar esta acción?');
}

// Manejo de eventos
document.querySelector('#btnRegistrar').addEventListener('click', function(e) {
    e.preventDefault();
    // Lógica de registro
});
```

---

## 🔧 Backend (Servlets)

### Servlets Principales

| Servlet | URL Mapping | Descripción |
|---------|-------------|-------------|
| `LoginServlet` | `/login` | Autenticación de usuarios |
| `SignupServlet` | `/signup` | Registro de nuevos usuarios |
| `LogoutServlet` | `/logout` | Cierre de sesión |
| `InicioServlet` | `/inicio` | Página principal |
| `ConsultaEventoServlet` | `/consultaEvento` | Consulta de eventos |
| `ConsultaEdicionServlet` | `/consultaEdicion` | Consulta de ediciones |
| `ConsultaTipoRegistroServlet` | `/consultaTipoRegistro` | Consulta tipos de registro |
| `RegistroAEdicionServlet` | `/registroAedicion` | Registro a edición |
| `PerfilUsuarioServlet` | `/miPerfil`, `/perfilUsuario` | Perfil de usuario |
| `ModificarUsuarioServlet` | `/modificarUsuario` | Modificar datos de usuario |
| `ListarUsuariosServlet` | `/listarUsuarios` | Listar usuarios |
| `MisRegistrosServlet` | `/misRegistros` | Registros del usuario |
| `EdicionesOrganizadasServlet` | `/edicionesOrganizadas` | Ediciones organizadas |
| `ConsultaRegistroServlet` | `/consultaRegistro` | Consulta de registros |
| `ConsultaPatrocinioServlet` | `/consultaPatrocinio` | Consulta de patrocinios |
| `DescargaConstanciaServlet` | `/descargaConstancia` | Descargar constancia PDF |
| `VerificarDisponibilidadServlet` | `/verificarDisponibilidad` | Validar disponibilidad |
| `CargarDatosServlet` | `/cargarDatos` | Carga datos de prueba |

### Ejemplo de Servlet

```java
@WebServlet("/consultaEvento")
public class ConsultaEventoServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtener parámetros
        String nombreEvento = request.getParameter("evento");
        
        // Procesar lógica
        // (En esta iteración, aún usa lógica en memoria)
        IControladorEvento ctrl = Factory.getInstance().getIControladorEvento();
        
        try {
            DtEvento evento = ctrl.obtenerEvento(nombreEvento);
            
            // Preparar datos para la vista
            request.setAttribute("evento", evento);
            request.setAttribute("ediciones", evento.getEdiciones());
            
            // Forward a JSP
            request.getRequestDispatcher("/consultaEvento.jsp")
                   .forward(request, response);
                   
        } catch (EventoNoExisteException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/error.jsp")
                   .forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lógica para POST (si aplica)
    }
}
```

---

## 📄 JSP (JavaServer Pages)

### Ejemplo de Vista JSP

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Consulta de Evento</title>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
</head>
<body>
    <jsp:include page="/WEB-INF/includes/header.jsp" />
    
    <div class="container">
        <h1>Evento: <c:out value="${evento.nombre}"/></h1>
        
        <div class="evento-detalle">
            <p><strong>Descripción:</strong> ${evento.descripcion}</p>
            <p><strong>Fecha Inicio:</strong> 
               <fmt:formatDate value="${evento.fechaInicio}" pattern="dd/MM/yyyy"/>
            </p>
            <p><strong>Fecha Fin:</strong> 
               <fmt:formatDate value="${evento.fechaFin}" pattern="dd/MM/yyyy"/>
            </p>
        </div>
        
        <h2>Ediciones</h2>
        <c:choose>
            <c:when test="${not empty ediciones}">
                <ul>
                    <c:forEach items="${ediciones}" var="edicion">
                        <li>
                            <a href="consultaEdicion?edicion=${edicion.nombre}">
                                <c:out value="${edicion.nombre}"/>
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:otherwise>
                <p>No hay ediciones disponibles.</p>
            </c:otherwise>
        </c:choose>
    </div>
    
    <jsp:include page="/WEB-INF/includes/footer.jsp" />
</body>
</html>
```

---

## 🔐 Autenticación y Sesiones

### LoginServlet

```java
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");
        
        IControladorUsuario ctrl = Factory.getInstance().getIControladorUsuario();
        
        try {
            DtUsuario usuario = ctrl.autenticar(nickname, password);
            
            // Crear sesión
            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioLogueado", usuario);
            session.setAttribute("nickname", nickname);
            session.setAttribute("rol", usuario.getRol()); // "Asistente" o "Organizador"
            
            // Redirigir según rol
            if ("Asistente".equals(usuario.getRol())) {
                response.sendRedirect("asistente/principalAsistente.html");
            } else {
                response.sendRedirect("organizador/principalOrganizador.html");
            }
            
        } catch (AutenticacionException e) {
            request.setAttribute("error", "Credenciales incorrectas");
            request.getRequestDispatcher("visitante/login.html")
                   .forward(request, response);
        }
    }
}
```

### Protección de Vistas

**Filtro (ContadorVisitasFilter):**
```java
@WebFilter("/*")
public class ContadorVisitasFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        
        String path = request.getServletPath();
        HttpSession session = request.getSession(false);
        
        // Verificar autenticación para rutas protegidas
        if (path.startsWith("/asistente/") || path.startsWith("/organizador/")) {
            if (session == null || session.getAttribute("usuarioLogueado") == null) {
                response.sendRedirect(request.getContextPath() + "/visitante/login.html");
                return;
            }
        }
        
        chain.doFilter(request, response);
    }
}
```

---

## 🗂️ Vistas por Rol

### Visitante (Sin autenticación)
- `login.html` - Inicio de sesión
- `signup.html` - Registro de usuario
- `principalVisitante.html` - Página principal pública
- `consultaEventoVisitante*.html` - Consulta de eventos
- `listarUsuariosVisitante.html` - Listar usuarios
- `innovacionVisitante.html` - Eventos de innovación

### Asistente (Requiere login)
- `principalAsistente.html` - Dashboard del asistente
- `consultaEdicionAsistenteEv*.html` - Consulta de ediciones
- `registroAedicion.html` - Registrarse a edición
- `misRegistros.html` - Mis registros
- `consultaPerfil.html` - Ver perfil
- `modificarusuario-*.html` - Modificar perfil
- `consultaTipodeRegistroAsistente*.html` - Tipos de registro
- `deporteAsistente.html` - Eventos deportivos
- `innovacionAsistente.html` - Eventos de innovación

### Organizador (Requiere login)
- `principalOrganizador.html` - Dashboard del organizador
- `edicionesOrganizadas*.html` - Mis ediciones organizadas
- `registrosEdicion*.html` - Registros de mis ediciones
- `consultaEdicionOrganizador*.html` - Consulta de ediciones

---

## 📊 DTOs (Data Transfer Objects)

Aunque en esta iteración aún se usa lógica en memoria, se introducen DTOs básicos:

```java
public class EventoDTO {
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String descripcion;
    private List<EdicionDTO> ediciones;
    
    // Getters y setters
}

public class UsuarioDTO {
    private String nickname;
    private String nombre;
    private String apellido;
    private String rol; // "Asistente" o "Organizador"
    
    // Getters y setters
}
```

---

## 🎨 Diseño y UX

### Navegación con Sidebar

```html
<aside class="sidebar">
    <h2>EventosUy</h2>
    <ul>
        <li><a href="principalAsistente.html">Inicio</a></li>
        <li><a href="consultaEventoAsistente01.html">Eventos</a></li>
        <li><a href="registroAedicion.html">Registrarme</a></li>
        <li><a href="misRegistros.html">Mis Registros</a></li>
        <li><a href="consultaPerfil.html">Mi Perfil</a></li>
        <li><a href="../logout">Cerrar Sesión</a></li>
    </ul>
</aside>
```

### Componentes Visuales
- **Cards** para eventos y ediciones
- **Formularios** estilizados
- **Tablas** para listados
- **Botones** con estados (hover, active)
- **Alertas** para mensajes

---

## 🔄 Flujo de Navegación

```
Visitante
    └─→ login.html
         ├─→ (Asistente) principalAsistente.html
         │       ├─→ consultaEvento
         │       ├─→ registroAedicion
         │       ├─→ misRegistros
         │       └─→ miPerfil
         │
         └─→ (Organizador) principalOrganizador.html
                 ├─→ edicionesOrganizadas
                 ├─→ registrosEdicion
                 └─→ consultaEdicion
```

---

## 🚀 Despliegue

### Despliegue en Tomcat

1. **Copiar archivos:**
   ```bash
   cp -r iteracion-2-web-servlets-jsp $CATALINA_HOME/webapps/eventosuy-v2/
   ```

2. **Iniciar Tomcat:**
   ```bash
   $CATALINA_HOME/bin/startup.sh
   ```

3. **Acceder:**
   ```
   http://localhost:8080/eventosuy-v2/
   ```

### Servidor HTTP Simple (Solo HTML)

```bash
cd iteracion-2-web-servlets-jsp
python -m http.server 8000
```

Acceder en: http://localhost:8000

---

## 📝 Casos de Uso Implementados

Esta iteración implementa versiones web de:

✅ Alta de Usuario (signup.html)  
✅ Consulta de Usuario (listarUsuarios)  
✅ Modificar Usuario (modificarusuario)  
✅ Consulta de Evento (consultaEvento)  
✅ Consulta de Edición (consultaEdicion)  
✅ Consulta de Tipo de Registro (consultaTipoRegistro)  
✅ Registro a Edición (registroAedicion)  
✅ Consulta de Registro (consultaRegistro)  
✅ Mis Registros (misRegistros)  
✅ Ediciones Organizadas (edicionesOrganizadas)  
✅ Consulta de Patrocinio (consultaPatrocinio)  

---

## ⚠️ Limitaciones

1. **Sin persistencia real**: Aún usa datos en memoria (sesiones HTTP)
2. **No es un WAR completo**: Archivos HTML estáticos sin servlets compilados
3. **Validaciones limitadas**: Principalmente en cliente (JavaScript)
4. **Sin HTTPS**: Autenticación no segura
5. **Sesiones básicas**: Sin timeout configurado

---

## 🔄 Evolución a Iteración 3

Esta iteración prepara el terreno para:
- Separación completa frontend/backend (SOAP)
- Empaquetado como WAR
- Servlets consumiendo servicios remotos
- Persistencia real con JPA

---

## 📚 Notas del README Original

Ver [README.txt](README.txt) para notas de desarrollo originales:
- Casos de uso pendientes
- Consideraciones de diseño
- Links entre páginas a completar

---

**Desarrollado por:** Equipo EventosUy  
**Asignatura:** Taller de Programación - Facultad de Ingeniería UdelaR
