<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>Perfil de Usuario · eventos.uy</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css"/>
  <link rel="preconnect" href="https://fonts.googleapis.com"/>
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet"/>
  <style>
    .auth-card { width: 760px; max-width: 95%; margin: 0 auto; }
    .table { width:100%; border-collapse: collapse; }
    .table th, .table td { padding:.6rem; border-bottom:1px solid #ddd; text-align:left; }
    .actions { display:flex; gap:.5rem; flex-wrap:wrap; }
    .btn-outline { display:inline-block; padding:.4rem .7rem; border:1px solid #ccc; border-radius:10px; text-decoration:none; }
    .user-details-list p { margin: 0.5rem 0; }
  </style>
</head>
<body>
  <div>
    <!-- Header -->
    <header class="header">
      <h1><a href="${pageContext.request.contextPath}/inicio" style="color: inherit; text-decoration: none;">eventos.uy</a></h1>
      <div class="header-right">
        <c:choose>
          <c:when test="${not empty role}">
            <div class="user-badge" style="display: flex; align-items: center; gap: 0.5rem;">
              <img class="avatar" src="${avatar}" alt="Avatar de usuario" />
              <span class="nickname">${nickname}</span>
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

    <div class="content">
      <!-- Sidebar -->
      <aside class="sidebar">
        <c:choose>
          <c:when test="${role == 'organizador'}">
            <div class="panel sidebar">
              <div class="panel-header">Mi perfil</div>
              <ul class="menu-list">
                <li><a href="#">Alta Evento</a></li>
                <li><a href="#">Alta Edición</a></li>
                <li><a href="#">Alta Institución</a></li>
                <li><a href="#">Alta Tipo de Registro</a></li>
                <li><a href="#">Alta Patrocinio</a></li>
                <li><a href="#">Consulta Registro</a></li>
              </ul>
            </div>
          </c:when>
          <c:when test="${role == 'asistente'}">
            <div class="panel sidebar">
              <div class="panel-header">Mi perfil</div>
              <ul class="menu-list">
                <li><a href="#">Registro a Edición</a></li>
              </ul>
            </div>
          </c:when>
        </c:choose>

        <!-- Categorías -->
        <div class="panel sidebar" style="margin-top: 1rem;">
          <div class="panel-header">Categorías</div>
          <ul class="menu-list">
            <c:choose>
              <c:when test="${empty categorias}">
                <li><span class="muted">No hay categorías disponibles.</span></li>
              </c:when>
              <c:otherwise>
                <li>
                  <c:url var="urlTodas" value="/inicio"/>
                  <a href="${urlTodas}">Todas</a>
                </li>
                <c:forEach var="categoria" items="${categorias}">
                  <li>
                    <c:url var="categoriaUrl" value="/inicio">
                      <c:param name="categoria" value="${categoria}" />
                    </c:url>
                    <a href="${categoriaUrl}">${categoria}</a>
                  </li>
                </c:forEach>
              </c:otherwise>
            </c:choose>
          </ul>
        </div>

        <!-- Botón "Ver listado de Usuarios" -->
        <div style="margin-top: 2rem; border-top: 1px solid #e0e0e0; padding-top: 1rem;">
          <a href="${pageContext.request.contextPath}/listarUsuarios" style="
              display: flex;
              align-items: center;
              gap: 0.5rem;
              color: #182080;
              font-weight: 600;
              text-decoration: none;
              padding: 0.75rem;
              border-radius: 6px;
              transition: background-color 0.2s;
              background-color: rgba(24, 32, 128, 0.05);">
              Ver listado de usuarios
          </a>
        </div>
      </aside>

      <!-- Main: Perfil de Usuario -->
      <main>
        <section class="auth-container">
          <div class="auth-card">
            <h2>Perfil de Usuario</h2>

            <!-- Encabezado: avatar + datos del usuario -->
            <div style="display:flex; gap:1rem; align-items:center; margin-bottom:1rem;">
              <img src="${not empty usuario.avatar ? usuario.avatar : pageContext.request.contextPath.concat('/img/eventoSinImagen.jpeg')}" 
                   alt="Avatar de ${usuario.nickname}"
                   style="width:110px;height:110px;border-radius:50%;object-fit:cover;">
              <div class="user-details-list">
                <p><strong>Nickname:</strong> ${usuario.nickname}</p>
                <p><strong>Nombre:</strong> ${usuario.nombre}</p>
                <p><strong>Correo:</strong> ${usuario.correo}</p>
                <p><strong>Rol:</strong> ${tipoUsuario}</p>
                
                <!-- Información específica para Asistente -->
                <c:if test="${tipoUsuario == 'Asistente' && not empty asistente}">
                  <p><strong>Apellido:</strong> ${asistente.apellido}</p>
                  <p><strong>Fecha de nacimiento:</strong> ${asistente.fechaNacimiento.dia}/${asistente.fechaNacimiento.mes}/${asistente.fechaNacimiento.anio}</p>
                  <c:if test="${not empty asistente.institucion}">
                    <p><strong>Institución:</strong> ${asistente.institucion}</p>
                  </c:if>
                </c:if>
                
                <!-- Información específica para Organizador -->
                <c:if test="${tipoUsuario == 'Organizador' && not empty organizador}">
                  <c:if test="${not empty organizador.descripcion}">
                    <p><strong>Descripción:</strong> ${organizador.descripcion}</p>
                  </c:if>
                  <c:if test="${not empty organizador.link}">
                    <p><strong>Enlace:</strong> <a href="${organizador.link}" target="_blank">${organizador.link}</a></p>
                  </c:if>
                </c:if>
              </div>
            </div>

            <!-- Botón de volver -->
            <div style="margin-top:1rem;">
              <a class="btn-primary" href="${pageContext.request.contextPath}/listarUsuarios">← Volver al listado</a>
            </div>
          </div>
        </section>
      </main>
    </div>
  </div>

  <footer></footer>
</body>
</html>