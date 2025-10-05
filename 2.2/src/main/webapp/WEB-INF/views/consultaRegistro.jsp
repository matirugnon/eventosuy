<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consulta de Registro · eventos.uy</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap" rel="stylesheet">
    <style>
        .table { width:100%; border-collapse: collapse; margin-top:1rem; }
        .table th, .table td { padding:.6rem; border-bottom:1px solid #e5e5e5; text-align:left; }
    </style>
</head>
<body>
    <div>
        <header class="header">
            <h1><a href="${pageContext.request.contextPath}/inicio" style="color: inherit; text-decoration: none;">eventos.uy</a></h1>
            <div class="header-right">
                <c:choose>
                    <c:when test="${not empty role}">
                        <div class="user-badge">
                            <a href="${pageContext.request.contextPath}/perfilUsuario?nickname=${nickname}" style="display:flex;align-items:center;gap:.5rem;text-decoration:none;color:inherit;">
                                <img src="${pageContext.request.contextPath}/img/${avatar}" alt="${nickname}" class="avatar">
                                <span class="nickname">${nickname}</span>
                            </a>
                            <a href="${pageContext.request.contextPath}/logout" class="btn-primary">Cerrar sesión</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login" class="btn-primary">Iniciar sesión</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </header>

        <div class="content">
            <aside class="sidebar">
                <c:choose>
                  <c:when test="${role == 'organizador'}">
                    <div class="panel sidebar">
                        <div class="panel-header" style="white-space: nowrap;">Mi perfil</div>
                        <ul class="menu-list">
                            <li><a href="${pageContext.request.contextPath}/altaEvento" style="white-space: nowrap;">Crear Evento</a></li>
                            <li><a href="${pageContext.request.contextPath}/altaEdicion" style="white-space: nowrap;">Crear Edición</a></li>
                            <li><a href="${pageContext.request.contextPath}/altaPatrocinio" style="white-space: nowrap;">Crear Patrocinio</a></li>
                            <li><a href="${pageContext.request.contextPath}/altaTipoDeRegistro" style="white-space: nowrap;">Crear Tipo de Registro</a></li>
                        </ul>
                    </div>
                  </c:when>
                  <c:when test="${role == 'asistente'}">
                    <div class="panel sidebar">
                        <div class="panel-header" style="white-space: nowrap;">Mi perfil</div>
                        <ul class="menu-list">
                            <li><a href="${pageContext.request.contextPath}/registroAEdicion" style="white-space: nowrap;">Registro a Edición</a></li>
                            <li><a href="${pageContext.request.contextPath}/misRegistros" style="white-space: nowrap;">Mis Registros</a></li>
                        </ul>
                    </div>
                  </c:when>
                </c:choose>

                <!-- Categorías -->
                <div class="panel sidebar" style="margin-top: 1rem;">
                    <div class="panel-header">Categorías</div>
                    <ul class="menu-list">
                        <c:forEach var="categoria" items="${categorias}">
                            <li>
                                <a href="${pageContext.request.contextPath}/consultaCategoria?categoria=${categoria}">
                                    ${categoria}
                                </a>
                            </li>
                        </c:forEach>
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
                    <div class="panel-header">Consulta de Registro</div>
                    <div class="panel-body">
                        <!-- Datos básicos del registro -->
                        <p><strong>Asistente:</strong> ${registro.asistente}</p>
                        <p><strong>Evento:</strong> ${edicionInfo.evento}</p>
                        <p><strong>Edición:</strong> ${registro.nomEdicion}</p>
                        <p><strong>Tipo de Registro:</strong> ${registro.tipoDeRegistro}</p>
                        <p><strong>Fecha de Registro:</strong> ${registro.fechaRegistro}</p>
                        <p><strong>Costo:</strong> $ ${registro.costo}</p>

                        <!-- Enlaces relacionados -->
                        <div style="margin-top:1.5rem; display:flex; gap:.75rem;">
                            <c:choose>
                                <c:when test="${param.from == 'misRegistros'}">
                                    <a class="btn-outline" href="${pageContext.request.contextPath}/misRegistros">Volver a Mis Registros</a>
                                </c:when>
                                <c:otherwise>
                                    <a class="btn-outline" href="${pageContext.request.contextPath}/perfilUsuario?nickname=${registro.asistente}">Volver al perfil de usuario</a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </section>
            </main>
        </div>
    </div>
</body>
</html>