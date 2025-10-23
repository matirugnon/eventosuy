<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Usuarios · eventos.uy</title>
<link rel="icon" type="image/png"
	href="${pageContext.request.contextPath}/img/favicon.png">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/styles.css" />
<link rel="preconnect" href="https://fonts.googleapis.com" />
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
<link
	href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap"
	rel="stylesheet" />
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

		<!-- Header : ahora el header esta en la carpeta componentes, para que se cambie en una sola pag y sea igual para todas-->
		<jsp:include page="/WEB-INF/views/componentes/header.jsp" />

		<div class="content">
			<!-- Sidebar -->
			<jsp:include page="/WEB-INF/views/componentes/header.jsp" />

			<!-- Main: Listado de usuarios -->
			<main>
				<section class="auth-container">
					<div class="auth-card">
						<h2>Listado de usuarios</h2>
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
												<td colspan="5"
													style="padding: 2rem; text-align: center; color: #666;">
													No hay usuarios registrados.</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach var="usuario" items="${usuariosOrdenados}">
												<tr style="border-bottom: 1px solid #eee;">
													<td style="padding: .5rem;"><c:choose>
															<c:when test="${not empty usuario.avatar}">
																<img
																	src="${pageContext.request.contextPath}${usuario.avatar}"
																	alt="Avatar de ${usuario.nickname}"
																	style="width: 64px; height: 64px; border-radius: 50%; object-fit: cover;" />
															</c:when>
															<c:otherwise>
																<img
																	src="${pageContext.request.contextPath}/img/avatar-default.png"
																	alt="Avatar de ${usuario.nickname}"
																	style="width: 64px; height: 64px; border-radius: 50%; object-fit: cover;" />
															</c:otherwise>
														</c:choose></td>
													<td style="padding: .5rem;">${usuario.nickname}</td>
													<td style="padding: .5rem;">${usuario.nombre}</td>
													<td style="padding: .5rem;">${tiposUsuarios[usuario.nickname]}</td>
													<td style="padding: .5rem;"><c:url
															var="perfilUsuarioUrl" value="/perfilUsuario">
															<c:param name="nickname" value="${usuario.nickname}" />
														</c:url> <a class="btn-primary" href="${perfilUsuarioUrl}">
															Seleccionar </a></td>
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
									</c:url>
									<c:url var="nextUrl" value="/listarUsuarios">
										<c:param name="page" value="${currentPage + 1}" />
									</c:url>
									<a class="btn-primary"
										href="${currentPage > 1 ? prevUrl : '#'}"
										style="${currentPage == 1 ? 'pointer-events:none; opacity:0.5;' : ''}">Anterior</a>
    				Página ${currentPage} de ${totalPages}
    				<a class="btn-primary"
										href="${currentPage < totalPages ? nextUrl : '#'}"
										style="${currentPage == totalPages ? 'pointer-events:none; opacity:0.5;' : ''}">Siguiente</a>
								</c:if>
							</div>
						</div>
				</section>
			</main>
		</div>
	</div>
</body>
</html>
