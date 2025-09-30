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
      <a href="#">Iniciar sesión</a>
      <a href="#">Registrarse</a>
    </nav>
  </header>

  <!-- Contenido principal -->
  <div class="content">

    <!-- Sidebar -->
    <aside class="sidebar panel">
      <div class="panel-header">Categorías</div>
      <div class="panel-body menu-list">
        <c:choose>
          <c:when test="${empty categorias}">
            <p class="muted">No hay categorías disponibles.</p>
          </c:when>
          <c:otherwise>
            <c:forEach var="cat" items="${categorias}">
              <a href="#">${cat}</a>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </div>
    </aside>

    <!-- Main -->
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
                <h3>${e}</h3>
                <p>Descripción del evento…</p>
              </div>
            </div>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </main>
  </div>

</body>
</html>

