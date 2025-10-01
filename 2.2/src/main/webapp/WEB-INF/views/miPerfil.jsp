<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>Mi Perfil · eventos.uy</title>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css"/>
  <link rel="preconnect" href="https://fonts.googleapis.com"/>
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet"/>
  <style>
    .auth-card { width: 760px; max-width: 95%; margin: 0 auto; }
    .table { width:100%; border-collapse: collapse; }
    .table th, .table td { padding:.6rem; border-bottom:1px solid #ddd; text-align:left; }
    .btn-outline { display:inline-block; padding:.4rem .7rem; border:1px solid #ccc; border-radius:10px; text-decoration:none; cursor: pointer; background: none; }
    .btn-consultar { background-color: #182080; color: white; padding: 0.4rem 0.8rem; border-radius: 6px; text-decoration: none; font-size: 0.9rem; }
    .btn-rechazada { background-color: #dc3545; color: white; padding: 0.4rem 0.8rem; border-radius: 6px; text-decoration: none; font-size: 0.9rem; }
    .btn-ingresada { background-color: #28a745; color: white; padding: 0.4rem 0.8rem; border-radius: 6px; text-decoration: none; font-size: 0.9rem; }
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
              <img class="avatar" 
                   src="${not empty avatar ? pageContext.request.contextPath.concat(avatar) : pageContext.request.contextPath.concat('/img/eventoSinImagen.jpeg')}" 
                   alt="Avatar de usuario" />
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
              <ul class="menu-list" style="margin-bottom: 3rem;">
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
              <ul class="menu-list" style="margin-bottom: 3rem;">
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

      <!-- Main: Mi Perfil -->
      <main>
        <section class="panel" style="max-width: 760px; margin: 2rem auto 0 auto;">
          <!-- Header del perfil con avatar y nombre -->
          <div style="display: flex; align-items: center; gap: 1.5rem;">
            <div style="flex-shrink:0;">
              <img src="${not empty usuario.avatar ? pageContext.request.contextPath.concat(usuario.avatar) : pageContext.request.contextPath.concat('/img/eventoSinImagen.jpeg')}" 
                   alt="Avatar de ${usuario.nickname}" 
                   style="width:110px;height:110px;border-radius:12px;object-fit:cover;display:block;" />
            </div>
            <div style="flex:1; display: flex; align-items: center; min-height:110px;">
              <h2 style="font-size:1.5rem;font-weight:700;margin:0; text-align:left;">
                <c:choose>
                  <c:when test="${tipoUsuario == 'Asistente' && not empty asistente}">
                    ${asistente.nombre} ${asistente.apellido}
                  </c:when>
                  <c:otherwise>
                    ${usuario.nombre}
                  </c:otherwise>
                </c:choose>
              </h2>
            </div>
          </div>

          <!-- Tabs de contenido -->
          <div style="margin-top:1.5rem;">
            <div style="display:flex;gap:0.5rem;">
              <button id="tab-datos" class="btn-outline" 
                      style="border-bottom-left-radius:0;border-bottom-right-radius:0;border-bottom:0;background:#fff;font-weight:600;color:#000;">
                Datos del usuario
              </button>
              <c:choose>
                <c:when test="${tipoUsuario == 'Asistente'}">
                  <button id="tab-registros" class="btn-outline" 
                          style="border-bottom-left-radius:0;border-bottom-right-radius:0;border-bottom:0;background:#f5f5f5;font-weight:600;color:#000;">
                    Registros
                  </button>
                </c:when>
                <c:when test="${tipoUsuario == 'Organizador'}">
                  <button id="tab-ediciones" class="btn-outline" 
                          style="border-bottom-left-radius:0;border-bottom-right-radius:0;border-bottom:0;background:#f5f5f5;font-weight:600;color:#000;">
                    Ediciones
                  </button>
                </c:when>
              </c:choose>
            </div>

            <!-- Panel de Datos del Usuario -->
            <div id="panel-datos" style="border:1px solid #bbb;border-radius:0 0 12px 12px;padding:1.2rem 1.5rem;background:#fff;">
              <h3 style="margin:0 0 1rem 0; color:#182080;">Datos del usuario</h3>
              <div style="display:flex;align-items:center;justify-content:space-between;">
                <div style="font-size:1rem; line-height:1.7;">
                  <b>Nickname:</b> ${usuario.nickname}<br>
                  <b>Nombre:</b> ${usuario.nombre}<br>
                  <b>Correo:</b> ${usuario.correo}<br>
                  <b>Rol:</b> ${tipoUsuario}<br>
                  
                  <!-- Información específica para Asistente -->
                  <c:if test="${tipoUsuario == 'Asistente' && not empty asistente}">
                    <b>Apellido:</b> ${asistente.apellido}<br>
                    <b>Fecha de nacimiento:</b> ${asistente.fechaNacimiento.dia}/${asistente.fechaNacimiento.mes}/${asistente.fechaNacimiento.anio}<br>
                    <c:if test="${not empty asistente.institucion}">
                      <b>Institución:</b> ${asistente.institucion}<br>
                    </c:if>
                  </c:if>
                  
                  <!-- Información específica para Organizador -->
                  <c:if test="${tipoUsuario == 'Organizador' && not empty organizador}">
                    <c:if test="${not empty organizador.descripcion}">
                      <b>Descripción:</b> ${organizador.descripcion}<br>
                    </c:if>
                    <c:if test="${not empty organizador.link}">
                      <b>Sitio web:</b> <a href="${organizador.link}" target="_blank">${organizador.link}</a><br>
                    </c:if>
                  </c:if>
                </div>
                <a href="#" class="btn-primary" style="margin-left:1.5rem;white-space:nowrap;">Editar usuario</a>
              </div>
            </div>

            <!-- Panel de Registros (solo para Asistentes) -->
            <c:if test="${tipoUsuario == 'Asistente'}">
              <div id="panel-registros" style="display:none;border:1px solid #bbb;border-radius:0 0 12px 12px;padding:1.2rem 1.5rem;background:#fff;">
                <h3 style="margin:0 0 1rem 0; color:#182080;">Registros</h3>
                <c:choose>
                  <c:when test="${not empty registros}">
                    <table class="table">
                      <thead>
                        <tr>
                          <th>Edición</th>
                          <th>Tipo de Registro</th>
                          <th>Fecha Registro</th>
                          <th>Costo</th>
                          <th>Acciones</th>
                        </tr>
                      </thead>
                      <tbody>
                        <c:forEach var="registro" items="${registros}">
                          <tr>
                            <td>${registro.nomEdicion}</td>
                            <td>${registro.tipoDeRegistro}</td>
                            <td>${registro.fechaRegistro.dia}/${registro.fechaRegistro.mes}/${registro.fechaRegistro.anio}</td>
                            <td>$${registro.costo}</td>
                            <td><a class="btn-consultar" href="#">Consultar</a></td>
                          </tr>
                        </c:forEach>
                      </tbody>
                    </table>
                  </c:when>
                  <c:otherwise>
                    <p>No tienes registros en ediciones.</p>
                  </c:otherwise>
                </c:choose>
              </div>
            </c:if>

            <!-- Panel de Ediciones (solo para Organizadores) -->
            <c:if test="${tipoUsuario == 'Organizador'}">
              <div id="panel-ediciones" style="display:none;border:1px solid #bbb;border-radius:0 0 12px 12px;padding:1.2rem 1.5rem;background:#fff;">
                <h3 style="margin:0 0 1rem 0; color:#182080;">Ediciones</h3>
                <c:choose>
                  <c:when test="${not empty edicionesOrganizadas}">
                    <table class="table">
                      <thead>
                        <tr>
                          <th>Evento</th>
                          <th>Nombre</th>
                          <th>Sigla</th>
                          <th>Acciones</th>
                        </tr>
                      </thead>
                      <tbody>
                        <c:forEach var="edicion" items="${edicionesOrganizadas}">
                          <tr>
                            <td>${edicion.nombreEvento}</td>
                            <td>${edicion.nombre}</td>
                            <td>${edicion.sigla}</td>
                            <td>
                              <c:choose>
                                <c:when test="${edicion.estado == 'CONFIRMADA'}">
                                  <a class="btn-consultar" href="#">Consultar</a>
                                </c:when>
                                <c:when test="${edicion.estado == 'RECHAZADA'}">
                                  <span class="btn-rechazada">Rechazada</span>
                                </c:when>
                                <c:otherwise>
                                  <span class="btn-ingresada">Ingresada</span>
                                </c:otherwise>
                              </c:choose>
                            </td>
                          </tr>
                        </c:forEach>
                      </tbody>
                    </table>
                  </c:when>
                  <c:otherwise>
                    <p>No has organizado ediciones.</p>
                  </c:otherwise>
                </c:choose>
              </div>
            </c:if>
          </div>
        </section>
      </main>
    </div>
  </div>

  <footer></footer>

  <script>
    // Script para manejar los tabs
    document.addEventListener('DOMContentLoaded', function() {
      const tabDatos = document.getElementById('tab-datos');
      const panelDatos = document.getElementById('panel-datos');
      
      // Para asistentes
      <c:if test="${tipoUsuario == 'Asistente'}">
      const tabRegistros = document.getElementById('tab-registros');
      const panelRegistros = document.getElementById('panel-registros');
      
      tabDatos.onclick = function() {
        panelDatos.style.display = '';
        panelRegistros.style.display = 'none';
        tabDatos.style.background = '#fff';
        tabRegistros.style.background = '#f5f5f5';
      };
      
      tabRegistros.onclick = function() {
        panelDatos.style.display = 'none';
        panelRegistros.style.display = '';
        tabDatos.style.background = '#f5f5f5';
        tabRegistros.style.background = '#fff';
      };
      </c:if>
      
      // Para organizadores
      <c:if test="${tipoUsuario == 'Organizador'}">
      const tabEdiciones = document.getElementById('tab-ediciones');
      const panelEdiciones = document.getElementById('panel-ediciones');
      
      tabDatos.onclick = function() {
        panelDatos.style.display = '';
        panelEdiciones.style.display = 'none';
        tabDatos.style.background = '#fff';
        tabEdiciones.style.background = '#f5f5f5';
      };
      
      tabEdiciones.onclick = function() {
        panelDatos.style.display = 'none';
        panelEdiciones.style.display = '';
        tabDatos.style.background = '#f5f5f5';
        tabEdiciones.style.background = '#fff';
      };
      </c:if>
    });
  </script>
</body>
</html>