
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>eventos.uy</title>
<link rel="icon" type="image/png"
	href="${pageContext.request.contextPath}/img/favicon.png">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/styles.css">

<link rel="preconnect" href="https://fonts.googleapis.com" />
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
<link
	href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap"
	rel="stylesheet" />

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap"
	rel="stylesheet">

</head>
<body>

	<!-- Header -->
	<header class="header">
		<h1>
			<a href="<%=request.getContextPath()%>/inicio">eventos.uy</a>
		</h1>
	<div class="header-right" style="display: flex; align-items: center; gap: 1rem;">
		<c:choose>
				<c:when test="${not empty role}">
					<div class="user-badge"
						style="display: flex; align-items: center; gap: 0.5rem;">
						<a href="${pageContext.request.contextPath}/miPerfil"
							style="display: flex; align-items: center; gap: 0.5rem; text-decoration: none; color: inherit;">
							<img class="avatar"
							src="${not empty avatar ? pageContext.request.contextPath.concat(avatar) : pageContext.request.contextPath.concat('/img/usSinFoto.webp')}"
							alt="Avatar de usuario" /> <span class="nickname">${nickname}</span>
						</a> <a href="${pageContext.request.contextPath}/logout"
							class="btn-primary">Cerrar sesión</a>
					</div>
				</c:when>
				<c:otherwise>
					<nav class="nav-links">
						<a href="${pageContext.request.contextPath}/login"
							style="color: white; text-decoration: none; font-weight: 600; padding: 0.5rem 1rem; border-radius: 6px; transition: background-color 0.2s;"
							onmouseover="this.style.backgroundColor='rgba(255,255,255,0.1)'"
							onmouseout="this.style.backgroundColor='transparent'">Iniciar
							sesión</a> <a href="${pageContext.request.contextPath}/signup"
							style="color: white; text-decoration: none; font-weight: 600; padding: 0.5rem 1rem; border-radius: 6px; transition: background-color 0.2s;"
							onmouseover="this.style.backgroundColor='rgba(255,255,255,0.1)'"
							onmouseout="this.style.backgroundColor='transparent'">Registrarse</a>
					</nav>
				</c:otherwise>
			</c:choose>
		   <!-- Botón para cargar datos eliminado -->
		</div>
	</header>

	<!-- Contenido principal -->
	<c:if test="${not empty datosMensaje}">
		<c:set var="alertBg" value="#e7f6f2" />
		<c:set var="alertColor" value="#0d5c49" />
		<c:if test="${datosMensajeTipo == 'error'}">
			<c:set var="alertBg" value="#fdecea" />
			<c:set var="alertColor" value="#c0392b" />
		</c:if>
		<c:if test="${datosMensajeTipo == 'info'}">
			<c:set var="alertBg" value="#edf2ff" />
			<c:set var="alertColor" value="#1f3f72" />
		</c:if>
		<div style="max-width: 960px; margin: 1rem auto; padding: 0.75rem 1rem; border-radius: 6px; background-color: ${alertBg}; color: ${alertColor}; font-weight: 600; text-align: center;">
			${datosMensaje}
		</div>
	</c:if>
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
							<li><a href="edicionesOrganizadas">Ediciones Organizadas</a></li>
						</ul>
					</div>
				</c:when>
				<c:when test="${role == 'asistente'}">
					<div class="panel sidebar">
						<div class="panel-header">Mi perfil</div>
						<ul class="menu-list">
							<li><a
								href="${pageContext.request.contextPath}/registroAedicion">Registro
									a Edición</a></li>
							<li><a
								href="${pageContext.request.contextPath}/misRegistros">Mis
									Registros</a></li>
						</ul>
					</div>
				</c:when>
			</c:choose>

			<!-- CategorÃ­as -->
			<div class="panel sidebar" style="margin-top: 1rem;">
				<div class="panel-header">Categorías</div>
				<ul class="menu-list">
					<c:choose>
						<c:when test="${empty categorias}">
							<li><span class="muted">No hay categorías
									disponibles.</span></li>
						</c:when>
						<c:otherwise>
							<li><c:url var="urlTodas" value="/inicio" /> <a
								href="${urlTodas}"
								class="${categoriaSeleccionada == 'todas' ? 'active' : ''}">Todas</a>
							</li>
							<c:forEach var="cat" items="${categorias}">
								<li><c:url var="catUrl" value="/inicio">
										<c:param name="categoria" value="${cat}" />
									</c:url> <a href="${catUrl}"
									class="${cat eq categoriaSeleccionada ? 'active' : ''}">${cat}</a>
								</li>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</ul>
			</div>

			<!-- BotÃ³n "Ver listado de Usuarios" -->
			<div
				style="margin-top: 2rem; border-top: 1px solid #e0e0e0; padding-top: 1rem;">
				<a href="${pageContext.request.contextPath}/listarUsuarios"
					style="display: flex; align-items: center; gap: 0.5rem; color: #182080; font-weight: 600; text-decoration: none; padding: 0.75rem; border-radius: 6px; transition: background-color 0.2s; background-color: rgba(24, 32, 128, 0.05);">
					Ver listado de Usuarios </a>
			</div>
		</aside>

		<!-- Main: eventos -->
		<main>
			<!-- Barra de bÃºsqueda -->
			<form action="${pageContext.request.contextPath}/inicio" method="get"
				style="margin-bottom: 1rem; display: flex; gap: 0.5rem; flex-wrap: wrap;">
				<input type="hidden" name="categoria"
					value="${categoriaSeleccionada}" /> <input type="text"
					name="busqueda" placeholder="Buscar evento..." value="${busqueda}"
					style="padding: 0.5rem 1rem; flex: 1; min-width: 200px; border-radius: 6px; border: 1px solid #ccc;">
				<button type="submit"
					style="padding: 0.5rem 1rem; border-radius: 6px; border: none; background-color: #182080; color: white; cursor: pointer;">Buscar</button>
			</form>
			<h2>Próximos Eventos</h2>
			<c:choose>
				<c:when test="${empty eventosOrdenados}">
					<div class="event">
						<div class="event-content">
							<p class="muted">No hay eventos disponibles.</p>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<c:forEach var="e" items="${eventosOrdenados}">
						<div class="event">
							<c:url var="eventoUrl" value="/consultaEvento">
								<c:param name="evento" value="${e.nombre}" />
							</c:url>
							<a href="${eventoUrl}"
								style="text-decoration: none; color: inherit; display: flex; gap: 1rem; width: 100%;">
								<div class="event-image"
									style="flex-shrink: 0; width: 120px; height: 120px; border-radius: 8px; overflow: hidden;">
									<img
										src="${not empty e.imagen ? pageContext.request.contextPath.concat(e.imagen) : pageContext.request.contextPath.concat('/img/eventoSinImagen.png')}"
										alt="${e.nombre}"
										style="width: 100%; height: 100%; object-fit: cover;" />
								</div>
								<div class="event-content"
									style="flex: 1; display: flex; flex-direction: column; justify-content: center;">
									<h3 style="margin: 0 0 0.5rem 0;">${e.nombre}</h3>
									<p style="margin: 0;">${empty e.descripcion ? 'Sin descripciÃ³n' : e.descripcion}</p>
								</div>
							</a>
						</div>
					</c:forEach>
				</c:otherwise>
			</c:choose>
			<div style="margin-top: 1rem; text-align: center;">
				<c:if test="${totalPages > 1}">
					<!-- Construimos la URL base incluyendo posibles parÃ¡metros de bÃºsqueda y categorÃ­a -->
					<c:set var="baseUrl"
						value="${pageContext.request.contextPath}/inicio" />

					<c:set var="prevPage" value="${currentPage - 1}" />
					<c:set var="nextPage" value="${currentPage + 1}" />

					<c:url var="prevUrl" value="/inicio">
						<c:param name="page" value="${prevPage}" />
						<c:if test="${not empty param.busqueda}">
							<c:param name="busqueda" value="${param.busqueda}" />
						</c:if>
						<c:if
							test="${not empty categoriaSeleccionada && categoriaSeleccionada != 'todas'}">
							<c:param name="categoria" value="${categoriaSeleccionada}" />
						</c:if>
					</c:url>

					<c:url var="nextUrl" value="/inicio">
						<c:param name="page" value="${nextPage}" />
						<c:if test="${not empty param.busqueda}">
							<c:param name="busqueda" value="${param.busqueda}" />
						</c:if>
						<c:if
							test="${not empty categoriaSeleccionada && categoriaSeleccionada != 'todas'}">
							<c:param name="categoria" value="${categoriaSeleccionada}" />
						</c:if>
					</c:url>

					<div style="margin-top: 1rem; text-align: center;">
						<a class="btn-primary" href="${currentPage > 1 ? prevUrl : '#'}"
							style="${currentPage == 1 ? 'pointer-events:none; opacity:0.5;' : ''}">Anterior</a>
						Página ${currentPage} de ${totalPages} <a class="btn-primary"
							href="${currentPage < totalPages ? nextUrl : '#'}"
							style="${currentPage == totalPages ? 'pointer-events:none; opacity:0.5;' : ''}">Siguiente</a>
					</div>
				</c:if>

			</div>
		</main>
	</div>

</body>
</html>
