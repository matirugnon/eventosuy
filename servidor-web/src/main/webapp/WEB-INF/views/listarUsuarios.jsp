<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Usuarios · eventos.uy</title>
<link rel="icon" type="image/png" href="${pageContext.request.contextPath}/img/favicon.png">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
<link rel="preconnect" href="https://fonts.googleapis.com" />
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet" />
<style>
.auth-card {
	width: 800px;
	max-width: 95%;
	margin: 0 auto;
}
</style>
</head>
<body>
<div>
    <jsp:include page="/WEB-INF/views/componentes/header.jsp" />
    <div class="content">
        <jsp:include page="/WEB-INF/views/componentes/aside.jsp" />
        <main>
            <section class="auth-container">
                <div class="auth-card">
                	<h2>
   				 		<c:choose>
        					<c:when test="${param.filtro eq 'seguidores'}">
            					Seguidores de ${param.nickname}
        					</c:when>
        					<c:when test="${param.filtro eq 'seguidos'}">
            					Seguidos de ${param.nickname}
        					</c:when>
        					<c:otherwise>
            					Listado de usuarios
       						 </c:otherwise>
				   		 </c:choose>
					</h2>

                    <div style="overflow: auto; margin-top: 1rem;">
                        <table style="width: 100%; border-collapse: collapse;">
                            <thead>
                                <tr style="text-align: left; border-bottom: 1px solid #ddd;">
                                    <th style="padding: .5rem;">Avatar</th>
                                    <th style="padding: .5rem;">Nickname</th>
                                    <th style="padding: .5rem;">Nombre</th>
                                    <th style="padding: .5rem;">Rol</th>
                                    <th style="padding: .5rem;">Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty usuariosOrdenados}">
                                        <tr>
                                            <td colspan="5" style="padding: 2rem; text-align: center; color: #666;">
                                                No hay usuarios registrados.
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="usuario" items="${usuariosOrdenados}">
                                            <tr style="border-bottom: 1px solid #eee;">
                                                <td style="padding: .5rem;">
                                                    <a href="perfilUsuario?nickname=${usuario.nickname}">
                                                        <img src="${pageContext.request.contextPath}${empty usuario.avatar ? '/img/avatar-default.png' : usuario.avatar}"
                                                             alt="Avatar de ${usuario.nickname}"
                                                             style="width: 64px; height: 64px; border-radius: 50%; object-fit: cover;" />
                                                    </a>
                                                </td>
                                                <td style="padding: .5rem;">${usuario.nickname}</td>
                                                <td style="padding: .5rem;">${usuario.nombre}</td>
                                                <td style="padding: .5rem;">${tiposUsuarios[usuario.nickname]}</td>
                                                <td style="padding: .5rem;">
                                                    <c:choose>
                                                        <c:when test="${empty nickname}">
                                                            <span style="color: #888;">Inicia sesión para seguir</span>
                                                        </c:when>

                                                        <c:when test="${usuario.nickname eq nickname}">
                                                            <span style="color: #888;">Este es tu usuario</span>
                                                        </c:when>

                                                        <c:otherwise>
                                                            <c:set var="esSeguido" value="false" />
                                                            <c:forEach var="s" items="${seguidos}">
                                                                <c:if test="${s eq usuario.nickname}">
                                                                    <c:set var="esSeguido" value="true" />
                                                                </c:if>
                                                            </c:forEach>

                                                            <c:choose>
                                                                <c:when test="${esSeguido}">
                                                                    <form method="post" action="listarUsuarios" style="display:inline;">
                                                                        <input type="hidden" name="accion" value="dejar"/>
                                                                        <input type="hidden" name="seguido" value="${usuario.nickname}"/>
                                                                        <!-- Mantenemos el contexto -->
                                                                        <input type="hidden" name="filtro" value="${param.filtro}"/>
                                                                        <input type="hidden" name="nicknameOrigen" value="${param.nickname}"/>
                                                                        <button type="submit" class="btn-secondary">Dejar de seguir</button>
                                                                    </form>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <form method="post" action="listarUsuarios" style="display:inline;">
                                                                        <input type="hidden" name="accion" value="seguir"/>
                                                                        <input type="hidden" name="seguido" value="${usuario.nickname}"/>
                                                                        <!-- Mantenemos el contexto -->
                                                                        <input type="hidden" name="filtro" value="${param.filtro}"/>
                                                                        <input type="hidden" name="nicknameOrigen" value="${param.nickname}"/>
                                                                        <button type="submit" class="btn-primary">Seguir</button>
                                                                    </form>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>

                        <div style="margin-top: 1rem; text-align: center;">
                            <c:if test="${totalPages > 1}">
                               <c:url var="prevUrl" value="/listarUsuarios">
    							<c:param name="page" value="${currentPage - 1}" />
    							<c:param name="filtro" value="${param.filtro}" />
    							<c:param name="nickname" value="${param.nickname}" />
								</c:url>
								<c:url var="nextUrl" value="/listarUsuarios">
    							<c:param name="page" value="${currentPage + 1}" />
    							<c:param name="filtro" value="${param.filtro}" />
    							<c:param name="nickname" value="${param.nickname}" />
								</c:url>

                                <a class="btn-primary" href="${currentPage > 1 ? prevUrl : '#'}"
                                   style="${currentPage == 1 ? 'pointer-events:none; opacity:0.5;' : ''}">Anterior</a>
                                Página ${currentPage} de ${totalPages}
                                <a class="btn-primary" href="${currentPage < totalPages ? nextUrl : '#'}"
                                   style="${currentPage == totalPages ? 'pointer-events:none; opacity:0.5;' : ''}">Siguiente</a>
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



