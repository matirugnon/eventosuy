<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consulta de Edición · eventos.uy</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap" rel="stylesheet">
</head>
<body>
    <div>
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
                            <c:forEach items="${categorias}" var="categoria">
                              <li>
                                <c:url var="catUrl" value="/inicio">
                                  <c:param name="categoria" value="${categoria}"/>
                                </c:url>
                                <a href="${catUrl}">${categoria}</a>
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
                        Ver listado de usuarios
                    </a>
                </div>
            </aside>

            <main>
                <section class="panel">
                    <div class="panel-body">
                        <div class="event-detail" style="display: flex; gap: 2rem;">
                            <div style="flex: 1;">
                                <div class="event-image" style="margin-bottom: 1rem; max-width: 250px;">
                                    <img src="${pageContext.request.contextPath}/img/eventoSinImagen.jpeg" alt="${edicion.nombre}" style="width: 100%; height: auto; border-radius: 8px;">
                                </div>
                            </div>
                            
                            <div style="flex: 2;">
                                <div>
                                    <h2 style="margin:0.5rem 0;">${edicion.nombre}</h2>
                                    <p style="margin:0.25rem 0;"><strong>Ciudad:</strong> ${edicion.ciudad}</p>
                                    <p style="margin:0.25rem 0;"><strong>País:</strong> ${edicion.pais}</p>
                                    <p style="margin:0.25rem 0;"><strong>Sigla:</strong> ${edicion.sigla}</p>
                                    <p style="margin:0.25rem 0;"><strong>Fecha Inicio:</strong> 
                                        ${edicion.fechaInicio.dia}/${edicion.fechaInicio.mes}/${edicion.fechaInicio.anio}
                                    </p>
                                    <p style="margin:0.25rem 0;"><strong>Fecha Fin:</strong> 
                                        ${edicion.fechaFin.dia}/${edicion.fechaFin.mes}/${edicion.fechaFin.anio}
                                    </p>
                                    <p style="margin:0.25rem 0;"><strong>Fecha Alta:</strong> 
                                        ${edicion.altaEdicion.dia}/${edicion.altaEdicion.mes}/${edicion.altaEdicion.anio}
                                    </p>
                                </div>

                                <c:if test="${not empty edicion.tiposDeRegistro}">
                                    <div class="panel" style="margin-top: 1rem; max-width: 200px;">
                                        <div class="panel-header">Tipos de registro</div>
                                        <div class="panel-body">
                                            <ul class="menu-list" style="margin-left: 0;">
                                                <c:forEach items="${edicion.tiposDeRegistro}" var="tipoRegistro">
                                                    <c:url var="tipoRegistroUrl" value="/consultaTipoRegistro">
                                                        <c:param name="tipo" value="${tipoRegistro}" />
                                                        <c:param name="edicion" value="${edicion.nombre}" />
                                                    </c:url>
                                                    <li><a href="${tipoRegistroUrl}" style="display: block; padding: 0.5rem 0;">${tipoRegistro}</a></li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </div>
                                </c:if>
                            </div>

                            <div style="width: 250px; display: flex; flex-direction: column; gap: 2rem;">
                                <c:if test="${not empty eventoPadre}">
                                    <div>
                                        <h3 style="color: #182080; margin-bottom: 1rem;">Evento</h3>
                                        <div style="border-radius: 8px;">
                                            <h4 style="margin: 0; font-size: 1.1rem; color: #333; font-family: 'Inter', sans-serif;">
                                                <c:url var="eventoUrl" value="/consultaEvento">
                                                    <c:param name="evento" value="${eventoPadre.nombre}" />
                                                </c:url>
                                                <a href="${eventoUrl}" style="color: inherit; text-decoration: none;">
                                                    ${eventoPadre.nombre}
                                                </a>
                                            </h4>
                                        </div>
                                    </div>
                                </c:if>

                                <div>
                                    <h3 style="color: #182080; margin-bottom: 1rem;">Organizador</h3>
                                    <div style="border-radius: 8px; display: flex; justify-content: center; padding: 0.75rem 0;">
                                        <a href="${pageContext.request.contextPath}/perfilUsuario?nickname=${edicion.organizador}&from=edicion&edicion=${edicion.nombre}" style="text-decoration: none;">
                                            <div style="position: relative; width: 168px; height: 112px;">
                                                <img src="${pageContext.request.contextPath}${avatarOrganizador}" 
                                                     alt="Avatar de ${edicion.organizador}" 
                                                     style="width: 168px; height: 112px; border-radius: 12px; object-fit: cover; border: 3px solid rgba(24, 32, 128, 0.15); cursor: pointer; transition: transform 0.2s, box-shadow 0.2s;" 
                                                     onmouseover="this.style.transform='scale(1.05)'; this.style.boxShadow='0 4px 8px rgba(0,0,0,0.2)';" 
                                                     onmouseout="this.style.transform='scale(1)'; this.style.boxShadow='none';"
                                                     onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';" />
                                                <div style="display: none; width: 168px; height: 112px; border-radius: 12px; border: 3px solid rgba(24, 32, 128, 0.15); background: linear-gradient(135deg, #182080, #4a90e2); color: white; justify-content: center; align-items: center; font-size: 2rem; font-weight: bold; cursor: pointer; transition: transform 0.2s, box-shadow 0.2s;"
                                                     onmouseover="this.style.transform='scale(1.05)'; this.style.boxShadow='0 4px 8px rgba(0,0,0,0.2)';" 
                                                     onmouseout="this.style.transform='scale(1)'; this.style.boxShadow='none';">
                                                    ${edicion.organizador.substring(0, 1).toUpperCase()}
                                                </div>
                                            </div>
                                        </a>
                                    </div>
                                    <div style="padding: 0.5rem; text-align: center;">
                                        <a href="${pageContext.request.contextPath}/perfilUsuario?nickname=${edicion.organizador}&from=edicion&edicion=${edicion.nombre}" style="text-decoration: none;">
                                            <span style="font-weight: 600; color: #182080; cursor: pointer; transition: color 0.2s;" onmouseover="this.style.color='#0d4f8c';" onmouseout="this.style.color='#182080';">${edicion.organizador}</span>
                                        </a>
                                    </div>
                                </div>

                                <c:if test="${not empty edicion.patrocinios}">
                                    <div>
                                        <h3 style="color: #182080; margin-bottom: 1rem;">Patrocinios</h3>
                                        <div class="panel" style="border: 1px solid #e0e0e0; border-radius: 8px;">
                                            <ul class="menu-list" style="margin: 0; padding: 0.5rem 0;">
                                                <c:forEach items="${edicion.patrocinios}" var="patrocinio">
                                                    <li style="padding: 0.5rem 1rem;">
                                                        <c:url var="patrocinioUrl" value="/consultaPatrocinio">
                                                            <c:param name="codigo" value="${patrocinio}" />
                                                            <c:param name="edicion" value="${edicion.nombre}" />
                                                        </c:url>
                                                        <a href="${patrocinioUrl}" style="display: flex; align-items: center; gap: 0.75rem; text-decoration: none; color: inherit;">
                                                            <span style="font-weight: 500;">${patrocinio}</span>
                                                        </a>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </section>
            </main>
        </div>
    </div>
</body>
</html>