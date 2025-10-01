<!-- filepath: c:\Users\facu3\git\tpgr15\2.2\src\main\webapp\WEB-INF\views\inicio.jsp -->
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>eventos.uy</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

  <!-- Header -->
  <header class="header">
    <h1>eventos.uy</h1>
    <div class="header-right">
      <c:choose>
        <c:when test="${not empty role}">
          <div class="user-badge" style="display: flex; align-items: center; gap: 0.5rem;">
            <a href="${pageContext.request.contextPath}/miPerfil" style="display: flex; align-items: center; gap: 0.5rem; text-decoration: none; color: inherit;">
              <img class="avatar" 
                   src="${not empty avatar ? pageContext.request.contextPath.concat(avatar) : pageContext.request.contextPath.concat('/img/eventoSinImagen.jpeg')}" 
                   alt="Avatar de usuario" />
              <span class="nickname">${nickname}</span>
            </a>
            <a href="/EventosUy/logout" class="btn-primary">Cerrar sesión</a>
          </div>
        </c:when>
        <c:otherwise>
          <nav class="nav-links">
            <a href="${pageContext.request.contextPath}/login">Iniciar sesión</a>
            <a href="${pageContext.request.contextPath}/signup">Registrarse</a>
          </nav>
        </c:otherwise>
      </c:choose>
    </div>
  </header>

  <!-- Contenido principal -->
  <div class="content">
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
                <a href="${urlTodas}" class="${categoriaSeleccionada == 'todas' ? 'active' : ''}">Todas</a>
              </li>
              <c:forEach var="cat" items="${categorias}">
                <li>
                  <c:url var="catUrl" value="/inicio">
                    <c:param name="categoria" value="${cat}"/>
                  </c:url>
                  <a href="${catUrl}" class="${cat eq categoriaSeleccionada ? 'active' : ''}">${cat}</a>
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
            background-color: rgba(24, 32, 128, 0.05);
        ">
          Ver listado de Usuarios
        </a>
      </div>
    </aside>

    <!-- Main: eventos -->
    <main>
      <h2>Próximos Eventos</h2>
      <c:choose>
        <c:when test="${empty eventos}">
          <div class="event">
            <div class="event-content">
              <p class="muted">No hay eventos disponibles.</p>
            </div>
          </div>
        </c:when>
        <c:otherwise>
          <c:forEach var="e" items="${eventos}">
            <div class="event">
              <div class="event-content">
                <c:url var="eventoUrl" value="/consultaEvento">
                  <c:param name="evento" value="${e.nombre}" />
                </c:url>
                <h3><a href="${eventoUrl}" style="color: inherit; text-decoration: none;">${e.nombre}</a></h3>
                <p>${empty e.descripcion ? 'Sin descripción' : e.descripcion}</p>
              </div>
            </div>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </main>
  </div>

</body>
</html>

