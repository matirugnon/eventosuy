<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>Modificar Usuario · ${nickname}</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css"/>
  <style>
    .auth-card { width: 760px; max-width: 95%; margin: 0 auto; }
    .grid-2 { display:grid; grid-template-columns:1fr 1fr; gap:.75rem; }
    @media (max-width: 640px){ .grid-2{ grid-template-columns:1fr; } }
    .form-group { display:flex; flex-direction:column; gap:.35rem; }
    .form-group input, .form-group select, .form-group textarea {
      width:100%; padding:.55rem .7rem; border:1px solid #ddd; border-radius:10px;
    }
    .help { color:#666; font-size:.9rem; }
    .readonly { background-color: #f5f5f5; color: #666; }
  </style>
</head>
<body>
  <!-- Header -->
  <header class="header">
    <h1><a href="${pageContext.request.contextPath}/inicio" style="color: inherit; text-decoration: none;">eventos.uy</a></h1>
    <div class="header-right">
      <c:choose>
        <c:when test="${not empty role}">
          <div class="user-badge" style="display: flex; align-items: center; gap: 0.5rem;">
            <a href="${pageContext.request.contextPath}/miPerfil" style="display:flex;align-items:center;gap:.5rem;text-decoration:none;color:inherit;">
              <img class="avatar" 
                   src="${not empty avatar ? pageContext.request.contextPath.concat(avatar) : pageContext.request.contextPath.concat('/img/eventoSinImagen.jpeg')}" 
                   alt="Avatar de usuario" />
              <span class="nickname">${nickname}</span>
            </a>
            <a href="${pageContext.request.contextPath}/logout" class="btn-primary">Cerrar sesión</a>
          </div>
        </c:when>
        <c:otherwise>
          <a href="${pageContext.request.contextPath}/login" class="btn-primary">Iniciar sesión</a>
          <a href="${pageContext.request.contextPath}/signup" class="btn-primary">Registrarse</a>
        </c:otherwise>
      </c:choose>
    </div>
  </header>

  <div class="content single">
    <main>
      <section class="auth-container">
        <div class="auth-card">
          <h2>Modificar datos — ${tipoUsuario}</h2>

          <!-- Mostrar mensaje de error si existe -->
          <c:if test="${not empty error}">
            <div style="color:#c00; margin-bottom:1rem; padding:0.5rem; background:#ffe6e6; border-radius:4px;">
              ${error}
            </div>
          </c:if>

          <!-- Mostrar mensaje de éxito si existe -->
          <c:if test="${not empty mensaje}">
            <div style="color:#2a7f2e; margin-bottom:1rem; padding:0.5rem; background:#e6ffe6; border-radius:4px;">
              ${mensaje}
            </div>
          </c:if>

          <form class="auth-form" action="${pageContext.request.contextPath}/modificarUsuario" method="post" enctype="multipart/form-data">
            <!-- Campos no editables -->
            <div class="grid-2">
              <label class="form-group">
                <span>Nickname</span>
                <input value="${usuario.nickname}" readonly class="readonly" />
              </label>
              <label class="form-group">
                <span>Correo electrónico</span>
                <input value="${usuario.correo}" readonly class="readonly" />
              </label>
            </div>

            <!-- Campos editables básicos -->
            <div class="grid-2">
              <label class="form-group">
                <span>Nombre</span>
                <input name="nombre" value="${param.nombre != null ? param.nombre : usuario.nombre}" required maxlength="60" />
              </label>
              
              <!-- Campo específico para Asistente -->
              <c:if test="${tipoUsuario == 'Asistente'}">
                <label class="form-group">
                  <span>Apellido</span>
                  <input name="apellido" value="${param.apellido != null ? param.apellido : asistente.apellido}" required maxlength="60" />
                </label>
              </c:if>
              
              <!-- Espacio vacío para Organizador -->
              <c:if test="${tipoUsuario == 'Organizador'}">
                <div></div>
              </c:if>
            </div>

            <!-- Campos específicos según tipo de usuario -->
            <c:choose>
              <c:when test="${tipoUsuario == 'Asistente'}">
                <div class="grid-2">
                  <label class="form-group">
                    <span>Fecha de nacimiento</span>
                    <input name="fechaNac" 
                           value="${param.fechaNac != null ? param.fechaNac : asistente.fechaNacimiento.dia}/${asistente.fechaNacimiento.mes}/${asistente.fechaNacimiento.anio}" 
                           placeholder="DD/MM/YYYY" />
                  </label>
                  <label class="form-group">
                    <span>Institución</span>
                    <select name="institucion">
                      <option value="">(Sin institución)</option>
                      <c:forEach var="inst" items="${instituciones}">
                        <option value="${inst}" 
                                ${(param.institucion != null ? param.institucion : asistente.institucion) == inst ? 'selected' : ''}>
                          ${inst}
                        </option>
                      </c:forEach>
                    </select>
                  </label>
                </div>
              </c:when>
              
              <c:when test="${tipoUsuario == 'Organizador'}">
                <label class="form-group">
                  <span>Descripción</span>
                  <textarea name="descripcion" rows="3" maxlength="500" 
                            placeholder="Descripción de la organización...">${param.descripcion != null ? param.descripcion : organizador.descripcion}</textarea>
                </label>
                
                <label class="form-group">
                  <span>Sitio web</span>
                  <input name="web" type="url" 
                         value="${param.web != null ? param.web : organizador.link}" 
                         placeholder="https://mi-sitio.org" />
                </label>
              </c:when>
            </c:choose>

            <!-- Imagen -->
            <label class="form-group">
              <span>Imagen</span>
              <input name="imagen" type="file" accept="image/*" />
              <span class="help">
                <c:choose>
                  <c:when test="${not empty usuario.avatar}">
                    Imagen actual: <code>${usuario.avatar}</code>
                  </c:when>
                  <c:otherwise>
                    Sin imagen actual. Selecciona una imagen para agregar.
                  </c:otherwise>
                </c:choose>
              </span>
            </label>

            <!-- Seguridad -->
            <div class="grid-2">
              <label class="form-group">
                <span>Nueva contraseña</span>
                <input type="password" name="password" minlength="6" placeholder="Dejar vacío para mantener actual" />
              </label>
              <label class="form-group">
                <span>Confirmar contraseña</span>
                <input type="password" name="confirm" minlength="6" placeholder="Confirmar nueva contraseña" />
              </label>
            </div>
            <p class="help">El nickname y el correo no se pueden modificar. Si no deseas cambiar la contraseña, deja los campos vacíos.</p>

            <div style="display:flex; gap:.5rem; margin-top:.5rem;">
              <button class="btn-primary" type="submit">Guardar cambios</button>
              <a class="btn-outline" href="${pageContext.request.contextPath}/miPerfil">Cancelar</a>
            </div>
          </form>
        </div>
      </section>
    </main>
  </div>

  <footer></footer>

  <script>
    // Script para validación de contraseñas
    document.addEventListener('DOMContentLoaded', function() {
      const form = document.querySelector('form');
      const passwordInput = document.querySelector('input[name="password"]');
      const confirmInput = document.querySelector('input[name="confirm"]');
      
      form.addEventListener('submit', function(e) {
        const password = passwordInput.value.trim();
        const confirm = confirmInput.value.trim();
        
        // Si se está intentando cambiar la contraseña
        if (password || confirm) {
          if (password.length < 6) {
            alert('La contraseña debe tener al menos 6 caracteres');
            e.preventDefault();
            return;
          }
          
          if (password !== confirm) {
            alert('Las contraseñas no coinciden');
            e.preventDefault();
            return;
          }
        }
      });
      
      // Validación en tiempo real
      confirmInput.addEventListener('input', function() {
        const password = passwordInput.value;
        const confirm = this.value;
        
        if (password && confirm && password !== confirm) {
          this.setCustomValidity('Las contraseñas no coinciden');
        } else {
          this.setCustomValidity('');
        }
      });
    });
  </script>
</body>
</html>