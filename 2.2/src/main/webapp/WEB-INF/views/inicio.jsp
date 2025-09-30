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
 <nav class="nav-links">
  <a href="<c:url value='/login'/>">Iniciar sesión</a>
  <a href="<c:url value='/registro'/>">Registrarse</a>
</nav>
  </header>

  <!-- Contenido principal -->
  <div class="content">

   <!-- Sidebar: categorías -->
<aside class="sidebar panel">
  <div class="panel-header">Categorías</div>
  <div class="panel-body menu-list">
    <c:choose>
      <c:when test="${empty categorias}">
        <p class="muted">No hay categorías disponibles.</p>
      </c:when>
      <c:otherwise>
        <!-- Link para ver todas -->
        <c:url var="urlTodas" value="/inicio"/>
        <a href="${urlTodas}"
           class="${categoriaSeleccionada == 'todas' ? 'active' : ''}">Todas</a>

        <!-- Links por categoría -->
        <c:forEach var="cat" items="${categorias}">
          <c:url var="catUrl" value="/inicio">
            <c:param name="categoria" value="${cat}"/>
          </c:url>
          <a href="${catUrl}"
             class="${cat eq categoriaSeleccionada ? 'active' : ''}">
            ${cat}
          </a>
        </c:forEach>
      </c:otherwise>
    </c:choose>
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

