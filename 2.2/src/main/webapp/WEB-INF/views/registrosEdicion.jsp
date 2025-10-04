<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registros de Edición · eventos.uy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .table { width:100%; border-collapse: collapse; margin-top:1rem; }
        .table th, .table td { padding:.6rem; border-bottom:1px solid #e5e5e5; text-align:left; }
        .actions { display:flex; gap:.5rem; }
    </style>
</head>
<body>
    <header class="header">
        <h1><a href="${pageContext.request.contextPath}/inicio">eventos.uy</a></h1>
        <div class="header-right">
            <c:choose>
                <c:when test="${not empty role}">
                    <div class="user-badge">
                        <a href="${pageContext.request.contextPath}/miPerfil" style="display:flex;align-items:center;gap:.5rem;text-decoration:none;color:inherit;">
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
            <div class="panel sidebar">
                <div class="panel-header">Mi perfil</div>
                <ul class="menu-list">
                    <li><a href="${pageContext.request.contextPath}/altaEvento">Alta Evento</a></li>
                    <li><a href="${pageContext.request.contextPath}/altaEdicion">Alta Edición</a></li>
                    <li><a href="${pageContext.request.contextPath}/edicionesOrganizadas">Ediciones Organizadas</a></li>
                </ul>
            </div>

            <div class="panel sidebar" style="margin-top: 1rem;">
                <div class="panel-header">Categorías</div>
                <ul class="menu-list">
                    <c:forEach var="categoria" items="${categorias}">
                        <li><a href="${pageContext.request.contextPath}/inicio?categoria=${categoria}">${categoria}</a></li>
                    </c:forEach>
                </ul>
            </div>
        </aside>

        <main>
            <section class="panel">
                <div class="panel-header">Registros - Edición: ${edicion.nombre}</div>
                <div class="panel-body">
                    <c:choose>
                        <c:when test="${empty edicion.registros}">
                            <p>No hay registros para esta edición.</p>
                        </c:when>
                        <c:otherwise>
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Asistente</th>
                                        <th>Tipo de Registro</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="entry" items="${edicion.registros}">
                                        <tr>
                                            <td>${entry.key}</td>
                                            <td>${entry.value}</td>
                                            <td>
                                                <div class="actions">
                                                    <a class="btn-edicion" href="consultaRegistro?asistente=${entry.key}&edicion=${edicion.nombre}&tipoRegistro=${entry.value}">Ver registro</a>
                                                    <a class="btn-outline" href="perfilUsuario?nickname=${entry.key}&from=edicion&edicion=${edicion.nombre}">Ver perfil</a>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>

                    <div style="margin-top:1rem;">
                        <a class="btn-outline" href="${pageContext.request.contextPath}/edicionesOrganizadas">← Volver a mis ediciones</a>
                    </div>
                </div>
            </section>
        </main>
    </div>
</body>
</html>
