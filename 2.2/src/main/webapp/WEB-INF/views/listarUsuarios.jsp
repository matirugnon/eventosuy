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
  <link rel="preconnect" href="https://fonts.googleapis.com"/>
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet"/>
  <style>
    /* Agrandar caja de usuarios solo para esta página */
    .auth-card {
      width: 800px;
      max-width: 95%;
      margin: 0 auto;
    }
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
              <a href="${pageContext.request.contextPath}/miPerfil" style="display: flex; align-items: center; gap: 0.5rem; text-decoration: none; color: inherit;">
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

    <div class="content">
      <!-- Sidebar -->
      <aside class="sidebar">
        <c:choose>
          <c:when test="${role == 'organizador'}">
            <div class="panel sidebar">
              <div class="panel-header">Mi perfil</div>
              <ul class="menu-list">
                <li><a href="${pageContext.request.contextPath}/altaEvento">Alta Evento</a></li>
                <li><a href="${pageContext.request.contextPath}/altaEdicion">Alta Edición</a></li>
                                    <li><a href="altaInstitucion">Alta Institución</a></li>
                <li><a href="edicionesOrganizadas">Ediciones Organizadas</a></li>
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
                  <c:choose>
                    <c:when test="${empty usuarios}">
                      <tr>
                        <td colspan="5" style="padding:2rem; text-align:center; color:#666;">
                          No hay usuarios registrados.
                        </td>
                      </tr>
                    </c:when>
                    <c:otherwise>
                      <c:forEach var="usuario" items="${usuarios}">
                        <tr style="border-bottom:1px solid #eee;">
                          <td style="padding:.5rem;">
                            <c:choose>
                              <c:when test="${not empty usuario.avatar}">
                                <img src="${pageContext.request.contextPath}${usuario.avatar}" alt="Avatar de ${usuario.nickname}"
                                     style="width:64px;height:64px;border-radius:50%;object-fit:cover;"/>
                              </c:when>
                              <c:otherwise>
                                <img src="${pageContext.request.contextPath}/img/avatar-default.png" alt="Avatar de ${usuario.nickname}"
                                     style="width:64px;height:64px;border-radius:50%;object-fit:cover;"/>
                              </c:otherwise>
                            </c:choose>
                          </td>
                          <td style="padding:.5rem;">${usuario.nickname}</td>
                          <td style="padding:.5rem;">${usuario.nombre}</td>
                          <td style="padding:.5rem;">${tiposUsuarios[usuario.nickname]}</td>
                          <td style="padding:.5rem;">
                            <c:url var="perfilUsuarioUrl" value="/perfilUsuario">
                              <c:param name="nickname" value="${usuario.nickname}" />
                            </c:url>
                            <a class="btn-primary" href="${perfilUsuarioUrl}">
                              Seleccionar
                            </a>
                          </td>
                        </tr>
                      </c:forEach>
                    </c:otherwise>
                  </c:choose>
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
