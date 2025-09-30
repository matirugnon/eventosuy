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
            <img class="avatar" src="${avatar}" alt="Avatar de usuario" />
            <span class="nickname">${nickname}</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn-primary">Cerrar sesión</a>
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
        <div class="panel-body menu-list">
          <c:choose>
            <c:when test="${empty categorias}">
              <p class="muted">No hay categorías disponibles.</p>
            </c:when>
            <c:otherwise>
              <c:url var="urlTodas" value="/inicio"/>
              <a href="${urlTodas}" class="${categoriaSeleccionada == 'todas' ? 'active' : ''}">Todas</a>
              <c:forEach var="cat" items="${categorias}">
                <c:url var="catUrl" value="/inicio">
                  <c:param name="categoria" value="${cat}"/>
                </c:url>
                <a href="${catUrl}" class="${cat eq categoriaSeleccionada ? 'active' : ''}">${cat}</a>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </div>
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
                <h3>${e.nombre}</h3>
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

