<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>Usuarios · eventos.uy</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css"/>
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
            <nav class="nav-links">
              <a href="${pageContext.request.contextPath}/login">Iniciar sesión</a>
              <a href="${pageContext.request.contextPath}/signup">Registrarse</a>
            </nav>
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

      <!-- Main: Listado de usuarios -->
      <main>
        <section class="auth-container">
          <div class="auth-card">
            <h2>Listado de usuarios</h2>
            <div style="overflow:auto; margin-top:1rem;">
              <table style="width:100%; border-collapse:collapse;">
                <thead>
                  <tr style="text-align:left; border-bottom:1px solid #ddd;">
                    <th style="padding:.5rem;">Avatar</th>
                    <th style="padding:.5rem;">Nickname</th>
                    <th style="padding:.5rem;">Nombre</th>
                    <th style="padding:.5rem;">Rol</th>
                    <th style="padding:.5rem;">Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach var="usuario" items="${usuarios}">
                    <tr style="border-bottom:1px solid #eee;">
                      <td style="padding:.5rem;">
                        <img src="${usuario.avatar}" alt="Avatar de ${usuario.nickname}"
                             style="width:64px;height:64px;border-radius:50%;object-fit:cover;"/>
                      </td>
                      <td style="padding:.5rem;">${usuario.nickname}</td>
                      <td style="padding:.5rem;">${usuario.nombre}</td>
                      <td style="padding:.5rem;">${usuario.tipo}</td>
                      <td style="padding:.5rem;">
                        <a class="btn-primary" href="${pageContext.request.contextPath}/consultausuario?nickname=${usuario.nickname}">
                          Seleccionar
                        </a>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
          </div>
        </section>
      </main>
    </div>
  </div>
</body>
</html>
