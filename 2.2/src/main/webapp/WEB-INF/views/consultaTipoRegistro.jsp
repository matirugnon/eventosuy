<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consulta Tipo de Registro · eventos.uy</title>
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
            <aside class="sidebar">
                <c:choose>
                  <c:when test="${role == 'organizador'}">
                    <div class="panel sidebar">
                      <div class="panel-header">Mi perfil</div>
                      <ul class="menu-list">
                        <li><a href="${pageContext.request.contextPath}/altaEvento">Alta Evento</a></li>
                        <li><a href="${pageContext.request.contextPath}/altaEdicion">Alta Edición</a></li>
                        <li><a href="altaInstitucion">Alta Institución</a></li>
                        <li><a href="#">Ediciones Organizadas</a></li>
                      </ul>
                    </div>
                  </c:when>
                  <c:when test="${role == 'asistente'}">
                    <div class="panel sidebar">
                      <div class="panel-header">Mi perfil</div>
                      <ul class="menu-list">
                        <li><a href="${pageContext.request.contextPath}/registroAedicion">Registro a Edición</a></li>
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
                <!-- Navegación de vuelta -->
                <div style="margin-bottom: 1rem;">
                    <a href="${pageContext.request.contextPath}/consultaEdicion?edicion=${edicionInfo.nombre}" 
                       style="color: #182080; text-decoration: none; font-weight: 600;">← Volver a ${edicionInfo.nombre}</a>
                </div>

                <!-- Información del tipo de registro -->
                <section class="panel">
                    <div class="panel-body">
                        <h2 style="margin: 0 0 1rem 0;">Tipo de Registro: ${tipoRegistro.nombre}</h2>
                        
                        <div style="display: flex; flex-direction: column; gap: 0.5rem;">
                            <p style="margin: 0;"><strong>Descripción:</strong> ${tipoRegistro.descripcion}</p>
                            <p style="margin: 0;"><strong>Costo:</strong> $${tipoRegistro.costo}</p>
                            <p style="margin: 0;"><strong>Cupo:</strong> ${tipoRegistro.cupo} lugares</p>
                            <p style="margin: 0;"><strong>Edición:</strong> ${edicionInfo.nombre}</p>
                            <p style="margin: 0;"><strong>Evento:</strong> ${edicionInfo.evento}</p>
                        </div>
                    </div>
                </section>

                <!-- Botón de registro para asistentes -->
                <c:if test="${esAsistente and not yaRegistrado}">
                    <section class="panel" style="margin-top: 1rem;">
                        <div class="panel-body">
                            <h3 style="margin: 0 0 1rem 0;">Registro</h3>
                            <form action="${pageContext.request.contextPath}/registrarAsistente" method="post">
                                <input type="hidden" name="edicion" value="${edicionInfo.nombre}">
                                <input type="hidden" name="tipoRegistro" value="${tipoRegistro.nombre}">
                                <button type="submit" class="register-button">Registrarse a edición</button>
                            </form>
                        </div>
                    </section>
                </c:if>
                
                <c:if test="${esAsistente and yaRegistrado}">
                    <section class="panel" style="margin-top: 1rem;">
                        <div class="panel-body">
                            <p class="already-registered">Ya estás registrado en esta edición</p>
                        </div>
                    </section>
                </c:if>
                
                <c:if test="${not esAsistente and not empty role}">
                    <section class="panel" style="margin-top: 1rem;">
                        <div class="panel-body">
                            <p class="not-assistant">Solo los asistentes pueden registrarse en las ediciones</p>
                        </div>
                    </section>
                </c:if>
                
                <c:if test="${empty role}">
                    <section class="panel" style="margin-top: 1rem;">
                        <div class="panel-body">
                            <p class="login-required">
                                <a href="${pageContext.request.contextPath}/login">Inicia sesión</a> como asistente para registrarte
                            </p>
                        </div>
                    </section>
                </c:if>
            </main>
        </div>
    </div>
</body>
</html>