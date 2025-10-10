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
    .actions { display:flex; gap:.5rem; flex-wrap:wrap; }
    .btn-outline { display:inline-block; padding:.4rem .7rem; border:1px solid #ccc; border-radius:10px; text-decoration:none; }
    .user-details-list p { margin: 0.5rem 0; }
    
    /* Tabs y contenedor fijo */
    .tabs { display: flex; border-bottom: 2px solid #e0e0e0; margin-bottom: 1rem; }
    .tab { 
      padding: 0.75rem 1.5rem; 
      background: #f8f9fa; 
      border: 1px solid #ddd; 
      border-bottom: none; 
      cursor: pointer; 
      border-radius: 8px 8px 0 0; 
      margin-right: 4px;
      transition: all 0.2s;
    }
    .tab.active { 
      background: white; 
      border-bottom: 2px solid white; 
      margin-bottom: -2px; 
      font-weight: 600;
    }
    .tab:hover:not(.active) { background: #e9ecef; }
    
    .tab-content { 
      min-height: 400px; 
      background: white; 
      padding: 1.5rem; 
      border-radius: 0 8px 8px 8px; 
      border: 1px solid #e0e0e0; 
      margin-bottom: 1rem;
    }
    .tab-panel { display: none; }
    .tab-panel.active { display: block; }
    
    .profile-header { 
      display: flex; 
      gap: 1rem; 
      align-items: center; 
      margin-bottom: 1.5rem; 
      padding-bottom: 1rem; 
      border-bottom: 1px solid #eee; 
    }
    .profile-avatar { 
      width: 110px; 
      height: 110px; 
      border-radius: 50%; 
      object-fit: cover; 
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
              <img class="avatar" 
                   src="${not empty avatar ? pageContext.request.contextPath.concat(avatar) : pageContext.request.contextPath.concat('/img/usSinFoto.webp')}" 
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
              <ul class="menu-list">
                <li><a href="${pageContext.request.contextPath}/altaEvento">Alta Evento</a></li>
                <li><a href="${pageContext.request.contextPath}/altaEdicion">Alta Edición</a></li>
                                    <li><a href="altaInstitucion">Alta Institución</a></li>
                <li><a href="edicionesOrganizadas">Ediciones Organizadas</a></li>
              </ul>
            </div>
          </c:when>
          <c:when test="${role == 'asistente'}">
            <div class="panel sidebar">
              <div class="panel-header">Mi perfil</div>
              <ul class="menu-list">
                <li><a href="#">Registro a Edición</a></li>
                <li><a href="${pageContext.request.contextPath}/misRegistros">Mis Registros</a></li>
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
        <section class="auth-container">
          <div class="auth-card">
            <!-- Encabezado: avatar + nombre del usuario -->
            <div class="profile-header">
              <img src="${not empty usuario.avatar ? pageContext.request.contextPath.concat(usuario.avatar) : pageContext.request.contextPath.concat('/img/usSinFoto.webp')}" 
                   alt="Avatar de ${usuario.nickname}"
                   class="profile-avatar">
              <div>
                <h2 style="margin: 0; color: #333;">
                  <c:choose>
                    <c:when test="${tipoUsuario == 'Asistente' && not empty asistente}">
                      ${asistente.nombre} ${asistente.apellido}
                    </c:when>
                    <c:otherwise>
                      ${usuario.nombre}
                    </c:otherwise>
                  </c:choose>
                </h2>
                <p style="margin: 0.25rem 0; color: #666; font-size: 1.1rem;">@${usuario.nickname}</p>
              </div>
            </div>

            <!-- Tabs de navegación -->
            <div class="tabs">
              <div class="tab active" onclick="showTab('datos')">Datos del usuario</div>
              <c:if test="${tipoUsuario == 'Asistente'}">
                <div class="tab" onclick="showTab('registros')">Registros</div>
              </c:if>
              <c:if test="${tipoUsuario == 'Organizador'}">
                <div class="tab" onclick="showTab('ediciones')">Ediciones</div>
              </c:if>
            </div>

            <!-- Contenido de los tabs con tamaño fijo -->
            <div class="tab-content">
              <!-- Panel de Datos del Usuario -->
              <div id="datos" class="tab-panel active">
                <h3 style="color: #182080; margin-top: 0;">Datos del usuario</h3>
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
                      <p><strong>Sitio web:</strong> <a href="${organizador.link}" target="_blank">${organizador.link}</a></p>
                    </c:if>
                  </c:if>
                </div>
                
                <!-- Botón Editar usuario (solo en miPerfil) -->
                <div style="margin-top: 1.5rem; padding-top: 1rem; border-top: 1px solid #eee;">
                  <a href="${pageContext.request.contextPath}/modificarUsuario" class="btn-primary">Editar usuario</a>
                </div>
              </div>

              <!-- Panel de Registros para Asistente -->
              <c:if test="${tipoUsuario == 'Asistente'}">
                <div id="registros" class="tab-panel">
                  <h3 style="color: #182080; margin-top: 0;">Registros</h3>
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
                              <td>
                                <c:url var="consultaRegistroUrl" value="/consultaRegistro">
                                  <c:param name="asistente" value="${registro.asistente}" />
                                  <c:param name="edicion" value="${registro.nomEdicion}" />
                                  <c:param name="tipoRegistro" value="${registro.tipoDeRegistro}" />
                                </c:url>
                                <a href="${consultaRegistroUrl}" 
                                   class="btn-primary" style="background-color: #182080; color: white; padding: 0.3rem 0.6rem; border-radius: 6px; text-decoration: none; font-size: 0.9rem;">
                                   Consultar
                                </a>
                              </td>
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

              <!-- Panel de Ediciones para Organizador -->
              <c:if test="${tipoUsuario == 'Organizador'}">
                <div id="ediciones" class="tab-panel">
                  <h3 style="color: #182080; margin-top: 0;">Ediciones</h3>
                  <c:choose>
                    <c:when test="${not empty edicionesAceptadas || not empty edicionesIngresadas || not empty edicionesRechazadas}">
                      <table class="table">
                        <thead>
                          <tr>
                            <th>Evento</th>
                            <th>Nombre</th>
                            <th>Sigla</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                          </tr>
                        </thead>
                        <tbody>
                          <!-- Ediciones Aceptadas -->
                          <c:forEach var="edicion" items="${edicionesAceptadas}">
                            <tr>
                              <td>${edicion.evento}</td>
                              <td>${edicion.nombre}</td>
                              <td>${edicion.sigla}</td>
                              <td><span style="color: #28a745; font-weight: 600;">Aceptada</span></td>
                              <td>
                                <a href="${pageContext.request.contextPath}/consultaEdicion?edicion=${edicion.nombre}" 
                                   class="btn-primary" style="background-color: #182080; color: white; padding: 0.3rem 0.6rem; border-radius: 6px; text-decoration: none; font-size: 0.9rem;">
                                   Consultar
                                </a>
                              </td>
                            </tr>
                          </c:forEach>
                          <!-- Ediciones Ingresadas -->
                          <c:forEach var="edicion" items="${edicionesIngresadas}">
                            <tr>
                              <td>${edicion.evento}</td>
                              <td>${edicion.nombre}</td>
                              <td>${edicion.sigla}</td>
                              <td><span style="color: #b8860b; font-weight: 600;">Ingresada</span></td>
                              <td>
                                <a href="${pageContext.request.contextPath}/consultaEdicion?edicion=${edicion.nombre}" 
                                   class="btn-primary" style="background-color: #182080; color: white; padding: 0.3rem 0.6rem; border-radius: 6px; text-decoration: none; font-size: 0.9rem;">
                                   Consultar
                                </a>
                              </td>
                            </tr>
                          </c:forEach>
                          <!-- Ediciones Rechazadas -->
                          <c:forEach var="edicion" items="${edicionesRechazadas}">
                            <tr>
                              <td>${edicion.evento}</td>
                              <td>${edicion.nombre}</td>
                              <td>${edicion.sigla}</td>
                              <td><span style="color: #dc3545; font-weight: 600;">Rechazada</span></td>
                              <td>
                                <a href="${pageContext.request.contextPath}/consultaEdicion?edicion=${edicion.nombre}" 
                                   class="btn-primary" style="background-color: #182080; color: white; padding: 0.3rem 0.6rem; border-radius: 6px; text-decoration: none; font-size: 0.9rem;">
                                   Consultar
                                </a>
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
          </div>
        </section>
      </main>
    </div>
  </div>

  <footer></footer>

  <script>
    function showTab(tabName) {
      // Ocultar todos los paneles
      const panels = document.querySelectorAll('.tab-panel');
      panels.forEach(panel => panel.classList.remove('active'));
      
      // Desactivar todas las tabs
      const tabs = document.querySelectorAll('.tab');
      tabs.forEach(tab => tab.classList.remove('active'));
      
      // Mostrar el panel seleccionado
      const selectedPanel = document.getElementById(tabName);
      if (selectedPanel) {
        selectedPanel.classList.add('active');
      }
      
      // Activar la tab correspondiente
      const selectedTab = event.target;
      selectedTab.classList.add('active');
    }

    // Asegurar que funcione cuando el DOM esté listo
    document.addEventListener('DOMContentLoaded', function() {
      console.log('DOM cargado - miPerfil.jsp');
      
      // Verificar que los elementos existan
      const datosPanel = document.getElementById('datos');
      const registrosPanel = document.getElementById('registros');
      const edicionesPanel = document.getElementById('ediciones');
      
      console.log('Panel datos:', datosPanel);
      console.log('Panel registros:', registrosPanel);
      console.log('Panel ediciones:', edicionesPanel);
      
      // También agregar event listeners por si acaso
      const tabs = document.querySelectorAll('.tab');
      tabs.forEach((tab, index) => {
        tab.addEventListener('click', function() {
          const tabText = this.textContent.trim();
          console.log('Tab clickeado:', tabText);
          
          if (tabText === 'Datos del usuario') {
            showTab('datos');
          } else if (tabText === 'Registros') {
            showTab('registros');
          } else if (tabText === 'Ediciones') {
            showTab('ediciones');
          }
        });
      });
    });
  </script>
</body>
</html>